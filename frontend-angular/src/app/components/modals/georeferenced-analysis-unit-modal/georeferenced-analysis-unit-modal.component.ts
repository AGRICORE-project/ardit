import { Component, OnInit, Input } from '@angular/core';
import { FormGroup, FormControl, AbstractControl, Validators } from '@angular/forms';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { AnalysisUnit } from '../../../models/analysis-unit';
import { VocabularyValue } from '../../../models/vocabulary-value';
import { VocabularyValueService } from '../../../services/vocabulary-value/vocabulary-value.service';
import { AlertService } from '../../../services/alert/alert.service';
import { DatasetDescription } from '../../../models/dataset-description';

// Utils
import { formatTmpExtentToString } from '../../../shared/utils/temporal-extent-parser';

@Component({
  selector: 'app-georeferenced-analysis-unit-modal',
  templateUrl: './georeferenced-analysis-unit-modal.component.html',
  styleUrls: ['./georeferenced-analysis-unit-modal.component.css']
})
export class GeoreferencedAnalysisUnitModalComponent implements OnInit {

  @Input() analysisUnitSelected: AnalysisUnit; // Unit of analysis selected for edition
  @Input() measuresValues: VocabularyValue[]; // Necessary vocabulary
  @Input() popoversDescriptions: {[property: string]: DatasetDescription;}; // List of popovers help descriptions

  // Forms groups
  analysisUnitForm: FormGroup;

  // Config options for alerts
  options = {
    autoClose: true,
    keepAfterRouteChange: false
  };

  optionsRouteChange = {
    autoClose: true,
    keepAfterRouteChange: true
  };

  constructor(public activeModal: NgbActiveModal, private vocabularyValueService: VocabularyValueService, private alertService: AlertService) { }

  ngOnInit(): void {

    // Georeferenced unit of analysis form validators
    this.analysisUnitForm = new FormGroup({
      unitReference: new FormControl([], [
        Validators.required
      ]),
      tmpExtentFrom: new FormControl(null, [
        Validators.required
      ]),
      tmpExtentTo: new FormControl(null, [
        Validators.required
      ]),
      aggregationLevel: new FormControl(null, []),
      aggregationUnit: new FormControl(null, []),
      aggregationScale: new FormControl(null, []),
      census: new FormControl(false, []),
      populationCoverage: new FormControl(null, []),
      unitAnalysisNumber: new FormControl(null, []),
      areaSizeValue: new FormControl(null, [
        Validators.required
      ]),
      areaSizeUnit: new FormControl(null, [
        Validators.required
      ]),
      statsRepresentativeness: new FormControl(null, [
        Validators.maxLength(1000)
      ]),
      downscalingMethodology: new FormControl(null, [
        Validators.maxLength(1000)
      ])
    });

    // If the user has selected an unit of analysis for editing, populate the form with its attributes
    if (this.analysisUnitSelected) {
      this.populateModal();
    }
  }

  /**
   * Populate the modal with a given georeferenced unit of analysis data
  */
  private populateModal(): void {
    this.analysisUnitForm.patchValue({
      unitReference: this.analysisUnitSelected.unitReference,
      tmpExtentFrom: formatTmpExtentToString(this.analysisUnitSelected.tmpExtentFrom), // Format temporal extent to strings
      tmpExtentTo: formatTmpExtentToString(this.analysisUnitSelected.tmpExtentTo), // Format temporal extent to strings
      aggregationLevel: this.analysisUnitSelected.aggregationLevel,
      aggregationUnit: this.analysisUnitSelected.aggregationUnit,
      aggregationScale: this.analysisUnitSelected.aggregationScale,
      census: this.analysisUnitSelected.census,
      populationCoverage: this.analysisUnitSelected.populationCoverage,
      areaSizeValue: this.analysisUnitSelected.areaSizeValue,
      areaSizeUnit: this.analysisUnitSelected.areaSizeUnit,
      unitAnalysisNumber: this.analysisUnitSelected.unitAnalysisNumber,
      statsRepresentativeness: this.analysisUnitSelected.statsRepresentativeness,
      downscalingMethodology: this.analysisUnitSelected.downscalingMethodology
    });
  }

  /**
   * Retrieve the values inserted in the form and return them to the parent component
  */
  public submitAnalysisUnit(): void {
    let analysisUnit: AnalysisUnit = this.analysisUnitForm.value; // Get form values
    if(this.analysisUnitSelected) analysisUnit.id = this.analysisUnitSelected.id; // Assign the ID
    analysisUnit.analysisUnitType="GEOREFERENCED"; // Mark the analysis unit type

    this.activeModal.close(analysisUnit); // Send the unit of analysis back to the parent component
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
    if(err.error.message) {
      this.alertService.error(err.error.message, this.optionsRouteChange);
    }
  }
}
