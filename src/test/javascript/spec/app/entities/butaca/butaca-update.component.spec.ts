/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { CineTestModule } from '../../../test.module';
import { ButacaUpdateComponent } from 'app/entities/butaca/butaca-update.component';
import { ButacaService } from 'app/entities/butaca/butaca.service';
import { Butaca } from 'app/shared/model/butaca.model';

describe('Component Tests', () => {
    describe('Butaca Management Update Component', () => {
        let comp: ButacaUpdateComponent;
        let fixture: ComponentFixture<ButacaUpdateComponent>;
        let service: ButacaService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [CineTestModule],
                declarations: [ButacaUpdateComponent]
            })
                .overrideTemplate(ButacaUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ButacaUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ButacaService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Butaca(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.butaca = entity;
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
                    const entity = new Butaca();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.butaca = entity;
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
