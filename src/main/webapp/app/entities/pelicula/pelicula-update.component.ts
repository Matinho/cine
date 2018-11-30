import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { IPelicula } from 'app/shared/model/pelicula.model';
import { PeliculaService } from './pelicula.service';
import { ICalificacion } from 'app/shared/model/calificacion.model';
import { CalificacionService } from 'app/entities/calificacion';

@Component({
    selector: 'jhi-pelicula-update',
    templateUrl: './pelicula-update.component.html'
})
export class PeliculaUpdateComponent implements OnInit {
    private _pelicula: IPelicula;
    isSaving: boolean;

    calificacions: ICalificacion[];
    estreno: string;
    created: string;
    updated: string;

    constructor(
        private jhiAlertService: JhiAlertService,
        private peliculaService: PeliculaService,
        private calificacionService: CalificacionService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ pelicula }) => {
            this.pelicula = pelicula;
        });
        this.calificacionService.query().subscribe(
            (res: HttpResponse<ICalificacion[]>) => {
                this.calificacions = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.pelicula.estreno = moment(this.estreno, DATE_TIME_FORMAT);
        this.pelicula.created = moment(this.created, DATE_TIME_FORMAT);
        this.pelicula.updated = moment(this.updated, DATE_TIME_FORMAT);
        if (this.pelicula.id !== undefined) {
            this.subscribeToSaveResponse(this.peliculaService.update(this.pelicula));
        } else {
            this.subscribeToSaveResponse(this.peliculaService.create(this.pelicula));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IPelicula>>) {
        result.subscribe((res: HttpResponse<IPelicula>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackCalificacionById(index: number, item: ICalificacion) {
        return item.id;
    }
    get pelicula() {
        return this._pelicula;
    }

    set pelicula(pelicula: IPelicula) {
        this._pelicula = pelicula;
        this.estreno = moment(pelicula.estreno).format(DATE_TIME_FORMAT);
        this.created = moment(pelicula.created).format(DATE_TIME_FORMAT);
        this.updated = moment(pelicula.updated).format(DATE_TIME_FORMAT);
    }
}
