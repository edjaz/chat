import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { ExtraInformationComponent } from './extra-information.component';
import { ExtraInformationDetailComponent } from './extra-information-detail.component';
import { ExtraInformationPopupComponent } from './extra-information-dialog.component';
import { ExtraInformationDeletePopupComponent } from './extra-information-delete-dialog.component';

@Injectable()
export class ExtraInformationResolvePagingParams implements Resolve<any> {

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

export const extraInformationRoute: Routes = [
    {
        path: 'extra-information',
        component: ExtraInformationComponent,
        resolve: {
            'pagingParams': ExtraInformationResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'chatApp.extraInformation.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'extra-information/:id',
        component: ExtraInformationDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'chatApp.extraInformation.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const extraInformationPopupRoute: Routes = [
    {
        path: 'extra-information-new',
        component: ExtraInformationPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'chatApp.extraInformation.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'extra-information/:id/edit',
        component: ExtraInformationPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'chatApp.extraInformation.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'extra-information/:id/delete',
        component: ExtraInformationDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'chatApp.extraInformation.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
