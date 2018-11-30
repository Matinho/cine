import { Moment } from 'moment';
import { ITicket } from 'app/shared/model//ticket.model';

export interface ICliente {
    id?: number;
    documento?: number;
    apellido?: string;
    nombre?: string;
    created?: Moment;
    updated?: Moment;
    tickets?: ITicket[];
}

export class Cliente implements ICliente {
    constructor(
        public id?: number,
        public documento?: number,
        public apellido?: string,
        public nombre?: string,
        public created?: Moment,
        public updated?: Moment,
        public tickets?: ITicket[]
    ) {}
}
