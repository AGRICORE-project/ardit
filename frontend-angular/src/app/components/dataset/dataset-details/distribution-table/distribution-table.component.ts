import { Component, OnInit, Input } from '@angular/core';

import { DistributionDTO } from '../../../../models/dto/distribution-dto';
import { DistributionDetailsModalComponent } from '../../../modals/distribution-details-modal/distribution-details-modal.component';

import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-distribution-table',
  templateUrl: './distribution-table.component.html',
  styleUrls: ['./distribution-table.component.css']

})
export class DistributionTableComponent implements OnInit {

  @Input() distributions: DistributionDTO[]; // Immutable list with all the dataset distributions

  distributionsFiltered: DistributionDTO[]; // List storing the distributions displayed on the screen. Its content can change dinamically depending on the searches

  searchInput: string = ""; // Variable to store the text typed by the user in the search box
  
  // Pagination
  page = 1;
  pageSize = 5;
  collectionSize = 0;

  constructor(private modalService: NgbModal) {}

  ngOnInit(): void {
    // Pagination initialisation
    this.collectionSize = this.distributions.length;

    // Dinamic list initialisation
    this.distributionsFiltered = this.distributions;
  }

  /**
   * Method that is automatically triggered when the user types something in the search box
   * @param event: input text
   */
   onSearch(text: string) {
    this.distributionsFiltered = this.distributions.filter(distribution => {
      const term = text.toLowerCase();
      return distribution.title.toLowerCase().includes(term)
          || distribution.format.toLowerCase().includes(term)
          || distribution.accessRights.toLowerCase().includes(term);
    });

    this.collectionSize = this.distributionsFiltered.length; // Update the table size
  }

  /**
   * Opens a modal to display all the information of the selected distribution
   */
  openDistributionModal(index: number) {

    const modal = this.modalService.open(DistributionDetailsModalComponent, { size: 'lg', backdrop: 'static' });

    // Distribution selected
    modal.componentInstance.distributionSelected = this.distributionsFiltered[index];
    
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
