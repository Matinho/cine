/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { CineTestModule } from '../../../test.module';
import { EntradaDeleteDialogComponent } from 'app/entities/entrada/entrada-delete-dialog.component';
import { EntradaService } from 'app/entities/entrada/entrada.service';

describe('Component Tests', () => {
    describe('Entrada Management Delete Component', () => {
        let comp: EntradaDeleteDialogComponent;
        let fixture: ComponentFixture<EntradaDeleteDialogComponent>;
        let service: EntradaService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [CineTestModule],
                declarations: [EntradaDeleteDialogComponent]
            })
                .overrideTemplate(EntradaDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(EntradaDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(EntradaService);
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
