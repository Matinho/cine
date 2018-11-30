import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IFuncion } from 'app/shared/model/funcion.model';

type EntityResponseType = HttpResponse<IFuncion>;
type EntityArrayResponseType = HttpResponse<IFuncion[]>;

@Injectable({ providedIn: 'root' })
export class FuncionService {
    private resourceUrl = SERVER_API_URL + 'api/funcions';

    constructor(private http: HttpClient) {}

    create(funcion: IFuncion): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(funcion);
        return this.http
            .post<IFuncion>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(funcion: IFuncion): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(funcion);
        return this.http
            .put<IFuncion>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IFuncion>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IFuncion[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(funcion: IFuncion): IFuncion {
        const copy: IFuncion = Object.assign({}, funcion, {
            fecha: funcion.fecha != null && funcion.fecha.isValid() ? funcion.fecha.format(DATE_FORMAT) : null,
            created: funcion.created != null && funcion.created.isValid() ? funcion.created.toJSON() : null,
            updated: funcion.updated != null && funcion.updated.isValid() ? funcion.updated.toJSON() : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.fecha = res.body.fecha != null ? moment(res.body.fecha) : null;
        res.body.created = res.body.created != null ? moment(res.body.created) : null;
        res.body.updated = res.body.updated != null ? moment(res.body.updated) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((funcion: IFuncion) => {
            funcion.fecha = funcion.fecha != null ? moment(funcion.fecha) : null;
            funcion.created = funcion.created != null ? moment(funcion.created) : null;
            funcion.updated = funcion.updated != null ? moment(funcion.updated) : null;
        });
        return res;
    }
}
