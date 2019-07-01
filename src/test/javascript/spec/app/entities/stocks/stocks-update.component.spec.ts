/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { FirstmonolithTestModule } from '../../../test.module';
import { StocksUpdateComponent } from 'app/entities/stocks/stocks-update.component';
import { StocksService } from 'app/entities/stocks/stocks.service';
import { Stocks } from 'app/shared/model/stocks.model';

describe('Component Tests', () => {
  describe('Stocks Management Update Component', () => {
    let comp: StocksUpdateComponent;
    let fixture: ComponentFixture<StocksUpdateComponent>;
    let service: StocksService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FirstmonolithTestModule],
        declarations: [StocksUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(StocksUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(StocksUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(StocksService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Stocks(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Stocks();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
