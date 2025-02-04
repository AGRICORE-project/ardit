import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormGroup,  FormControl, AbstractControl, Validators } from '@angular/forms';
import { of, Subject, concat } from 'rxjs';
import {catchError, distinctUntilChanged, switchMap, tap} from 'rxjs/operators';

import { DatasetService } from '../../../services/dataset/dataset.service';
import { CatalogueService } from '../../../services/catalogue/catalogue.service';
import { AlertService } from '../../../services/alert/alert.service';
import { VocabularyValueService } from '../../../services/vocabulary-value/vocabulary-value.service';

import { Dataset } from '../../../models/dataset';
import { VocabularyValue } from '../../../models/vocabulary-value';
import { DatasetVariable } from '../../../models/dataset-variable';
import { AnalysisUnit } from '../../../models/analysis-unit';
import { DatasetDescription } from '../../../models/dataset-description';
import { DatasetGenerationActivity } from '../../../models/dataset-generation-activity';
import { DatasetReferencedResource } from '../../../models/dataset-refereced-resource';
import { Catalogue } from '../../../models/catalogue';
import { Distribution } from '../../../models/distribution';
import { Keyword } from '../../../models/keyword';
import { RoutesNames } from '../../../shared/utils/routes.names';

import { DistributionModalComponent } from '../../modals/distribution-modal/distribution-modal.component';
import { SocioeconomicAnalysisUnitModalComponent } from '../../modals/socioeconomic-analysis-unit-modal/socioeconomic-analysis-unit-modal.component';
import { GeoreferencedAnalysisUnitModalComponent } from '../../modals/georeferenced-analysis-unit-modal/georeferenced-analysis-unit-modal.component';
import { DatasetVariableModalComponent } from '../../modals/dataset-variable-modal/dataset-variable-modal.component';
import { PriceDatasetVariableModalComponent } from '../../modals/price-dataset-variable-modal/price-dataset-variable-modal.component';

import { NgbModal, NgbDateParserFormatter } from '@ng-bootstrap/ng-bootstrap';
import { AuthService } from '../../../services/auth/auth.service';
import { User } from 'src/app/models/user';
import { UsersService } from 'src/app/services/users/users.service';

@Component({
  selector: 'app-dataset-create',
  templateUrl: './dataset-create.component.html',
  styleUrls: ['./dataset-create.component.css']
})
export class DatasetCreateComponent implements OnInit {

  @ViewChild('activityValue') generationActivityInput: ElementRef; // Was generated by property input
  @ViewChild('resourceValue') referencedResourceInput: ElementRef; // Is referenced by property input

  // Form groups
  datasetCreateForm: FormGroup;

  // Alert info message
  alertInfo = 'The dataset characterization methodology is based on the methodology defined AGRICORE DCAT-AP 2.0. The ontology can be consulted on';

  // Form sections
  generalSection = 'General';
  purposeSection = 'Purpose';
  geoCoverageSection = 'Geographical coverage';
  distributionSection = 'Distributions';
  resolutionSection = 'Units of analysis';
  variablesSection = 'Variables included';
  keywordsSection = 'Keywords';

  // Info popovers content
  datasetDescriptions: {[property: string]: DatasetDescription;} = {};

  // Select fields
  datasetTypes: String[];

  catalogues: Catalogue[];

  tasksValues: VocabularyValue[];
  taskTopic = 'report_to_task';

  resourceTypes: VocabularyValue[];
  resourceTypeTopic = 'resource_type';

  languageValues: VocabularyValue[];
  languageTopic = 'language';

  periodicityValues: VocabularyValue[];
  periodicityTopic = 'periodicity';

  themesValues: VocabularyValue[];
  themesTopic = 'theme';

  subjectValues: VocabularyValue[];
  subjectTopic = 'subject';

  purposesValues: VocabularyValue[];
  purposeTopic = 'purpose';

  mathRepValues: VocabularyValue[];
  mathRepTopic = 'math_representation';

  accessRightsValues: VocabularyValue[];
  accessRightsTopic = 'access_right';

  formatsValues: VocabularyValue[];
  formatsTopic = 'format';

  measuresValues: VocabularyValue[];
  measureTopic = 'measure';

  continentsValues: VocabularyValue[];
  continentTopic = 'continent';

  countriesValues: VocabularyValue[];
  countryTopic = 'country';

  europeCountriesId = "EU"
  europeCountries: VocabularyValue[];

  nuts1Values: VocabularyValue[];
  nuts1Topic = 'nuts1';

  nuts2Values: VocabularyValue[];
  nuts2Topic = 'nuts2';

  nuts3Values: VocabularyValue[];
  nuts3Topic = 'nuts3';

  adm1Values: VocabularyValue[];
  adm1Topic = 'adm1';

  adm2Values: VocabularyValue[];
  adm2Topic = 'adm2';

  variableDataOriginValues: VocabularyValue[];
  variableDataOriginTopic = 'variable_data_origin';

  currencyValues: VocabularyValue[];
  currencyTopic = 'currency';

  priceTypeValues: VocabularyValue[];
  priceTypeTopic = 'price_type';

  sizeUnitValues: VocabularyValue[];
  sizeUnitTopic = 'size_unit';

  generationActivities: DatasetGenerationActivity[] = []; // List of activities for dataset's waGeneratedBy property
  referencedResources: DatasetReferencedResource[] = []; // List of resurces for dataset's isReferencedBy property

  distributions: Distribution[] = []; // Dataset's distributions
  
  analysisUnits: AnalysisUnit[] = []; // Dataset's units of analysis

  variables: DatasetVariable[] = []; // Dataset's variables

  keywordsFound: any; // List of keywords obtained after a search
  keywordsLoading = false; // To display a spinner when loading keyword searches
  keywordsInput$ = new Subject<string>(); // To get the value introduced by the user in the keywords form input

  distributionSelected: number = null;
  analysisUnitSelected: number = null;
  variableSelected: number = null;

  //current user logged in
  currentUser: User;

  //All user names
  userNames: string[];

  // Config options for alerts
  options = {
    autoClose: true,
    keepAfterRouteChange: false
  };

  optionsRouteChange = {
    autoClose: true,
    keepAfterRouteChange: true
  };

  // Routes
  datasetListRoute = `/${RoutesNames.DATASETS}`;

  constructor(private datasetService: DatasetService, private router: Router, private route: ActivatedRoute, 
    private modalService: NgbModal, private alertService: AlertService, private vocabularyValueService: VocabularyValueService, private userService: UsersService,
    private catalogueService: CatalogueService, private authService: AuthService,private ngbDateParserFormatter: NgbDateParserFormatter) { }

  ngOnInit(): void {
    
    // Populate selects with vocabulary values
    this.getDatasetTypes();
    this.getWPTasks();
    this.getResourceTypes();
    this.getCatalogues();
    this.getLanguageValues();
    this.getPeriodicityValues();
    this.getThemesValues();
    this.getSubjectsValues();
    this.getPurposesValues();
    this.getMathRepValues();
    this.getAccessRightsValues();
    this.getFormatsValues();
    this.getMeasuresValues();
    this.getContinentsValues();
    this.getCountriesValues();
    this.getEuropeCountries();
    this.getNuts1Values();
    this.getNuts2Values();
    this.getNuts3Values();
    this.getAdm1Values();
    this.getAdm2Values();
    this.getVariableDataOriginValues();
    this.getCurrencyValues();
    this.getPriceTypeValues();
    this.getSizeUnitValues();

    this.searchKeywords();

    // Populate info popovers
    this.getPropetiesDescriptions();

    this.currentUser = this.authService.getCurrentUserValue();
    this.getAllUsernames();

    // Dataset form validators
    this.datasetCreateForm = new FormGroup({
      title: new FormControl(null, [
        Validators.required
      ]),
      description: new FormControl(null, [
        Validators.maxLength(1000)
      ]),
      issued: new FormControl(null, []),
      modified: new FormControl(null, []),
      datasetType: new FormControl(null, [
        Validators.required
      ]),
      wpTask: new FormControl(null, []),
      draft: new FormControl(this.getDraftValue(this.currentUser), []),
      producer: new FormControl(null, [
        Validators.required
      ]),
      link: new FormControl(null, [
        Validators.maxLength(1000)
      ]),
      languages: new FormControl([], [
        Validators.required
      ]),
      periodicity: new FormControl(null, [
        Validators.required
      ]),
      catalogue: new FormControl(null, []),
      spatialResolutionInMeters: new FormControl(null, []),
      temporalResolution: new FormControl(null, []),
      wasGeneratedBy: new FormControl([], []),
      isReferencedBy: new FormControl([], []),
      resourceType: new FormControl(null, []),
      tmpExtentFrom: new FormControl(null, [
        Validators.required
      ]),
      tmpExtentTo: new FormControl(null, [
        Validators.required
      ]),
      subjects: new FormControl([], []),
      purposes: new FormControl([], []),
      themes: new FormControl([], []),
      continentalCoverage: new FormControl([], []),
      countryCoverage: new FormControl([], []),
      nuts1: new FormControl([], []),
      nuts2: new FormControl([], []),
      nuts3: new FormControl([], []),
      adm1: new FormControl([], []),
      adm2: new FormControl([], []),
      keywords: new FormControl([], []),
      owner: new FormControl(this.currentUser.username, [])
    });
  }

  private getDatasetTypes(): void {
    this.datasetTypes = null;
    this.datasetService.getDatasetTypes().subscribe(
      res => this.datasetTypes = res,
      err => this.handleError(err)
    );
  }

  public getCatalogues(): void {
    this.catalogues = null;
    this.catalogueService.getAllCatalogues().subscribe(
      res => this.catalogues = res,
      err => this.handleError(err)
    );
  }

  public getWPTasks(): void{
    this.tasksValues = null;
    this.vocabularyValueService.getVocabularyValuesByTopic(this.taskTopic).subscribe(
      res => this.tasksValues = res,
      err => this.handleError(err)
    );
  }

  public getResourceTypes(): void{
    this.resourceTypes = null;
    this.vocabularyValueService.getVocabularyValuesByTopic(this.resourceTypeTopic).subscribe(
      res => this.resourceTypes = res,
      err => this.handleError(err)
    );
  }

  public getLanguageValues(): void{
    this.languageValues = null;
    this.vocabularyValueService.getVocabularyValuesByTopic(this.languageTopic).subscribe(
      res => this.languageValues = res,
      err => this.handleError(err)
    );
  }

  public getPeriodicityValues(): void{
    this.periodicityValues = null;
    this.vocabularyValueService.getVocabularyValuesByTopic(this.periodicityTopic).subscribe(
      res => this.periodicityValues = res,
      err => this.handleError(err)
    );
  }

  public getThemesValues(): void{
    this.themesValues = null;
    this.vocabularyValueService.getVocabularyValuesByTopic(this.themesTopic).subscribe(
      res => this.themesValues = res,
      err => this.handleError(err)
    );
  }

  public getSubjectsValues(): void{
    this.subjectValues = null;
    this.vocabularyValueService.getVocabularyValuesByTopic(this.subjectTopic).subscribe(
      res => this.subjectValues = res,
      err => this.handleError(err)
    );
  }

  public getPurposesValues(): void{
    this.purposesValues = null;
    this.vocabularyValueService.getVocabularyValuesByTopic(this.purposeTopic).subscribe(
      res => this.purposesValues = res,
      err => this.handleError(err)
    );
  }

  public getMathRepValues(): void{
    this.mathRepValues = null;
    this.vocabularyValueService.getVocabularyValuesByTopic(this.mathRepTopic).subscribe(
      res => this.mathRepValues = res,
      err => this.handleError(err)
    );
  }

  public getAccessRightsValues(): void{
    this.accessRightsValues = null;
    this.vocabularyValueService.getVocabularyValuesByTopic(this.accessRightsTopic).subscribe(
      res => this.accessRightsValues = res,
      err => this.handleError(err)
    );
  }

  public getFormatsValues(): void{
    this.formatsValues = null;
    this.vocabularyValueService.getVocabularyValuesByTopic(this.formatsTopic).subscribe(
      res => this.formatsValues = res,
      err => this.handleError(err)
    );
  }

  public getMeasuresValues(): void{
    this.measuresValues = null;
    this.vocabularyValueService.getVocabularyValuesByTopic(this.measureTopic).subscribe(
      res => this.measuresValues = res,
      err => this.handleError(err)
    );
  }

  public getContinentsValues(): void{
    this.continentsValues = null;
    this.vocabularyValueService.getVocabularyValuesByTopic(this.continentTopic).subscribe(
      res => this.continentsValues = res,
      err => this.handleError(err)
    );
  }

  public getCountriesValues(): void{
    this.countriesValues = null;
    this.vocabularyValueService.getVocabularyValuesByTopic(this.countryTopic).subscribe(
      res => this.countriesValues = res,
      err => this.handleError(err)
    );
  }

  public getNuts1Values(): void{
    this.nuts1Values = null;
    this.vocabularyValueService.getVocabularyValuesByTopic(this.nuts1Topic).subscribe(
      res => this.nuts1Values = res,
      err => this.handleError(err)
    );
  }

  public getNuts2Values(): void{
    this.nuts2Values = null;
    this.vocabularyValueService.getVocabularyValuesByTopic(this.nuts2Topic).subscribe(
      res => this.nuts2Values = res,
      err => this.handleError(err)
    );
  }

  public getNuts3Values(): void{
    this.nuts3Values = null;
    this.vocabularyValueService.getVocabularyValuesByTopic(this.nuts3Topic).subscribe(
      res => this.nuts3Values = res,
      err => this.handleError(err)
    );
  }

  public getAdm1Values(): void{
    this.adm1Values = null;
    this.vocabularyValueService.getVocabularyValuesByTopic(this.adm1Topic).subscribe(
      res => this.adm1Values = res,
      err => this.handleError(err)
    );
  }

  public getAdm2Values(): void{
    this.adm2Values = null;
    this.vocabularyValueService.getVocabularyValuesByTopic(this.adm2Topic).subscribe(
      res => this.adm2Values = res,
      err => this.handleError(err)
    );
  }

  public getCurrencyValues(): void{
    this.currencyValues = null;
    this.vocabularyValueService.getVocabularyValuesByTopic(this.currencyTopic).subscribe(
      res => this.currencyValues = res,
      err => this.handleError(err)
    );
  }

  public getPriceTypeValues(): void{
    this.priceTypeValues = null;
    this.vocabularyValueService.getVocabularyValuesByTopic(this.priceTypeTopic).subscribe(
      res => this.priceTypeValues = res,
      err => this.handleError(err)
    );
  }

  public getSizeUnitValues(): void{
    this.sizeUnitValues = null;
    this.vocabularyValueService.getVocabularyValuesByTopic(this.sizeUnitTopic).subscribe(
      res => this.sizeUnitValues = res,
      err => this.handleError(err)
    );
  }

  public getVariableDataOriginValues(): void{
    this.variableDataOriginValues = null;
    this.vocabularyValueService.getVocabularyValuesByTopic(this.variableDataOriginTopic).subscribe(
      res => this.variableDataOriginValues = res,
      err => this.handleError(err)
    );
  }

  /**
   * Get a list of VocabularyValues with the countries that compose the European Union.
   */
  public getEuropeCountries(): void{
    this.europeCountries = null;
    this.vocabularyValueService.getVocabularyValuesByExtraData(this.europeCountriesId).subscribe(
      res => this.europeCountries = res,
      err => this.handleError(err)
    );
  }

  /**
   * Add european union countries to the country coverage list
   */
  public addEuropeanCountries(): void {
    this.datasetCreateForm.controls['countryCoverage'].setValue(this.europeCountries); // Add european countries to the multiselect
  }

  /**
   * Get a list of descriptions to populate info popovers
   * Convert the list into a dictionary [property: description]
   */
  public getPropetiesDescriptions(): void{
    this.datasetDescriptions = null;
    this.datasetService.getPropertiesDescriptions().subscribe(
      res => this.datasetDescriptions = res.reduce((a, x) => ({...a, [x.property]: x.description}), {}),
      err => this.handleError(err)
    );
  }

  /**
   * Downloads the dataset characterisation in JSON format file
   */
   public downloadJsonFile(): void {

    this.datasetCreateForm.get('datasetType').enable(); // Enable the dataset type input in order to retrieve the value

    let dataset: Dataset = this.datasetCreateForm.value;

    // NgbDateStruct to string
    dataset.issued = this.ngbDateParserFormatter.format(this.datasetCreateForm.controls['issued'].value);
    dataset.modified = this.ngbDateParserFormatter.format(this.datasetCreateForm.controls['modified'].value);

    // Transform temporal extent string to complete Date objects
    dataset.tmpExtentFrom = this.formatTmpExtentToDate(String(dataset.tmpExtentFrom));
    dataset.tmpExtentTo =  this.formatTmpExtentToDate(String(dataset.tmpExtentTo));

    dataset.distributions = this.distributions; // Assignment of the distributions created
    dataset.analysisUnits = this.analysisUnits;  // Assignment of the units of analysis created
    dataset.variables = this.variables; // Assignment of the variables created

    // Format the dataset to JSON with line break in each element (parameter "1" in JSON.stringify method)
    var data = "data:text/json;charset=utf-8," + encodeURIComponent(JSON.stringify(dataset, null, 1));

    // Creation of an anchor element to simulate the download link
    var downloader = document.createElement('a'); // Anchor element
    downloader.setAttribute('href', data); // Href attribute
    downloader.setAttribute('download', dataset.title + '.json'); // Download attribute, the file gets the dataset title
    downloader.click(); // Simulate the click to download the file
  }

  /**
   * Submit dataset form data
   */
  public createDataset(): void {

    this.datasetCreateForm.get('datasetType').enable(); // Enable the dataset type input in order to retrieve the value

    let dataset: Dataset = this.datasetCreateForm.value;
    
    // Validate geocoverage mutual exclusiveness
    if (this.validateGeocoverage(dataset)) {

      // NgbDateStruct to string
      dataset.issued = this.ngbDateParserFormatter.format(this.datasetCreateForm.controls['issued'].value);
      dataset.modified = this.ngbDateParserFormatter.format(this.datasetCreateForm.controls['modified'].value);

      // Transform temporal extent string to complete Date objects
      dataset.tmpExtentFrom = this.formatTmpExtentToDate(String(dataset.tmpExtentFrom));
      dataset.tmpExtentTo =  this.formatTmpExtentToDate(String(dataset.tmpExtentTo));

      dataset.distributions = this.distributions; // Assignment of the distributions created
      dataset.analysisUnits = this.analysisUnits;  // Assignment of the units of analysis created
      dataset.variables = this.variables; // Assignment of the variables created

      this.datasetService.createDataset(dataset).subscribe(
        res => {
          this.alertService.success('Dataset created', this.optionsRouteChange),
          this.router.navigate([`/${RoutesNames.DATASETS}/` + res.id])},
        err => this.handleError(err)
      );

    } else {
      this.alertService.warn('Geocoverage section can only contain one property set', this.options);
    }
  }

  /**
   * Check if geocoverage mutual exclusiveness is being applied
   * @param dataset: the dataset to be submitted
   * @returns: true if only one geocoverage property is set, false otherwise
   */
  public validateGeocoverage(dataset: Dataset): boolean {
    let counter = 0;
    if(dataset.continentalCoverage.length > 0) counter++;
    if(dataset.countryCoverage.length > 0) counter++;
    if(dataset.nuts1.length > 0) counter++;
    if(dataset.nuts2.length > 0) counter++;
    if(dataset.nuts3.length > 0) counter++;
    if(dataset.adm1.length > 0) counter++;
    if(dataset.adm2.length > 0) counter++;

    return ((counter <= 1) ? true: false);
  }

  /**
   * Opens a modal to create a new distribution property
   */
  openDistributionModal(index: number = null) {

    const modal = this.modalService.open(DistributionModalComponent, { size: 'lg', backdrop: 'static' });

    // If any existing distribution has been selected, pass it to the modal
    if (index != null) {
      this.distributionSelected = index;
      modal.componentInstance.distributionSelected = this.distributions[this.distributionSelected];
    }

    // Pass popovers descriptions to the modal
    modal.componentInstance.popoversDescriptions = this.datasetDescriptions;

    // Pass vocabularies to the modals
    modal.componentInstance.accessRightsValues = this.accessRightsValues;
    modal.componentInstance.formatsValues = this.formatsValues;

    // Add the distribution returned by the modal to the distribution list
    modal.result.then((result) => {
      this.addDistribution(result);
    }, (reason) => {
      // Uncheck the distribution selected when the modal is dismissed
      this.distributionSelected = null;
    });
  }

  /**
   * Submit distribution form data
   */
   public addDistribution(newDistribution: Distribution): void {

    if(this.distributionSelected != null) {
      this.distributions[this.distributionSelected] = newDistribution; // Update the distribution value
      this.distributionSelected = null;
    }else{
      this.distributions.push(newDistribution); // Add a new distribution to the list
    }

    this.alertService.success('Distribution added', this.options) // Notify success
  }

  /**
   * Duplicate an existing distribution
   * @param index: index in the array of the distribution to be duplicated
   */
   public duplicateDistribution(index: number): void {

    // Duplicate the distribution
    let duplicatedDistribution: Distribution = {...this.distributions[index]};

    // Add duplicated label to the title
    if(!duplicatedDistribution.title.includes("[DUPLICATED]")) duplicatedDistribution.title = "[DUPLICATED]" + " " + duplicatedDistribution.title;

    // Add the new distribution to the list
    this.distributions = [...this.distributions, duplicatedDistribution];

    // Notify success
    this.alertService.success('Distribution duplicated', this.options);
  }

  /**
   * Delete a distribution from the list
   */
  public deleteDistribution(distribution: number): void {
    this.distributions.splice(distribution, 1); // Delete the distribution
    this.alertService.success('Distribution deleted', this.options) // Notify success
  }

  /**
   * Opens a modal to create a new unit of analysis property
   */
   openAnalysisUnitModal(index: number = null) {

    // Check dataset type
    const datasetType: string = this.checkDatasetType();

    let modal;
    if (datasetType === 'GEOREFERENCED') {
      modal = this.modalService.open(GeoreferencedAnalysisUnitModalComponent, { size: 'lg', backdrop: 'static' });
    } else if (datasetType === 'SOCIOECONOMIC'){
      modal = this.modalService.open(SocioeconomicAnalysisUnitModalComponent, { size: 'lg', backdrop: 'static' });
    } else {
      this.alertService.warn('To create a new unit of analysis, first select the datataset type', this.options) //Notify error
      return;
    }

    // If any existing unit of analysis has been selected, pass it to the modal
    if (index != null) {
      this.analysisUnitSelected = index;
      modal.componentInstance.analysisUnitSelected = this.analysisUnits[this.analysisUnitSelected];
    }

    //Pass necessary vocabularies to the modal
    modal.componentInstance.measuresValues = this.measuresValues;
    // Pass popovers descriptions to the modal
    modal.componentInstance.popoversDescriptions = this.datasetDescriptions;

    // Add the unit of analysis returned by the modal to the unit of analysis list
    modal.result.then((result) => {
      this.addAnalysisUnit(result);
    }, (reason) => {
      // Uncheck the unit of analysis selected when the modal is dismissed
      this.analysisUnitSelected = null;
    });
  }

  /**
   * Submit the unit of analysis form data
   */
  public addAnalysisUnit(newAnalysisUnit: AnalysisUnit): void {

    // Transform temporal extent string to complete Date objects
    newAnalysisUnit.tmpExtentFrom = this.formatTmpExtentToDate(String(newAnalysisUnit.tmpExtentFrom));
    newAnalysisUnit.tmpExtentTo =  this.formatTmpExtentToDate(String(newAnalysisUnit.tmpExtentTo));

    if(this.analysisUnitSelected != null) {
      this.analysisUnits[this.analysisUnitSelected] = newAnalysisUnit; // Update the unit of analysis value
      this.analysisUnitSelected = null;
    }else{
      this.analysisUnits.push(newAnalysisUnit); // Add a new unit of analysis to the list
    }

    this.alertService.success('Unit of analysis added', this.options) //Notify success

    this.disableDatasetTypeInput();
  }

  /**
   * Delete an unit of analysis from the list and remove the relationship with a variable if exists
   */
  public deleteAnalysisUnit(analysisUnit: number): void {
   
    // Before delete it, check if it is assigned to a variable
    for (let variable of this.variables) {

      let index = variable.analysisUnits.indexOf(this.analysisUnits[analysisUnit]); // Get the index on the variable list if exists

      if (index != -1) { // The unit of analysis is present on the variable list
        variable.analysisUnits.splice(index, 1);
      }
    } 

    this.alertService.success('Unit of analysis deleted', this.options) //Notify success

    this.analysisUnits.splice(analysisUnit, 1); // Remove the unit of analysis from the list
  }

  /**
   * Opens a modal to create a new variable property 
   */
  openVariableModal(targetModal: string, index: number = null) {

    // Check dataset type
    const datasetType: string = this.checkDatasetType();

    let modal;
    if (datasetType === 'GEOREFERENCED' || datasetType === 'SOCIOECONOMIC') {

      if (datasetType === 'SOCIOECONOMIC' && targetModal == 'PRICE') {
        modal = this.modalService.open(PriceDatasetVariableModalComponent, { size: 'lg', backdrop: 'static' });
      } else {
        modal = this.modalService.open(DatasetVariableModalComponent, { size: 'lg', backdrop: 'static' });
      }
    } else {
      this.alertService.warn('To create a new dataset variable, first select the datataset type', this.options) //Notify error
      return;
    }

    // If any existing dataset variable has been selected, pass it to the modal
    if (index != null) {
      this.variableSelected = index;
      modal.componentInstance.variableSelected = this.variables[this.variableSelected];
    }

    modal.componentInstance.variableType = datasetType;

    // Pass vocabulary values to be displayed on the select dropdowns
    modal.componentInstance.mathRepValues = this.mathRepValues;
    modal.componentInstance.periodicityValues = this.periodicityValues;
    modal.componentInstance.measuresValues = this.measuresValues;
    modal.componentInstance.variableDataOriginValues = this.variableDataOriginValues;
    modal.componentInstance.currencyValues = this.currencyValues;
    modal.componentInstance.priceTypeValues = this.priceTypeValues;
    modal.componentInstance.sizeUnitValues = this.sizeUnitValues;
    modal.componentInstance.analysisUnits = this.analysisUnits;

    // Pass popovers descriptions to the modal
    modal.componentInstance.datasetDescriptions = this.datasetDescriptions;

    // Add the dataset variable returned by the modal to the dataset variables list
    modal.result.then((result) => {
      this.addDatasetVariable(result);
    }, (reason) => {
      // Uncheck the dataset variable selected when the modal is dismissed
      this.variableSelected = null;
    });
  }

  /**
   * Submit dataset variable form data
   */
  public addDatasetVariable(newVariable: DatasetVariable): void {

    // Transform temporal extent string to complete Date objects
    newVariable.tmpExtentFrom = this.formatTmpExtentToDate(String(newVariable.tmpExtentFrom));
    newVariable.tmpExtentTo =  this.formatTmpExtentToDate(String(newVariable.tmpExtentTo));

    if(this.variableSelected != null) {
      this.variables[this.variableSelected] = newVariable; // Update the the variable value
      this.variableSelected = null;
    }else{
      this.variables.push(newVariable); // Add a new variable to the list
    }
  
    this.alertService.success('Variable added', this.options) // Notify success

    this.disableDatasetTypeInput();
  }

  /**
   * Duplicate an existing variable
   * @param index: index in the array of the variable to be duplicated
   */
   public duplicateVariable(index: number): void {
    
    // Get the variable to be duplicated
    let duplicatedVariable: DatasetVariable = {...this.variables[index]};
 
    // Add duplicated label to the title
    if(!duplicatedVariable.name.includes("[DUPLICATED]")) duplicatedVariable.name = "[DUPLICATED]" + " " + duplicatedVariable.name;
 
    // Add the new variable to the list
    this.variables = [...this.variables, duplicatedVariable];
 
    // Notify success
    this.alertService.success('Variable duplicated', this.options);
  }

  /**
   * Delete a variable form the list
   */
  public deleteVariable(variable: number): void {
   
    this.variables.splice(variable, 1); // Delete the variable
    this.alertService.success('Variable deleted', this.options) // Notify success
  }

  /**
   * Check the dataset type through its value in the form
   * @returns: string -> ENVIRONMETAL, SOCIOECONOMIC or null if the input field has not been modified 
   */
  private checkDatasetType(): string {
    return this.datasetCreateForm.get('datasetType').value;
  }

  /**
   * Disable the dataset type input to avoid changes on the type
   */
  private disableDatasetTypeInput(): void {
    // Check if it is already disabled
    if (this.datasetCreateForm.get('datasetType').enabled) this.datasetCreateForm.get('datasetType').disable();
  }

  /**
   * Adds a new generation activity to the list 
   * @param value: activity value obtained from the input
   */
   public addGenerationActivity(value: string): void {

    if (!value) return;

    // Create a new DatasetGenerationActivity instance
    let generationActivity: DatasetGenerationActivity = new DatasetGenerationActivity();

    // Assign the value obtained from the input
    generationActivity.label = value;

    // TODO: check duplicate values

    // Store the new activity in the list
    // It is not possible to use the 'push' method of Typescript because it does not update the ng-select content dynamically
    this.generationActivities = [...this.generationActivities, generationActivity];
    
    // Update the form control to display the new value on the select
    this.datasetCreateForm.patchValue({
      wasGeneratedBy: this.generationActivities
    });

    // Set form control to allow the user to save the progress
    this.datasetCreateForm.controls['wasGeneratedBy'].markAsDirty();

    // Clear input field
    this.generationActivityInput.nativeElement.value = '';
  }

  /**
   * Method executed every time any value of the ng-select is modified or removed
   * @param values: updated list of generation activities
   */
  public updateGenerationActivities(values) {
    // The form control does not need to be updated because the formControlName attribute of the ng-select does it automatically
    // Update the list of activities
    this.generationActivities = values;
  }

  /**
   * Adds a new referenced resoruce to the list 
   * @param value: value obtained from the input
   */
   public addReferencedResource(value: string): void {

    if (!value) return;

    // Create a new DatasetReferencedResource instance
    let referencedResource: DatasetReferencedResource = new DatasetReferencedResource();

    // Assign the value obtained from the input
    referencedResource.label = value;

    // TODO: check duplicate values

    // Store the new resource in the list
    // It is not possible to use the 'push' method of Typescript because it does not update the ng-select content dynamically
    this.referencedResources = [...this.referencedResources, referencedResource];
    
    // Update the form control to display the new value on the select
    this.datasetCreateForm.patchValue({
      isReferencedBy: this.referencedResources
    });

    // Set form control to allow the user to save the progress
    this.datasetCreateForm.controls['isReferencedBy'].markAsDirty();

    // Clear input field
    this.referencedResourceInput.nativeElement.value = '';
  }

  /**
   * Method executed every time any value of the ng-select is modified or removed
   * @param values: updated list of referenced resources
   */
  public updateReferencedResources(values) {
    // The form control does not need to be updated because the formControlName attribute of the ng-select does it automatically
    // Update the list of referenced resources
    this.referencedResources = values;
  }

  /**
   * Form validation helpers
   */
  public fieldInvalid(form: FormGroup, fieldName: string): boolean {
    const control: AbstractControl = form.controls[fieldName];
    const invalid: boolean = control.invalid;
    const dirty: boolean = control.dirty;
    const touched: boolean = control.touched;
    return invalid && (dirty || touched);
  }

  /**
   * Adds a day to the temporal extent string and generates a complete Date
   * @param tmpExtent: string with format yyyy-mm
   */
  public formatTmpExtentToDate(tmpExtent: string): Date {

    let dateString = tmpExtent.concat("-01"); // Add the 1st day of the month
    return new Date(dateString); // Generate a Date from the string 
  }

   /**
   * Creates a string with the year and the month, deleting the day
   * @param tmpExtent: date with format yyyy-mm-dd
   */
  public formatTmpExtentToString(tmpExtent: Date): String {

    let tmpExtentDate = new Date(String(tmpExtent));

    // Month format: getMonth() starts counting from 0
    // Use 2 digit format, adding a the 0
    let month = ("0" + (tmpExtentDate.getMonth() + 1)).slice(-2);

    return tmpExtentDate.getFullYear() + "-" + month;
  }

  /**
   * Method to get all NUTS values starting with a given list of codes
   * @param level: NUTS level: 1, 2 or 3
   * @param regionType: to filter by the whole country or by a specific country region 
   */
  public getNUTSByCountry(level: number, regionType) {

    let codes: string[] = [];

    let values: VocabularyValue[] = [];
    values = this.datasetCreateForm.controls["nuts" + level].value; // Get form control values by NUTS level

    if (values.length > 0) {
      for (let value of values) {
        codes.push(value.code); // Get the NUTS codes only and store them on a list
      }
  
      this.vocabularyValueService.getVocabularyValuesByCode(codes, String(level), regionType).subscribe(
        res => this.addNUTSByCountry(res, level), // Update the input with the values found
        err => this.handleError(err)
      );
    }
  }

  /**
   * Method that updates nuts input fields with values found by filters
   * @param values: list of NUTS values (VocabularyValue)
   * @param level: NUTS level: 1, 2 or 3
   */
  public addNUTSByCountry(values: VocabularyValue[], level: number): void {
    
    if(level == 1) {
      this.datasetCreateForm.controls["nuts1"].reset();
      this.datasetCreateForm.patchValue({
        nuts1: values
      });
    } else if(level == 2) {
      this.datasetCreateForm.controls["nuts2"].reset();
      this.datasetCreateForm.patchValue({
        nuts2: values
      });
    } else {
      this.datasetCreateForm.controls["nuts3"].reset();
      this.datasetCreateForm.patchValue({
        nuts3: values
      });
    }
  }
  
  public getAllUsernames(): void {
    this.userService.getAllUsers().subscribe(
      res => this.userNames = res.map(x => x.username),
      err => this.handleError(err)
    )
  }

  /**
   * Launches a request to the backend to search for keywords based on what the user has typed in the input
   */
  private searchKeywords() {
    this.keywordsFound = concat(
      of([]), // Default keywords, an empty list
      this.keywordsInput$.pipe(
        distinctUntilChanged(),
        tap(() => this.keywordsLoading = true),
        switchMap(term => this.datasetService.getKeywordsByTitle(term).pipe(
          catchError(() => of([])), // If an error occurs, load an empty list
          tap(() => this.keywordsLoading = false)
        ))
      )
    );
  }

    /**
   * If the current user has the "user" role, can't create a finished dataset (only draft)
   */
  private getDraftValue(currentUser: User): boolean {
    return currentUser.roles.includes('USER') && currentUser.roles.length < 2 ? true : false
  }

  /**
   * Allows the user to add a new keyword, if it does not exist on the database
   * @param term: keyword to be added
   */
  public addNewKeyword = (term: string) =>{
    // Create a new Keyword instance
    let newKeyword: Keyword = new Keyword();

    // Assign the label value obtained from the input
    newKeyword.label = term;

    // Get the current list of keywords
    let currentKeywords: Keyword[] = [];
    currentKeywords = this.datasetCreateForm.get('keywords').value;
    
    // Add the new keyword to the list
    currentKeywords = [...currentKeywords, newKeyword];
    this.datasetCreateForm.patchValue({
      keywords: currentKeywords
    });
  }

  handleError(err: any): any{
    if(err.status === 403) {
      this.alertService.warn("You do not have permission to perform this action", this.optionsRouteChange);
    } else if(err.error.message) {
      this.alertService.error(err.error.message, this.optionsRouteChange);
    }
  }
}

