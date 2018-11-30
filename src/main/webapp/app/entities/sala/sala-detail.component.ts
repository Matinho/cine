import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISala } from 'app/shared/model/sala.model';

@Component({
    selector: 'jhi-sala-detail',
    templateUrl: './sala-detail.component.html'
})
export class SalaDetailComponent implements OnInit {
    sala: ISala;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ sala }) => {
            this.sala = sala;
        });
    }

    previousState() {
        window.history.back();
    }
}
