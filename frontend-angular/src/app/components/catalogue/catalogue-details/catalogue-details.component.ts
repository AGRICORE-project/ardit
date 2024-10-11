import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FormGroup, FormControl, AbstractControl, Validators } from '@angular/forms';

import { Catalogue } from '../../../models/catalogue';
import { Dataset } from '../../../models/dataset';
import { DatasetService } from '../../../services/dataset/dataset.service';
import { CatalogueService } from '../../../services/catalogue/catalogue.service';
import { AlertService } from '../../../services/alert/alert.service';
import { RoutesNames } from '../../../shared/utils/routes.names';
import { VocabularyValue } from '../../../models/vocabulary-value';
import { VocabularyValueService } from '../../../services/vocabulary-value/vocabulary-value.service';
import { DatasetDTO } from '../../../models/dto/dataset-dto';

@Component({
  selector: 'app-catalogue-details',
  templateUrl: './catalogue-details.component.html',
  styleUrls: ['./catalogue-details.component.css']
})
export class CatalogueDetailsComponent implements OnInit {

  catalogue: Catalogue;
  catalogueId: number;

  datasets: DatasetDTO[];

  //Pagination
  page: number = 1;
  pageSize: number = 10;
  collectionSize: number = 0;

  // Forms
  editCatalogueForm: FormGroup;

  // Select fields
  periodicityValues: VocabularyValue[];
  periodicityTopic = 'periodicity';

  languageValues: VocabularyValue[];
  languageTopic = 'language';

  themesValues: VocabularyValue[];
  themesTopic = 'theme';

  // Routes
  datasetsRoute = `/${RoutesNames.DATASETS}/`;
  cataloguesListRoute = `/${RoutesNames.CATALOGUES}/`;
  catalogueRoute = `/${RoutesNames.CATALOGUES}/`;

  // Config options for alerts
  options = {
    autoClose: true,
    keepAfterRouteChange: false
  };

  optionsRouteChange = {
    autoClose: true,
    keepAfterRouteChange: true
  };

  constructor(private datasetService: DatasetService, private catalogueService: CatalogueService, 
    private alertService: AlertService, private router: Router, private route: ActivatedRoute, private vocabularyValueService: VocabularyValueService, private modalService: NgbModal) { }

  ngOnInit(): void {
    this.catalogueId = Number(this.route.snapshot.paramMap.get('id'));

    this.getCatalogue(); // Get catalogue info

     // Populate selects with vocabulary values
     this.getPeriodicityValues();
     this.getLanguageValues();
     this.getThemesValues();

    // Form validators
    this.editCatalogueForm = new FormGroup({
      title: new FormControl(null, [
        Validators.required
      ]),
      description: new FormControl(null, [
        Validators.maxLength(500)
      ]),
      creator: new FormControl(null, []),
      publisher: new FormControl(null, []),
      link: new FormControl(null, []),
      tmpExtentFrom: new FormControl(null, []),
      tmpExtentTo: new FormControl(null, []),
      periodicity: new FormControl(null, []),
      languages: new FormControl([], []),
      themes: new FormControl([], [])
    });

    // Check query params for search filters
    this.route.queryParams.subscribe(params => {

      // Check if there are query params
      if(Object.keys(params).length) {

        // Parse query params
        if(params.page) this.page = Number.parseInt(params.page, 10) || 1;

        // Search datasets according to the selected page
        this.getCatalogueDatasets(this.page);
         
      } else {

        // No query params, default load
        this.getCatalogueDatasets();
      }
    });
  }

  /**
   * Get the catalogue data
   */
  public getCatalogue(): void {
    this.catalogueService.getCatalogue(this.catalogueId).subscribe(
      res => {
        this.catalogue = res;
        this.getCatalogueDatasets();
      },
      err => this.handleError(err)
    );
  }

  /**
   * Get a list of datasets linked with the catalogue
   */
  public getCatalogueDatasets(page: number = 1): void {
    this.datasetService.getDatasetsByCatalogue(this.catalogueId, page-1).subscribe(
      res => {
        this.datasets = res.content,
        this.collectionSize = res.totalElements
      },
      err => this.handleError(err)
    );
  }

  /**
   * Method executed when changing the page in the pagination component
   * @param newPage: new page of elements
   */
   public pageChanged(newPage: number): void {

    // Reload the page adding in the route the page query parameter
    this.router.navigate([this.catalogueRoute + this.catalogueId], { queryParams: { page: newPage }, queryParamsHandling: 'merge' });
  }

  public getPeriodicityValues(): void{
    this.periodicityValues = null;
    this.vocabularyValueService.getVocabularyValuesByTopic(this.periodicityTopic).subscribe(
      res => this.periodicityValues = res,
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

  public getThemesValues(): void{
    this.themesValues = null;
    this.vocabularyValueService.getVocabularyValuesByTopic(this.themesTopic).subscribe(
      res => this.themesValues = res,
      err => this.handleError(err)
    );
  }

  /**
   * Open a new modal to edit an existing Catalogue
   * @param targetModal 
   */
  public openEditModal(targetModal: any) {
    this.modalService.open(targetModal, { size: 'lg', backdrop: 'static' }).result.then((result) => {}, (reason) => {
      this.editCatalogueForm.reset(); // Reset modal form
    });

    // Populate modal form with Catalogue entity data
    this.editCatalogueForm.patchValue({
      title: this.catalogue.title,
      description: this.catalogue.description,
      creator: this.catalogue.creator,
      publisher: this.catalogue.publisher,
      link: this.catalogue.link,
      tmpExtentFrom: this.formatTmpExtentToString(this.catalogue.tmpExtentFrom),
      tmpExtentTo: this.formatTmpExtentToString(this.catalogue.tmpExtentTo),
      periodicity: this.catalogue.periodicity,
      languages: this.catalogue.languages,
      themes: this.catalogue.themes
    });
  }

  /**
   * Submit the catalogue edition form content
   */
  public editCatalogue(): void {

    let catalogue: Catalogue = this.editCatalogueForm.value;
    catalogue.id = this.catalogueId;

    // Transform temporal extent string to complete Date objects
    if (catalogue.tmpExtentFrom) catalogue.tmpExtentFrom = this.formatTmpExtentToDate(String(catalogue.tmpExtentFrom));
    if (catalogue.tmpExtentTo) catalogue.tmpExtentTo =  this.formatTmpExtentToDate(String(catalogue.tmpExtentTo));

    this.catalogueService.updateCatalogue(catalogue).subscribe(
      res => {
        this.catalogue = res,
        this.alertService.success("Catalogue updated", this.options);
      },
      err => this.handleError(err)
    );
  }

  /**
   * Open a new modal to delete an existing Catalogue
   * @param targetModal 
   */
  public openDeleteModal(targetModal: any) {
    this.modalService.open(targetModal, { size: 'lg', backdrop: 'static' }).result.then((result) => {}, (reason) => {});
  }

  /**
   * Submit the catalogue deletion form content
   */
  public deleteCatalogue(): void {

    this.modalService.dismissAll(); // Dismiss modal
    
    this.catalogueService.deleteCatalogue(this.catalogueId).subscribe(
      _ => {
        this.alertService.success('Catalogue deleted', this.optionsRouteChange), // Notify success
        this.router.navigate([`/${RoutesNames.CATALOGUES}`]) // Redirect to catalogue list
      },
      err => this.handleError(err)
    );
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
  public formatTmpExtentToString(tmpExtent: Date): any {

    if (tmpExtent) {
      let tmpExtentDate = new Date(String(tmpExtent));

      // Month format: getMonth() starts counting from 0
      // Use 2 digit format, adding a the 0
      let month = ("0" + (tmpExtentDate.getMonth() + 1)).slice(-2);
  
      return tmpExtentDate.getFullYear() + "-" + month;
    }

    return null;
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

  handleError(err: any): any{
    if(err.status === 403) {
      this.alertService.warn("You do not have permission to perform this action", this.optionsRouteChange);
    } else if(err.error.message) {
      this.alertService.error(err.error.message, this.optionsRouteChange);
    }
  }
}
