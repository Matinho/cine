/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CineTestModule } from '../../../test.module';
import { EntradaDetailComponent } from 'app/entities/entrada/entrada-detail.component';
import { Entrada } from 'app/shared/model/entrada.model';

describe('Component Tests', () => {
    describe('Entrada Management Detail Component', () => {
        let comp: EntradaDetailComponent;
        let fixture: ComponentFixture<EntradaDetailComponent>;
        const route = ({ data: of({ entrada: new Entrada(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [CineTestModule],
                declarations: [EntradaDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(EntradaDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(EntradaDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.entrada).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
