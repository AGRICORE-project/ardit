package eu.agricore.indexer.model.dataset;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "dataset_description")
public class DatasetDescription {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(name = "property")
	@Enumerated(EnumType.STRING)
	private DatasetProperty property;
	
	@Size(max = 500)
	@Column(name = "description",  length = 500)
	private String description;

	public DatasetDescription() {}

	public DatasetDescription(DatasetProperty property, String description) {
		this.property = property;
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public DatasetProperty getProperty() {
		return property;
	}

	public void setProperty(DatasetProperty property) {
		this.property = property;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DatasetDescription other = (DatasetDescription) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "Description [property=" + property.toString() + ", description=" + description + "]";
	}
}
