import { Moment } from 'moment';
import { IFuncion } from 'app/shared/model//funcion.model';
import { IButaca } from 'app/shared/model//butaca.model';

export interface ISala {
    id?: number;
    descripcion?: string;
    capacidad?: number;
    created?: Moment;
    updated?: Moment;
    funcions?: IFuncion[];
    butacas?: IButaca[];
}

export class Sala implements ISala {
    constructor(
        public id?: number,
        public descripcion?: string,
        public capacidad?: number,
        public created?: Moment,
        public updated?: Moment,
        public funcions?: IFuncion[],
        public butacas?: IButaca[]
    ) {}
}
