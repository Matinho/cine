import { Moment } from 'moment';
import { ITicket } from 'app/shared/model//ticket.model';
import { IEntrada } from 'app/shared/model//entrada.model';
import { IButaca } from 'app/shared/model//butaca.model';
import { IFuncion } from 'app/shared/model//funcion.model';

export interface IOcupacion {
    id?: number;
    valor?: number;
    created?: Moment;
    updated?: Moment;
    ticket?: ITicket;
    entrada?: IEntrada;
    butaca?: IButaca;
    funcion?: IFuncion;
}

export class Ocupacion implements IOcupacion {
    constructor(
        public id?: number,
        public valor?: number,
        public created?: Moment,
        public updated?: Moment,
        public ticket?: ITicket,
        public entrada?: IEntrada,
        public butaca?: IButaca,
        public funcion?: IFuncion
    ) {}
}
