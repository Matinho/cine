import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IButaca } from 'app/shared/model/butaca.model';

@Component({
    selector: 'jhi-butaca-detail',
    templateUrl: './butaca-detail.component.html'
})
export class ButacaDetailComponent implements OnInit {
    butaca: IButaca;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ butaca }) => {
            this.butaca = butaca;
        });
    }

    previousState() {
        window.history.back();
    }
}
