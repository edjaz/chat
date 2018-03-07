import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ChatSharedModule } from '../../shared';
import {
    ChatService,
    ChatPopupService,
    ChatComponent,
    ChatDetailComponent,
    ChatDialogComponent,
    ChatPopupComponent,
    ChatDeletePopupComponent,
    ChatDeleteDialogComponent,
    chatRoute,
    chatPopupRoute,
    ChatResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...chatRoute,
    ...chatPopupRoute,
];

@NgModule({
    imports: [
        ChatSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        ChatComponent,
        ChatDetailComponent,
        ChatDialogComponent,
        ChatDeleteDialogComponent,
        ChatPopupComponent,
        ChatDeletePopupComponent,
    ],
    entryComponents: [
        ChatComponent,
        ChatDialogComponent,
        ChatPopupComponent,
        ChatDeleteDialogComponent,
        ChatDeletePopupComponent,
    ],
    providers: [
        ChatService,
        ChatPopupService,
        ChatResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ChatChatModule {}
