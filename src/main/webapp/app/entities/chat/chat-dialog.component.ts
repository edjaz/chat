import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Chat } from './chat.model';
import { ChatPopupService } from './chat-popup.service';
import { ChatService } from './chat.service';
import { Client, ClientService } from '../client';
import { Conseiller, ConseillerService } from '../conseiller';

@Component({
    selector: 'jhi-chat-dialog',
    templateUrl: './chat-dialog.component.html'
})
export class ChatDialogComponent implements OnInit {

    chat: Chat;
    isSaving: boolean;

    clients: Client[];

    conseillers: Conseiller[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private chatService: ChatService,
        private clientService: ClientService,
        private conseillerService: ConseillerService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.clientService.query()
            .subscribe((res: HttpResponse<Client[]>) => { this.clients = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.conseillerService.query()
            .subscribe((res: HttpResponse<Conseiller[]>) => { this.conseillers = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.chat.id !== undefined) {
            this.subscribeToSaveResponse(
                this.chatService.update(this.chat));
        } else {
            this.subscribeToSaveResponse(
                this.chatService.create(this.chat));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Chat>>) {
        result.subscribe((res: HttpResponse<Chat>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Chat) {
        this.eventManager.broadcast({ name: 'chatListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackClientById(index: number, item: Client) {
        return item.id;
    }

    trackConseillerById(index: number, item: Conseiller) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}

@Component({
    selector: 'jhi-chat-popup',
    template: ''
})
export class ChatPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private chatPopupService: ChatPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.chatPopupService
                    .open(ChatDialogComponent as Component, params['id']);
            } else {
                this.chatPopupService
                    .open(ChatDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
