/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CineTestModule } from '../../../test.module';
import { ButacaDetailComponent } from 'app/entities/butaca/butaca-detail.component';
import { Butaca } from 'app/shared/model/butaca.model';

describe('Component Tests', () => {
    describe('Butaca Management Detail Component', () => {
        let comp: ButacaDetailComponent;
        let fixture: ComponentFixture<ButacaDetailComponent>;
        const route = ({ data: of({ butaca: new Butaca(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [CineTestModule],
                declarations: [ButacaDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(ButacaDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ButacaDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.butaca).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
