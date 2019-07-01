import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IStocks } from 'app/shared/model/stocks.model';
import { StocksService } from './stocks.service';

@Component({
  selector: 'jhi-stocks-delete-dialog',
  templateUrl: './stocks-delete-dialog.component.html'
})
export class StocksDeleteDialogComponent {
  stocks: IStocks;

  constructor(protected stocksService: StocksService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.stocksService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'stocksListModification',
        content: 'Deleted an stocks'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-stocks-delete-popup',
  template: ''
})
export class StocksDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ stocks }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(StocksDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.stocks = stocks;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/stocks', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/stocks', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
