import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ChatSharedModule } from '../../shared';
import {
    ClientService,
    ClientPopupService,
    ClientComponent,
    ClientDetailComponent,
    ClientDialogComponent,
    ClientPopupComponent,
    ClientDeletePopupComponent,
    ClientDeleteDialogComponent,
    clientRoute,
    clientPopupRoute,
    ClientResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...clientRoute,
    ...clientPopupRoute,
];

@NgModule({
    imports: [
        ChatSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        ClientComponent,
        ClientDetailComponent,
        ClientDialogComponent,
        ClientDeleteDialogComponent,
        ClientPopupComponent,
        ClientDeletePopupComponent,
    ],
    entryComponents: [
        ClientComponent,
        ClientDialogComponent,
        ClientPopupComponent,
        ClientDeleteDialogComponent,
        ClientDeletePopupComponent,
    ],
    providers: [
        ClientService,
        ClientPopupService,
        ClientResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ChatClientModule {}
