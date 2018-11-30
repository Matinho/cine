/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { CineTestModule } from '../../../test.module';
import { FuncionComponent } from 'app/entities/funcion/funcion.component';
import { FuncionService } from 'app/entities/funcion/funcion.service';
import { Funcion } from 'app/shared/model/funcion.model';

describe('Component Tests', () => {
    describe('Funcion Management Component', () => {
        let comp: FuncionComponent;
        let fixture: ComponentFixture<FuncionComponent>;
        let service: FuncionService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [CineTestModule],
                declarations: [FuncionComponent],
                providers: []
            })
                .overrideTemplate(FuncionComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(FuncionComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(FuncionService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Funcion(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.funcions[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
