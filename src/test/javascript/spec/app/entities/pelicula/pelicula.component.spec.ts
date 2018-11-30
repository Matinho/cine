/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { CineTestModule } from '../../../test.module';
import { PeliculaComponent } from 'app/entities/pelicula/pelicula.component';
import { PeliculaService } from 'app/entities/pelicula/pelicula.service';
import { Pelicula } from 'app/shared/model/pelicula.model';

describe('Component Tests', () => {
    describe('Pelicula Management Component', () => {
        let comp: PeliculaComponent;
        let fixture: ComponentFixture<PeliculaComponent>;
        let service: PeliculaService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [CineTestModule],
                declarations: [PeliculaComponent],
                providers: []
            })
                .overrideTemplate(PeliculaComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(PeliculaComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PeliculaService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Pelicula(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.peliculas[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
