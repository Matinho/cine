import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CineSharedModule } from 'app/shared';
import {
    EntradaComponent,
    EntradaDetailComponent,
    EntradaUpdateComponent,
    EntradaDeletePopupComponent,
    EntradaDeleteDialogComponent,
    entradaRoute,
    entradaPopupRoute
} from './';

const ENTITY_STATES = [...entradaRoute, ...entradaPopupRoute];

@NgModule({
    imports: [CineSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        EntradaComponent,
        EntradaDetailComponent,
        EntradaUpdateComponent,
        EntradaDeleteDialogComponent,
        EntradaDeletePopupComponent
    ],
    entryComponents: [EntradaComponent, EntradaUpdateComponent, EntradaDeleteDialogComponent, EntradaDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CineEntradaModule {}
