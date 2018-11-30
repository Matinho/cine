/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CineTestModule } from '../../../test.module';
import { OcupacionDetailComponent } from 'app/entities/ocupacion/ocupacion-detail.component';
import { Ocupacion } from 'app/shared/model/ocupacion.model';

describe('Component Tests', () => {
    describe('Ocupacion Management Detail Component', () => {
        let comp: OcupacionDetailComponent;
        let fixture: ComponentFixture<OcupacionDetailComponent>;
        const route = ({ data: of({ ocupacion: new Ocupacion(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [CineTestModule],
                declarations: [OcupacionDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(OcupacionDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(OcupacionDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.ocupacion).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
