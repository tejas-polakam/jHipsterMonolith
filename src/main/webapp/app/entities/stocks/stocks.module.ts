import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { FirstmonolithSharedModule } from 'app/shared';
import {
  StocksComponent,
  StocksDetailComponent,
  StocksUpdateComponent,
  StocksDeletePopupComponent,
  StocksDeleteDialogComponent,
  stocksRoute,
  stocksPopupRoute
} from './';

const ENTITY_STATES = [...stocksRoute, ...stocksPopupRoute];

@NgModule({
  imports: [FirstmonolithSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [StocksComponent, StocksDetailComponent, StocksUpdateComponent, StocksDeleteDialogComponent, StocksDeletePopupComponent],
  entryComponents: [StocksComponent, StocksUpdateComponent, StocksDeleteDialogComponent, StocksDeletePopupComponent],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FirstmonolithStocksModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey !== undefined) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
