package eu.agricore.indexer.model.dataset;

import javax.validation.constraints.NotNull;


public class DatasetDescriptionWrapper {
	
	@NotNull(message = "Dataset property descriptions must not be null")
	String description;

	public String getDescritpion() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
