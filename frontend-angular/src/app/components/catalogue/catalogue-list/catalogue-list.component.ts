import { Component, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FormGroup, FormControl, AbstractControl, Validators } from '@angular/forms';

import { Catalogue } from '../../../models/catalogue';
import { CatalogueService } from '../../../services/catalogue/catalogue.service';
import { AlertService } from '../../../services/alert/alert.service';
import { RoutesNames } from '../../../shared/utils/routes.names';
import { VocabularyValue } from '../../../models/vocabulary-value';
import { VocabularyValueService } from '../../../services/vocabulary-value/vocabulary-value.service';


@Component({
  selector: 'app-catalogue-list',
  templateUrl: './catalogue-list.component.html',
  styleUrls: ['./catalogue-list.component.css']
})
export class CatalogueListComponent implements OnInit {

  catalogues: Catalogue[]; // List of catalogues

  // Forms
  createCatalogueForm: FormGroup;

  // Select fields
  periodicityValues: VocabularyValue[];
  periodicityTopic = 'periodicity';

  languageValues: VocabularyValue[];
  languageTopic = 'language';

  themesValues: VocabularyValue[];
  themesTopic = 'theme';

  // Routes
  catalogueRoute = `/${RoutesNames.CATALOGUES}/`;

  //Pagination
  page: number = 1;
  pageSize: number = 10;

  // Config options for alerts
  options = {
    autoClose: true,
    keepAfterRouteChange: false
  };

  optionsRouteChange = {
    autoClose: true,
    keepAfterRouteChange: true
  };

  constructor(private catalogueService: CatalogueService, private alertService: AlertService, private vocabularyValueService: VocabularyValueService, private modalService: NgbModal) { }

  ngOnInit(): void {

    // Get catalogues available
    this.getCatalogues();

    // Populate selects with vocabulary values
    this.getPeriodicityValues();
    this.getLanguageValues();
    this.getThemesValues();

    // Form validators
    this.createCatalogueForm = new FormGroup({
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
   * Get all catalogues available
   */
   public getCatalogues(): void {
    this.catalogues = null;
    this.catalogueService.getAllCatalogues().subscribe(
      res => this.catalogues = res,
      err => this.handleError(err)
    );
  }

  /**
   * Open a new modal to edit an existing Catalogue
   * @param targetModal 
   */
   public openCreateModal(targetModal: any) {
    this.modalService.open(targetModal, { size: 'lg', backdrop: 'static' }).result.then((result) => {
    }, (reason) => {
      this.createCatalogueForm.reset(); // Reset modal form
    });
  }

  /**
   * Submit the catalogue creation form content
   */
  public createCatalogue(): void {

    let catalogue: Catalogue = this.createCatalogueForm.value; // Form content

    // Transform temporal extent string to complete Date objects
    if (catalogue.tmpExtentFrom) catalogue.tmpExtentFrom = this.formatTmpExtentToDate(String(catalogue.tmpExtentFrom));
    if (catalogue.tmpExtentTo) catalogue.tmpExtentTo =  this.formatTmpExtentToDate(String(catalogue.tmpExtentTo));

    this.catalogueService.createCatalogue(catalogue).subscribe(
      res => {
        this.alertService.success('Catalogue created', this.optionsRouteChange), // Notify success
        this.catalogues.unshift(res),
        this.createCatalogueForm.reset(); // Reset modal form
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
