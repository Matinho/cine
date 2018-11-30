import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ITicket } from 'app/shared/model/ticket.model';

type EntityResponseType = HttpResponse<ITicket>;
type EntityArrayResponseType = HttpResponse<ITicket[]>;

@Injectable({ providedIn: 'root' })
export class TicketService {
    private resourceUrl = SERVER_API_URL + 'api/tickets';

    constructor(private http: HttpClient) {}

    create(ticket: ITicket): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(ticket);
        return this.http
            .post<ITicket>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(ticket: ITicket): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(ticket);
        return this.http
            .put<ITicket>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<ITicket>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ITicket[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(ticket: ITicket): ITicket {
        const copy: ITicket = Object.assign({}, ticket, {
            fechaTransaccion:
                ticket.fechaTransaccion != null && ticket.fechaTransaccion.isValid() ? ticket.fechaTransaccion.format(DATE_FORMAT) : null,
            created: ticket.created != null && ticket.created.isValid() ? ticket.created.toJSON() : null,
            updated: ticket.updated != null && ticket.updated.isValid() ? ticket.updated.toJSON() : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.fechaTransaccion = res.body.fechaTransaccion != null ? moment(res.body.fechaTransaccion) : null;
        res.body.created = res.body.created != null ? moment(res.body.created) : null;
        res.body.updated = res.body.updated != null ? moment(res.body.updated) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((ticket: ITicket) => {
            ticket.fechaTransaccion = ticket.fechaTransaccion != null ? moment(ticket.fechaTransaccion) : null;
            ticket.created = ticket.created != null ? moment(ticket.created) : null;
            ticket.updated = ticket.updated != null ? moment(ticket.updated) : null;
        });
        return res;
    }
}
