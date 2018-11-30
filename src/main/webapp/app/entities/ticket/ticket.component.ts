import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ITicket } from 'app/shared/model/ticket.model';
import { Principal } from 'app/core';
import { TicketService } from './ticket.service';

@Component({
    selector: 'jhi-ticket',
    templateUrl: './ticket.component.html'
})
export class TicketComponent implements OnInit, OnDestroy {
    tickets: ITicket[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private ticketService: TicketService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {}

    loadAll() {
        this.ticketService.query().subscribe(
            (res: HttpResponse<ITicket[]>) => {
                this.tickets = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInTickets();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ITicket) {
        return item.id;
    }

    registerChangeInTickets() {
        this.eventSubscriber = this.eventManager.subscribe('ticketListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
