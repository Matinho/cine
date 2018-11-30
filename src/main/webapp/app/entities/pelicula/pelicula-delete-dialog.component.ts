import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPelicula } from 'app/shared/model/pelicula.model';
import { PeliculaService } from './pelicula.service';

@Component({
    selector: 'jhi-pelicula-delete-dialog',
    templateUrl: './pelicula-delete-dialog.component.html'
})
export class PeliculaDeleteDialogComponent {
    pelicula: IPelicula;

    constructor(private peliculaService: PeliculaService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.peliculaService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'peliculaListModification',
                content: 'Deleted an pelicula'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-pelicula-delete-popup',
    template: ''
})
export class PeliculaDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ pelicula }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(PeliculaDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.pelicula = pelicula;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
