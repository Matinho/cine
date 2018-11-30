import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IEntrada } from 'app/shared/model/entrada.model';
import { EntradaService } from './entrada.service';

@Component({
    selector: 'jhi-entrada-update',
    templateUrl: './entrada-update.component.html'
})
export class EntradaUpdateComponent implements OnInit {
    private _entrada: IEntrada;
    isSaving: boolean;
    created: string;
    updated: string;

    constructor(private entradaService: EntradaService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ entrada }) => {
            this.entrada = entrada;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.entrada.created = moment(this.created, DATE_TIME_FORMAT);
        this.entrada.updated = moment(this.updated, DATE_TIME_FORMAT);
        if (this.entrada.id !== undefined) {
            this.subscribeToSaveResponse(this.entradaService.update(this.entrada));
        } else {
            this.subscribeToSaveResponse(this.entradaService.create(this.entrada));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IEntrada>>) {
        result.subscribe((res: HttpResponse<IEntrada>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get entrada() {
        return this._entrada;
    }

    set entrada(entrada: IEntrada) {
        this._entrada = entrada;
        this.created = moment(entrada.created).format(DATE_TIME_FORMAT);
        this.updated = moment(entrada.updated).format(DATE_TIME_FORMAT);
    }
}
