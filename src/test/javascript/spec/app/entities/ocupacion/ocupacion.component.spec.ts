/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { CineTestModule } from '../../../test.module';
import { OcupacionComponent } from 'app/entities/ocupacion/ocupacion.component';
import { OcupacionService } from 'app/entities/ocupacion/ocupacion.service';
import { Ocupacion } from 'app/shared/model/ocupacion.model';

describe('Component Tests', () => {
    describe('Ocupacion Management Component', () => {
        let comp: OcupacionComponent;
        let fixture: ComponentFixture<OcupacionComponent>;
        let service: OcupacionService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [CineTestModule],
                declarations: [OcupacionComponent],
                providers: []
            })
                .overrideTemplate(OcupacionComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(OcupacionComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(OcupacionService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Ocupacion(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.ocupacions[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
