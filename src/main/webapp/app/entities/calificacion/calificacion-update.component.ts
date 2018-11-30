import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { ICalificacion } from 'app/shared/model/calificacion.model';
import { CalificacionService } from './calificacion.service';

@Component({
    selector: 'jhi-calificacion-update',
    templateUrl: './calificacion-update.component.html'
})
export class CalificacionUpdateComponent implements OnInit {
    private _calificacion: ICalificacion;
    isSaving: boolean;
    created: string;
    updated: string;

    constructor(private calificacionService: CalificacionService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ calificacion }) => {
            this.calificacion = calificacion;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.calificacion.created = moment(this.created, DATE_TIME_FORMAT);
        this.calificacion.updated = moment(this.updated, DATE_TIME_FORMAT);
        if (this.calificacion.id !== undefined) {
            this.subscribeToSaveResponse(this.calificacionService.update(this.calificacion));
        } else {
            this.subscribeToSaveResponse(this.calificacionService.create(this.calificacion));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ICalificacion>>) {
        result.subscribe((res: HttpResponse<ICalificacion>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get calificacion() {
        return this._calificacion;
    }

    set calificacion(calificacion: ICalificacion) {
        this._calificacion = calificacion;
        this.created = moment(calificacion.created).format(DATE_TIME_FORMAT);
        this.updated = moment(calificacion.updated).format(DATE_TIME_FORMAT);
    }
}
