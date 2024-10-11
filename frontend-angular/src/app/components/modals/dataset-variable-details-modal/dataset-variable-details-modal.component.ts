import { Component, OnInit, Input } from '@angular/core';

import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { DatasetVariableDTO } from '../../../models/dto/dataset-variable-dto';

@Component({
  selector: 'app-dataset-variable-details-modal',
  templateUrl: './dataset-variable-details-modal.component.html',
  styleUrls: ['./dataset-variable-details-modal.component.css']
})
export class DatasetVariableDetailsModalComponent implements OnInit {

  @Input() variableSelected: DatasetVariableDTO; // Variable selected

  constructor(public activeModal: NgbActiveModal, private modalService: NgbModal) { }

  ngOnInit(): void {
  }

}
