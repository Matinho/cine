import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IOcupacion } from 'app/shared/model/ocupacion.model';

type EntityResponseType = HttpResponse<IOcupacion>;
type EntityArrayResponseType = HttpResponse<IOcupacion[]>;

@Injectable({ providedIn: 'root' })
export class OcupacionService {
    private resourceUrl = SERVER_API_URL + 'api/ocupacions';

    constructor(private http: HttpClient) {}

    create(ocupacion: IOcupacion): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(ocupacion);
        return this.http
            .post<IOcupacion>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(ocupacion: IOcupacion): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(ocupacion);
        return this.http
            .put<IOcupacion>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IOcupacion>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IOcupacion[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(ocupacion: IOcupacion): IOcupacion {
        const copy: IOcupacion = Object.assign({}, ocupacion, {
            created: ocupacion.created != null && ocupacion.created.isValid() ? ocupacion.created.toJSON() : null,
            updated: ocupacion.updated != null && ocupacion.updated.isValid() ? ocupacion.updated.toJSON() : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.created = res.body.created != null ? moment(res.body.created) : null;
        res.body.updated = res.body.updated != null ? moment(res.body.updated) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((ocupacion: IOcupacion) => {
            ocupacion.created = ocupacion.created != null ? moment(ocupacion.created) : null;
            ocupacion.updated = ocupacion.updated != null ? moment(ocupacion.updated) : null;
        });
        return res;
    }
}
