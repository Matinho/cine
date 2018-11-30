import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IPelicula } from 'app/shared/model/pelicula.model';

type EntityResponseType = HttpResponse<IPelicula>;
type EntityArrayResponseType = HttpResponse<IPelicula[]>;

@Injectable({ providedIn: 'root' })
export class PeliculaService {
    private resourceUrl = SERVER_API_URL + 'api/peliculas';

    constructor(private http: HttpClient) {}

    create(pelicula: IPelicula): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(pelicula);
        return this.http
            .post<IPelicula>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(pelicula: IPelicula): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(pelicula);
        return this.http
            .put<IPelicula>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IPelicula>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IPelicula[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(pelicula: IPelicula): IPelicula {
        const copy: IPelicula = Object.assign({}, pelicula, {
            estreno: pelicula.estreno != null && pelicula.estreno.isValid() ? pelicula.estreno.toJSON() : null,
            created: pelicula.created != null && pelicula.created.isValid() ? pelicula.created.toJSON() : null,
            updated: pelicula.updated != null && pelicula.updated.isValid() ? pelicula.updated.toJSON() : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.estreno = res.body.estreno != null ? moment(res.body.estreno) : null;
        res.body.created = res.body.created != null ? moment(res.body.created) : null;
        res.body.updated = res.body.updated != null ? moment(res.body.updated) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((pelicula: IPelicula) => {
            pelicula.estreno = pelicula.estreno != null ? moment(pelicula.estreno) : null;
            pelicula.created = pelicula.created != null ? moment(pelicula.created) : null;
            pelicula.updated = pelicula.updated != null ? moment(pelicula.updated) : null;
        });
        return res;
    }
}
