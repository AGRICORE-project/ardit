import { Component, OnInit, Input, ViewChild, ElementRef } from '@angular/core';
import { DatasetVariable } from '../../../models/dataset-variable';
import { DatasetDescription } from '../../../models/dataset-description';
import { FormGroup, FormControl, AbstractControl, Validators } from '@angular/forms';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { VariableReferenceValue } from '../../../models/variable-reference-value';
import { VocabularyValue } from '../../../models/vocabulary-value';

@Component({
  selector: 'app-price-dataset-variable-modal',
  templateUrl: './price-dataset-variable-modal.component.html',
  styleUrls: ['./price-dataset-variable-modal.component.css']
})
export class PriceDatasetVariableModalComponent implements OnInit {

  @ViewChild('referenceValue') referenceValueInput: ElementRef;

  @Input() variableSelected: DatasetVariable; // Dataset variable selected for edition
  @Input() datasetDescriptions: {[property: string]: DatasetDescription;}; // List of popovers help descriptions

  //Form select values
  @Input() mathRepValues: VocabularyValue[];
  @Input() periodicityValues: VocabularyValue[];
  @Input() measuresValues: VocabularyValue[];
  @Input() variableDataOriginValues: VocabularyValue[];
  @Input() currencyValues: VocabularyValue[];
  @Input() priceTypeValues: VocabularyValue[];
  @Input() sizeUnitValues: VocabularyValue[];
  @Input() analysisUnits: VocabularyValue[];

  // Forms groups
  datasetVariableForm: FormGroup;

  variableReferenceValues: VariableReferenceValue[] = []; // Variable's reference values list

  constructor(public activeModal: NgbActiveModal) { }

  ngOnInit(): void {

    // Dataset variable form validators
    this.datasetVariableForm = new FormGroup({
      name: new FormControl(null, [
        Validators.required,
        Validators.maxLength(500)
      ]),
      measurementUnit: new FormControl(null, []),
      tmpExtentFrom: new FormControl(null, [
        Validators.required
      ]),
      tmpExtentTo: new FormControl(null, [
        Validators.required
      ]),
      mathRepresentation: new FormControl(null, []),
      frequency: new FormControl(null, []),
      frequencyMathRep: new FormControl(null, []),
      analysisUnits: new FormControl([], []),
      aggregationLevel: new FormControl(null, []),
      aggregationUnit: new FormControl(null, []),
      aggregationScale: new FormControl(null, []),
      statsRepresentativeness:  new FormControl(null, [
        Validators.maxLength(1000)
      ]),
      dataOrigin: new FormControl(null, []),
      referenceValues: new FormControl([], []),
      currency: new FormControl(null, []),
      priceType: new FormControl(null, [
        Validators.required
      ]),
      sizeUnitAmount: new FormControl(null, []),
      sizeUnit: new FormControl(null, []),
      downscalingMethodology: new FormControl(null, [
        Validators.maxLength(1000)
      ])
    });

    // If the user has selected a dataset variable unit for editing, populate the form with its attributes
    if (this.variableSelected) {
      this.populateModal();
    }
  }

  /**
   * Populate the modal with a given dataset variable data
  */
  private populateModal(): void {

    // Initialise list of reference values with those of the selected variable
    this.variableReferenceValues = this.variableSelected.referenceValues;

    this.datasetVariableForm.patchValue({
      name: this.variableSelected.name,
      measurementUnit: this.variableSelected.measurementUnit,
      tmpExtentFrom: this.formatTmpExtentToString(this.variableSelected.tmpExtentFrom), // Format temporal extent to strings
      tmpExtentTo: this.formatTmpExtentToString(this.variableSelected.tmpExtentTo), // Format temporal extent to strings
      mathRepresentation: this.variableSelected.mathRepresentation,
      frequency: this.variableSelected.frequency,
      frequencyMathRep: this.variableSelected.frequencyMathRep,
      analysisUnits: this.variableSelected.analysisUnits,
      aggregationLevel: this.variableSelected.aggregationLevel,
      aggregationUnit: this.variableSelected.aggregationUnit,
      aggregationScale: this.variableSelected.aggregationScale,
      statsRepresentativeness: this.variableSelected.statsRepresentativeness,
      dataOrigin: this.variableSelected.dataOrigin,
      referenceValues: this.variableSelected.referenceValues,
      currency: this.variableSelected.currency,
      priceType: this.variableSelected.priceType,
      sizeUnitAmount: this.variableSelected.sizeUnitAmount,
      sizeUnit: this.variableSelected.sizeUnit,
      downscalingMethodology: this.variableSelected.downscalingMethodology
    });
  }

  /**
   * Retrieve the values inserted in the form and return them to the parent component
   */
   public submitDatasetVariable(): void {
    let datasetVariable: DatasetVariable = this.datasetVariableForm.value; // Get form values
    datasetVariable.variableType = 'PRICE'; // Mark the dataset variable type

    if(this.variableSelected) datasetVariable.id = this.variableSelected.id; // Assign the ID
    this.datasetVariableForm.reset(); // Reset the form

    this.activeModal.close(datasetVariable); // Send the dataset variable back to the parent component
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
   * Adds a new referencevvalue to the list 
   * @param value: reference value obtained from the input
   */
   public addReferenceValue(value: string): void {

    if (!value) return;

    // Create a new VariableReferenceValue instance
    let referenceValue: VariableReferenceValue = new VariableReferenceValue();

    // Assign the value obtained from the input
    referenceValue.label = value;

    // TODO: check duplicate values

    // Store the new reference value in the list
    // It is not possible to use the 'push' method of Typescript because it does not update the ng-select content dynamically
    this.variableReferenceValues = [...this.variableReferenceValues, referenceValue];
    
    // Update the form control to display the new value on the select
    this.datasetVariableForm.patchValue({
      referenceValues: this.variableReferenceValues
    });

    // Set form control to allow the user to save the progress
    this.datasetVariableForm.controls['referenceValues'].markAsDirty();

    // Clear input field
    this.referenceValueInput.nativeElement.value = '';
  }

  /**
   * Method executed every time any value of the ng-select is modified or removed
   * @param values: updated list of reference values
   */
  public updateReferenceValue(values) {
    // The form control does not need to be updated because the formControlName attribute of the ng-select does it automatically
    // Update the list of reference values
    this.variableReferenceValues = values;
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
