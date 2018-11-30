import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CineSharedModule } from 'app/shared';
import {
    OcupacionComponent,
    OcupacionDetailComponent,
    OcupacionUpdateComponent,
    OcupacionDeletePopupComponent,
    OcupacionDeleteDialogComponent,
    ocupacionRoute,
    ocupacionPopupRoute
} from './';

const ENTITY_STATES = [...ocupacionRoute, ...ocupacionPopupRoute];

@NgModule({
    imports: [CineSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        OcupacionComponent,
        OcupacionDetailComponent,
        OcupacionUpdateComponent,
        OcupacionDeleteDialogComponent,
        OcupacionDeletePopupComponent
    ],
    entryComponents: [OcupacionComponent, OcupacionUpdateComponent, OcupacionDeleteDialogComponent, OcupacionDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CineOcupacionModule {}
