import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IOcupacion } from 'app/shared/model/ocupacion.model';
import { Principal } from 'app/core';
import { OcupacionService } from './ocupacion.service';

@Component({
    selector: 'jhi-ocupacion',
    templateUrl: './ocupacion.component.html'
})
export class OcupacionComponent implements OnInit, OnDestroy {
    ocupacions: IOcupacion[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private ocupacionService: OcupacionService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {}

    loadAll() {
        this.ocupacionService.query().subscribe(
            (res: HttpResponse<IOcupacion[]>) => {
                this.ocupacions = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInOcupacions();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IOcupacion) {
        return item.id;
    }

    registerChangeInOcupacions() {
        this.eventSubscriber = this.eventManager.subscribe('ocupacionListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
