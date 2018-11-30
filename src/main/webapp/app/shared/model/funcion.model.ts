import { Moment } from 'moment';
import { IOcupacion } from 'app/shared/model//ocupacion.model';
import { IPelicula } from 'app/shared/model//pelicula.model';
import { ISala } from 'app/shared/model//sala.model';

export interface IFuncion {
    id?: number;
    fecha?: Moment;
    valor?: number;
    created?: Moment;
    updated?: Moment;
    ocupacions?: IOcupacion[];
    pelicula?: IPelicula;
    sala?: ISala;
}

export class Funcion implements IFuncion {
    constructor(
        public id?: number,
        public fecha?: Moment,
        public valor?: number,
        public created?: Moment,
        public updated?: Moment,
        public ocupacions?: IOcupacion[],
        public pelicula?: IPelicula,
        public sala?: ISala
    ) {}
}
