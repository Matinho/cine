import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Ocupacion } from 'app/shared/model/ocupacion.model';
import { OcupacionService } from './ocupacion.service';
import { OcupacionComponent } from './ocupacion.component';
import { OcupacionDetailComponent } from './ocupacion-detail.component';
import { OcupacionUpdateComponent } from './ocupacion-update.component';
import { OcupacionDeletePopupComponent } from './ocupacion-delete-dialog.component';
import { IOcupacion } from 'app/shared/model/ocupacion.model';

@Injectable({ providedIn: 'root' })
export class OcupacionResolve implements Resolve<IOcupacion> {
    constructor(private service: OcupacionService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((ocupacion: HttpResponse<Ocupacion>) => ocupacion.body));
        }
        return of(new Ocupacion());
    }
}

export const ocupacionRoute: Routes = [
    {
        path: 'ocupacion',
        component: OcupacionComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Ocupacions'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'ocupacion/:id/view',
        component: OcupacionDetailComponent,
        resolve: {
            ocupacion: OcupacionResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Ocupacions'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'ocupacion/new',
        component: OcupacionUpdateComponent,
        resolve: {
            ocupacion: OcupacionResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Ocupacions'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'ocupacion/:id/edit',
        component: OcupacionUpdateComponent,
        resolve: {
            ocupacion: OcupacionResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Ocupacions'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const ocupacionPopupRoute: Routes = [
    {
        path: 'ocupacion/:id/delete',
        component: OcupacionDeletePopupComponent,
        resolve: {
            ocupacion: OcupacionResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Ocupacions'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
