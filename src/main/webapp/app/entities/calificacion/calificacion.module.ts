import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CineSharedModule } from 'app/shared';
import {
    CalificacionComponent,
    CalificacionDetailComponent,
    CalificacionUpdateComponent,
    CalificacionDeletePopupComponent,
    CalificacionDeleteDialogComponent,
    calificacionRoute,
    calificacionPopupRoute
} from './';

const ENTITY_STATES = [...calificacionRoute, ...calificacionPopupRoute];

@NgModule({
    imports: [CineSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        CalificacionComponent,
        CalificacionDetailComponent,
        CalificacionUpdateComponent,
        CalificacionDeleteDialogComponent,
        CalificacionDeletePopupComponent
    ],
    entryComponents: [
        CalificacionComponent,
        CalificacionUpdateComponent,
        CalificacionDeleteDialogComponent,
        CalificacionDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CineCalificacionModule {}
