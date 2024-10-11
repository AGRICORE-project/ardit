import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormGroup,  FormControl, AbstractControl, Validators } from '@angular/forms';

import { VocabularyService } from '../../../services/vocabulary/vocabulary.service';
import { VocabularyValueService } from '../../../services/vocabulary-value/vocabulary-value.service';
import { AlertService } from '../../../services/alert/alert.service';
import { Vocabulary } from '../../../models/vocabulary';
import { VocabularyValue } from '../../../models/vocabulary-value';
import { RoutesNames } from '../../../shared/utils/routes.names';
import { VocabularyValueModalComponent } from '../../modals/vocabulary-value-modal/vocabulary-value-modal.component';

import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { simulateDownload } from 'src/app/shared/utils/simulate-download';

@Component({
  selector: 'app-vocabulary-edit',
  templateUrl: './vocabulary-edit.component.html',
  styleUrls: ['./vocabulary-edit.component.css']
})
export class VocabularyEditComponent implements OnInit {

  vocabularyId = Number(this.route.snapshot.paramMap.get('id')); // ID of the selected vocabulary

  vocabulary: Vocabulary = null; // Vocabulary to be edited
  vocabularyValues: VocabularyValue[] = null; // Vocabulary values

  vocabularyValuesFiltered: VocabularyValue[]; // List storing the vocabularies values displayed on the screen. Its content can change dinamically depending on the searches

  searchInput: string = ""; // Variable to store the text typed by the user in the search box

  vocabularyTopics: String[]; // List of topis

  //Pagination
  page = 1;
  pageSize = 10;
  collectionSize = 0;

  // Form groups
  vocabularyForm: FormGroup;

  valueSelected: number = null;

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
  vocabularyListRoute = `/${RoutesNames.VOCABULARIES}`;

  constructor(private vocabularyService: VocabularyService, private vocabularyValueService: VocabularyValueService, private router: Router, 
    private route: ActivatedRoute, private modalService: NgbModal, public alertService: AlertService) { }

  ngOnInit(): void {

    this.getVocabulary(this.vocabularyId); // Get the vocabulary data

    this.getVocabularyValues(this.vocabularyId); // Get the vocabulary data

    this.getVocabularyTopics(); // Get the list of possible topics

    // Vocabulary form validators
    this.vocabularyForm = new FormGroup({
      name: new FormControl(null, [
        Validators.required
      ]),
      description: new FormControl(null, [
        Validators.required
      ]),
      url: new FormControl(null, []),
      topic: new FormControl(null, [
        Validators.required
      ])
    });
  }

  /**
   * Gets all the vocabulary data except its values
   * @param id:  vocabulary id
   */
  private getVocabulary(id: number): void {
    this.vocabularyService.getVocabulary(id).subscribe(
      res => {
        this.vocabulary = res,
        // Populate form inputs with the given vocabulary values
        this.vocabularyForm.patchValue({
          name: this.vocabulary.name,
          description: this.vocabulary.description,
          url: this.vocabulary.url,
          topic: this.vocabulary.topic
        });
      },
      err => this.handleError(err)
    );
  }

  /**
   * Gets the vocabulary values
   * @param id: vocabulary id
   */
  private getVocabularyValues(id: number): void {
    this.vocabularyValueService.getVocabularyValues(id).subscribe(
      res => {
        this.vocabularyValues = res,
        this.collectionSize = this.vocabularyValues.length, // Pagination initialisation
        this.vocabularyValuesFiltered = this.vocabularyValues  // Dinamic list initialisation
      },
      err => this.handleError(err)
    );
  }

  /**
   * Gets the list of possible topics or dataset properties
   */
  private getVocabularyTopics(): void {
    this.vocabularyTopics = null;
    this.vocabularyService.getVocabularyTopics().subscribe(
      res => this.vocabularyTopics = res,
      err => this.handleError(err)
    );
  }

  /**
   * Method that is automatically triggered when the user types something in the search box
   * @param event: input text
   */
  public onSearch(text: string) {
    this.vocabularyValuesFiltered = this.vocabularyValues.filter(vocabularyValue => {
      const term = text.toLowerCase();
      return vocabularyValue.code.toLowerCase().includes(term) ||
      vocabularyValue.label.toLowerCase().includes(term);
    });

    this.collectionSize = this.vocabularyValuesFiltered.length; // Update the table size
  }

  /**
   * Update the data of the vocabulary, except its values
   */
  public updateVocabulary(): void {

    let vocabulary:Vocabulary = this.vocabularyForm.value;
    vocabulary.id = this.vocabularyId;

    this.vocabularyService.updateVocabulary(vocabulary).subscribe(
      _ => {
        this.router.navigate([`/${RoutesNames.VOCABULARIES}`]),
        this.alertService.success("Vocabulary updated", this.optionsRouteChange)}, //Notify success
      err => this.handleError(err)
    );
  }

  /**
   * Opens a modal to create a new value or edit an existing one
   */
  public openValueModal(index: number = null) {

    const modal = this.modalService.open(VocabularyValueModalComponent, { size: 'lg', backdrop: 'static' });

    // If an existing vocabulary value has been selected
    if (index != null) { 

      this.valueSelected = index;
      
      // Populate modal with value data to edit it
      modal.componentInstance.vocabularyValueSelected = this.vocabularyValuesFiltered[index];
    }
    
    // Triggered when the moddal is closed
    modal.result.then((result) => {
      if(this.valueSelected != null) {
        this.updateVocabularyValue(result); // Update an existing value
      }else{
        this.createVocabularyValue(result); // Add a new value to the list
      }
    }, (reason) => {
    });
  }

  /**
   * Create a new vocabulary value and add it to the list
   * @param value: new vocabulary value
   */
  public createVocabularyValue(value: VocabularyValue): void {
    this.vocabularyValueService.createVocabularyValue(this.vocabularyId, value).subscribe(
      res => {
        this.vocabularyValues.unshift(res); // Add a new value to the list
        this.vocabularyValuesFiltered = this.vocabularyValues; // Update the table list
        this.alertService.success("Vocabulary value created", this.options)}, //Notify success
      err => this.handleError(err)
    );
  }

  /**
   * Update a value from the list
   * @param value: vocabulary value to update
   */
  public updateVocabularyValue(value: VocabularyValue): void {
    this.vocabularyValueService.updateVocabularyValue(this.vocabularyId, value.id, value).subscribe(
      res => {
        this.getVocabularyValues(this.vocabularyId); // Reload the table
        this.valueSelected = null;
        this.alertService.success("Vocabulary value updated", this.options)}, // Notify success
      err => this.handleError(err)
    );
  }

  /**
   * Delete a value from the list
   * @param index: position in the list of vocabulary values
   */
  public deleteVocabularyValue(index: number): void {

    const value = this.vocabularyValuesFiltered[index];

    //API delete request
    this.vocabularyValueService.deleteVocabularyValue(this.vocabularyId, value.id).subscribe(
      _ => {
        // Reload the table
        this.getVocabularyValues(this.vocabularyId);
        this.alertService.success("Vocabulary value deleted", this.options)}, // Notify success
      err => this.handleError(err)
    );
  }

  /**
   * Form validation helpers
   */
  public fieldInvalid(form: FormGroup, fieldName: string): boolean {
    const control: AbstractControl = form.controls[fieldName];
    const invalid: boolean = control.invalid;
    return invalid;
  }

   /**
  * Downloads vocabulary in JSON format
  */
   public getJsonVocabulary(id: number): void {
     this.vocabularyValueService.getVocabularyValues(id).subscribe(
       res => {
         const vocabulary: VocabularyValue[] = res
         const jsonObj = "data:text/json;charset=utf-8," + encodeURIComponent(JSON.stringify(vocabulary, null, 1));
         simulateDownload(jsonObj)
       },
       err => this.handleError(err)
     )
   }

  /**
   * Method that is automatically triggered when changing pages, updating the value of the page
   * @param event: new page
   */
  public pageChanged(event: any): void {
    this.page = event; // New page
  }

  handleError(err: any): any{
    if(err.status === 403) {
      this.alertService.warn("You do not have permission to perform this action", this.optionsRouteChange);
    } else if(err.error.message) {
      this.alertService.error(err.error.message, this.optionsRouteChange);
    }
  }
}
