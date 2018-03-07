import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { NgbDateAdapter } from '@ng-bootstrap/ng-bootstrap';
import { DateFormatPipe, ParsePipe } from 'angular2-moment';

import { NgbDateMomentAdapter } from './util/datepicker-adapter';
import { ChatSharedLibsModule, ChatSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective, JhiSocialComponent } from './';

@NgModule({
    imports: [ChatSharedLibsModule, ChatSharedCommonModule],
    declarations: [JhiSocialComponent, JhiLoginModalComponent, HasAnyAuthorityDirective],
    providers: [ParsePipe, DateFormatPipe, { provide: NgbDateAdapter, useClass: NgbDateMomentAdapter }],
    entryComponents: [JhiLoginModalComponent],
    exports: [ChatSharedCommonModule, JhiSocialComponent, JhiLoginModalComponent, HasAnyAuthorityDirective],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ChatSharedModule {}
