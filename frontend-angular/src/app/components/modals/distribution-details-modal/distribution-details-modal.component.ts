import { Component, OnInit, Input } from '@angular/core';

import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { DistributionDTO } from '../../../models/dto/distribution-dto';

@Component({
  selector: 'app-distribution-details-modal',
  templateUrl: './distribution-details-modal.component.html',
  styleUrls: ['./distribution-details-modal.component.css']
})
export class DistributionDetailsModalComponent implements OnInit {

  @Input() distributionSelected: DistributionDTO; // Distribution selected

  constructor(public activeModal: NgbActiveModal, private modalService: NgbModal) { }

  ngOnInit(): void {

  }

}
