import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IEntrada } from 'app/shared/model/entrada.model';
import { Principal } from 'app/core';
import { EntradaService } from './entrada.service';

@Component({
    selector: 'jhi-entrada',
    templateUrl: './entrada.component.html'
})
export class EntradaComponent implements OnInit, OnDestroy {
    entradas: IEntrada[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private entradaService: EntradaService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {}

    loadAll() {
        this.entradaService.query().subscribe(
            (res: HttpResponse<IEntrada[]>) => {
                this.entradas = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInEntradas();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IEntrada) {
        return item.id;
    }

    registerChangeInEntradas() {
        this.eventSubscriber = this.eventManager.subscribe('entradaListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
