import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ExtraInformation } from './extra-information.model';
import { ExtraInformationPopupService } from './extra-information-popup.service';
import { ExtraInformationService } from './extra-information.service';

@Component({
    selector: 'jhi-extra-information-dialog',
    templateUrl: './extra-information-dialog.component.html'
})
export class ExtraInformationDialogComponent implements OnInit {

    extraInformation: ExtraInformation;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private extraInformationService: ExtraInformationService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.extraInformation.id !== undefined) {
            this.subscribeToSaveResponse(
                this.extraInformationService.update(this.extraInformation));
        } else {
            this.subscribeToSaveResponse(
                this.extraInformationService.create(this.extraInformation));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ExtraInformation>>) {
        result.subscribe((res: HttpResponse<ExtraInformation>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: ExtraInformation) {
        this.eventManager.broadcast({ name: 'extraInformationListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-extra-information-popup',
    template: ''
})
export class ExtraInformationPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private extraInformationPopupService: ExtraInformationPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.extraInformationPopupService
                    .open(ExtraInformationDialogComponent as Component, params['id']);
            } else {
                this.extraInformationPopupService
                    .open(ExtraInformationDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
