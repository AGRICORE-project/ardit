import { Component, OnInit, Input } from '@angular/core';
import { FormGroup, FormControl, Validators, AbstractControl } from '@angular/forms';

import { VocabularyValue } from '../../../models/vocabulary-value';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-vocabulary-value-modal',
  templateUrl: './vocabulary-value-modal.component.html',
  styleUrls: ['./vocabulary-value-modal.component.css']
})
export class VocabularyValueModalComponent implements OnInit {

  @Input() vocabularyValueSelected: VocabularyValue; // Vocabulary value selected

  title: string = ""; // Modal title

  // Form
  vocabularyValueForm: FormGroup;

  constructor(public activeModal: NgbActiveModal) { }

  ngOnInit(): void {
    // Vocabulary value form validators
    this.vocabularyValueForm = new FormGroup({
      code: new FormControl(null, [
        Validators.required
      ]),
      label: new FormControl(null, [
        Validators.required
      ]),
      url: new FormControl(null, []),
      extraData: new FormControl(null, [])
    });

    // If the user has selected a vocabulary value for editing, populate the form with its attributes
    if (this.vocabularyValueSelected) {
      this.populateModal();
      this.title = "Edit a vocabulary value";
    } else {
      this.title = "Create a new vocabulary value";
    }
  }

  /**
   * Populate the modal with a given vocabulary value
  */
  private populateModal(): void {
    this.vocabularyValueForm.patchValue({
      code: this.vocabularyValueSelected.code,
      label: this.vocabularyValueSelected.label,
      url: this.vocabularyValueSelected.url,
      extraData: this.vocabularyValueSelected.extra_data
    });
  }

  /**
   * Retrieve the values inserted in the form and return them to the parent component
  */
  public submitVocabularyValue(): void {
    let vocabularyValue: VocabularyValue = this.vocabularyValueForm.value; // Get form values
    if(this.vocabularyValueSelected) vocabularyValue.id = this.vocabularyValueSelected.id; // Assign the ID

    this.activeModal.close(vocabularyValue); // Send the vocabulary value back to the parent component
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
