package eu.agricore.indexer.controller.api;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import eu.agricore.indexer.ldap.model.LdapUser;
import eu.agricore.indexer.util.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import eu.agricore.indexer.dto.CommentDTO;
import eu.agricore.indexer.model.Comment;
import eu.agricore.indexer.model.dataset.Dataset;
import eu.agricore.indexer.service.CommentService;
import eu.agricore.indexer.service.DatasetService;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@CrossOrigin(allowCredentials = "true")
@RestController
@RequestMapping("/api/v1/comments")
public class  CommentController {
	
	@Autowired
	private CommentService commentService;
	
	@Autowired
	private DatasetService datasetService;

	@Autowired
	private UserAccountService userAccountService;
	
	@GetMapping(params = "dataset")
	public ResponseEntity<Object> getDatasetComments(@RequestParam(value = "dataset") Long datasetId) {
		
		// Check if dataset exists
		if(datasetService.findOne(datasetId).isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dataset not found");
		}
		
		List<Comment> comments = commentService.findByDatasetId(datasetId);
		
		return ResponseEntity.ok()
        		.body(comments);	
	}
	
	@PostMapping(value = "")
	public ResponseEntity<Object> createComment(@RequestBody @Valid CommentDTO commentDTO) {
		
		Optional<Dataset> dataset = datasetService.findOne(commentDTO.getDatasetId());
		
		Integer level = 0;
		
		// Check if the dataset exists
		if (dataset.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dataset not found");
		}
		
		if(commentDTO.getRootId() != null) {	
			
			Optional<Comment> root = commentService.findById(commentDTO.getRootId());
			
			// Check if the root exists
			if(root.isEmpty()) { 
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Root comment does not exists");
			}
			
			//Check if the root comment belongs to the same dataset
			if (root.get().getDataset().getId() != commentDTO.getDatasetId()) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Root comment does not belong to the same dataset");
			}
			
			
		} else {
			// If the root is null, parent must be also null
			if (commentDTO.getParentId() != null) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "If the root ID is null, parent must be also null");
			}
		}
				
		// The comment to be created has a parent
		if(commentDTO.getParentId() != null) {
			
			Optional<Comment> parent = commentService.findById(commentDTO.getParentId());
			
			// Check if the parent exists
			if(parent.isEmpty()) { 
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent comment does not exists");
			}
			
			// Check if the parent comment belongs to the same dataset
			if(parent.get().getDataset().getId() != commentDTO.getDatasetId()) { 
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parent comment does not belong to the same dataset");
			}
			
			// Check if the parent belongs to same thread of comments
			if(parent.get().getRootId() != commentDTO.getRootId()) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The comment to reply must be in the same thread");
			}
			
			// Check if the parent is deleted to avoid new replies
			if(parent.get().getDeleted()) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "It is not possible to create replies to deleted comments");
			}
			
			switch(parent.get().getLevel()) {
				case 0:
					level = 1;
					break;
				case 1:
					level = 2;
					break;
				case 2:
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Comments are only allowed up to three levels. Please, write a new comment thread)");
			}
			
		} else {
			level = 0; // The comment has no parent, its a new thread
		}
		
		Comment comment = new Comment(null, false, getCurrentUsername(), commentDTO.getContent(), commentDTO.getParentId(), commentDTO.getRootId(), level, dataset.get());
		
		// Save the new comment
		Comment newComment = commentService.save(comment);
        
        return ResponseEntity.ok()
        		.body(newComment);
	}
	
	// TODO: add this functionality in the front
	@PutMapping(value = "/{id}")
	public ResponseEntity<?> updateComment(@RequestBody @Valid CommentDTO commentDTO, @PathVariable Long id) {
		
		Optional<Dataset> dataset = datasetService.findOne(commentDTO.getDatasetId());
		Optional<Comment> commentToUpdate = commentService.findById(id);
		
		// Check if the dataset exists
		if(dataset.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dataset not found");
		}
		
		// Check if the comment IDs match
		if(commentDTO.getId() != id) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Comment ID does not match");
		}
		
		// Check if the comment exists
		if(commentToUpdate.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found");
		}
		
		// Check if the user is the author of the comment
		if(!getCurrentUsername().equals(commentToUpdate.get().getUserId())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The author of the comment does not match");
		}
		
		// Check if the comment belongs to the specific dataset
		if(commentToUpdate.get().getDataset().getId() != commentDTO.getDatasetId()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The comment does not belong to the given dataset");
		}
		
		Comment comment = new Comment(commentDTO.getId(), false, getCurrentUsername(), commentDTO.getContent(), commentDTO.getParentId(), commentDTO.getRootId(), commentDTO.getLevel(), dataset.get());
		
		Comment commentModified = commentService.update(comment);
		
	    return ResponseEntity.ok(commentModified);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteComment(@PathVariable Long id) {
		
		Optional<Comment> comment = commentService.findById(id);
		
		// Check if the comment exists
		if(comment.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found");
		}

		LdapUser userLogged = userAccountService.getCurrentLdapUser();
		if (userLogged != null) {
			Set<String> userRol = userAccountService.getUserRole(userLogged.getRoles());
			if (userRol.contains("EDITOR") || userRol.contains("USER")) {
				Comment commentToDelete = comment.get();
				// Check the author of the comment
				if(!(userLogged.getUsername().equals(commentToDelete.getUserId()))) {
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The author of the comment does not match");
				}
			}
		}

		// If the comment is a root and does not have replies, purge it
		List<Comment> replies = commentService.findByParentId(id);
		
		if(comment.get().getRootId() == id && replies.isEmpty()) {
			commentService.purge(id);
			
			return ResponseEntity.noContent().build();
			
		} else {
			Comment deletedComment = commentService.delete(id);
			
			return ResponseEntity.ok(deletedComment);
		}
	}
	
	@DeleteMapping("/purge/{id}")
	public ResponseEntity<?> purgeComment(@PathVariable Long id) {
		
		Optional<Comment> comment = commentService.findById(id);
		
		// Check if the comment exists
		if(comment.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found");
		}
		
		// If the comment has replies, delete the replies too
		if(comment.get().getRootId() == comment.get().getId()) {
			// The comment is the root, delete the thread
			commentService.purgeByRootId(comment.get().getRootId());
		} else {
			// Delete the all the children at different levels
			List<Comment> level1Replies = commentService.findByParentId(comment.get().getId());
			
			for (Comment level1Reply: level1Replies) {
				List<Comment> level2Replies = commentService.findByParentId(level1Reply.getId());
				for (Comment level2Reply: level2Replies) {
					commentService.purge(level2Reply.getId());
				}
				commentService.purge(level1Reply.getId());
			}
			
			// Purge the root comment
			commentService.purge(id);
		}
		
		return ResponseEntity.noContent().build();
	}
	
	private String getCurrentUsername() {
		UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
		String currentUsername = auth.getPrincipal().toString();
		return currentUsername;
	}

}
