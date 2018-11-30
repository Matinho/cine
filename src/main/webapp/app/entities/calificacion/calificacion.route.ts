import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Calificacion } from 'app/shared/model/calificacion.model';
import { CalificacionService } from './calificacion.service';
import { CalificacionComponent } from './calificacion.component';
import { CalificacionDetailComponent } from './calificacion-detail.component';
import { CalificacionUpdateComponent } from './calificacion-update.component';
import { CalificacionDeletePopupComponent } from './calificacion-delete-dialog.component';
import { ICalificacion } from 'app/shared/model/calificacion.model';

@Injectable({ providedIn: 'root' })
export class CalificacionResolve implements Resolve<ICalificacion> {
    constructor(private service: CalificacionService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((calificacion: HttpResponse<Calificacion>) => calificacion.body));
        }
        return of(new Calificacion());
    }
}

export const calificacionRoute: Routes = [
    {
        path: 'calificacion',
        component: CalificacionComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Calificacions'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'calificacion/:id/view',
        component: CalificacionDetailComponent,
        resolve: {
            calificacion: CalificacionResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Calificacions'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'calificacion/new',
        component: CalificacionUpdateComponent,
        resolve: {
            calificacion: CalificacionResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Calificacions'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'calificacion/:id/edit',
        component: CalificacionUpdateComponent,
        resolve: {
            calificacion: CalificacionResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Calificacions'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const calificacionPopupRoute: Routes = [
    {
        path: 'calificacion/:id/delete',
        component: CalificacionDeletePopupComponent,
        resolve: {
            calificacion: CalificacionResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Calificacions'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
