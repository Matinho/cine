import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Ticket } from 'app/shared/model/ticket.model';
import { TicketService } from './ticket.service';
import { TicketComponent } from './ticket.component';
import { TicketDetailComponent } from './ticket-detail.component';
import { TicketUpdateComponent } from './ticket-update.component';
import { TicketDeletePopupComponent } from './ticket-delete-dialog.component';
import { ITicket } from 'app/shared/model/ticket.model';

@Injectable({ providedIn: 'root' })
export class TicketResolve implements Resolve<ITicket> {
    constructor(private service: TicketService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((ticket: HttpResponse<Ticket>) => ticket.body));
        }
        return of(new Ticket());
    }
}

export const ticketRoute: Routes = [
    {
        path: 'ticket',
        component: TicketComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Tickets'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'ticket/:id/view',
        component: TicketDetailComponent,
        resolve: {
            ticket: TicketResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Tickets'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'ticket/new',
        component: TicketUpdateComponent,
        resolve: {
            ticket: TicketResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Tickets'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'ticket/:id/edit',
        component: TicketUpdateComponent,
        resolve: {
            ticket: TicketResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Tickets'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const ticketPopupRoute: Routes = [
    {
        path: 'ticket/:id/delete',
        component: TicketDeletePopupComponent,
        resolve: {
            ticket: TicketResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Tickets'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
