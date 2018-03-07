import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Chat } from './chat.model';
import { ChatService } from './chat.service';

@Component({
    selector: 'jhi-chat-detail',
    templateUrl: './chat-detail.component.html'
})
export class ChatDetailComponent implements OnInit, OnDestroy {

    chat: Chat;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private chatService: ChatService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInChats();
    }

    load(id) {
        this.chatService.find(id)
            .subscribe((chatResponse: HttpResponse<Chat>) => {
                this.chat = chatResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInChats() {
        this.eventSubscriber = this.eventManager.subscribe(
            'chatListModification',
            (response) => this.load(this.chat.id)
        );
    }
}
