import { NgModule, LOCALE_ID } from '@angular/core';
import { DatePipe, registerLocaleData } from '@angular/common';
import { Title } from '@angular/platform-browser';
import locale from '@angular/common/locales/fr';

import {
    JhiTrackerService,
    WindowRef,
    JhiLanguageHelper,
    LoginService,
    LoginModalService,
    SocialService,
    AccountService,
    StateStorageService,
    Principal,
    CSRFService,
    AuthServerProvider,
    UserService,
    UserRouteAccessService
} from './';

@NgModule({
    imports: [],
    exports: [],
    declarations: [],
    providers: [
        JhiTrackerService,
        WindowRef,
        LoginService,
        LoginModalService,
        SocialService,
        Title,
        {
            provide: LOCALE_ID,
            useValue: 'fr'
        },
        JhiLanguageHelper,
        AccountService,
        StateStorageService,
        Principal,
        CSRFService,
        AuthServerProvider,
        UserService,
        DatePipe,
        UserRouteAccessService
    ]
})
export class ChatCoreModule {
    constructor() {
        registerLocaleData(locale);
    }
}
