import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Stocks } from 'app/shared/model/stocks.model';
import { StocksService } from './stocks.service';
import { StocksComponent } from './stocks.component';
import { StocksDetailComponent } from './stocks-detail.component';
import { StocksUpdateComponent } from './stocks-update.component';
import { StocksDeletePopupComponent } from './stocks-delete-dialog.component';
import { IStocks } from 'app/shared/model/stocks.model';

@Injectable({ providedIn: 'root' })
export class StocksResolve implements Resolve<IStocks> {
  constructor(private service: StocksService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IStocks> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Stocks>) => response.ok),
        map((stocks: HttpResponse<Stocks>) => stocks.body)
      );
    }
    return of(new Stocks());
  }
}

export const stocksRoute: Routes = [
  {
    path: '',
    component: StocksComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'firstmonolithApp.stocks.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: StocksDetailComponent,
    resolve: {
      stocks: StocksResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'firstmonolithApp.stocks.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: StocksUpdateComponent,
    resolve: {
      stocks: StocksResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'firstmonolithApp.stocks.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: StocksUpdateComponent,
    resolve: {
      stocks: StocksResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'firstmonolithApp.stocks.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const stocksPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: StocksDeletePopupComponent,
    resolve: {
      stocks: StocksResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'firstmonolithApp.stocks.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
