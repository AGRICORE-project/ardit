package eu.agricore.indexer.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CommentDTO {
	
    private Long id;
	
    @NotEmpty
	private String content;
	
	private Long parentId;
	
	private Long rootId;
	
	@NotNull
	private Long datasetId;

	@Min(value = 0)
	@Max(value = 2)
	private Integer level;
	
	public CommentDTO() {}

	public CommentDTO(Long id, String content, Long parentId, Long rootId, Integer level, Long datasetId) {
		
		this.id = id;
		this.content = content;
		this.parentId = parentId;
		this.rootId = rootId;
		this.level = level;
		this.datasetId = datasetId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Long getRootId() {
		return rootId;
	}

	public void setRootId(Long rootId) {
		this.rootId = rootId;
	}

	public Long getDatasetId() {
		return datasetId;
	}

	public void setDatasetId(Long datasetId) {
		this.datasetId = datasetId;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
}
