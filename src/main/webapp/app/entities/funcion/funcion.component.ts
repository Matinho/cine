import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IFuncion } from 'app/shared/model/funcion.model';
import { Principal } from 'app/core';
import { FuncionService } from './funcion.service';

@Component({
    selector: 'jhi-funcion',
    templateUrl: './funcion.component.html'
})
export class FuncionComponent implements OnInit, OnDestroy {
    funcions: IFuncion[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private funcionService: FuncionService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {}

    loadAll() {
        this.funcionService.query().subscribe(
            (res: HttpResponse<IFuncion[]>) => {
                this.funcions = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInFuncions();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IFuncion) {
        return item.id;
    }

    registerChangeInFuncions() {
        this.eventSubscriber = this.eventManager.subscribe('funcionListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
