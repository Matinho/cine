import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOcupacion } from 'app/shared/model/ocupacion.model';

@Component({
    selector: 'jhi-ocupacion-detail',
    templateUrl: './ocupacion-detail.component.html'
})
export class OcupacionDetailComponent implements OnInit {
    ocupacion: IOcupacion;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ ocupacion }) => {
            this.ocupacion = ocupacion;
        });
    }

    previousState() {
        window.history.back();
    }
}
