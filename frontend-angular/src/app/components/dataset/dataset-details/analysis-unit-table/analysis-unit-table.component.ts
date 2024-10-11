import { Component, OnInit, Input } from '@angular/core';

import { AnalysisUnitDTO } from '../../../../models/dto/analysis-unit-dto';
import { AnalysisUnitDetailsModalComponent } from '../../../modals/analysis-unit-details-modal/analysis-unit-details-modal.component';

import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-analysis-unit-table',
  templateUrl: './analysis-unit-table.component.html',
  styleUrls: ['./analysis-unit-table.component.css']
})
export class AnalysisUnitTableComponent implements OnInit {

  
  @Input() analysisUnits: AnalysisUnitDTO[]; // Immutable list with all the dataset unit of analysis

  @Input() datasetType: string; // Dataset type (socio-economic or geo-referenced)

  analysisUnitsFiltered: AnalysisUnitDTO[]; // List storing the unit of analysis displayed on the screen. Its content can change dinamically depending on the searches

  searchInput: string = ""; // Variable to store the text typed by the user in the search box
  
  // Pagination
  page = 1;
  pageSize = 5;
  collectionSize = 0;

  constructor(private modalService: NgbModal) {}

  ngOnInit(): void {
    // Pagination initialisation
    this.collectionSize = this.analysisUnits.length;

    // Dinamic list initialisation
    this.analysisUnitsFiltered = this.analysisUnits;
  }

  /**
   * Method that is automatically triggered when the user types something in the search box
   * @param event: input text
   */
   onSearch(text: string) {
    this.analysisUnitsFiltered = this.analysisUnits.filter(analysisUnit => {
      const term = text.toLowerCase();
      return analysisUnit.unitReference.toLowerCase().includes(term);
    });

    this.collectionSize = this.analysisUnitsFiltered.length; // Update the table size
  }

  /**
   * Opens a modal to display all the information of the selected unit of analysis
   */
  openAnalysisUnitModal(index: number) {

    const modal = this.modalService.open(AnalysisUnitDetailsModalComponent, { size: 'lg', backdrop: 'static' });

    // Unit of analysis selected
    modal.componentInstance.analysisUnitSelected = this.analysisUnitsFiltered[index];
    modal.componentInstance.datasetType = this.datasetType;
    
    // Triggered when the moddal is closed
    /*modal.result.then((result) => { 
    }, (reason) => {
    });*/
  }

  /**
   * Method that is automatically triggered when changing pages, updating the value of the page
   * @param event: new page
   */
  public pageChanged(event: any): void {
    this.page = event; // New page
  }

}
