import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Conseiller } from './conseiller.model';
import { ConseillerPopupService } from './conseiller-popup.service';
import { ConseillerService } from './conseiller.service';
import { User, UserService } from '../../shared';

@Component({
    selector: 'jhi-conseiller-dialog',
    templateUrl: './conseiller-dialog.component.html'
})
export class ConseillerDialogComponent implements OnInit {

    conseiller: Conseiller;
    isSaving: boolean;

    users: User[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private conseillerService: ConseillerService,
        private userService: UserService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.userService.query()
            .subscribe((res: HttpResponse<User[]>) => { this.users = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.conseiller.id !== undefined) {
            this.subscribeToSaveResponse(
                this.conseillerService.update(this.conseiller));
        } else {
            this.subscribeToSaveResponse(
                this.conseillerService.create(this.conseiller));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Conseiller>>) {
        result.subscribe((res: HttpResponse<Conseiller>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Conseiller) {
        this.eventManager.broadcast({ name: 'conseillerListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackUserById(index: number, item: User) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-conseiller-popup',
    template: ''
})
export class ConseillerPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private conseillerPopupService: ConseillerPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.conseillerPopupService
                    .open(ConseillerDialogComponent as Component, params['id']);
            } else {
                this.conseillerPopupService
                    .open(ConseillerDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
