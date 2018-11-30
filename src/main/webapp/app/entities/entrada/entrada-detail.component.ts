import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEntrada } from 'app/shared/model/entrada.model';

@Component({
    selector: 'jhi-entrada-detail',
    templateUrl: './entrada-detail.component.html'
})
export class EntradaDetailComponent implements OnInit {
    entrada: IEntrada;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ entrada }) => {
            this.entrada = entrada;
        });
    }

    previousState() {
        window.history.back();
    }
}
