import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ISala } from 'app/shared/model/sala.model';

type EntityResponseType = HttpResponse<ISala>;
type EntityArrayResponseType = HttpResponse<ISala[]>;

@Injectable({ providedIn: 'root' })
export class SalaService {
    private resourceUrl = SERVER_API_URL + 'api/salas';

    constructor(private http: HttpClient) {}

    create(sala: ISala): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(sala);
        return this.http
            .post<ISala>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(sala: ISala): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(sala);
        return this.http
            .put<ISala>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<ISala>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ISala[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(sala: ISala): ISala {
        const copy: ISala = Object.assign({}, sala, {
            created: sala.created != null && sala.created.isValid() ? sala.created.toJSON() : null,
            updated: sala.updated != null && sala.updated.isValid() ? sala.updated.toJSON() : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.created = res.body.created != null ? moment(res.body.created) : null;
        res.body.updated = res.body.updated != null ? moment(res.body.updated) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((sala: ISala) => {
            sala.created = sala.created != null ? moment(sala.created) : null;
            sala.updated = sala.updated != null ? moment(sala.updated) : null;
        });
        return res;
    }
}
