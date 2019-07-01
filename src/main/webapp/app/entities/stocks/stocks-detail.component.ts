import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IStocks } from 'app/shared/model/stocks.model';

@Component({
  selector: 'jhi-stocks-detail',
  templateUrl: './stocks-detail.component.html'
})
export class StocksDetailComponent implements OnInit {
  stocks: IStocks;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ stocks }) => {
      this.stocks = stocks;
    });
  }

  previousState() {
    window.history.back();
  }
}
