import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IPelicula } from 'app/shared/model/pelicula.model';
import { Principal } from 'app/core';
import { PeliculaService } from './pelicula.service';

@Component({
    selector: 'jhi-pelicula',
    templateUrl: './pelicula.component.html'
})
export class PeliculaComponent implements OnInit, OnDestroy {
    peliculas: IPelicula[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private peliculaService: PeliculaService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {}

    loadAll() {
        this.peliculaService.query().subscribe(
            (res: HttpResponse<IPelicula[]>) => {
                this.peliculas = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInPeliculas();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IPelicula) {
        return item.id;
    }

    registerChangeInPeliculas() {
        this.eventSubscriber = this.eventManager.subscribe('peliculaListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
