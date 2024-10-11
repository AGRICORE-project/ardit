package eu.agricore.indexer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import eu.agricore.indexer.model.Comment;

public interface CommentRepository extends CrudRepository<Comment, Long> {
	
	@Query("select c from Comment c where c.dataset.id=?1 order by c.createdAt DESC")
	public List<Comment> findByDatasetId(Long datasetId);
	
	@Query("select c from Comment c where c.parentId=?1")
	public List<Comment> findByParentId(Long parentId);
	
	@Query("select c from Comment c where c.rootId=?1")
	public List<Comment> findByRootId(Long rootId);
	
	@Modifying
	@Query("delete from Comment c where c.rootId = :#{#rootId}")
	public void deleteByRootId(@Param("rootId") Long rootId);
	
	@Modifying
	@Query("delete from Comment c where c.dataset.id = :#{#datasetId}")
	public void deleteByDatasetId(@Param("datasetId") Long datasetId);

	@Modifying
	@Query("update Comment set userId=:#{#newUserId} where userId = :#{#oldUserId}")
	public void deleteCommentUnknown(@Param("oldUserId")String userName,@Param("newUserId") String newUsername);

}
