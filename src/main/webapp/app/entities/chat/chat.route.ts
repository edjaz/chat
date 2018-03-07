import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { ChatComponent } from './chat.component';
import { ChatDetailComponent } from './chat-detail.component';
import { ChatPopupComponent } from './chat-dialog.component';
import { ChatDeletePopupComponent } from './chat-delete-dialog.component';

@Injectable()
export class ChatResolvePagingParams implements Resolve<any> {

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

export const chatRoute: Routes = [
    {
        path: 'chat',
        component: ChatComponent,
        resolve: {
            'pagingParams': ChatResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'chatApp.chat.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'chat/:id',
        component: ChatDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'chatApp.chat.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const chatPopupRoute: Routes = [
    {
        path: 'chat-new',
        component: ChatPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'chatApp.chat.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'chat/:id/edit',
        component: ChatPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'chatApp.chat.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'chat/:id/delete',
        component: ChatDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'chatApp.chat.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
