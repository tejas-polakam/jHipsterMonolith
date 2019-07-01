/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { FirstmonolithTestModule } from '../../../test.module';
import { StocksDeleteDialogComponent } from 'app/entities/stocks/stocks-delete-dialog.component';
import { StocksService } from 'app/entities/stocks/stocks.service';

describe('Component Tests', () => {
  describe('Stocks Management Delete Component', () => {
    let comp: StocksDeleteDialogComponent;
    let fixture: ComponentFixture<StocksDeleteDialogComponent>;
    let service: StocksService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FirstmonolithTestModule],
        declarations: [StocksDeleteDialogComponent]
      })
        .overrideTemplate(StocksDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(StocksDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(StocksService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});
