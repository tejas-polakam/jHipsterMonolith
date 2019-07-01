/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FirstmonolithTestModule } from '../../../test.module';
import { StocksDetailComponent } from 'app/entities/stocks/stocks-detail.component';
import { Stocks } from 'app/shared/model/stocks.model';

describe('Component Tests', () => {
  describe('Stocks Management Detail Component', () => {
    let comp: StocksDetailComponent;
    let fixture: ComponentFixture<StocksDetailComponent>;
    const route = ({ data: of({ stocks: new Stocks(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FirstmonolithTestModule],
        declarations: [StocksDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(StocksDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(StocksDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.stocks).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
