/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { CineTestModule } from '../../../test.module';
import { OcupacionUpdateComponent } from 'app/entities/ocupacion/ocupacion-update.component';
import { OcupacionService } from 'app/entities/ocupacion/ocupacion.service';
import { Ocupacion } from 'app/shared/model/ocupacion.model';

describe('Component Tests', () => {
    describe('Ocupacion Management Update Component', () => {
        let comp: OcupacionUpdateComponent;
        let fixture: ComponentFixture<OcupacionUpdateComponent>;
        let service: OcupacionService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [CineTestModule],
                declarations: [OcupacionUpdateComponent]
            })
                .overrideTemplate(OcupacionUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(OcupacionUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(OcupacionService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Ocupacion(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.ocupacion = entity;
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
                    const entity = new Ocupacion();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.ocupacion = entity;
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
