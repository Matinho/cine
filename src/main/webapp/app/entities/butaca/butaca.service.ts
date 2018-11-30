import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IButaca } from 'app/shared/model/butaca.model';

type EntityResponseType = HttpResponse<IButaca>;
type EntityArrayResponseType = HttpResponse<IButaca[]>;

@Injectable({ providedIn: 'root' })
export class ButacaService {
    private resourceUrl = SERVER_API_URL + 'api/butacas';

    constructor(private http: HttpClient) {}

    create(butaca: IButaca): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(butaca);
        return this.http
            .post<IButaca>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(butaca: IButaca): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(butaca);
        return this.http
            .put<IButaca>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IButaca>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IButaca[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(butaca: IButaca): IButaca {
        const copy: IButaca = Object.assign({}, butaca, {
            created: butaca.created != null && butaca.created.isValid() ? butaca.created.toJSON() : null,
            updated: butaca.updated != null && butaca.updated.isValid() ? butaca.updated.toJSON() : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.created = res.body.created != null ? moment(res.body.created) : null;
        res.body.updated = res.body.updated != null ? moment(res.body.updated) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((butaca: IButaca) => {
            butaca.created = butaca.created != null ? moment(butaca.created) : null;
            butaca.updated = butaca.updated != null ? moment(butaca.updated) : null;
        });
        return res;
    }
}
