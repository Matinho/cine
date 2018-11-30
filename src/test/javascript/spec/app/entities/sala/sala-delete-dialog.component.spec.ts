/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { CineTestModule } from '../../../test.module';
import { SalaDeleteDialogComponent } from 'app/entities/sala/sala-delete-dialog.component';
import { SalaService } from 'app/entities/sala/sala.service';

describe('Component Tests', () => {
    describe('Sala Management Delete Component', () => {
        let comp: SalaDeleteDialogComponent;
        let fixture: ComponentFixture<SalaDeleteDialogComponent>;
        let service: SalaService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [CineTestModule],
                declarations: [SalaDeleteDialogComponent]
            })
                .overrideTemplate(SalaDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SalaDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SalaService);
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
