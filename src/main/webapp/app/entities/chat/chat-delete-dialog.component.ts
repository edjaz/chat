import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Chat } from './chat.model';
import { ChatPopupService } from './chat-popup.service';
import { ChatService } from './chat.service';

@Component({
    selector: 'jhi-chat-delete-dialog',
    templateUrl: './chat-delete-dialog.component.html'
})
export class ChatDeleteDialogComponent {

    chat: Chat;

    constructor(
        private chatService: ChatService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.chatService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'chatListModification',
                content: 'Deleted an chat'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-chat-delete-popup',
    template: ''
})
export class ChatDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private chatPopupService: ChatPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.chatPopupService
                .open(ChatDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
