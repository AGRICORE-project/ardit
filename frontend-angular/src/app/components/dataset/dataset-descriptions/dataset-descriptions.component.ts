import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormGroup,  FormControl, AbstractControl, Validators } from '@angular/forms';

import { DatasetService } from '../../../services/dataset/dataset.service';
import { AlertService } from '../../../services/alert/alert.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { DatasetDescription } from '../../../models/dataset-description';
import { RoutesNames } from '../../../shared/utils/routes.names';

@Component({
  selector: 'app-dataset-descriptions',
  templateUrl: './dataset-descriptions.component.html',
  styleUrls: ['./dataset-descriptions.component.css']
})
export class DatasetDescriptionsComponent implements OnInit {

  descriptions: DatasetDescription[];

  searchInput: string = ""; // Variable to store the text typed by the user in the search box

  // Pagination
  page = 1;
  pageSize = 5;
  collectionSize = 0;

  // Form groups
  descriptionForm: FormGroup; 

  // To store the ID of a description selected on the view
  descriptionSelected: number = null;

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
  datasetsListRoute = `/${RoutesNames.DATASETS}`;
  datasetCreationRoute = `/${RoutesNames.DATASET_CREATE}`;

  constructor(private datasetService: DatasetService, private router: Router, private route: ActivatedRoute, private modalService: NgbModal, public alertService: AlertService) { }

  ngOnInit(): void {

    this.getPropetiesDescriptions(); // Get all descriptions available

    // Dataset description form validators
    this.descriptionForm = new FormGroup({
      description: new FormControl(null, [
        Validators.required,
        Validators.maxLength(500)
      ])
    });
  }

  /**
   * Get a list of descriptions to populate info popovers
   */
  public getPropetiesDescriptions(): void{
    this.descriptions = null;
    this.datasetService.getPropertiesDescriptions().subscribe(
      res => {
        this.descriptions = res,
        this.collectionSize = this.descriptions.length; // Pagination initialisation
      },
      err => this.handleError(err)
    );
  }

  /**
   * Open a modal to edit a dataset's property description
   * @param targetModal: modal to be opened
   * @param index: position on array descriptions with the description to be updated
   */
  public openDescriptionModal(targetModal: any, index: number): void {

    this.modalService.open(targetModal, { size: 'lg', backdrop: 'static' }).result.then((result) => {}, (reason) => {
      this.descriptionForm.reset(); // Reset form after modal is dismissed
    });

    this.descriptionSelected = this.descriptions[index].id; // Store the ID of the description selected

    this.descriptionForm.patchValue({ // Populate the form with the current value
      description: this.descriptions[index].description,
    });
  }

  /**
   * Update the description field of a dataset property
   */
  public updateDescription(): void {

    // Update the description suing the ID (descriptionSelected) and the new value (string)
    this.datasetService.updatePropertyDescription(this.descriptionSelected, this.descriptionForm.value).subscribe(
      res => {
        this.alertService.success('Description updated', this.options), // Success message
        this.getPropetiesDescriptions(); // Update table
      },
      err => this.handleError(err)
    );
  }

  /**
   * Delete a property description
   */
  public deleteDescription(index: number): void {

    const descriptionToDelete = this.descriptions[index].id;// The ID of the description selected

    this.datasetService.deletePropertyDescription(descriptionToDelete).subscribe(
      res => {
        this.alertService.success('Description deleted', this.options), // Success message
        this.getPropetiesDescriptions(); // Update table
      },
      err => this.handleError(err)
    );
  }

  /**
   * Modal form validation helpers
   */
  public modalFieldInvalid(form: FormGroup, fieldName: string): boolean {
    const control: AbstractControl = form.controls[fieldName];
    const invalid: boolean = control.invalid;
    const dirty: boolean = control.dirty;
    const touched: boolean = control.touched;
    return invalid && (dirty || touched);
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
