import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { ConseillerComponent } from './conseiller.component';
import { ConseillerDetailComponent } from './conseiller-detail.component';
import { ConseillerPopupComponent } from './conseiller-dialog.component';
import { ConseillerDeletePopupComponent } from './conseiller-delete-dialog.component';

@Injectable()
export class ConseillerResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const conseillerRoute: Routes = [
    {
        path: 'conseiller',
        component: ConseillerComponent,
        resolve: {
            'pagingParams': ConseillerResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'chatApp.conseiller.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'conseiller/:id',
        component: ConseillerDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'chatApp.conseiller.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const conseillerPopupRoute: Routes = [
    {
        path: 'conseiller-new',
        component: ConseillerPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'chatApp.conseiller.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'conseiller/:id/edit',
        component: ConseillerPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'chatApp.conseiller.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'conseiller/:id/delete',
        component: ConseillerDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'chatApp.conseiller.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
