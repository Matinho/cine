import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICalificacion } from 'app/shared/model/calificacion.model';

@Component({
    selector: 'jhi-calificacion-detail',
    templateUrl: './calificacion-detail.component.html'
})
export class CalificacionDetailComponent implements OnInit {
    calificacion: ICalificacion;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ calificacion }) => {
            this.calificacion = calificacion;
        });
    }

    previousState() {
        window.history.back();
    }
}
