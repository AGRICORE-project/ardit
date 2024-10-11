package eu.agricore.indexer.controller.api;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import eu.agricore.indexer.ldap.model.LdapUser;
import eu.agricore.indexer.util.AppUtils;
import eu.agricore.indexer.util.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import eu.agricore.indexer.dto.DatasetDTO;
import eu.agricore.indexer.dto.DatasetSimplifiedDTO;
import eu.agricore.indexer.model.Catalogue;
import eu.agricore.indexer.model.dataset.Dataset;
import eu.agricore.indexer.model.dataset.Dataset.DatasetType;
import eu.agricore.indexer.service.CatalogueService;
import eu.agricore.indexer.service.DatasetService;
import eu.agricore.indexer.util.DatasetEntityToDTO;
import eu.agricore.indexer.util.DatasetEntityToSimplifiedDTO;

@CrossOrigin(allowCredentials = "true")
@RestController
@RequestMapping("/api/v1/datasets/search")
public class DatasetSearchController {

    @Autowired
    private DatasetService datasetService;

    @Autowired
    private CatalogueService catalogueService;

    @Autowired
    private UserAccountService userAccountService;


    @GetMapping(value = "")
    public ResponseEntity<Object> getDatasetsByFilters(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                       @RequestParam(name = "query", required = false) String query,
                                                       @RequestParam(name = "type", required = false) String type,
                                                       @RequestParam(name = "draft", required = false) String draft,
                                                       @RequestParam(name = "task", required = false) String task,
                                                       @RequestParam(name = "producer", required = false) String producer,
                                                       @RequestParam(name = "periodicity", required = false) String periodicity,
                                                       @RequestParam(name = "language", required = false) String language,
                                                       @RequestParam(name = "from", required = false) String dataFrom,
                                                       @RequestParam(name = "to", required = false) String dataTo,
                                                       @RequestParam(name = "catalogue", required = false) Long catalogue,
                                                       @RequestParam(name = "format", required = false) String format,
                                                       @RequestParam(name = "analysisUnit", required = false) String analysisUnit,
                                                       @RequestParam(name = "variable", required = false) String variable,
                                                       @RequestParam(name = "continent", required = false) String continent,
                                                       @RequestParam(name = "country", required = false) String country,
                                                       @RequestParam(name = "nuts1", required = false) String nuts1,
                                                       @RequestParam(name = "nuts2", required = false) String nuts2,
                                                       @RequestParam(name = "nuts3", required = false) String nuts3,
                                                       @RequestParam(name = "owner", required = false) String owner,
                                                       @RequestParam(name = "sortBy", required = false) List<String> sortBy) {


        // Validate the dataset type parameter if it exists
        DatasetType datasetType = null;
        if (type != null) {
            try {

                datasetType = DatasetType.valueOf(type.toUpperCase());

            } catch (IllegalArgumentException e) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vocabulary topic not found");
            }
        }

        // Validate and give format the temporal extent parameters
        Date tmpExtentFrom = null;
        Date tmpExtentTo = null;
        try {
            if (dataFrom != null) {
                dataFrom = dataFrom.concat("-01-01");
                tmpExtentFrom = new SimpleDateFormat("yyyy-MM-dd").parse(dataFrom);
            }

            if (dataTo != null) {
                dataTo = dataTo.concat("-01-01");
                tmpExtentTo = new SimpleDateFormat("yyyy-MM-dd").parse(dataTo);
            }
        } catch (ParseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The dates provided do not have the right formatting");
        }

        // Validate the draft parameter
        Boolean draftBool = null;
        if (draft != null) {
            if (draft.equalsIgnoreCase("true") || draft.equalsIgnoreCase("false")) { // Draft parameter can only take true or false values
                draftBool = Boolean.parseBoolean(draft); // Convert to Boolean

            } else {
                // The value provided for draft parameter is not a boolean value
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The value of the draft parameter must be 'true' or 'false'");
            }
        }


        if (draftBool != null && draftBool) {
            LdapUser userLogged = userAccountService.getCurrentLdapUser();
            if (userLogged == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "This action is forbidden for your user rol");
            }
        }

        List<Dataset> datasets = Collections.emptyList();
        Page<Dataset> datasetList = new PageImpl<>(datasets);
        Boolean semaphore = true;
        LdapUser userLogged = userAccountService.getCurrentLdapUser();
        if (userLogged != null) {
            Set<String> userRol = userAccountService.getUserRole(userLogged.getRoles());
            if (userRol.contains("USER")) {
                if (draftBool == null) {
                    datasetList = datasetService.findDatasetUser(query, datasetType, task, producer, periodicity, language, tmpExtentFrom, tmpExtentTo, catalogue,
                            format, analysisUnit, variable, continent, country, nuts1, nuts2, nuts3, owner, sortBy, page, userLogged);
                    semaphore = false;
                }else{
                    if(draftBool){
                        owner = userLogged.getUsername();
                    }
                }
            }
        } else {
            datasetList = datasetService.findAllByFilters(query, page, datasetType, false, task, producer, periodicity, language, tmpExtentFrom, tmpExtentTo, catalogue,
                    format, analysisUnit, variable, continent, country, nuts1, nuts2, nuts3, owner, sortBy);
            semaphore = false;
        }
        if (semaphore) {
            datasetList = datasetService.findAllByFilters(query, page, datasetType, draftBool, task, producer, periodicity, language, tmpExtentFrom, tmpExtentTo, catalogue,
                    format, analysisUnit, variable, continent, country, nuts1, nuts2, nuts3, owner, sortBy);
        }
        // Convert the Page of entities to Page of DTOs
        Page<DatasetSimplifiedDTO> datasetsDTO = datasetList.map(datasetEntity -> {
            DatasetSimplifiedDTO datasetDTO = DatasetEntityToSimplifiedDTO.convertEntityToDTO(datasetEntity);
            return datasetDTO;
        });

        return ResponseEntity.ok()
                .body(datasetsDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getDataset(@PathVariable Long id) {

        Optional<Dataset> datasetOptional = datasetService.findOneAddView(id);


        if (datasetOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dataset not found");
        }
        Dataset dataset = datasetOptional.get();
        LdapUser userLogged = userAccountService.getCurrentLdapUser();
        Boolean draft = dataset.getDraft();
        String owner = dataset.getOwner();
        if (userLogged == null) {
            if (draft) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not allow to search draft datasets");
            }

        } else {
            Set<String> rol = userAccountService.getUserRole(userLogged.getRoles());
            if (rol.contains("USER")) {
                if (owner != null && !(userLogged.getUsername().equals(owner)) && draft) {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not allow to search draft datasets");
                }
            }
        }


        DatasetDTO datasetDTO = DatasetEntityToDTO.convertEntityToDTO(dataset);

        return ResponseEntity.ok()
                .body(datasetDTO);
    }

    @GetMapping(value = "", params = {"catalogue", "page", "sortBy"})
    public ResponseEntity<Object> getDatasetsByCatalogue(@RequestParam(value = "catalogue") String catalogue, @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                         @RequestParam(name = "sortBy", required = false) List<String> sortBy) {

        Long catalogueId = Long.parseLong(catalogue);

        Optional<Catalogue> targetCatalogue = catalogueService.findOne(catalogueId);

        if (targetCatalogue.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Catalogue not found");
        }


        LdapUser userLogged = userAccountService.getCurrentLdapUser();
        // Get the list of datasets associated with the catalogue
        Page<Dataset> datasetList;
        if (userLogged == null) {

            datasetList = datasetService.findByCatalogueNotLogged(catalogueId, page, sortBy);
        } else {
            Set<String> rol = userAccountService.getUserRole(userLogged.getRoles());
            if (rol.contains("USER")) {
                String username = userLogged.getUsername();
                datasetList = datasetService.findByCatalogueUser(catalogueId, username, page, sortBy);
            } else {

                // Get the list of datasets associated with the catalogue
                datasetList = datasetService.findByCatalogue(catalogueId, page, sortBy);
            }

        }

        // Convert the Page of entities to Page of DTOs
        Page<DatasetSimplifiedDTO> datasetsDTO = datasetList.map(datasetEntity -> {
            DatasetSimplifiedDTO datasetDTO = DatasetEntityToSimplifiedDTO.convertEntityToDTO(datasetEntity);
            return datasetDTO;
        });

        return ResponseEntity.ok()
                .body(datasetsDTO);
    }
}
