/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { CineTestModule } from '../../../test.module';
import { CalificacionComponent } from 'app/entities/calificacion/calificacion.component';
import { CalificacionService } from 'app/entities/calificacion/calificacion.service';
import { Calificacion } from 'app/shared/model/calificacion.model';

describe('Component Tests', () => {
    describe('Calificacion Management Component', () => {
        let comp: CalificacionComponent;
        let fixture: ComponentFixture<CalificacionComponent>;
        let service: CalificacionService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [CineTestModule],
                declarations: [CalificacionComponent],
                providers: []
            })
                .overrideTemplate(CalificacionComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(CalificacionComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CalificacionService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Calificacion(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.calificacions[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
