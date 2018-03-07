import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Conseiller } from './conseiller.model';
import { ConseillerPopupService } from './conseiller-popup.service';
import { ConseillerService } from './conseiller.service';

@Component({
    selector: 'jhi-conseiller-delete-dialog',
    templateUrl: './conseiller-delete-dialog.component.html'
})
export class ConseillerDeleteDialogComponent {

    conseiller: Conseiller;

    constructor(
        private conseillerService: ConseillerService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.conseillerService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'conseillerListModification',
                content: 'Deleted an conseiller'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-conseiller-delete-popup',
    template: ''
})
export class ConseillerDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private conseillerPopupService: ConseillerPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.conseillerPopupService
                .open(ConseillerDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
