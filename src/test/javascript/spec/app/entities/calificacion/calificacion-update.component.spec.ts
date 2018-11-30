/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { CineTestModule } from '../../../test.module';
import { CalificacionUpdateComponent } from 'app/entities/calificacion/calificacion-update.component';
import { CalificacionService } from 'app/entities/calificacion/calificacion.service';
import { Calificacion } from 'app/shared/model/calificacion.model';

describe('Component Tests', () => {
    describe('Calificacion Management Update Component', () => {
        let comp: CalificacionUpdateComponent;
        let fixture: ComponentFixture<CalificacionUpdateComponent>;
        let service: CalificacionService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [CineTestModule],
                declarations: [CalificacionUpdateComponent]
            })
                .overrideTemplate(CalificacionUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(CalificacionUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CalificacionService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Calificacion(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.calificacion = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Calificacion();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.calificacion = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
