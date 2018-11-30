import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ICalificacion } from 'app/shared/model/calificacion.model';
import { Principal } from 'app/core';
import { CalificacionService } from './calificacion.service';

@Component({
    selector: 'jhi-calificacion',
    templateUrl: './calificacion.component.html'
})
export class CalificacionComponent implements OnInit, OnDestroy {
    calificacions: ICalificacion[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private calificacionService: CalificacionService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {}

    loadAll() {
        this.calificacionService.query().subscribe(
            (res: HttpResponse<ICalificacion[]>) => {
                this.calificacions = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInCalificacions();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ICalificacion) {
        return item.id;
    }

    registerChangeInCalificacions() {
        this.eventSubscriber = this.eventManager.subscribe('calificacionListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
