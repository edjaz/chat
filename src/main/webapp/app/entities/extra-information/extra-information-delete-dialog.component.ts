import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ExtraInformation } from './extra-information.model';
import { ExtraInformationPopupService } from './extra-information-popup.service';
import { ExtraInformationService } from './extra-information.service';

@Component({
    selector: 'jhi-extra-information-delete-dialog',
    templateUrl: './extra-information-delete-dialog.component.html'
})
export class ExtraInformationDeleteDialogComponent {

    extraInformation: ExtraInformation;

    constructor(
        private extraInformationService: ExtraInformationService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.extraInformationService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'extraInformationListModification',
                content: 'Deleted an extraInformation'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-extra-information-delete-popup',
    template: ''
})
export class ExtraInformationDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private extraInformationPopupService: ExtraInformationPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.extraInformationPopupService
                .open(ExtraInformationDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
