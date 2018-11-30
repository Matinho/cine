import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CineSharedModule } from 'app/shared';
import {
    PeliculaComponent,
    PeliculaDetailComponent,
    PeliculaUpdateComponent,
    PeliculaDeletePopupComponent,
    PeliculaDeleteDialogComponent,
    peliculaRoute,
    peliculaPopupRoute
} from './';

const ENTITY_STATES = [...peliculaRoute, ...peliculaPopupRoute];

@NgModule({
    imports: [CineSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        PeliculaComponent,
        PeliculaDetailComponent,
        PeliculaUpdateComponent,
        PeliculaDeleteDialogComponent,
        PeliculaDeletePopupComponent
    ],
    entryComponents: [PeliculaComponent, PeliculaUpdateComponent, PeliculaDeleteDialogComponent, PeliculaDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CinePeliculaModule {}
