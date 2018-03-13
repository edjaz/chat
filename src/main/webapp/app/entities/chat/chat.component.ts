import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiAlertService, JhiEventManager, JhiParseLinks } from 'ng-jhipster';

import { Chat } from './chat.model';
import { ChatService } from './chat.service';
import { ITEMS_PER_PAGE } from '../../shared';
import { Principal } from 'app/core';
import { LocalStorageService, SessionStorageService } from 'ngx-webstorage';

declare var EventSourcePolyfill: any;

@Component({
    selector: 'jhi-chat',
    templateUrl: './chat.component.html'
})
export class ChatComponent implements OnInit, OnDestroy {
    currentAccount: any;
    chats: Chat[];
    error: any;
    success: any;
    eventSubscriber: Subscription;
    currentSearch: string;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;

    constructor(
        private localStorage: LocalStorageService,
        private sessionStorage: SessionStorageService,
        private chatService: ChatService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager,
        private http: HttpClient
    ) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe((data) => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
        });
        this.currentSearch =
            this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search']
                ? this.activatedRoute.snapshot.params['search']
                : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.chatService
                .search({
                    page: this.page - 1,
                    query: this.currentSearch,
                    size: this.itemsPerPage,
                    sort: this.sort()
                })
                .subscribe(
                    (res: HttpResponse<Chat[]>) => this.onSuccess(res.body, res.headers),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        }
        this.chatService
            .query({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort()
            })
            .subscribe(
                (res: HttpResponse<Chat[]>) => this.onSuccess(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    transition() {
        this.router.navigate(['/chat'], {
            queryParams: {
                page: this.page,
                size: this.itemsPerPage,
                search: this.currentSearch,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.loadAll();
    }

    clear() {
        this.page = 0;
        this.currentSearch = '';
        this.router.navigate([
            '/chat',
            {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        this.loadAll();
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.page = 0;
        this.currentSearch = query;
        this.router.navigate([
            '/chat',
            {
                search: this.currentSearch,
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInChats();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Chat) {
        return item.id;
    }

    registerChangeInChats() {
        this.eventSubscriber = this.eventManager.subscribe('chatListModification', (response) => this.loadAll());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private onSuccess(data, headers) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        this.queryCount = this.totalItems;
        // this.page = pagingParams.page;
        this.chats = data;
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }

    subscribe() {
        var headers = {};

        const token = this.localStorage.retrieve('authenticationToken') || this.sessionStorage.retrieve('authenticationToken');
        if (!!token) {
            headers = {
                headers: {
                    Authorization: 'Bearer ' + token
                }
            };
        }

        var es = new EventSourcePolyfill('/api/chats/conseiller/subscribe', headers);
        es.addEventListener('open', (e) => {
            console.log(e);
        });
        es.addEventListener('message', (e) => {
            console.log(e);
        });
        es.addEventListener('error', (e) => {
            console.log(e);
        });

        /*
        const req = new HttpRequest('GET', '/api/chats/conseiller/subscribe', {
            reportProgress: true
        });

        this.http
            .request(req)
            .pipe(
                map((event) => {
                    console.log(event);
                }),
                tap((message) => {
                    console.log(message);
                }),
                last(), // return last (completed) message to caller
                catchError((err, caught) => {
                    return caught;
                })
            )
            .subscribe((next) => {
                console.log(next);
            });
*/

        /*
        this.http.get('/api/chats/conseiller/subscribe').subscribe(
            value => {
                console.log(value)
            }
        );
*/
    }
}
