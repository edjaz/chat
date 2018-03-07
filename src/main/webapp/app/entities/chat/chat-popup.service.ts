import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { DatePipe } from '@angular/common';
import { Chat } from './chat.model';
import { ChatService } from './chat.service';

@Injectable()
export class ChatPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private chatService: ChatService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.chatService.find(id)
                    .subscribe((chatResponse: HttpResponse<Chat>) => {
                        const chat: Chat = chatResponse.body;
                        chat.created = this.datePipe
                            .transform(chat.created, 'yyyy-MM-ddTHH:mm:ss');
                        chat.opened = this.datePipe
                            .transform(chat.opened, 'yyyy-MM-ddTHH:mm:ss');
                        chat.closed = this.datePipe
                            .transform(chat.closed, 'yyyy-MM-ddTHH:mm:ss');
                        this.ngbModalRef = this.chatModalRef(component, chat);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.chatModalRef(component, new Chat());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    chatModalRef(component: Component, chat: Chat): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.chat = chat;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
