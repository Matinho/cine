import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IButaca } from 'app/shared/model/butaca.model';
import { Principal } from 'app/core';
import { ButacaService } from './butaca.service';

@Component({
    selector: 'jhi-butaca',
    templateUrl: './butaca.component.html'
})
export class ButacaComponent implements OnInit, OnDestroy {
    butacas: IButaca[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private butacaService: ButacaService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {}

    loadAll() {
        this.butacaService.query().subscribe(
            (res: HttpResponse<IButaca[]>) => {
                this.butacas = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInButacas();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IButaca) {
        return item.id;
    }

    registerChangeInButacas() {
        this.eventSubscriber = this.eventManager.subscribe('butacaListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
