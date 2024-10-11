package eu.agricore.indexer.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import eu.agricore.indexer.model.vocabulary.VocabularyValue;

@Entity
@Table(name = "catalogue")
public class Catalogue {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(name = "issued", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
    private Date issued;
	
	@Column(name = "modified")
	@Temporal(TemporalType.TIMESTAMP)
    private Date modified;
	
	@NotEmpty
	private String title;
	
	@Size(max = 500)
	private String description;
	
	private String creator;
	
	private String publisher;
	
	private String link;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date tmpExtentFrom;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date tmpExtentTo;

	private Float spatialResolutionInMeters;
	
	private String temporalResolution;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="periodicity", referencedColumnName="id")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private VocabularyValue periodicity;
	
	@ManyToMany
	@JoinTable(name = "catalogues_languages",
		joinColumns = @JoinColumn(name = "catalogue_id", referencedColumnName = "id"),
		inverseJoinColumns = @JoinColumn(name = "vocabulary_value_id", referencedColumnName = "id"))
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private List<VocabularyValue> languages;
	
	@ManyToMany
	@JoinTable(name = "catalogues_themes",
		joinColumns = @JoinColumn(name = "catalogue_id", referencedColumnName = "id"),
		inverseJoinColumns = @JoinColumn(name = "vocabulary_value_id", referencedColumnName = "id"))
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private List<VocabularyValue> themes;
	
	
	@PrePersist
	protected void prePersist() {
		this.issued = new Date();
		this.modified = new Date();
	}
	
	@PreUpdate
	protected void preUpdate() {
		this.modified = new Date();
	}
	
	public Catalogue() {}
	
	public Catalogue(Date issued, Date modified, @NotEmpty String title, String description, String creator,
			String publisher, String link, Date tmpExtentFrom, Date tmpExtentTo, Float spatialResolutionInMeters,
			String temporalResolution, VocabularyValue periodicity) {

		this.issued = issued;
		this.modified = modified;
		this.title = title;
		this.description = description;
		this.creator = creator;
		this.publisher = publisher;
		this.link = link;
		this.tmpExtentFrom = tmpExtentFrom;
		this.tmpExtentTo = tmpExtentTo;
		this.spatialResolutionInMeters = spatialResolutionInMeters;
		this.temporalResolution = temporalResolution;
		this.periodicity = periodicity;
		this.languages = new ArrayList<VocabularyValue>();
		this.themes = new ArrayList<VocabularyValue>();
	}
	
	public Long getId() {
		return id;
	}

	public Date getIssued() {
		return issued;
	}

	public void setIssued(Date issued) {
		this.issued = issued;
	}

	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public Date getTmpExtentFrom() {
		return tmpExtentFrom;
	}

	public void setTmpExtentFrom(Date tmpExtentFrom) {
		this.tmpExtentFrom = tmpExtentFrom;
	}

	public Date getTmpExtentTo() {
		return tmpExtentTo;
	}

	public void setTmpExtentTo(Date tmpExtentTo) {
		this.tmpExtentTo = tmpExtentTo;
	}

	public Float getSpatialResolutionInMeters() {
		return spatialResolutionInMeters;
	}

	public void setSpatialResolutionInMeters(Float spatialResolutionInMeters) {
		this.spatialResolutionInMeters = spatialResolutionInMeters;
	}

	public String getTemporalResolution() {
		return temporalResolution;
	}

	public void setTemporalResolution(String temporalResolution) {
		this.temporalResolution = temporalResolution;
	}

	public VocabularyValue getPeriodicity() {
		return periodicity;
	}

	public void setPeriodicity(VocabularyValue periodicity) {
		this.periodicity = periodicity;
	}

	public List<VocabularyValue> getLanguages() {
		return languages;
	}

	public void setLanguages(List<VocabularyValue> languages) {
		this.languages = languages;
	}
	
	public void addLanguage(VocabularyValue language) {
		this.languages.add(language);
	}

	public List<VocabularyValue> getThemes() {
		return themes;
	}

	public void setThemes(List<VocabularyValue> themes) {
		this.themes = themes;
	}
	
	public void addTheme(VocabularyValue theme) {
		this.themes.add(theme);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Catalogue other = (Catalogue) obj;
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
}