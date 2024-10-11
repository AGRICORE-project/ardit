package eu.agricore.indexer.service;

import java.util.*;
import java.util.stream.Collectors;

import eu.agricore.indexer.ldap.model.AppUser;
import eu.agricore.indexer.ldap.model.LdapUser;
import eu.agricore.indexer.ldap.service.AppUserService;
import eu.agricore.indexer.ldap.service.EmailService;
import eu.agricore.indexer.util.AppUtils;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eu.agricore.indexer.model.analysisunit.AnalysisUnit;
import eu.agricore.indexer.model.dataset.Dataset;
import eu.agricore.indexer.model.dataset.DatasetGenerationActivity;
import eu.agricore.indexer.model.dataset.DatasetReferencedResource;
import eu.agricore.indexer.model.dataset.Keyword;
import eu.agricore.indexer.model.dataset.Dataset.DatasetType;
import eu.agricore.indexer.model.datasetvariable.DatasetVariable;
import eu.agricore.indexer.model.datasetvariable.VariableReferenceValue;
import eu.agricore.indexer.model.distribution.DataServiceServedDataset;
import eu.agricore.indexer.model.distribution.Distribution;
import eu.agricore.indexer.model.vocabulary.VocabularyValue;
import eu.agricore.indexer.repository.AnalysisUnitRepository;
import eu.agricore.indexer.repository.DatasetRepository;
import eu.agricore.indexer.repository.DatasetVariableRepository;
import eu.agricore.indexer.repository.VocabularyValueRepository;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;

@Service
public class DatasetService {

    @Autowired
    private DatasetRepository datasetRepository;

    @Autowired
    private DatasetVariableRepository datasetVariableRepository;

    @Autowired
    private AnalysisUnitRepository analysisUnitRepository;

    @Autowired
    private VocabularyValueRepository vocabularyValueRepository;

    @Autowired
    private KeywordService keywordService;

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private CommentService commentService;


    @Transactional(readOnly = true)
    public void sendDateSetNotification(Dataset dataset) {

        List<AppUser> usersToNotificate = appUserService.findAppUserDatasetSubscription();
        if (usersToNotificate != null) {
            for (AppUser appUser : usersToNotificate) {
                emailService.sendBlueMailExplicit(appUser.getEmail(), "ARDIT notification - dataset modified",
                        buildDatasetEmail(dataset));

            }

        }


    }


    @Transactional(readOnly = true)
    public List<Dataset> findAll() {

        Pageable pageRequest = Pageable.unpaged();

        return datasetRepository.findAll(pageRequest).getContent();
    }


    @Transactional(readOnly = true)
    public Page<Dataset> findAllByFilters(String queryString, Integer page, DatasetType type, Boolean draft, String task, String producer, String periodicity, String language,
                                          Date tmpExtentFrom, Date tmpExtentoTo, Long catalogue, String format, String analysisUnitName, String variableName, String continent,
                                          String country, String nuts1, String nuts2, String nuts3, String owner,List<String> sortBy) {



        Sort sort = null;
        if (sortBy != null) {
            for (String sortFor :sortBy) {
                Sort sortAux = null;
                if (sortFor.substring(0,1).equals("-")) {
                    String fieldName = sortFor.substring(1,sortFor.length());
                    sortAux =  Sort.by(fieldName).descending();
                } else {
                    sortAux = Sort.by(sortFor).ascending();
                }
                if (sort != null) {
                    sort.and(sortAux);
                } else {
                    sort = sortAux;
                }
            }
        }

        Pageable pageRequest;
        if(sort!=null){
            pageRequest = PageRequest.of(page, 10, sort);
        }else{
            pageRequest = PageRequest.of(page, 10);
        }


        return datasetRepository.findAll(getDatasetQuery(queryString, type, draft, task, producer, periodicity, language, tmpExtentFrom, tmpExtentoTo, catalogue, format,
                analysisUnitName, variableName, continent, country, nuts1, nuts2, nuts3, owner), pageRequest);
    }


    @Transactional(readOnly = true)
    public List<Dataset> findAllByFiltersList(String queryString, DatasetType type, Boolean draft, String task, String producer, String periodicity, String language,
                                              Date tmpExtentFrom, Date tmpExtentoTo, Long catalogue, String format, String analysisUnitName, String variableName, String continent,
                                              String country, String nuts1, String nuts2, String nuts3, String owner,List<String> sortBy) {


        List<Dataset> result;
        Sort sort = null;
        if (sortBy != null) {
            for (String sortFor :sortBy) {
                Sort sortAux = null;
                if (sortFor.substring(0,1).equals("-")) {
                    String fieldName = sortFor.substring(1,sortFor.length());
                    sortAux =  Sort.by(fieldName).descending();
                } else {
                    sortAux = Sort.by(sortFor).ascending();
                }
                if (sort != null) {
                    sort.and(sortAux);
                } else {
                    sort = sortAux;
                }
            }
        }


        if(sort!=null){
           result = datasetRepository.findAll(getDatasetQuery(queryString, type, draft, task, producer, periodicity, language, tmpExtentFrom, tmpExtentoTo, catalogue, format,
                   analysisUnitName, variableName, continent, country, nuts1, nuts2, nuts3, owner),sort);
        }else{
            result = datasetRepository.findAll(getDatasetQuery(queryString, type, draft, task, producer, periodicity, language, tmpExtentFrom, tmpExtentoTo, catalogue, format,
                    analysisUnitName, variableName, continent, country, nuts1, nuts2, nuts3, owner));
        }


        return result;

    }


    @Transactional(readOnly = true)
    public Page<Dataset> findByCatalogue(Long catalogueId, Integer page,List<String> sortBy) {
        Sort sort = null;
        if (sortBy != null) {
            for (String sortFor :sortBy) {
                Sort sortAux = null;
                if (sortFor.substring(0,1).equals("-")) {
                    String fieldName = sortFor.substring(1,sortFor.length());
                    sortAux =  Sort.by(fieldName).descending();
                } else {
                    sortAux = Sort.by(sortFor).ascending();
                }
                if (sort != null) {
                    sort.and(sortAux);
                } else {
                    sort = sortAux;
                }
            }
        }

        Pageable pageRequest;
        if(sort!=null){
            pageRequest = PageRequest.of(page, 10, sort);
        }else{
            pageRequest = PageRequest.of(page, 10);
        }

        return datasetRepository.findByCatalogue(catalogueId, pageRequest);
    }


    @Transactional(readOnly = true)
    public Page<Dataset> findByCatalogueNotLogged(Long catalogueId, Integer page,List<String> sortBy) {

        Sort sort = null;
        if (sortBy != null) {
            for (String sortFor :sortBy) {
                Sort sortAux = null;
                if (sortFor.substring(0,1).equals("-")) {
                    String fieldName = sortFor.substring(1,sortFor.length());
                    sortAux =  Sort.by(fieldName).descending();
                } else {
                    sortAux = Sort.by(sortFor).ascending();
                }
                if (sort != null) {
                    sort.and(sortAux);
                } else {
                    sort = sortAux;
                }
            }
        }

        Pageable pageRequest;
        if(sort!=null){
            pageRequest = PageRequest.of(page, 10, sort);
        }else{
            pageRequest = PageRequest.of(page, 10);
        }

        return datasetRepository.findByCatalogueNotLogged(catalogueId, pageRequest);
    }



    @Transactional(readOnly = true)
    public Page<Dataset> findDatasetUser(String queryString, DatasetType type, String task, String producer, String periodicity, String language,
                                         Date tmpExtentFrom, Date tmpExtentoTo, Long catalogue, String format, String analysisUnitName, String variableName, String continent,
                                         String country, String nuts1, String nuts2, String nuts3, String owner, List<String> sortBy, Integer page, LdapUser userLogged) {


        List<Dataset> mergeList = new ArrayList<>();

        Pageable pageRequest = PageRequest.of(page, 10);

        Page<Dataset> result = new PageImpl<>(mergeList, pageRequest, mergeList.size());


        List<Dataset> datasetUserLoggedDraftList = findAllByFiltersList(queryString, type, true, task, producer, periodicity, language,
                tmpExtentFrom, tmpExtentoTo, catalogue, format, analysisUnitName, variableName, continent, country, nuts1, nuts2, nuts3, userLogged.getUsername(),sortBy);

        if (datasetUserLoggedDraftList != null) {

            List<Dataset> datasetUserList = findAllByFiltersList(queryString, type, false, task, producer, periodicity,
                    language, tmpExtentFrom, tmpExtentoTo, catalogue, format, analysisUnitName, variableName, continent, country, nuts1, nuts2, nuts3, owner, sortBy);

            mergeList.addAll(datasetUserLoggedDraftList);
            if (datasetUserList != null) {
                mergeList.addAll(datasetUserList);
            }
            Set<Dataset> datasetHashSet = new HashSet<>(mergeList);
            List<Dataset> datasetsFinalList;
            List<Dataset> searchResult;
            datasetsFinalList = new ArrayList<>(datasetHashSet);
            searchResult = datasetsFinalList;

            if(sortBy!=null){
                List<Dataset> datasetsFinalListOrderedASC = datasetsFinalList.stream().sorted(Comparator.comparing(Dataset::getTmpExtentFrom))
                        .collect(Collectors.toList());
                if(sortBy.get(0).substring(0,1).equals("-")){
                 Collections.reverse(datasetsFinalListOrderedASC);
                }
                searchResult = datasetsFinalListOrderedASC;
            }
            result = AppUtils.convertToPage(searchResult,pageRequest);
        }

        return result;
    }

    @Transactional(readOnly = true)
    public Page<Dataset> findByCatalogueUser(Long catalogueId, String user, Integer page,List<String> sortBy) {

        List<Dataset> mergeList = new ArrayList<>();
        Pageable pageRequest = PageRequest.of(page, 10);

        Page<Dataset> result = new PageImpl<>(mergeList, pageRequest, mergeList.size());


        List<Dataset> userDataset = datasetRepository.findListByCatalogueUser(catalogueId, user);
        if (userDataset != null && !userDataset.isEmpty()) {
            mergeList.addAll(userDataset);
        }
        List<Dataset> finalDataset = datasetRepository.findListByCatalogueNotLogged(catalogueId);
        if (finalDataset != null && !finalDataset.isEmpty()) {
            mergeList.addAll(finalDataset);
        }
        if (!mergeList.isEmpty()) {
            Set<Dataset> datasetHashSet = new HashSet<>(mergeList);
            List<Dataset> datasetsFinalList;
            List<Dataset> searchResult;
            datasetsFinalList = new ArrayList<>(datasetHashSet);
            searchResult = datasetsFinalList;

            if(sortBy!=null){
                List<Dataset> datasetsFinalListOrderedASC = datasetsFinalList.stream().sorted(Comparator.comparing(Dataset::getTmpExtentFrom))
                        .collect(Collectors.toList());
                if(sortBy.get(0).substring(0,1).equals("-")){
                    Collections.reverse(datasetsFinalListOrderedASC);
                }
                searchResult = datasetsFinalListOrderedASC;
            }
            result = AppUtils.convertToPage(searchResult,pageRequest);
        }

        return result;
    }


    @Transactional(readOnly = true)
    public Optional<Dataset> findOne(Long id) {
        Optional<Dataset> dataset = datasetRepository.findById(id);
        if (!dataset.isEmpty()) {
            Hibernate.initialize(dataset.get().getAnalysisUnits());
            Hibernate.initialize(dataset.get().getVariables());

        }

        return datasetRepository.findById(id);
    }


    @Transactional()
    public Optional<Dataset> findOneAddView(Long id) {
        Optional<Dataset> dataset = datasetRepository.findById(id);
        if (!dataset.isEmpty()) {
            Hibernate.initialize(dataset.get().getAnalysisUnits());
            Hibernate.initialize(dataset.get().getVariables());
            // if the result is retrieved, add one visit
            String currentUsername = getCurrentUsername();
            if (dataset.get().getOwner() != null && !(dataset.get().getOwner().equals(currentUsername))) {
                Dataset datasetCopy = dataset.get();
                datasetCopy.setView(dataset.get().getView() + 1);
                save(datasetCopy);
            }

        }

        return datasetRepository.findById(id);
    }


    public Specification<Dataset> getDatasetQuery(String queryString, DatasetType type, Boolean draft, String task, String producer, String periodicity, String language,
                                                  Date tmpExtentFrom, Date tmpExtentTo, Long catalogue, String format, String analysisUnitName, String variableName,
                                                  String continent, String country, String nuts1, String nuts2, String nuts3, String owner) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (queryString != null)
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + queryString.toLowerCase() + "%"));
            if (producer != null)
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("producer")), "%" + producer.toLowerCase() + "%"));
            if (type != null) predicates.add(criteriaBuilder.equal(root.get("datasetType"), type));

            if (draft != null) {
                if (draft) {
                    predicates.add(criteriaBuilder.isTrue(root.get("draft")));
                } else {
                    predicates.add(criteriaBuilder.isFalse(root.get("draft")));
                }
            }

            if (task != null) {
                List<VocabularyValue> results = vocabularyValueRepository.findByLabel(task);

                if (results.size() > 0) {
                    predicates.add(criteriaBuilder.equal(root.get("wpTask"), results.get(0).getId()));
                }
            }

            if (periodicity != null) {
                List<VocabularyValue> results = vocabularyValueRepository.findByLabel(periodicity);

                if (results.size() > 0) {
                    predicates.add(criteriaBuilder.equal(root.get("periodicity"), results.get(0).getId()));
                }
            }

            if (catalogue != null) predicates.add(criteriaBuilder.equal(root.get("catalogue"), catalogue));

            if (language != null) {
                List<VocabularyValue> results = vocabularyValueRepository.findByLabel(language);

                if (results.size() > 0) {
                    Join<Dataset, VocabularyValue> languageJoin = root.join("languages");
                    predicates.add(criteriaBuilder.equal(languageJoin.get("id"), results.get(0).getId()));
                }
            }

            if (format != null) {

                List<VocabularyValue> results = vocabularyValueRepository.findByLabel(format);

                if (results.size() > 0) {
                    Join<Dataset, Distribution> distroJoin = root.join("distributions");
                    Join<Distribution, VocabularyValue> formatJoin = distroJoin.join("format");
                    predicates.add(criteriaBuilder.equal(formatJoin.get("id"), results.get(0).getId()));
                }
            }

            if (tmpExtentFrom != null && tmpExtentTo != null) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.get("tmpExtentFrom"), tmpExtentFrom), criteriaBuilder.lessThanOrEqualTo(root.get("tmpExtentTo"), tmpExtentTo)));
            } else if (tmpExtentFrom != null && tmpExtentTo == null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("tmpExtentFrom"), tmpExtentFrom));
            } else if (tmpExtentFrom == null && tmpExtentTo != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("tmpExtentTo"), tmpExtentTo));
            }

            if (analysisUnitName != null) {
                Join<Dataset, AnalysisUnit> analysisUnitJoin = root.join("analysisUnits");
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(analysisUnitJoin.get("unitReference")), "%" + analysisUnitName.toLowerCase() + "%"));
            }

            if (variableName != null) {
                Join<Dataset, DatasetVariable> variableJoin = root.join("variables");
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(variableJoin.get("name")), "%" + variableName.toLowerCase() + "%"));
            }

            if (continent != null) {
                List<VocabularyValue> results = vocabularyValueRepository.findByLabel(continent);

                if (results.size() > 0) {
                    Join<Dataset, VocabularyValue> continentJoin = root.join("continentalCoverage");
                    predicates.add(criteriaBuilder.equal(continentJoin.get("id"), results.get(0).getId()));
                }
            }

            if (country != null) {
                List<VocabularyValue> results = vocabularyValueRepository.findByLabel(country);

                if (results.size() > 0) {
                    Join<Dataset, VocabularyValue> countryJoin = root.join("countryCoverage");
                    predicates.add(criteriaBuilder.equal(countryJoin.get("id"), results.get(0).getId()));
                }
            }

            if (nuts1 != null) {
                List<VocabularyValue> results = vocabularyValueRepository.findByCode(nuts1);

                if (results.size() > 0) {
                    Join<Dataset, VocabularyValue> nuts1Join = root.join("nuts1");
                    predicates.add(criteriaBuilder.equal(nuts1Join.get("id"), results.get(0).getId()));
                }
            }

            if (nuts2 != null) {
                List<VocabularyValue> results = vocabularyValueRepository.findByCode(nuts2);

                if (results.size() > 0) {
                    Join<Dataset, VocabularyValue> nuts2Join = root.join("nuts2");
                    predicates.add(criteriaBuilder.equal(nuts2Join.get("id"), results.get(0).getId()));
                }
            }

            if (nuts3 != null) {
                List<VocabularyValue> results = vocabularyValueRepository.findByCode(nuts3);

                if (results.size() > 0) {
                    Join<Dataset, VocabularyValue> nuts3Join = root.join("nuts3");
                    predicates.add(criteriaBuilder.equal(nuts3Join.get("id"), results.get(0).getId()));
                }
            }

            if (owner != null) {
                predicates.add(criteriaBuilder.equal(root.get("owner"), owner));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }


    @Transactional
    public Dataset save(Dataset dataset) {

        dataset.setKeywords(saveKeywords(dataset));

        return datasetRepository.save(dataset);
    }

    @Transactional
    public Dataset duplicate(Dataset dataset) {

        if (!dataset.getTitle().contains("[DUPLICATED]")) dataset.setTitle("[DUPLICATED]" + " " + dataset.getTitle());

        // When duplicating a dataset, it is necessary to set properties IDs to null to store new ones
        dataset = clearIdentifiers(dataset);

        // Store the unit of analysis at dataset level first, in order to store the ones at variable level
        dataset.setAnalysisUnits(saveAnalysisUnits(dataset));
        dataset.setKeywords(saveKeywords(dataset));

        return datasetRepository.save(dataset);
    }

    @Transactional
    public Dataset update(Dataset dataset) {
        if (!(dataset.getId() > 0)) {
            throw new IllegalArgumentException("Dataset to update has not ID");
        }
        Long views = findOne(dataset.getId()).get().getView();

        dataset.setKeywords(saveKeywords(dataset));
        dataset.setView(views);

        return datasetRepository.save(dataset);
    }

    @Transactional
    public void delete(Long id) {

        //delete comment before removing dataset
        commentService.deleteByDatasetId(id);

        datasetRepository.deleteById(id);
    }

    @Transactional
    public void emptyCatalogue(Long catalogueId) {
        datasetRepository.emptyCatalogue(catalogueId);
    }

    @Transactional
    public void deleteAll() {
        analysisUnitRepository.deleteAll();
        datasetVariableRepository.deleteAll();
        datasetRepository.deleteAll();
    }

    /**
     * Method that parses all received keywords and converts them to lower case
     * Stores all transient Keyword objects (id=null) before persisting the dataset
     *
     * @param dataset: entity to be persisted
     */
    private List<Keyword> saveKeywords(Dataset dataset) {

        List<Keyword> currentKeywords = dataset.getKeywords(); // Get the keywords list

        for (int i = 0; i < currentKeywords.size(); i++) {

            // Convert the keyword to lower case
            String lowerCaseLabel = currentKeywords.get(i).getLabel().toLowerCase();
            currentKeywords.get(i).setLabel(lowerCaseLabel);

            // Save transient objects and update the list o keywords with new IDs
            if (currentKeywords.get(i).getId() == null) {
                currentKeywords.set(i, keywordService.save(currentKeywords.get(i)));
            }
        }

        return currentKeywords;
    }

    /**
     * Method stores the unit of analysis first, to set their ID
     * Then, set the IDs of the unit of analysis at variable level
     *
     * @param dataset: dataset to be created
     * @return the list of unit of analysis with their new IDs
     */
    private List<AnalysisUnit> saveAnalysisUnits(Dataset dataset) {

        List<AnalysisUnit> analysisUnits = dataset.getAnalysisUnits();

        for (int i = 0; i < analysisUnits.size(); i++) {

            for (DatasetVariable v : dataset.getVariables()) {
                for (AnalysisUnit vau : v.getAnalysisUnits()) {

                    // Check that each unit of analysis at variable level is the same as the one at dataset level
                    Boolean unitReference = vau.getUnitReference().equals(analysisUnits.get(i).getUnitReference());
                    Boolean tmpExtentFrom = vau.getTmpExtentFrom().equals(analysisUnits.get(i).getTmpExtentFrom());
                    Boolean tmpExtentTo = vau.getTmpExtentTo().equals(analysisUnits.get(i).getTmpExtentTo());

                    if (unitReference && tmpExtentFrom && tmpExtentTo) {
                        // Save transient object
                        AnalysisUnit newAnalysisUnit = analysisUnitRepository.save(analysisUnits.get(i));

                        //Update the list with the stored unit of analysis
                        analysisUnits.set(i, newAnalysisUnit);

                        // Set the ID of the unit of analysis at variable level
                        vau.setId(newAnalysisUnit.getId());
                    }
                }
            }
        }

        return analysisUnits;
    }

    /**
     * Method removes the IDs of all entities related to the dataset complex entities, to avoid Spring detached entities errors
     * Executed when entities are duplicated
     *
     * @param dataset: dataset to be created
     * @return the same dataset without IDs
     */
    private Dataset clearIdentifiers(Dataset dataset) {

        for (DatasetGenerationActivity ga : dataset.getWasGeneratedBy()) {
            if (ga.getId() != null) ga.setId(null); // For each activity, set the ID to null
        }

        for (DatasetReferencedResource rs : dataset.getIsReferencedBy()) {
            if (rs.getId() != null) rs.setId(null); // For each resource, set the ID to null
        }

        List<Distribution> distributions = dataset.getDistributions(); // Get the list of distributions
        List<AnalysisUnit> analysisUnits = dataset.getAnalysisUnits(); // Get the list of unit of analysis
        List<DatasetVariable> variables = dataset.getVariables(); // Get the list of variables

        // Clear the distributions IDs
        for (Distribution dis : distributions) {
            if (dis.getId() != null) dis.setId(null); // For each distribution, set the ID to null

            // If the distribution has a Data Service, set its ID to null
            if (dis.getAccessService() != null) {
                dis.getAccessService().setId(null);

                // If the data service has a list of datasets related to it, set the ID of each one to null
                if (dis.getAccessService().getServedDatasets().size() > 0) {
                    for (DataServiceServedDataset servedDataset : dis.getAccessService().getServedDatasets()) {
                        servedDataset.setId(null);
                    }
                }
            }
        }

        // Clear the unit of analysis IDs
        for (AnalysisUnit au : analysisUnits) {
            if (au.getId() != null) au.setId(null);
        }

        // Clear the variables IDS
        for (DatasetVariable v : variables) {
            if (v.getId() != null) v.setId(null); // Variable ID to null

            // Set the related unit of analysis IDs to null
            for (AnalysisUnit vua : v.getAnalysisUnits()) {
                if (vua.getId() != null) vua.setId(null);
            }

            // Set the reference values IDs to null
            for (VariableReferenceValue ref : v.getReferenceValues()) {
                if (ref.getId() != null) ref.setId(null);
            }
        }

        // Update the dataset
        dataset.setDistributions(distributions);
        dataset.setAnalysisUnits(analysisUnits);
        dataset.setVariables(variables);

        return dataset;
    }


    @Transactional
    public void deleteDatasetUnknown(String owner) {
        datasetRepository.deleteDatasetUnknown(owner);
    }


    private String getCurrentUsername() {

        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        return currentUsername;
    }

    private String buildDatasetEmail(Dataset dataset) {
        return "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\">\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "<meta content=\"width=device-width, initial-scale=1\" name=\"viewport\">\n" +
                "<meta name=\"x-apple-disable-message-reformatting\">\n" +
                "<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                "<meta content=\"telephone=no\" name=\"format-detection\">\n" +
                "<title>ARDIT AGRICORE REGISTER</title><!--[if (mso 16)]>\n" +
                "<style type=\"text/css\">\n" +
                "a {text-decoration: none;}\n" +
                "</style>\n" +
                "<![endif]--><!--[if gte mso 9]><style>sup { font-size: 100% !important; }</style><![endif]--><!--[if gte mso 9]>\n" +
                "<xml>\n" +
                "<o:OfficeDocumentSettings>\n" +
                "<o:AllowPNG></o:AllowPNG>\n" +
                "<o:PixelsPerInch>96</o:PixelsPerInch>\n" +
                "</o:OfficeDocumentSettings>\n" +
                "</xml>\n" +
                "<![endif]--><!--[if !mso]><!-- -->\n" +
                "<link href=\"https://fonts.googleapis.com/css?family=Roboto:400,400i,700,700i\" rel=\"stylesheet\"><!--<![endif]-->\n" +
                "<style type=\"text/css\">\n" +
                ".rollover:hover .rollover-first {\n" +
                "max-height:0px!important;\n" +
                "display:none!important;\n" +
                "}\n" +
                ".rollover:hover .rollover-second {\n" +
                "max-height:none!important;\n" +
                "display:block!important;\n" +
                "}\n" +
                "#outlook a {\n" +
                "padding:0;\n" +
                "}\n" +
                ".es-button {\n" +
                "mso-style-priority:100!important;\n" +
                "text-decoration:none!important;\n" +
                "}\n" +
                "a[x-apple-data-detectors] {\n" +
                "color:inherit!important;\n" +
                "text-decoration:none!important;\n" +
                "font-size:inherit!important;\n" +
                "font-family:inherit!important;\n" +
                "font-weight:inherit!important;\n" +
                "line-height:inherit!important;\n" +
                "}\n" +
                ".es-desk-hidden {\n" +
                "display:none;\n" +
                "float:left;\n" +
                "overflow:hidden;\n" +
                "width:0;\n" +
                "max-height:0;\n" +
                "line-height:0;\n" +
                "mso-hide:all;\n" +
                "}\n" +
                ".es-button-border:hover {\n" +
                "border-style:solid solid solid solid!important;\n" +
                "background:#0b317e!important;\n" +
                "border-color:#42d159 #42d159 #42d159 #42d159!important;\n" +
                "}\n" +
                ".es-button-border:hover a.es-button, .es-button-border:hover button.es-button {\n" +
                "background:#0b317e!important;\n" +
                "border-color:#0b317e!important;\n" +
                "}\n" +
                "[data-ogsb] .es-button {\n" +
                "border-width:0!important;\n" +
                "padding:10px 20px 10px 20px!important;\n" +
                "}\n" +
                "@media only screen and (max-width:600px) {p, ul li, ol li, a { line-height:150%!important } h1, h2, h3, h1 a, h2 a, h3 a { line-height:120%!important } h1 { font-size:30px!important; text-align:center } h2 { font-size:26px!important; text-align:center } h3 { font-size:20px!important; text-align:center } .st-br { padding-left:10px!important; padding-right:10px!important } h1 a { text-align:center } .es-header-body h1 a, .es-content-body h1 a, .es-footer-body h1 a { font-size:30px!important } h2 a { text-align:center } .es-header-body h2 a, .es-content-body h2 a, .es-footer-body h2 a { font-size:26px!important } h3 a { text-align:center } .es-header-body h3 a, .es-content-body h3 a, .es-footer-body h3 a { font-size:20px!important } .es-menu td a { font-size:14px!important } .es-header-body p, .es-header-body ul li, .es-header-body ol li, .es-header-body a { font-size:16px!important } .es-content-body p, .es-content-body ul li, .es-content-body ol li, .es-content-body a { font-size:16px!important } .es-footer-body p, .es-footer-body ul li, .es-footer-body ol li, .es-footer-body a { font-size:14px!important } .es-infoblock p, .es-infoblock ul li, .es-infoblock ol li, .es-infoblock a { font-size:12px!important } *[class=\"gmail-fix\"] { display:none!important } .es-m-txt-c, .es-m-txt-c h1, .es-m-txt-c h2, .es-m-txt-c h3 { text-align:center!important } .es-m-txt-r, .es-m-txt-r h1, .es-m-txt-r h2, .es-m-txt-r h3 { text-align:right!important } .es-m-txt-l, .es-m-txt-l h1, .es-m-txt-l h2, .es-m-txt-l h3 { text-align:left!important } .es-m-txt-r img, .es-m-txt-c img, .es-m-txt-l img { display:inline!important } .es-button-border { display:block!important } a.es-button, button.es-button { font-size:16px!important; display:block!important; border-left-width:0px!important; border-right-width:0px!important } .es-adaptive table, .es-left, .es-right { width:100%!important } .es-content table, .es-header table, .es-footer table, .es-content, .es-footer, .es-header { width:100%!important; max-width:600px!important } .es-adapt-td { display:block!important; width:100%!important } .adapt-img { width:100%!important; height:auto!important } .es-m-p0 { padding:0!important } .es-m-p0r { padding-right:0!important } .es-m-p0l { padding-left:0!important } .es-m-p0t { padding-top:0!important } .es-m-p0b { padding-bottom:0!important } .es-m-p20b { padding-bottom:20px!important } .es-mobile-hidden, .es-hidden { display:none!important } tr.es-desk-hidden, td.es-desk-hidden, table.es-desk-hidden { width:auto!important; overflow:visible!important; float:none!important; max-height:inherit!important; line-height:inherit!important } tr.es-desk-hidden { display:table-row!important } table.es-desk-hidden { display:table!important } td.es-desk-menu-hidden { display:table-cell!important } table.es-table-not-adapt, .esd-block-html table { width:auto!important } table.es-social { display:inline-block!important } table.es-social td { display:inline-block!important } .es-m-p5 { padding:5px!important } .es-m-p5t { padding-top:5px!important } .es-m-p5b { padding-bottom:5px!important } .es-m-p5r { padding-right:5px!important } .es-m-p5l { padding-left:5px!important } .es-m-p10 { padding:10px!important } .es-m-p10t { padding-top:10px!important } .es-m-p10b { padding-bottom:10px!important } .es-m-p10r { padding-right:10px!important } .es-m-p10l { padding-left:10px!important } .es-m-p15 { padding:15px!important } .es-m-p15t { padding-top:15px!important } .es-m-p15b { padding-bottom:15px!important } .es-m-p15r { padding-right:15px!important } .es-m-p15l { padding-left:15px!important } .es-m-p20 { padding:20px!important } .es-m-p20t { padding-top:20px!important } .es-m-p20r { padding-right:20px!important } .es-m-p20l { padding-left:20px!important } .es-m-p25 { padding:25px!important } .es-m-p25t { padding-top:25px!important } .es-m-p25b { padding-bottom:25px!important } .es-m-p25r { padding-right:25px!important } .es-m-p25l { padding-left:25px!important } .es-m-p30 { padding:30px!important } .es-m-p30t { padding-top:30px!important } .es-m-p30b { padding-bottom:30px!important } .es-m-p30r { padding-right:30px!important } .es-m-p30l { padding-left:30px!important } .es-m-p35 { padding:35px!important } .es-m-p35t { padding-top:35px!important } .es-m-p35b { padding-bottom:35px!important } .es-m-p35r { padding-right:35px!important } .es-m-p35l { padding-left:35px!important } .es-m-p40 { padding:40px!important } .es-m-p40t { padding-top:40px!important } .es-m-p40b { padding-bottom:40px!important } .es-m-p40r { padding-right:40px!important } .es-m-p40l { padding-left:40px!important } .es-desk-hidden { display:table-row!important; width:auto!important; overflow:visible!important; max-height:inherit!important } .h-auto { height:auto!important } }\n" +
                "</style>\n" +
                "</head>\n" +
                "<body style=\"width:100%;font-family:roboto, 'helvetica neue', helvetica, arial, sans-serif;-webkit-text-size-adjust:100%;-ms-text-size-adjust:100%;padding:0;Margin:0\">\n" +
                "<div class=\"es-wrapper-color\" style=\"background-color:#F8F9FD\"><!--[if gte mso 9]>\n" +
                "<v:background xmlns:v=\"urn:schemas-microsoft-com:vml\" fill=\"t\">\n" +
                "<v:fill type=\"tile\" color=\"#f8f9fd\"></v:fill>\n" +
                "</v:background>\n" +
                "<![endif]-->\n" +
                "<table class=\"es-wrapper\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;padding:0;Margin:0;width:100%;height:100%;background-repeat:repeat;background-position:center top\">\n" +
                "<tr>\n" +
                "<td valign=\"top\" style=\"padding:0;Margin:0\">\n" +
                "<table cellpadding=\"0\" cellspacing=\"0\" class=\"es-header\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;table-layout:fixed !important;width:100%;background-color:transparent;background-repeat:repeat;background-position:center top\">\n" +
                "<tr>\n" +
                "<td align=\"center\" bgcolor=\"#ffffff\" style=\"padding:0;Margin:0;background-color:#ffffff\">\n" +
                "<table bgcolor=\"#ffffff\" class=\"es-header-body\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:transparent;width:600px\">\n" +
                "<tr>\n" +
                "<td align=\"left\" bgcolor=\"#46673a\" style=\"Margin:0;padding-top:15px;padding-bottom:15px;padding-left:30px;padding-right:30px;background-color:#46673a\">\n" +
                "<table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\">\n" +
                "<tr>\n" +
                "<td align=\"center\" valign=\"top\" style=\"padding:0;Margin:0;width:540px\">\n" +
                "<table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" role=\"presentation\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\">\n" +
                "<tr>\n" +
                "<td align=\"center\" style=\"padding:0;Margin:0;font-size:0px\"><a target=\"_blank\" href=\"https://ardit.agricore-project.eu/\" style=\"-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;text-decoration:underline;color:#1376C8;font-size:14px\"><img class=\"adapt-img\" src=\"https://vsvlof.stripocdn.email/content/guids/8ab9b223-3f7d-471c-ae72-e9dfd9166d36/images/logoalt.png\" alt style=\"display:block;border:0;outline:none;text-decoration:none;-ms-interpolation-mode:bicubic\" width=\"80\" height=\"80\"></a></td>\n" +
                "</tr>\n" +
                "</table></td>\n" +
                "</tr>\n" +
                "</table></td>\n" +
                "</tr>\n" +
                "</table></td>\n" +
                "</tr>\n" +
                "</table>\n" +
                "<table cellpadding=\"0\" cellspacing=\"0\" class=\"es-content\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;table-layout:fixed !important;width:100%\">\n" +
                "<tr>\n" +
                "<td align=\"center\" bgcolor=\"#ffffff\" style=\"padding:0;Margin:0;background-color:#ffffff\">\n" +
                "<table bgcolor=\"#ffffff\" class=\"es-content-body\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:transparent;width:600px\">\n" +
                "<tr>\n" +
                "<td align=\"left\" bgcolor=\"#f8f8f8\" style=\"Margin:0;padding-left:30px;padding-right:30px;padding-top:40px;padding-bottom:40px;background-color:#f8f8f8\">\n" +
                "<table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\">\n" +
                "<tr>\n" +
                "<td align=\"center\" valign=\"top\" style=\"padding:0;Margin:0;width:540px\">\n" +
                "<table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" role=\"presentation\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\">\n" +
                "<tr>\n" +
                "<td align=\"left\" style=\"padding:0;Margin:0;padding-bottom:15px\"><p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:roboto, 'helvetica neue', helvetica, arial, sans-serif;line-height:32px;color:#131313;font-size:16px\"><em><strong>This message was sent through the ARDIT platform.</strong></em><br></p></td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td align=\"left\" style=\"padding:0;Margin:0;padding-bottom:15px\"><p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:roboto, 'helvetica neue', helvetica, arial, sans-serif;line-height:24px;color:#131313;font-size:16px\">This message was sent because you are subscribed to the dataset notification system.</p></td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td align=\"left\" style=\"padding:0;Margin:0;padding-bottom:15px\"><p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:roboto, 'helvetica neue', helvetica, arial, sans-serif;line-height:24px;color:#131313;font-size:16px\">The dataset \"" + dataset.getTitle() + "\" was either created, modified or deleted.</p></td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td align=\"left\" style=\"padding:0;Margin:0;padding-bottom:15px;color:#46673a\"><p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:roboto, 'helvetica neue', helvetica, arial, sans-serif;line-height:24px;color:#131313;font-size:16px\">If you no longer want to receive this mails, you can unsubscribe in the ARDIT platform.</p></td>\n" +
                "</tr>\n" +
                "</table></td>\n" +
                "</tr>\n" +
                "</table></td>\n" +
                "</tr>\n" +
                "</table></td>\n" +
                "</tr>\n" +
                "</table>\n" +
                "<table cellpadding=\"0\" cellspacing=\"0\" class=\"es-footer\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;table-layout:fixed !important;width:100%;background-color:#0A2B6E;background-image:url(https://vsvlof.stripocdn.email/content/guids/CABINET_9bfedeeeb9eeabe76f8ff794c5e228f9/images/2191625641866113.png);background-repeat:repeat;background-position:center center\" background=\"https://vsvlof.stripocdn.email/content/guids/CABINET_9bfedeeeb9eeabe76f8ff794c5e228f9/images/2191625641866113.png\">\n" +
                "<tr>\n" +
                "<td align=\"center\" bgcolor=\"#ffffff\" style=\"padding:0;Margin:0;background-color:#ffffff\">\n" +
                "<table class=\"es-footer-body\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:transparent;width:600px\">\n" +
                "<tr>\n" +
                "<td align=\"left\" bgcolor=\"#466d3a\" style=\"padding:0;Margin:0;padding-left:20px;padding-right:20px;background-color:#466d3a\">\n" +
                "<table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\">\n" +
                "<tr>\n" +
                "<td align=\"center\" valign=\"top\" style=\"padding:0;Margin:0;width:560px\">\n" +
                "<table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" role=\"presentation\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\">\n" +
                "<tr>\n" +
                "<td align=\"center\" style=\"padding:10px;Margin:0\"><p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:roboto, 'helvetica neue', helvetica, arial, sans-serif;line-height:20px;color:#ffffff;font-size:13px\"><em>Do not reply to this email</em></p></td>\n" +
                "</tr>\n" +
                "</table></td>\n" +
                "</tr>\n" +
                "</table></td>\n" +
                "</tr>\n" +
                "</table></td>\n" +
                "</tr>\n" +
                "</table></td>\n" +
                "</tr>\n" +
                "</table>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>";
    }


}
