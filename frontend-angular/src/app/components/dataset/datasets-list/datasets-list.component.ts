import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { FormGroup, FormControl } from '@angular/forms';

import { DatasetService } from '../../../services/dataset/dataset.service';
import { AlertService } from '../../../services/alert/alert.service';
import { RoutesNames } from '../../../shared/utils/routes.names';
import { VocabularyValue } from '../../../models/vocabulary-value';
import { VocabularyValueService } from '../../../services/vocabulary-value/vocabulary-value.service';
import { Catalogue } from '../../../models/catalogue';
import { CatalogueService } from '../../../services/catalogue/catalogue.service';
import { DatasetSimplified } from '../../../models/dto/dataset-simplified';
import { AuthService } from 'src/app/services/auth/auth.service';
import { User } from '../../../models/user';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { SubscriptionService } from '../../../services/subscription/subscription.service';

@Component({
  selector: 'app-datasets-list',
  templateUrl: './datasets-list.component.html',
  styleUrls: ['./datasets-list.component.css']
})
export class DatasetsListComponent implements OnInit {
  
  //Subscription
  subsText: string = ''
  subsError: string = ''

  // Form groups
  searchForm: FormGroup;
  filtersForm: FormGroup;

  // Listed datasets
  datasets: DatasetSimplified[];

  // Map to display all the filters applied with a list of badges
  queryParams = new Map();

  // Routes
  datasetRoute = `/${RoutesNames.DATASETS}/`;
  datasetEditRoute = `/${RoutesNames.DATASET_EDIT}/`;
  datasetCreateRoute = `/${RoutesNames.DATASET_CREATE}`;

  // Search filters selects
  tasksValues: VocabularyValue[];
  taskTopic = 'report_to_task';

  catalogues: Catalogue[];

  languageValues: VocabularyValue[];
  languageTopic = 'language';

  periodicityValues: VocabularyValue[];
  periodicityTopic = 'periodicity';

  formatsValues: VocabularyValue[];
  formatsTopic = 'format';

  continentsValues: VocabularyValue[];
  continentTopic = 'continent';

  countriesValues: VocabularyValue[];
  countryTopic = 'country';

  nuts1Values: VocabularyValue[];
  nuts1Topic = 'nuts1';

  nuts2Values: VocabularyValue[];
  nuts2Topic = 'nuts2';

  nuts3Values: VocabularyValue[];
  nuts3Topic = 'nuts3';

  //Pagination
  page: number = 1;
  pageSize: number = 10;
  collectionSize: number = 0;

  //User roles
  currentUser: User
  justUser: boolean = false;

  // Config options for alerts
  options = {
    autoClose: true,
    keepAfterRouteChange: false
  };

  sortParam: string;

  optionsRouteChange = {
    autoClose: true,
    keepAfterRouteChange: true
  };

  constructor(private datasetService: DatasetService, private vocabularyValueService: VocabularyValueService, private catalogueService: CatalogueService,
    private alertService: AlertService, private authService: AuthService, private route: ActivatedRoute, private router: Router, private modalService: NgbModal,
    private subscriptionService: SubscriptionService) {}

  ngOnInit(): void {

    this.currentUser = this.authService.getCurrentUserValue();
    
    // Forms initialisation
    this.searchForm = new FormGroup({
      query: new FormControl(null, [])
    });

    this.filtersForm = new FormGroup({
      type: new FormControl("all", []),
      draft: new FormControl("all", []),
      task: new FormControl(null, []),
      producer: new FormControl(null, []),
      catalogue: new FormControl(null, []),
      analysisUnit: new FormControl(null, []),
      variable: new FormControl(null, []),
      from: new FormControl(null, []),
      to: new FormControl(null, []),
      language: new FormControl(null, []),
      periodicity: new FormControl(null, []),
      format: new FormControl(null, []),
      continent: new FormControl(null, []),
      country: new FormControl(null, []),
      nuts1: new FormControl(null, []),
      nuts2: new FormControl(null, []),
      nuts3: new FormControl(null, [])
    });

    // Populate selects with vocabulary values
    this.getWPTasks();
    this.getCatalogues();
    this.getLanguageValues();
    this.getPeriodicityValues();
    this.getFormatsValues();
    this.getContinentsValues();
    this.getCountriesValues();
    this.getNuts1Values();
    this.getNuts2Values();
    this.getNuts3Values();
    this.isJustUser();

    // Check query params for search filters
    this.route.queryParams.subscribe(params => {
      
      // Check if there are query params
      if(Object.keys(params).length) {

        setTimeout(()=>{
          
          // Parse query params
          this.parseQueryParams(params);

          //Check for sort param if exist
          if(params.sortBy) {
            this.queryParams.set("sortBy", params.sortBy);
            this.sortParam = params.sortBy
          }
          
          // Search datasets according to the selected query params
          this.getDatasets(this.page, this.queryParams);
        }, 3000);
  
      } else {

        // No query params, default load
        this.getDatasets();
      }
    });
  }

  /**
   * Method executed when the page is loading, takes all the query params and initialises the forms,
   * in order to visualize the selected filters
   * @param params: route params
   */
  public parseQueryParams(params: Params): void {

    // Clear query params map
    this.queryParams.clear();

    // Initialise the filter forms with the query params values in the route
    if(params.page) this.page = Number.parseInt(params.page, 10) || 1;
    if(params.query) {
      this.queryParams.set("query", params.query);
      this.searchForm.patchValue({query: params.query});
    }
    if(params.type) {
      this.queryParams.set("type", params.type);
      this.filtersForm.patchValue({type: params.type});
    } else {
      this.queryParams.set("type", "all");
    }
    if(params.draft) {
      this.queryParams.set("draft", params.draft);
      this.filtersForm.patchValue({draft: params.draft});
    } else {
      this.queryParams.set("draft", "all");
    }
    if(params.task) {
      this.queryParams.set("task", params.task);
      let taskSelected = this.tasksValues.find(p => p.label == params.task);
      this.filtersForm.patchValue({task: taskSelected});
    }
    if(params.producer) {
      this.queryParams.set("producer", params.producer);
      this.filtersForm.patchValue({producer: params.producer});
    }
    if(params.catalogue) {
      this.queryParams.set("catalogue", params.catalogue);
      let catalogueSelected = this.catalogues.find(c => c.id == params.catalogue);
      this.filtersForm.patchValue({catalogue: catalogueSelected});
    }
    if(params.analysisUnit) {
      this.queryParams.set("analysisUnit", params.analysisUnit);
      this.filtersForm.patchValue({analysisUnit: params.analysisUnit});
    }
    if(params.variable) {
      this.queryParams.set("variable", params.variable);
      this.filtersForm.patchValue({variable: params.variable});
    }
    if(params.from) {
      this.queryParams.set("from", params.from);
      this.filtersForm.patchValue({from: params.from});
    }
    if(params.to){
      this.queryParams.set("to", params.to);
      this.filtersForm.patchValue({to: params.to});
    }
    if(params.language) {
      this.queryParams.set("language", params.language);
      let languageSelected = this.languageValues.find(l => l.label == params.language);
      this.filtersForm.patchValue({language: languageSelected});
    }
    if(params.periodicity) {
      this.queryParams.set("periodicity", params.periodicity);
      let periodicitySelected = this.periodicityValues.find(p => p.label == params.periodicity);
      this.filtersForm.patchValue({periodicity: periodicitySelected});
    }
    if(params.format) {
      this.queryParams.set("format", params.format);
      let formatSelected = this.formatsValues.find(f => f.label == params.format);
      this.filtersForm.patchValue({format: formatSelected});
    }
    if(params.continent) {
      this.queryParams.set("continent", params.continent);
      let continentSelected = this.continentsValues.find(c => c.label == params.continent);
      this.filtersForm.patchValue({continent: continentSelected});
    }
    if(params.country) {
      this.queryParams.set("country", params.country);
      let countrySelected = this.countriesValues.find(c => c.label == params.country);
      this.filtersForm.patchValue({country: countrySelected});
    }
    if(params.nuts1) {
      this.queryParams.set("nuts1", params.nuts1);
      let nuts1Selected = this.nuts1Values.find(n => n.code == params.nuts1);
      this.filtersForm.patchValue({nuts1: nuts1Selected});
    }
    if(params.nuts2) {
      this.queryParams.set("nuts2", params.nuts2);
      let nuts2Selected = this.nuts2Values.find(n => n.code == params.nuts2);
      this.filtersForm.patchValue({nuts2: nuts2Selected});
    }
    if(params.nuts3) {
      this.queryParams.set("nuts3", params.nuts3);
      let nuts3Selected = this.nuts3Values.find(n => n.code == params.nuts3);
      this.filtersForm.patchValue({nuts3: nuts3Selected});
    }
  }

  /**
   * Method executed when the user submit a search for datasets by their title
   */
  public submitTitleQuery(): void {

    // Get the text introduced in the searchbox
    let queryString: string = this.searchForm.controls['query'].value;

    // If the user deletes the text in input box, the value must be changed to null to remove the query param form the route
    if (queryString == "") queryString = null;

    // On each new search, restart the pagination to return to the first one
    this.page = 1;

    // Content to null to display the spinner icon
    this.datasets = null;

    // Reload the page adding the new query param
    this.router.navigate([this.datasetRoute], { queryParams: { query: queryString, page: this.page }, queryParamsHandling: 'merge' });
  }

  /**
   * Method executed when clicking on the "Apply" search filters button
   * Gets the values of each search fitler and builds the new route with the query params 
   */
  public submitSearchFilters(): void {
    // Validate geocoverage mutual exclusiveness
    if (this.validateGeocoverage(this.filtersForm.value)) {

      let filters: any = this.filtersForm.value;

      // Save all the filters that have been modified2
      if(filters.task) filters.task = filters.task.label;
      if(filters.catalogue) filters.catalogue = filters.catalogue.id;
      if(filters.language)filters.language = filters.language.label;
      if(filters.periodicity) filters.periodicity = filters.periodicity.label;
      if(filters.format) filters.format = filters.format.label;
      if(filters.continent) filters.continent = filters.continent.label;
      if(filters.country) filters.country = filters.country.label;
      if(filters.nuts1) filters.nuts1 = filters.nuts1.code;
      if(filters.nuts2) filters.nuts2 = filters.nuts2.code;
      if(filters.nuts3) filters.nuts3 = filters.nuts3.code;

      // Input text boxes content to null
      // If the user deletes the text in input box, the value must be changed to null to remove the query param form the route
      if(filters.producer == "") filters.producer = null;
      if(filters.analysisUnit == "") filters.analysisUnit = null;
      if(filters.variable == "") filters.variable = null;
      if(filters.from == "") filters.from = null;
      if(filters.to == "") filters.to = null;

      // On each new search, restart the pagination to return to the first one
      this.page = 1;

      // Content to null to display the spinner icon
      this.datasets = null;

      //If user not logged in the draft param will be false
      if (!this.currentUser) this.filtersForm.value.draft = false;
        
      // Reload the page updating the route with the query params
      this.router.navigate([this.datasetRoute], { queryParams: { page: this.page, type: filters.type, draft: filters.draft, task: filters.task, producer: filters.producer, 
       catalogue: filters.catalogue, analysisUnit: filters.analysisUnit, variable: filters.variable, from: filters.from, to: filters.to, language: filters.language, 
       periodicity: filters.periodicity, format: filters.format, continent: filters.continent, country: filters.country, nuts1: filters.nuts1, nuts2: filters.nuts2, nuts3: filters.nuts3 } , queryParamsHandling: 'merge' });

    } else {
      // Geocoverage mutual exclusiveness warning
      this.alertService.warn('There can be only one property set in the filters related to the geographical coverage', this.options);
    }
  }

  /**
   * Method executed when clicking on the button to delete all search filters
   * Reload the page purging all query params anc clearing the forms
   */
  public clearFilters(): void {
    // Clear query params map
    this.queryParams.clear();

    //In case is set, we reset the sort param
    this.sortParam = null;

    // Clear forms
    this.searchForm.reset();
    this.filtersForm.reset();
  
    // Reload the page purging the existing query params
    this.router.navigate([this.datasetRoute], { queryParams: {} });
  }

  /**
   * Method executed when changing the page in the pagination component
   * @param newPage: new page of elements
   */
   public pageChanged(newPage: number): void {

    // Content to null to display the spinner icon
    this.datasets = null;

    // Reload the page adding in the route the page query parameter
    this.router.navigate([this.datasetRoute], { queryParams: { page: newPage }, queryParamsHandling: 'merge' });
  }

  /**
   * Service call to get datasets based on the selected search parameters and filters
   * 
   * If no parameters are passed, by default, will search for all datasets order
   * by last update date
   */
  public getDatasets(page: number = 1, queryParams: Map<string, string> = null): void {

    this.datasets = null;
    this.datasetService.getDatasetsByFilters(page-1, queryParams).subscribe(
      res => {
        this.datasets = res.content,
        this.collectionSize = res.totalElements
      },
      err => this.handleError(err)
    );
  }

  /**
   * Methods used to populate the selects with data
   */
  public getWPTasks(): void{
    this.tasksValues = null;
    this.vocabularyValueService.getVocabularyValuesByTopic(this.taskTopic).subscribe(
      res => this.tasksValues = res,
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

  public getFormatsValues(): void{
    this.formatsValues = null;
    this.vocabularyValueService.getVocabularyValuesByTopic(this.formatsTopic).subscribe(
      res => this.formatsValues = res,
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

  /**
   * Check if geocoverage mutual exclusiveness is being applied
   * @param formData: data submitted on the search filters form
   * @returns: true if only one geocoverage property is set, false otherwise
   */
  public validateGeocoverage(filterData: any): boolean {
    let counter = 0;
    if(filterData.continent) counter++;
    if(filterData.country) counter++;
    if(filterData.nuts1) counter++;
    if(filterData.nuts2) counter++;
    if(filterData.nuts3) counter++;

    return ((counter <= 1) ? true: false);
  }

  /**
  * Check if user only role is "User"
  */
  private isJustUser() {
    if (this.currentUser && this.currentUser.roles.includes('USER') && this.currentUser.roles.length === 1) { this.justUser = true }
  }

  /**
  * Method to open the subscribe notifications modal
  */
  public openSubscribeModal(modal: any) {
    this.modalService.open(modal, { size: 'md', backdrop: 'static' }).result.then((result) => {}, (reason) => {});
  }

  /**
  * Dataset subscription notifications method
  */
  public datasetSubscribe(){
    this.subscriptionService.dtsubscribe().subscribe(
      res => {
        this.currentUser.subscribed = true
        this.subsText = 'You have successfully subscribed.'
      },
      err => {
        this.subsError = err.error.message
      }
    )
  }

  /**
  * Dataset unsubscription notifications method
  */
  public datasetUnsubscribe(){
    this.subscriptionService.dtunsubscribe().subscribe(
      res => {
        this.currentUser.subscribed = false
        this.subsText = 'You are not subscribed now.'
      },
      err => {
        this.subsError = err.error.message
      }
    )
  }

  /**
  * Sorting datasets by temporal extent
  */
  public sortDatasets() {

    //Set datasets to null to show spinner
    this.datasets = null;

    this.sortParam = this.queryParams.get('sortBy');
    
    // Set dataset direction
    if (!this.sortParam || this.sortParam == '-tmpExtentFrom') {
      this.sortParam  = 'tmpExtentFrom'
    } else {
      this.sortParam  = '-tmpExtentFrom'
    } 
    
    //Reaload page with new param
    this.router.navigate([this.datasetRoute], { queryParams: { sortBy: this.sortParam , page: this.page }, queryParamsHandling: 'merge' });
  }

  handleError(err: any): any{
    if(err.status === 403) {
      this.alertService.warn("You do not have permission to perform this action", this.optionsRouteChange);
    } else if(err.error.message) {
      this.alertService.error(err.error.message, this.optionsRouteChange);
    }
  }
}
