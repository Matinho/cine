import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CineSharedModule } from 'app/shared';
import {
    SalaComponent,
    SalaDetailComponent,
    SalaUpdateComponent,
    SalaDeletePopupComponent,
    SalaDeleteDialogComponent,
    salaRoute,
    salaPopupRoute
} from './';

const ENTITY_STATES = [...salaRoute, ...salaPopupRoute];

@NgModule({
    imports: [CineSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [SalaComponent, SalaDetailComponent, SalaUpdateComponent, SalaDeleteDialogComponent, SalaDeletePopupComponent],
    entryComponents: [SalaComponent, SalaUpdateComponent, SalaDeleteDialogComponent, SalaDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CineSalaModule {}
