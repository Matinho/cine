import { Moment } from 'moment';
import { IOcupacion } from 'app/shared/model//ocupacion.model';
import { ICliente } from 'app/shared/model//cliente.model';

export interface ITicket {
    id?: number;
    fechaTransaccion?: Moment;
    butacas?: number;
    importe?: number;
    pagoUuid?: string;
    created?: Moment;
    updated?: Moment;
    ocupacions?: IOcupacion[];
    cliente?: ICliente;
}

export class Ticket implements ITicket {
    constructor(
        public id?: number,
        public fechaTransaccion?: Moment,
        public butacas?: number,
        public importe?: number,
        public pagoUuid?: string,
        public created?: Moment,
        public updated?: Moment,
        public ocupacions?: IOcupacion[],
        public cliente?: ICliente
    ) {}
}
