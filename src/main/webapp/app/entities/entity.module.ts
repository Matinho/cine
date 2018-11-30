import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { CineClienteModule } from './cliente/cliente.module';
import { CineTicketModule } from './ticket/ticket.module';
import { CineEntradaModule } from './entrada/entrada.module';
import { CineOcupacionModule } from './ocupacion/ocupacion.module';
import { CineButacaModule } from './butaca/butaca.module';
import { CineSalaModule } from './sala/sala.module';
import { CineFuncionModule } from './funcion/funcion.module';
import { CinePeliculaModule } from './pelicula/pelicula.module';
import { CineCalificacionModule } from './calificacion/calificacion.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        CineClienteModule,
        CineTicketModule,
        CineEntradaModule,
        CineOcupacionModule,
        CineButacaModule,
        CineSalaModule,
        CineFuncionModule,
        CinePeliculaModule,
        CineCalificacionModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CineEntityModule {}
