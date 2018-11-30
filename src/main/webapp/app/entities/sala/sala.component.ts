import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ISala } from 'app/shared/model/sala.model';
import { Principal } from 'app/core';
import { SalaService } from './sala.service';

@Component({
    selector: 'jhi-sala',
    templateUrl: './sala.component.html'
})
export class SalaComponent implements OnInit, OnDestroy {
    salas: ISala[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private salaService: SalaService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {}

    loadAll() {
        this.salaService.query().subscribe(
            (res: HttpResponse<ISala[]>) => {
                this.salas = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInSalas();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ISala) {
        return item.id;
    }

    registerChangeInSalas() {
        this.eventSubscriber = this.eventManager.subscribe('salaListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
