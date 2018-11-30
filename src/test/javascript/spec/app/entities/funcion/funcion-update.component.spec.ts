/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { CineTestModule } from '../../../test.module';
import { FuncionUpdateComponent } from 'app/entities/funcion/funcion-update.component';
import { FuncionService } from 'app/entities/funcion/funcion.service';
import { Funcion } from 'app/shared/model/funcion.model';

describe('Component Tests', () => {
    describe('Funcion Management Update Component', () => {
        let comp: FuncionUpdateComponent;
        let fixture: ComponentFixture<FuncionUpdateComponent>;
        let service: FuncionService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [CineTestModule],
                declarations: [FuncionUpdateComponent]
            })
                .overrideTemplate(FuncionUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(FuncionUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(FuncionService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Funcion(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.funcion = entity;
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
                    const entity = new Funcion();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.funcion = entity;
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
