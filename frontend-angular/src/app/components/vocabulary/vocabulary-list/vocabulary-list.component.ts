import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { VocabularyService } from '../../../services/vocabulary/vocabulary.service';
import { AlertService } from '../../../services/alert/alert.service';
import { Vocabulary } from '../../../models/vocabulary';
import { RoutesNames } from '../../../shared/utils/routes.names';

import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { VocabularyValue } from 'src/app/models/vocabulary-value';
import { simulateDownload } from '../../../shared/utils/simulate-download';
import { VocabularyValueService } from 'src/app/services/vocabulary-value/vocabulary-value.service';

@Component({
  selector: 'app-vocabulary-list',
  templateUrl: './vocabulary-list.component.html',
  styleUrls: ['./vocabulary-list.component.css']
})
export class VocabularyListComponent implements OnInit {

  vocabularies: Vocabulary[]; // List of vocabularies

  vocabularySelectedId: number = null; // Vocabulary selected for deletion

  vocabulariesFiltered: Vocabulary[]; // List storing the vocabularies displayed on the screen. Its content can change dinamically depending on the searches

  searchInput: string = ""; // Variable to store the text typed by the user in the search box

  // Routes
  vocabularyEditRoute = `/${RoutesNames.VOCABULARY_EDIT}/`;
  vocabularyCreateRoute = `/${RoutesNames.VOCABULARY_CREATE}`;

  //Pagination
  page = 1;
  pageSize = 10;
  collectionSize = 0;

  // Config options for alerts
  options = {
    autoClose: true,
    keepAfterRouteChange: false
  };

  optionsRouteChange = {
    autoClose: true,
    keepAfterRouteChange: true
  };

  constructor(private vocabularyService: VocabularyService, private router: Router, private route: ActivatedRoute,
    private alertService: AlertService, private modalService: NgbModal, private vocabularyValueService: VocabularyValueService) { }

  ngOnInit(): void {
    this.getVocabularies(); // Get all vocabularies available
  }

  /**
   * Get all vocabularies available
   */
  public getVocabularies(): void {
    this.vocabularies = null;
    this.vocabularyService.getAllVocabularies().subscribe(
      res => {
        this.vocabularies = res,
        this.collectionSize = this.vocabularies.length, // Pagination initialisation
        this.vocabulariesFiltered = this.vocabularies  // Dinamic list initialisation
      },
      err => this.handleError(err)
    );
  }

  /**
   * Method that is automatically triggered when the user types something in the search box
   * @param event: input text
   */
  onSearch(text: string) {
    this.vocabulariesFiltered = this.vocabularies.filter(vocabulary => {
      const term = text.toLowerCase();
      return vocabulary.name.toLowerCase().includes(term) ||
      vocabulary.description.toLowerCase().includes(term) ||
      vocabulary.topic.toLowerCase().includes(term);
    });

    this.collectionSize = this.vocabulariesFiltered.length; // Update the table size
  }

  /**
   * Navigate to edit view
   */
  public editVocabulary(id: number): void {
    this.router.navigate([`vocabularyEditRoute`, id]);
  }

  /**
   * Opens a modal to confirm the deletion of a vocabulary
   */
   public openDeleteModal(targetModal: any, id: number) {
    this.modalService.open(targetModal, { size: 'lg', backdrop: 'static' }).result.then((result) => {}, (reason) => {
    });

    this.vocabularySelectedId = id; // Store the index of the vocabulary selected
  }

  /**
   * Deletes an entire vocabulary from the database
   */
  public deleteVocabulary(): void {

    this.modalService.dismissAll(); // Dismiss modal

    this.vocabularyService.deleteVocabulary(this.vocabularySelectedId).subscribe(
      _ => {
        this.alertService.success("Vocabulary deleted", this.optionsRouteChange), // Notify success
        this.getVocabularies(); // Reload the table
        this.vocabularySelectedId = null
      }, 
      err => this.handleError(err)
    );
  }

  /**
   * Downloads vocabulary in JSON format
   */
     public getJsonVocabulary(id: number): void {

      this.vocabularyValueService.getVocabularyValues(id).subscribe(
        res => {
          
          const vocabulary: VocabularyValue[] = res
          const jsonObj = "data:text/json;charset=utf-8," + encodeURIComponent(JSON.stringify(vocabulary, null, 1));
          simulateDownload(jsonObj)
        },
        err => this.handleError(err)
      )
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

