import { Moment } from 'moment';
import { IPelicula } from 'app/shared/model//pelicula.model';

export interface ICalificacion {
    id?: number;
    descripcion?: string;
    created?: Moment;
    updated?: Moment;
    peliculas?: IPelicula[];
}

export class Calificacion implements ICalificacion {
    constructor(
        public id?: number,
        public descripcion?: string,
        public created?: Moment,
        public updated?: Moment,
        public peliculas?: IPelicula[]
    ) {}
}
