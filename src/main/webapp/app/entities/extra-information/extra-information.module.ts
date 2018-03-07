import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ChatSharedModule } from '../../shared';
import {
    ExtraInformationService,
    ExtraInformationPopupService,
    ExtraInformationComponent,
    ExtraInformationDetailComponent,
    ExtraInformationDialogComponent,
    ExtraInformationPopupComponent,
    ExtraInformationDeletePopupComponent,
    ExtraInformationDeleteDialogComponent,
    extraInformationRoute,
    extraInformationPopupRoute,
    ExtraInformationResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...extraInformationRoute,
    ...extraInformationPopupRoute,
];

@NgModule({
    imports: [
        ChatSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        ExtraInformationComponent,
        ExtraInformationDetailComponent,
        ExtraInformationDialogComponent,
        ExtraInformationDeleteDialogComponent,
        ExtraInformationPopupComponent,
        ExtraInformationDeletePopupComponent,
    ],
    entryComponents: [
        ExtraInformationComponent,
        ExtraInformationDialogComponent,
        ExtraInformationPopupComponent,
        ExtraInformationDeleteDialogComponent,
        ExtraInformationDeletePopupComponent,
    ],
    providers: [
        ExtraInformationService,
        ExtraInformationPopupService,
        ExtraInformationResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ChatExtraInformationModule {}
