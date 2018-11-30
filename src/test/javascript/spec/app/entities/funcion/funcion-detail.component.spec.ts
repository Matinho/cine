/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CineTestModule } from '../../../test.module';
import { FuncionDetailComponent } from 'app/entities/funcion/funcion-detail.component';
import { Funcion } from 'app/shared/model/funcion.model';

describe('Component Tests', () => {
    describe('Funcion Management Detail Component', () => {
        let comp: FuncionDetailComponent;
        let fixture: ComponentFixture<FuncionDetailComponent>;
        const route = ({ data: of({ funcion: new Funcion(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [CineTestModule],
                declarations: [FuncionDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(FuncionDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(FuncionDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.funcion).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
