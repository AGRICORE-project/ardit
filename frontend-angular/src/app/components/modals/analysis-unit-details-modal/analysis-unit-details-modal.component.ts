import { Component, OnInit, Input } from '@angular/core';

import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { AnalysisUnitDTO } from '../../../models/dto/analysis-unit-dto';

@Component({
  selector: 'app-analysis-unit-details-modal',
  templateUrl: './analysis-unit-details-modal.component.html',
  styleUrls: ['./analysis-unit-details-modal.component.css']
})
export class AnalysisUnitDetailsModalComponent implements OnInit {

  @Input() analysisUnitSelected: AnalysisUnitDTO; // Unit of analysis selected

  @Input() datasetType: string; // Dataset type (socio-economic or geo-referenced)

  constructor(public activeModal: NgbActiveModal, private modalService: NgbModal) { }

  ngOnInit(): void {
  }

}
