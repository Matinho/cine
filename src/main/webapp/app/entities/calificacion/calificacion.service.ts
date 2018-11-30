import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICalificacion } from 'app/shared/model/calificacion.model';

type EntityResponseType = HttpResponse<ICalificacion>;
type EntityArrayResponseType = HttpResponse<ICalificacion[]>;

@Injectable({ providedIn: 'root' })
export class CalificacionService {
    private resourceUrl = SERVER_API_URL + 'api/calificacions';

    constructor(private http: HttpClient) {}

    create(calificacion: ICalificacion): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(calificacion);
        return this.http
            .post<ICalificacion>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(calificacion: ICalificacion): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(calificacion);
        return this.http
            .put<ICalificacion>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<ICalificacion>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ICalificacion[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(calificacion: ICalificacion): ICalificacion {
        const copy: ICalificacion = Object.assign({}, calificacion, {
            created: calificacion.created != null && calificacion.created.isValid() ? calificacion.created.toJSON() : null,
            updated: calificacion.updated != null && calificacion.updated.isValid() ? calificacion.updated.toJSON() : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.created = res.body.created != null ? moment(res.body.created) : null;
        res.body.updated = res.body.updated != null ? moment(res.body.updated) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((calificacion: ICalificacion) => {
            calificacion.created = calificacion.created != null ? moment(calificacion.created) : null;
            calificacion.updated = calificacion.updated != null ? moment(calificacion.updated) : null;
        });
        return res;
    }
}
