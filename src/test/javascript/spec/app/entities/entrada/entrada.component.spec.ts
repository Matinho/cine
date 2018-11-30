/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { CineTestModule } from '../../../test.module';
import { EntradaComponent } from 'app/entities/entrada/entrada.component';
import { EntradaService } from 'app/entities/entrada/entrada.service';
import { Entrada } from 'app/shared/model/entrada.model';

describe('Component Tests', () => {
    describe('Entrada Management Component', () => {
        let comp: EntradaComponent;
        let fixture: ComponentFixture<EntradaComponent>;
        let service: EntradaService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [CineTestModule],
                declarations: [EntradaComponent],
                providers: []
            })
                .overrideTemplate(EntradaComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(EntradaComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(EntradaService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Entrada(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.entradas[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
