import { Moment } from 'moment';
import { IOcupacion } from 'app/shared/model//ocupacion.model';

export interface IEntrada {
    id?: number;
    descripcion?: string;
    valor?: number;
    created?: Moment;
    updated?: Moment;
    ocupacions?: IOcupacion[];
}

export class Entrada implements IEntrada {
    constructor(
        public id?: number,
        public descripcion?: string,
        public valor?: number,
        public created?: Moment,
        public updated?: Moment,
        public ocupacions?: IOcupacion[]
    ) {}
}
