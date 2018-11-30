/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { CineTestModule } from '../../../test.module';
import { SalaUpdateComponent } from 'app/entities/sala/sala-update.component';
import { SalaService } from 'app/entities/sala/sala.service';
import { Sala } from 'app/shared/model/sala.model';

describe('Component Tests', () => {
    describe('Sala Management Update Component', () => {
        let comp: SalaUpdateComponent;
        let fixture: ComponentFixture<SalaUpdateComponent>;
        let service: SalaService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [CineTestModule],
                declarations: [SalaUpdateComponent]
            })
                .overrideTemplate(SalaUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(SalaUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SalaService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Sala(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.sala = entity;
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
                    const entity = new Sala();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.sala = entity;
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
