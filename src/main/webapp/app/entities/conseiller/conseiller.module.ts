import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ChatSharedModule } from '../../shared';
import { ChatAdminModule } from '../../admin/admin.module';
import {
    ConseillerService,
    ConseillerPopupService,
    ConseillerComponent,
    ConseillerDetailComponent,
    ConseillerDialogComponent,
    ConseillerPopupComponent,
    ConseillerDeletePopupComponent,
    ConseillerDeleteDialogComponent,
    conseillerRoute,
    conseillerPopupRoute,
    ConseillerResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...conseillerRoute,
    ...conseillerPopupRoute,
];

@NgModule({
    imports: [
        ChatSharedModule,
        ChatAdminModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        ConseillerComponent,
        ConseillerDetailComponent,
        ConseillerDialogComponent,
        ConseillerDeleteDialogComponent,
        ConseillerPopupComponent,
        ConseillerDeletePopupComponent,
    ],
    entryComponents: [
        ConseillerComponent,
        ConseillerDialogComponent,
        ConseillerPopupComponent,
        ConseillerDeleteDialogComponent,
        ConseillerDeletePopupComponent,
    ],
    providers: [
        ConseillerService,
        ConseillerPopupService,
        ConseillerResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ChatConseillerModule {}
