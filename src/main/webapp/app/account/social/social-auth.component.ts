import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CookieService } from 'ngx-cookie';

import { LoginService } from 'app/core';

@Component({
    selector: 'jhi-auth',
    template: ''
})
export class SocialAuthComponent implements OnInit {
    constructor(private loginService: LoginService, private cookieService: CookieService, private router: Router) {}

    ngOnInit() {
        const token = this.cookieService.get('social-authentication');
        if (token.length) {
            this.loginService.loginWithToken(token, true).then(
                () => {
                    this.cookieService.remove('social-authentication');
                    this.router.navigate(['']);
                },
                () => {
                    this.router.navigate(['social-register'], { queryParams: { success: 'false' } });
                }
            );
        }
    }
}
