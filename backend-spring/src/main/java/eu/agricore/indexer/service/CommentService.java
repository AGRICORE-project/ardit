package eu.agricore.indexer.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import eu.agricore.indexer.ldap.model.AppUser;
import eu.agricore.indexer.ldap.service.AppUserService;
import eu.agricore.indexer.ldap.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import eu.agricore.indexer.model.Comment;
import eu.agricore.indexer.repository.CommentRepository;

@Service
public class CommentService {

    private final String DELETE_COMMENT_MESSAGE = "This comment was deleted";
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private AppUserService appUserService;

    @Transactional(readOnly = true)
    public Optional<Comment> findById(Long id) {
        return commentRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Comment> findByDatasetId(Long datasetId) {

        List<Comment> comments = commentRepository.findByDatasetId(datasetId);

        return nestComments(comments);
    }

    @Transactional(readOnly = true)
    public List<Comment> findByParentId(Long parentId) {

        return commentRepository.findByParentId(parentId);
    }

    @Transactional(readOnly = true)
    public List<Comment> findByRootId(Long rootId) {

        return commentRepository.findByRootId(rootId);
    }

    @Transactional
    public Comment save(Comment comment) {

        Comment savedComment = commentRepository.save(comment);

        if (savedComment.getRootId() == null)
            savedComment.setRootId(savedComment.getId()); // Set the thread id to fetch all the children at different levels

        String userMail = null;
        String datasetOwner = comment.getDataset().getOwner();
        if (datasetOwner != null && !datasetOwner.isEmpty()) {
            AppUser appUser = appUserService.findAppUserByUsername(datasetOwner);
            userMail = appUser.getEmail();
        }

        String datasetTitle = comment.getDataset().getTitle();
        String userComment = comment.getUserId();

        if (comment.getParentId() != null) {
            Comment parentComment = commentRepository.findById(comment.getParentId()).get();
            AppUser appUser = appUserService.findAppUserByUsername(parentComment.getUserId());
            String email = appUser.getEmail();
            if (email != null && !(comment.getUserId().equals(parentComment.getUserId()))) {
                emailService.sendBlueMailExplicit(email, "There is a reply to your comment ",
                        "Reply to your comment " + userComment + " in dataset with title " + datasetTitle);
            }

        } else {

            if (userMail != null && !(datasetOwner.equals(userComment))) {
                emailService.sendBlueMailExplicit(userMail, "New comment created",
                        "New comment created by " + userComment + " in dataset with title " + datasetTitle);
            }

        }

        return commentRepository.save(comment);
    }

    @Transactional
    public Comment update(Comment comment) {

        Optional<Comment> commentBBDD = commentRepository.findById(comment.getId());

        String userMail = null;
        String datasetOwner = comment.getDataset().getOwner();
        String oldCommentContent = null;

        if (datasetOwner != null && !datasetOwner.isEmpty()) {
            userMail = appUserService.findAppUserByUsername(datasetOwner).getEmail();
        }

        String datasetTitle = comment.getDataset().getTitle();
        String userComment = comment.getUserId();

        if (!commentBBDD.isEmpty() && commentBBDD.isPresent()) {
            Comment commentStored = commentBBDD.get();
            comment.setCreatedAt(commentStored.getCreatedAt());
            comment.setLevel(commentStored.getLevel());
            oldCommentContent = comment.getContent();
        }
        comment.setLastUpdate(new Date());

        if (userMail != null && !(datasetOwner.equals(userComment))) {
            emailService.sendBlueMailExplicit(userMail, "Updated comment created", "The comment by " + userComment + " was updated in dataset with title " + datasetTitle);
        }

        return commentRepository.save(comment);
    }

    @Transactional
    public Comment delete(Long id) {

        Comment comment = commentRepository.findById(id).get();

        comment.setContent(DELETE_COMMENT_MESSAGE);
        comment.setUserId(DELETE_COMMENT_MESSAGE);
        comment.setDeleted(true);
        comment.setDeleteAt(new Date());

        return commentRepository.save(comment);
    }

    @Transactional
    public void purge(Long id) {
        commentRepository.deleteById(id);
    }

    @Transactional
    public void purgeByRootId(Long rootId) {
        commentRepository.deleteByRootId(rootId); // Delete all the comments in a thread of comments
    }

    @Transactional
    public void deleteByDatasetId(Long datasetId) {
        commentRepository.deleteByDatasetId(datasetId);
    }

    public List<Comment> nestComments(List<Comment> comments) {

        List<Comment> result = new ArrayList<Comment>();
        Map<Long, Comment> map = new HashMap<Long, Comment>();

        for (Comment c : comments) {

            c.setReplies(new ArrayList<Comment>());

            if (c.getParentId() == null) {
                result.add(c);
            }
            map.put(c.getId(), c);
        }

        for (Comment c : comments) {
            if (c.getParentId() != null) {
                Comment parent = map.get(c.getParentId());

                if (parent.getReplies() == null) {
                    parent.setReplies(new ArrayList<Comment>());
                }
                parent.addReply(c);
            }
        }

        return result;

    }


    @Transactional
    public void deleteCommentUnknown(String appUserNameToBeDeleted) {
        commentRepository.deleteCommentUnknown(appUserNameToBeDeleted,"unknown");
    }


}
