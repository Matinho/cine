import { Moment } from 'moment';
import { IFuncion } from 'app/shared/model//funcion.model';
import { ICalificacion } from 'app/shared/model//calificacion.model';

export interface IPelicula {
    id?: number;
    titulo?: string;
    estreno?: Moment;
    created?: Moment;
    updated?: Moment;
    funcions?: IFuncion[];
    calificacion?: ICalificacion;
}

export class Pelicula implements IPelicula {
    constructor(
        public id?: number,
        public titulo?: string,
        public estreno?: Moment,
        public created?: Moment,
        public updated?: Moment,
        public funcions?: IFuncion[],
        public calificacion?: ICalificacion
    ) {}
}
