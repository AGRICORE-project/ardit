import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormGroup,  FormControl, AbstractControl, Validators } from '@angular/forms';

import { VocabularyService } from '../../../services/vocabulary/vocabulary.service';
import { AlertService } from '../../../services/alert/alert.service';
import { Vocabulary } from '../../../models/vocabulary';
import { RoutesNames } from '../../../shared/utils/routes.names';

@Component({
  selector: 'app-vocabulary-create',
  templateUrl: './vocabulary-create.component.html',
  styleUrls: ['./vocabulary-create.component.css']
})
export class VocabularyCreateComponent implements OnInit {

  vocabularyTopics: String[]; // List of dataset properties

  // Form groups
  vocabularyForm: FormGroup; 

  // Routes
  vocabularyListRoute = `/${RoutesNames.VOCABULARIES}`;

  // Config options for alerts
  options = {
    autoClose: true,
    keepAfterRouteChange: false
  };

  optionsRouteChange = {
    autoClose: true,
    keepAfterRouteChange: true
  };

  constructor(private vocabularyService: VocabularyService, private router: Router, private route: ActivatedRoute, public alertService: AlertService) { }

  ngOnInit(): void {

    this.getVocabularyTopics(); // Get the list of possible topics

    // Vocabulary form validators
    this.vocabularyForm = new FormGroup({
      name: new FormControl(null, [
        Validators.required
      ]),
      description: new FormControl(null, [
        Validators.required
      ]),
      url: new FormControl(null, []),
      topic: new FormControl(null, [
        Validators.required
      ])
    });
  }

  /*
   * Get a list of topics
   */
  private getVocabularyTopics(): void {
    this.vocabularyTopics = null;
    this.vocabularyService.getVocabularyTopics().subscribe(
      res => this.vocabularyTopics = res,
      err => this.handleError(err)
    );
  }

  /**
   * Create a new vocabulary
   */
  public createVocabulary(): void {
    
    const vocabulary = this.vocabularyForm.value;

    this.vocabularyService.createVocabulary(vocabulary).subscribe(
      res => {
        this.alertService.success("Vocabulary created", this.optionsRouteChange) //Notify success
        this.router.navigate([`/${RoutesNames.VOCABULARY_EDIT}/` + res.id]) // Redirect to vocabulary update view
        },
      err => this.handleError(err)
    );
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
    if(err.status === 403) {
      this.alertService.warn("You do not have permission to perform this action", this.optionsRouteChange);
    } else if(err.error.message) {
      this.alertService.error(err.error.message, this.optionsRouteChange);
    }
  }

}
