import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IStocks, Stocks } from 'app/shared/model/stocks.model';
import { StocksService } from './stocks.service';

@Component({
  selector: 'jhi-stocks-update',
  templateUrl: './stocks-update.component.html'
})
export class StocksUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    name: [],
    open: [],
    high: [],
    close: [],
    low: [],
    volume: []
  });

  constructor(protected stocksService: StocksService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ stocks }) => {
      this.updateForm(stocks);
    });
  }

  updateForm(stocks: IStocks) {
    this.editForm.patchValue({
      id: stocks.id,
      name: stocks.name,
      open: stocks.open,
      high: stocks.high,
      close: stocks.close,
      low: stocks.low,
      volume: stocks.volume
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const stocks = this.createFromForm();
    if (stocks.id !== undefined) {
      this.subscribeToSaveResponse(this.stocksService.update(stocks));
    } else {
      this.subscribeToSaveResponse(this.stocksService.create(stocks));
    }
  }

  private createFromForm(): IStocks {
    const entity = {
      ...new Stocks(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      open: this.editForm.get(['open']).value,
      high: this.editForm.get(['high']).value,
      close: this.editForm.get(['close']).value,
      low: this.editForm.get(['low']).value,
      volume: this.editForm.get(['volume']).value
    };
    return entity;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStocks>>) {
    result.subscribe((res: HttpResponse<IStocks>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}
