import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Entrada } from 'app/shared/model/entrada.model';
import { EntradaService } from './entrada.service';
import { EntradaComponent } from './entrada.component';
import { EntradaDetailComponent } from './entrada-detail.component';
import { EntradaUpdateComponent } from './entrada-update.component';
import { EntradaDeletePopupComponent } from './entrada-delete-dialog.component';
import { IEntrada } from 'app/shared/model/entrada.model';

@Injectable({ providedIn: 'root' })
export class EntradaResolve implements Resolve<IEntrada> {
    constructor(private service: EntradaService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((entrada: HttpResponse<Entrada>) => entrada.body));
        }
        return of(new Entrada());
    }
}

export const entradaRoute: Routes = [
    {
        path: 'entrada',
        component: EntradaComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Entradas'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'entrada/:id/view',
        component: EntradaDetailComponent,
        resolve: {
            entrada: EntradaResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Entradas'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'entrada/new',
        component: EntradaUpdateComponent,
        resolve: {
            entrada: EntradaResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Entradas'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'entrada/:id/edit',
        component: EntradaUpdateComponent,
        resolve: {
            entrada: EntradaResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Entradas'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const entradaPopupRoute: Routes = [
    {
        path: 'entrada/:id/delete',
        component: EntradaDeletePopupComponent,
        resolve: {
            entrada: EntradaResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Entradas'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
