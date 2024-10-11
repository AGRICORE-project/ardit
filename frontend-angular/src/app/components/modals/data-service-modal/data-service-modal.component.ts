import { Component, OnInit, Input, ViewChild, ElementRef } from '@angular/core';
import { NgbActiveModal, NgbDateParserFormatter } from '@ng-bootstrap/ng-bootstrap';
import { FormGroup, AbstractControl, FormControl, Validators } from '@angular/forms';

import { DataService } from '../../../models/data-service';

import { DatasetDescription } from '../../../models/dataset-description';
import { VocabularyValue } from '../../../models/vocabulary-value';
import { DataServiceServedDataset } from '../../../models/data-service-served-dataset';

@Component({
  selector: 'app-data-service-modal',
  templateUrl: './data-service-modal.component.html',
  styleUrls: ['./data-service-modal.component.css']
})
export class DataServiceModalComponent implements OnInit {

  @Input() dataServiceSelected: DataService; // Data service selected for edition
  @Input() popoversDescriptions: {[property: string]: DatasetDescription;}; // List of popovers help descriptions
  @Input() accessRightsValues: VocabularyValue[];

  @ViewChild('servedDataset') servedDatasetInput: ElementRef;

  // Modal title
  title: string = "Specify a data service"

  // Form groups
  dataServiceForm: FormGroup;

  servedDatasets: DataServiceServedDataset[] = []; // List of datasets served by the data service

  constructor(public activeModal: NgbActiveModal, private ngbDateParserFormatter: NgbDateParserFormatter) { }

  ngOnInit(): void {

    // Data service form validators
    this.dataServiceForm = new FormGroup({
      title: new FormControl([], [
        Validators.required
      ]),
      description: new FormControl(null, [
        Validators.maxLength(1000)
      ]),
      issued: new FormControl(null, []),
      modified: new FormControl(null, []),
      creator: new FormControl(null, []),
      publisher: new FormControl(null, []),
      accessRights: new FormControl(null, []),
      endpointUrl: new FormControl(null, []),
      endpointDescription: new FormControl(null, []),
      servedDatasets: new FormControl([], [])
    });

    // If the user has selected an exsting data service to edit it, populate the form with its attributes
    if (this.dataServiceSelected) {
      this.title = "Edit a data service"; // Change modal title
      this.populateModal();
    }
  }

  /**
   * Populate the modal with a given data service data
  */
   private populateModal(): void {

    // Initialise list of served datasets with those of the selected data service
    this.servedDatasets = this.dataServiceSelected.servedDatasets;

    this.dataServiceForm.patchValue({
      title: this.dataServiceSelected.title,
      description: this.dataServiceSelected.description,
      issued: this.ngbDateParserFormatter.parse(this.dataServiceSelected.issued),
      modified: this.ngbDateParserFormatter.parse(this.dataServiceSelected.modified),
      creator: this.dataServiceSelected.creator,
      publisher: this.dataServiceSelected.publisher,
      accessRights: this.dataServiceSelected.accessRights,
      endpointUrl: this.dataServiceSelected.endpointUrl,
      endpointDescription: this.dataServiceSelected.endpointDescription,
      servedDatasets: this.dataServiceSelected.servedDatasets
    });
  }

  /**
   * Retrieve the values inserted in the form and return them to the parent component
  */
  public submitDataService(): void {

    let newDataService: DataService = this.dataServiceForm.value; // Get form values

    if(this.dataServiceSelected) newDataService.id = this.dataServiceSelected.id; //Assign ID

    // NgbDateStruct to Date
    newDataService.issued = this.ngbDateParserFormatter.format(this.dataServiceForm.controls['issued'].value);
    newDataService.modified = this.ngbDateParserFormatter.format(this.dataServiceForm.controls['modified'].value);

    this.dataServiceForm.reset(); // Reset the form

    this.activeModal.close(newDataService); // Send the data service back to the parent component
  }

  /**
   * Adds a new dataset served by the data service to the list 
   * @param value: value obtained from the input
   */
   public addServedDataset(value: string): void {

    if (!value) return;

    // Create a new DataServiceServedDataset instance
    let servedDataset: DataServiceServedDataset  = new DataServiceServedDataset();

    // Assign the value obtained from the input
    servedDataset.label = value;

    // TODO: check duplicate values

    // Store the new resource in the list
    // It is not possible to use the 'push' method of Typescript because it does not update the ng-select content dynamically
    this.servedDatasets = [...this.servedDatasets, servedDataset];
    
    // Update the form control to display the new value on the select
    this.dataServiceForm.patchValue({
      servedDatasets: this.servedDatasets
    });

    // Set form control to allow the user to save the progress
    this.dataServiceForm.controls['servedDatasets'].markAsDirty();

    // Clear input field
    this.servedDatasetInput.nativeElement.value = '';
  }

  /**
   * Method executed every time any value of the ng-select is modified or removed
   * @param values: updated list of served datasets
   */
  public updateServedDatasets(values) {
    // The form control does not need to be updated because the formControlName attribute of the ng-select does it automatically
    // Update the list of datasets served by the data service
    this.servedDatasets = values;
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

}
