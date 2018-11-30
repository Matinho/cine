import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Pelicula } from 'app/shared/model/pelicula.model';
import { PeliculaService } from './pelicula.service';
import { PeliculaComponent } from './pelicula.component';
import { PeliculaDetailComponent } from './pelicula-detail.component';
import { PeliculaUpdateComponent } from './pelicula-update.component';
import { PeliculaDeletePopupComponent } from './pelicula-delete-dialog.component';
import { IPelicula } from 'app/shared/model/pelicula.model';

@Injectable({ providedIn: 'root' })
export class PeliculaResolve implements Resolve<IPelicula> {
    constructor(private service: PeliculaService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((pelicula: HttpResponse<Pelicula>) => pelicula.body));
        }
        return of(new Pelicula());
    }
}

export const peliculaRoute: Routes = [
    {
        path: 'pelicula',
        component: PeliculaComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Peliculas'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'pelicula/:id/view',
        component: PeliculaDetailComponent,
        resolve: {
            pelicula: PeliculaResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Peliculas'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'pelicula/new',
        component: PeliculaUpdateComponent,
        resolve: {
            pelicula: PeliculaResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Peliculas'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'pelicula/:id/edit',
        component: PeliculaUpdateComponent,
        resolve: {
            pelicula: PeliculaResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Peliculas'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const peliculaPopupRoute: Routes = [
    {
        path: 'pelicula/:id/delete',
        component: PeliculaDeletePopupComponent,
        resolve: {
            pelicula: PeliculaResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Peliculas'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
