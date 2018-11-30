/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { CineTestModule } from '../../../test.module';
import { OcupacionDeleteDialogComponent } from 'app/entities/ocupacion/ocupacion-delete-dialog.component';
import { OcupacionService } from 'app/entities/ocupacion/ocupacion.service';

describe('Component Tests', () => {
    describe('Ocupacion Management Delete Component', () => {
        let comp: OcupacionDeleteDialogComponent;
        let fixture: ComponentFixture<OcupacionDeleteDialogComponent>;
        let service: OcupacionService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [CineTestModule],
                declarations: [OcupacionDeleteDialogComponent]
            })
                .overrideTemplate(OcupacionDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(OcupacionDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(OcupacionService);
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
