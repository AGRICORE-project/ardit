package eu.agricore.indexer.controller.api;

import eu.agricore.indexer.ldap.model.LdapUser;
import eu.agricore.indexer.ldap.service.AppUserService;
import eu.agricore.indexer.ldap.service.EmailService;
import eu.agricore.indexer.model.dataset.Dataset;
import eu.agricore.indexer.model.dataset.Dataset.DatasetType;
import eu.agricore.indexer.model.dataset.Keyword;
import eu.agricore.indexer.model.datasetvariable.DatasetVariable;
import eu.agricore.indexer.model.vocabulary.VocabularyTopic;
import eu.agricore.indexer.model.vocabulary.VocabularyValue;
import eu.agricore.indexer.service.DatasetService;
import eu.agricore.indexer.service.KeywordService;
import eu.agricore.indexer.service.VocabularyService;
import eu.agricore.indexer.service.VocabularyValueService;
import eu.agricore.indexer.util.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@CrossOrigin(allowCredentials = "true")
@RestController
@RequestMapping("/api/v1/datasets")
public class DatasetController {

    @Autowired
    private DatasetService datasetService;

    @Autowired
    private KeywordService keywordService;

    @Autowired
    private VocabularyService vocabularyService;

    @Autowired
    private VocabularyValueService vocabularyValueService;

    @Autowired
    private UserAccountService userAccountService;


    @GetMapping("/{id}")
    public ResponseEntity<Object> getDataset(@PathVariable Long id) {

        Optional<Dataset> dataset = datasetService.findOneAddView(id);

        if (dataset.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dataset not found");
        }


        return ResponseEntity.ok()
                .body(dataset);
    }

    @GetMapping(value = "/keywords", params = "query")
    public ResponseEntity<Object> searchKeywordsByLabel(@RequestParam(value = "query") String query) {

        List<Keyword> keyword = keywordService.findByLabel(query.toLowerCase()); // Covert the query string to lower case

        return ResponseEntity.ok()
                .body(keyword);
    }

    @GetMapping("/types")
    public ResponseEntity<Object> getDatasetTypes() {

        List<String> types = Stream.of(DatasetType.values())
                .map(Enum::name)
                .collect(Collectors.toList());


        return ResponseEntity.ok()
                .body(types);
    }

    @PostMapping("")
    public ResponseEntity<Object> createDataset(@RequestBody @Valid Dataset dataset) {

        // Validate the given dataset
        validiateDatasetProperties(dataset);

        Boolean draft = dataset.getDraft();

        if (draft!=null && !draft) {
            LdapUser userLogged = userAccountService.getCurrentLdapUser();
            if (userLogged == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Current user not found");
            } else {
                Set<String> userRol = userAccountService.getUserRole(userLogged.getRoles());
                if (userRol.contains("USER")) {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "This action is forbidden for your user rol");
                }
            }

        }

        if (dataset.getView() == null) {
            dataset.setView(0l);
        }

        // Save the new dataset
        Dataset newDataset = datasetService.save(dataset);

        datasetService.sendDateSetNotification(newDataset);

        return ResponseEntity.ok()
                .body(newDataset);
    }

    @PostMapping("/duplicate")
    public ResponseEntity<Object> duplicateDataset(@RequestBody @Valid Dataset dataset) {

        // Validate the given dataset
        validiateDatasetProperties(dataset);

        // Save a new dataset as a duplication of the existing one
        Dataset newDataset = datasetService.duplicate(dataset);

        return ResponseEntity.ok()
                .body(newDataset);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDataset(@RequestBody @Valid Dataset dataset, @PathVariable Long id) {


        LdapUser userLogged = userAccountService.getCurrentLdapUser();
        Set<String> userRol = null;

        if (userLogged == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Current user not found");
        } else {
            userRol = userAccountService.getUserRole(userLogged.getRoles());
        }

        Optional<Dataset> datasetOptional = datasetService.findOne(id);

        if (!dataset.getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dataset id doesn't match");
        }

        if (datasetOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dataset not found");
        }

        if (userRol != null) {

            if (userRol.contains("USER")) {

                String principalUsername = userLogged.getUsername();
                String datasetOwner = dataset.getOwner();

                if (datasetOwner != null && !(principalUsername.equals(datasetOwner))) {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You can not edit other user dataset");
                }

            }
        }


        // Validate the given dataset
        validiateDatasetProperties(dataset);

        Dataset datasetModified = datasetService.update(dataset);

        datasetService.sendDateSetNotification(datasetModified);

        return ResponseEntity.ok(datasetModified);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDataset(@PathVariable Long id) {


        Optional<Dataset> optionalDatasetToBeDeleted = datasetService.findOne(id);

        if (optionalDatasetToBeDeleted.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dataset not found");
        }
        Dataset datasetToDelete = optionalDatasetToBeDeleted.get();
        LdapUser userLogged = userAccountService.getCurrentLdapUser();
        if (userLogged != null) {
            Set<String> userRol = userAccountService.getUserRole(userLogged.getRoles());
            if (userRol.contains("EDITOR")) {
                String datasetOwner = datasetToDelete.getOwner();
                if (!datasetOwner.equals(userLogged.getUsername())) {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You can not edit other user dataset");
                }
            }
        }
        datasetService.delete(id);
        datasetService.sendDateSetNotification(datasetToDelete);
        return ResponseEntity.noContent().build();
    }

    private void validiateDatasetProperties(Dataset dataset) {

        /******************** VOCABULARY VALUE VALIDATION ********************/
        // Required properties
        if (!dataset.getLanguages().isEmpty())
            validateListOfValues(dataset.getLanguages(), VocabularyTopic.LANGUAGE); //Languages
        validateUniqueValue(dataset.getPeriodicity().getId(), VocabularyTopic.PERIODICITY); //Periodicity

        // Optional properties
        if (!dataset.getSubjects().isEmpty())
            validateListOfValues(dataset.getSubjects(), VocabularyTopic.SUBJECT); //Themes
        if (!dataset.getThemes().isEmpty()) validateListOfValues(dataset.getThemes(), VocabularyTopic.THEME); //Themes
        if (!dataset.getPurposes().isEmpty())
            validateListOfValues(dataset.getPurposes(), VocabularyTopic.PURPOSE); //Purposes
        if (dataset.getDataFrequency() != null)
            validateUniqueValue(dataset.getDataFrequency().getId(), VocabularyTopic.PERIODICITY); //Data frequency
        if (dataset.getAggregationUnit() != null)
            validateUniqueValue(dataset.getAggregationUnit().getId(), VocabularyTopic.MEASURE); //Aggregation level unit
        if (!dataset.getContinentalCoverage().isEmpty())
            validateListOfValues(dataset.getContinentalCoverage(), VocabularyTopic.CONTINENT); //Continental coverage
        if (!dataset.getCountryCoverage().isEmpty())
            validateListOfValues(dataset.getCountryCoverage(), VocabularyTopic.COUNTRY); //Country coverage
        if (!dataset.getNuts1().isEmpty()) validateListOfValues(dataset.getNuts1(), VocabularyTopic.NUTS1); //NUTS1
        if (!dataset.getNuts2().isEmpty()) validateListOfValues(dataset.getNuts2(), VocabularyTopic.NUTS2); //NUTS2
        if (!dataset.getNuts3().isEmpty()) validateListOfValues(dataset.getNuts3(), VocabularyTopic.NUTS3); //NUTS3

        /******************** DATASET VARIABLES VALIDATION ********************/
        if (!dataset.getVariables().isEmpty()) validateDatasetVariables(dataset.getVariables());

        /******************** TEMPORAL EXTENT VALIDATION ********************/
        validateTemporalExtent(dataset.getTmpExtentFrom(), dataset.getTmpExtentTo());

        /******************** GEOCOVERAGE VALIDATION ********************/
        if (!validateGeocoverage(dataset))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Geocoverage section can only contain one property set");
    }

    /**
     * Validate a property composed by one vocabulary value
     */
    private void validateUniqueValue(Long id, VocabularyTopic topic) {
        validateVocabularyValue(id, topic);
    }

    /**
     * Validate a property composed by a list of vocabulary values
     */
    private void validateListOfValues(List<VocabularyValue> values, VocabularyTopic topic) {
        for (VocabularyValue value : values) {
            validateVocabularyValue(value.getId(), topic);
        }
    }

    /**
     * Check that the vocabulary value exists and belongs to the correct property
     */
    private void validateVocabularyValue(Long id, VocabularyTopic topic) {

        Optional<VocabularyValue> value = vocabularyValueService.findOne(id);

        if (value.isEmpty()) { //Given vocabulary value does not exist
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The selected value for " + topic.name() + " does not exist");

        } else if (vocabularyService.findOne(value.get().getVocabulary().getId()).get().getTopic() != topic) { //Given vocabulary value is not related with the topic
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The selected value for " + topic.name() + " is not correct");
        }
    }

    private void validateDatasetVariables(List<DatasetVariable> variables) {
        for (DatasetVariable variable : variables) {

            // Optional properties
            // TODO: add new properties
            if (variable.getFrequency() != null)
                validateUniqueValue(variable.getFrequency().getId(), VocabularyTopic.PERIODICITY); //Math representation
            if (variable.getFrequencyMathRep() != null)
                validateUniqueValue(variable.getFrequencyMathRep().getId(), VocabularyTopic.MATH_REPRESENTATION); //Data frequency math representation
            if (variable.getMathRepresentation() != null)
                validateUniqueValue(variable.getMathRepresentation().getId(), VocabularyTopic.MATH_REPRESENTATION); //Math representation

            // Temporal extent
            validateTemporalExtent(variable.getTmpExtentFrom(), variable.getTmpExtentTo());
        }
    }

    /**
     * Check that the start date of the temporary extent is equal to or earlier than the end date
     */
    private void validateTemporalExtent(Date from, Date to) {
        if (from.after(to))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Temporal extent dates are not correct");
    }

    /**
     * Check geocoverage mutual exclusiveness
     * return: true if only one property is set, false otherwise
     */
    private boolean validateGeocoverage(Dataset dataset) {

        Integer counter = 0;

        if (dataset.getContinentalCoverage().size() > 0) counter++;
        if (dataset.getCountryCoverage().size() > 0) counter++;
        if (dataset.getNuts1().size() > 0) counter++;
        if (dataset.getNuts2().size() > 0) counter++;
        if (dataset.getNuts3().size() > 0) counter++;
        if (dataset.getAdm1().size() > 0) counter++;
        if (dataset.getAdm2().size() > 0) counter++;

        return counter <= 1;
    }


}
