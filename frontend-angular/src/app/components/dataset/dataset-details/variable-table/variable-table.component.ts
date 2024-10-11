import { Component, OnInit, Input } from '@angular/core';

import { DatasetVariableDTO } from '../../../../models/dto/dataset-variable-dto';
import { DatasetVariableDetailsModalComponent } from '../../../modals/dataset-variable-details-modal/dataset-variable-details-modal.component';

import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-variable-table',
  templateUrl: './variable-table.component.html',
  styleUrls: ['./variable-table.component.css']
})
export class VariableTableComponent implements OnInit {

  @Input() variables: DatasetVariableDTO[]; // Immutable list with all the dataset variables

  variablesFiltered: DatasetVariableDTO[]; // List storing the variables displayed on the screen. Its content can change dinamically depending on the searches

  searchInput: string = ""; // Variable to store the text typed by the user in the search box
  
  // Pagination
  page = 1;
  pageSize = 5;
  collectionSize = 0;

  // Array of booleans for collapsible rows
  // The array is needed to differentiate between the different rows
  variablesCollapsed: boolean[] = [];

  constructor(private modalService: NgbModal) {}

  ngOnInit(): void {
    // Pagination initialisation
    this.collectionSize = this.variables.length;

    // Dinamic list initialisation
    this.variablesFiltered = this.variables;

    this.updateCollapse(); // Set all the collapsible rows to 'True'
  }

  /**
   * Method that is automatically triggered when the user types something in the search box
   * @param event: input text
   */
   onSearch(text: string) {
    this.variablesFiltered = this.variables.filter(variable => {
      const term = text.toLowerCase();
      return variable.name.toLowerCase().includes(term) ||
      variable.variableType.toLowerCase().includes(term);
    });

    this.collectionSize = this.variablesFiltered.length; // Update the table size
    
    this.updateCollapse(); // Collapse all the rows
  }

  /**
   * Opens a modal to display all the information of the selected variable
   */
  openVariableModal(index: number) {

    const modal = this.modalService.open(DatasetVariableDetailsModalComponent, { size: 'lg', backdrop: 'static' });

    // Unit of analysis selected
    modal.componentInstance.variableSelected = this.variablesFiltered[index];
    
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

    this.updateCollapse();
  }

  /**
   * Method that initialises all collapsible rows to 'true' so that they appear to be closed/hidden, by default 
   */
  public updateCollapse() {

    for(let i = 0; i < this.variablesFiltered.length; i++) {
      this.variablesCollapsed[i] = true;
    }
  }

}
