package eu.agricore.indexer.model.datasetvariable;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import eu.agricore.indexer.model.vocabulary.VocabularyValue;

@Entity
@DiscriminatorValue(value="PRICE")
public class PriceDatasetVariable extends DatasetVariable {
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="currency", referencedColumnName="id")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private VocabularyValue currency;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="price_type", referencedColumnName="id")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private VocabularyValue priceType;
	
	@Column(name = "size_unit_amount")
	private Integer sizeUnitAmount;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="area_size_unit", referencedColumnName="id")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private VocabularyValue sizeUnit;
	
	public PriceDatasetVariable() {}
	
	public PriceDatasetVariable(String name, String measurementUnit, Date tmpExtentFrom, Date tmpExtentTo,
			VocabularyValue dataOrigin, VocabularyValue frequency, VocabularyValue frequencyMathRep,
			VocabularyValue mathRepresentation, Integer aggregationLevel, VocabularyValue aggregationUnit,
			Integer aggregationScale, String statsRepresentativeness, String downscalingMethodology, VocabularyValue currency, VocabularyValue priceType, Integer sizeUnitAmount,
			VocabularyValue sizeUnit) {
		
		super(name, measurementUnit, tmpExtentFrom, tmpExtentTo, dataOrigin, frequency, frequencyMathRep, mathRepresentation,
				aggregationLevel, aggregationUnit, aggregationScale, statsRepresentativeness, downscalingMethodology);
		
		this.currency = currency;
		this.priceType = priceType;
		this.sizeUnitAmount = sizeUnitAmount;
		this.sizeUnit = sizeUnit;
	}

	public VocabularyValue getCurrency() {
		return currency;
	}

	public void setCurrency(VocabularyValue currency) {
		this.currency = currency;
	}

	public VocabularyValue getPriceType() {
		return priceType;
	}

	public void setPriceType(VocabularyValue priceType) {
		this.priceType = priceType;
	}

	public Integer getSizeUnitAmount() {
		return sizeUnitAmount;
	}

	public void setSizeUnitAmount(Integer sizeUnitAmount) {
		this.sizeUnitAmount = sizeUnitAmount;
	}

	public VocabularyValue getSizeUnit() {
		return sizeUnit;
	}

	public void setSizeUnit(VocabularyValue sizeUnit) {
		this.sizeUnit = sizeUnit;
	}
}
