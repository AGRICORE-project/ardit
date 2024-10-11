import { Component, OnInit, Input } from '@angular/core';
import { Distribution } from '../../../models/distribution';
import { DatasetDescription } from '../../../models/dataset-description';
import { FormGroup, AbstractControl, FormControl, Validators } from '@angular/forms';
import { NgbActiveModal, NgbModal, NgbDateParserFormatter } from '@ng-bootstrap/ng-bootstrap';

import { AlertService } from '../../../services/alert/alert.service';
import { DataService } from '../../../models/data-service';

import { VocabularyValue } from '../../../models/vocabulary-value';
import { DataServiceModalComponent } from '../data-service-modal/data-service-modal.component';

@Component({
  selector: 'app-distribution-modal',
  templateUrl: './distribution-modal.component.html',
  styleUrls: ['./distribution-modal.component.css']
})
export class DistributionModalComponent implements OnInit {

  @Input() distributionSelected: Distribution; // Distribution selected for edition
  @Input() popoversDescriptions: {[property: string]: DatasetDescription;}; // List of popovers help descriptions
  @Input() accessRightsValues: VocabularyValue[];
  @Input() formatsValues: VocabularyValue[];

  // Modal title
  title: string = "Add a new distribution"

  // Form groups
  distributionForm: FormGroup;

  accessService: DataService = null; // Distribution data service

  accessServiceButtonDisabled: boolean = false;

  // Config options for alerts
  options = {
    autoClose: true,
    keepAfterRouteChange: false
  };

  optionsRouteChange = {
    autoClose: true,
    keepAfterRouteChange: true
  };

  constructor(public activeModal: NgbActiveModal, private modalService: NgbModal, private ngbDateParserFormatter: NgbDateParserFormatter, public alertService: AlertService) { }

  ngOnInit(): void {
    // Distribution form validators
    this.distributionForm = new FormGroup({
      title: new FormControl([], [
        Validators.required
      ]),
      description: new FormControl(null, [
        Validators.maxLength(1000)
      ]),
      issued: new FormControl(null, []),
      modified: new FormControl(null, []),
      license: new FormControl(null, []),
      accessRights: new FormControl(null, [
        Validators.required
      ]),
      accessProcedures: new FormControl(null, [
        Validators.maxLength(1000)
      ]),
      accessUrl: new FormControl(null, []),
      downloadUrl: new FormControl(null, []),
      byteSize: new FormControl(null, []), 
      format: new FormControl(null, [
        Validators.required
      ]),
      compressFormat: new FormControl(null, []),
      packagingFormat: new FormControl(null, []),
      accessService: new FormControl(null, [])
    });

    // If the user has selected an analysis unit for editing, populate the form with its attributes
    if (this.distributionSelected) {
      this.title = "Edit a distribution"; // Change modal title
      this.populateModal();

      if (this.distributionSelected.accessService) this.accessServiceButtonDisabled = true; // Disable the button because an access service already exists
    }

  }

  /**
   * Populate the modal with a given distribution data
  */
   private populateModal(): void {
    this.distributionForm.patchValue({
      title: this.distributionSelected.title,
      description: this.distributionSelected.description,
      issued: this.ngbDateParserFormatter.parse(this.distributionSelected.issued),
      modified: this.ngbDateParserFormatter.parse(this.distributionSelected.modified),
      license: this.distributionSelected.license,
      accessRights: this.distributionSelected.accessRights,
      accessProcedures: this.distributionSelected.accessProcedures,
      accessUrl: this.distributionSelected.accessUrl,
      downloadUrl:this.distributionSelected.downloadUrl,
      byteSize: this.distributionSelected.byteSize, 
      format: this.distributionSelected.format,
      compressFormat: this.distributionSelected.compressFormat,
      packagingFormat: this.distributionSelected.packagingFormat
    });

    // Assign data service
    this.accessService = this.distributionSelected.accessService;
  }

  /**
   * Retrieve the values inserted in the form and return them to the parent component
  */
  public submitDistribution(): void {
    let newDistribution: Distribution = this.distributionForm.value; // Get form values

    if(this.distributionSelected) newDistribution.id = this.distributionSelected.id; // Assign the ID
    newDistribution.accessService = this.accessService; //Assign the data service

    // NgbDateStruct to string
    newDistribution.issued = this.ngbDateParserFormatter.format(this.distributionForm.controls['issued'].value);
    newDistribution.modified = this.ngbDateParserFormatter.format(this.distributionForm.controls['modified'].value);

    this.distributionForm.reset(); // Reset the form

    this.activeModal.close(newDistribution); // Send the distribution back to the parent component
  }

  /**
   * Opens a modal to create a new data service
   */
  public openDataServiceModal(): void {

    const modal = this.modalService.open(DataServiceModalComponent, { size: 'lg', backdrop: 'static' });

    if (this.accessService) {
      modal.componentInstance.dataServiceSelected = this.accessService;
    }
    
    // Pass popovers descriptions to the modal
    modal.componentInstance.popoversDescriptions = this.popoversDescriptions;

    // Pass vocabulary values to the modal
    modal.componentInstance.accessRightsValues = this.accessRightsValues;
    
    // Add the data service returned by the modal
    modal.result.then((result) => {
      this.accessService = result; // Data service obtained
      this.accessServiceButtonDisabled = true; // Disable the button because an access service already exists
      this.alertService.success('Data service added', this.options) // Notify success
    }, (reason) => {
      // Modal is dismissed
    });
  
  }

  /**
   * Delete the data service that gives access to the distribution
  */
  public deleteDataService(): void {
    this.accessService = null;
    this.distributionSelected.accessService = null;
    this.accessServiceButtonDisabled = false; // Enable the button because the access service does not exist
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
