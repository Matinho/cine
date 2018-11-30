/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { CineTestModule } from '../../../test.module';
import { SalaComponent } from 'app/entities/sala/sala.component';
import { SalaService } from 'app/entities/sala/sala.service';
import { Sala } from 'app/shared/model/sala.model';

describe('Component Tests', () => {
    describe('Sala Management Component', () => {
        let comp: SalaComponent;
        let fixture: ComponentFixture<SalaComponent>;
        let service: SalaService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [CineTestModule],
                declarations: [SalaComponent],
                providers: []
            })
                .overrideTemplate(SalaComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(SalaComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SalaService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Sala(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.salas[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
