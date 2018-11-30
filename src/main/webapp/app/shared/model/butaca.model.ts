import { Moment } from 'moment';
import { IOcupacion } from 'app/shared/model//ocupacion.model';
import { ISala } from 'app/shared/model//sala.model';

export interface IButaca {
    id?: number;
    fila?: string;
    numero?: number;
    descripcion?: string;
    created?: Moment;
    updated?: Moment;
    ocupacions?: IOcupacion[];
    sala?: ISala;
}

export class Butaca implements IButaca {
    constructor(
        public id?: number,
        public fila?: string,
        public numero?: number,
        public descripcion?: string,
        public created?: Moment,
        public updated?: Moment,
        public ocupacions?: IOcupacion[],
        public sala?: ISala
    ) {}
}
