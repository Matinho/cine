import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { IFuncion } from 'app/shared/model/funcion.model';
import { FuncionService } from './funcion.service';
import { IPelicula } from 'app/shared/model/pelicula.model';
import { PeliculaService } from 'app/entities/pelicula';
import { ISala } from 'app/shared/model/sala.model';
import { SalaService } from 'app/entities/sala';

@Component({
    selector: 'jhi-funcion-update',
    templateUrl: './funcion-update.component.html'
})
export class FuncionUpdateComponent implements OnInit {
    private _funcion: IFuncion;
    isSaving: boolean;

    peliculas: IPelicula[];

    salas: ISala[];
    fechaDp: any;
    created: string;
    updated: string;

    constructor(
        private jhiAlertService: JhiAlertService,
        private funcionService: FuncionService,
        private peliculaService: PeliculaService,
        private salaService: SalaService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ funcion }) => {
            this.funcion = funcion;
        });
        this.peliculaService.query().subscribe(
            (res: HttpResponse<IPelicula[]>) => {
                this.peliculas = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.salaService.query().subscribe(
            (res: HttpResponse<ISala[]>) => {
                this.salas = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.funcion.created = moment(this.created, DATE_TIME_FORMAT);
        this.funcion.updated = moment(this.updated, DATE_TIME_FORMAT);
        if (this.funcion.id !== undefined) {
            this.subscribeToSaveResponse(this.funcionService.update(this.funcion));
        } else {
            this.subscribeToSaveResponse(this.funcionService.create(this.funcion));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IFuncion>>) {
        result.subscribe((res: HttpResponse<IFuncion>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackPeliculaById(index: number, item: IPelicula) {
        return item.id;
    }

    trackSalaById(index: number, item: ISala) {
        return item.id;
    }
    get funcion() {
        return this._funcion;
    }

    set funcion(funcion: IFuncion) {
        this._funcion = funcion;
        this.created = moment(funcion.created).format(DATE_TIME_FORMAT);
        this.updated = moment(funcion.updated).format(DATE_TIME_FORMAT);
    }
}
