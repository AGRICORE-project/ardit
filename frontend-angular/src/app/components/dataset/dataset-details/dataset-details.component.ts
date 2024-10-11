import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { DatasetService } from '../../../services/dataset/dataset.service';
import { AlertService } from '../../../services/alert/alert.service';
import { Dataset } from '../../../models/dataset';
import { RoutesNames } from '../../../shared/utils/routes.names';
import { VocabularyValue } from '../../../models/vocabulary-value';

import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Distribution } from '../../../models/distribution';
import { DatasetDTO } from '../../../models/dto/dataset-dto';

@Component({
  selector: 'app-dataset-details',
  templateUrl: './dataset-details.component.html',
  styleUrls: ['./dataset-details.component.css']
})
export class DatasetDetailsComponent implements OnInit {

  dataset: DatasetDTO = null;

  geocoverage: String[] = [];

  // Collapsible sections
  distributionsCollapsed: boolean = true;
  analysisUnitsCollapsed: boolean = true;
  variablesCollapsed: boolean = true;

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
  datasetEditRoute = `/${RoutesNames.DATASET_EDIT}/` + this.route.snapshot.paramMap.get('id');
  cataloguesRoute = `/${RoutesNames.CATALOGUES}/`;
  datasetListRoute = `/${RoutesNames.DATASETS}/`;

  constructor(private datasetService: DatasetService, private router: Router, private route: ActivatedRoute, public alertService: AlertService, private modalService: NgbModal) { }

  ngOnInit(): void {
    this.getDatasetDetails(Number(this.route.snapshot.paramMap.get('id')));
  }

  private getDatasetDetails(id: number): void {
    this.datasetService.getDatasetDTO(id).subscribe(
      res => {
        this.dataset = res;
        this.fillGeocoverageSection();
      },
      err => this.handleError(err)
    );
  }

  /**
   * To show geocoverage data inline
   */
  public fillGeocoverageSection():void {
    this.geocoverage = this.geocoverage.concat(this.dataset.continentalCoverage, this.dataset.countryCoverage, this.dataset.nuts1, this.dataset.nuts2, this.dataset.nuts3, this.dataset.adm1,this.dataset.adm2);
  }

  /**
   * Add a new item to list using label 
   * @param property: geocoverage property
   */
  public addGeocoverageValueLabel(property: VocabularyValue[]): void {
    for(let value of property){
      this.geocoverage.push(value.label);
    }
  }

  /**
   * Add a new item to list using code (NUTS)
   * @param property: geocoverage property
   */
  public addGeocoverageValueCode(property: VocabularyValue[]): void {
    for(let value of property){
      this.geocoverage.push(value.code);
    }
  }

  /**
   * Duplicate an existing dataset, creating a new one with the same properties
   */
  public duplicateDataset(): void {

    this.datasetService.getDataset(this.dataset.id).subscribe(
      res => {
        res.id = null,
        this.datasetService.duplicateDataset(res).subscribe(
          _ => {
            this.alertService.success('Dataset duplicated', this.optionsRouteChange),
            this.router.navigate([`/${RoutesNames.DATASETS}`])},
          err => this.handleError(err)
        );
      },
      err => this.handleError(err)
    );
  }

  /**
   * Downloads the dataset characterisation in JSON format file
   */
  public downloadJsonFile(): void {

    // Format the dataset to JSON with line break in each element (parameter "1" in JSON.stringify method)
    var data = "data:text/json;charset=utf-8," + encodeURIComponent(JSON.stringify(this.dataset, null, 1));

    // Creation of an anchor element to simulate the download link
    var downloader = document.createElement('a'); // Anchor element
    downloader.setAttribute('href', data); // Href attribute
    downloader.setAttribute('download', this.dataset.title + '.json'); // Download attribute, the file gets the dataset title
    downloader.click(); // Simulate the click to download the file
  }

  /**
   * Opens a modal to confirm the deletion of a dataset
    */
  public openDeleteModal(targetModal: any) {
    this.modalService.open(targetModal, { size: 'lg', backdrop: 'static' }).result.then((result) => {}, (reason) => {
    });
  }

  /**
   * Delete the dataset
   */
  public deleteDataset(): void {

    this.modalService.dismissAll(); // Dismiss modal

    this.datasetService.deleteDataset(this.dataset.id).subscribe(
      _ => {
        this.alertService.success('Dataset deleted', this.optionsRouteChange),
        this.router.navigate([`/${RoutesNames.DATASETS}`])},
      err => this.handleError(err)
    );
  }

  handleError(err: any): any{
    if(err.status === 403) {
      this.alertService.warn("You do not have permission to perform this action", this.optionsRouteChange);
    } else if(err.error.message) {
      this.alertService.error(err.error.message, this.optionsRouteChange);
    }
  }
}
