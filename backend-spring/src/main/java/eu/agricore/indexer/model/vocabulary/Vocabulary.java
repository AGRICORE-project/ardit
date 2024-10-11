package eu.agricore.indexer.model.vocabulary;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "vocabulary")
public class Vocabulary {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "url")
	private String url;
	
	@Column(name = "topic")
	@Enumerated(EnumType.STRING)
	private VocabularyTopic topic;
	
	public Vocabulary() {}
	
	public Vocabulary(String name, String description, String url, VocabularyTopic topic) {
		this.name = name;
		this.description = description;
		this.url = url;
		this.topic = topic;
	}
	
	public Long getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public VocabularyTopic getTopic() {
		return topic;
	}

	public void setTopic(VocabularyTopic topic) {
		this.topic = topic;
	}
    
    @Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vocabulary other = (Vocabulary) obj;
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
		return "Vocabulary [name=" + name + ", description=" + description + ", url=" + url + ", topic=" + topic + "]";
	}
}
