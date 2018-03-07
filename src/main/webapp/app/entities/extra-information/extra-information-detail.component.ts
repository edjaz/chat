import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { ExtraInformation } from './extra-information.model';
import { ExtraInformationService } from './extra-information.service';

@Component({
    selector: 'jhi-extra-information-detail',
    templateUrl: './extra-information-detail.component.html'
})
export class ExtraInformationDetailComponent implements OnInit, OnDestroy {

    extraInformation: ExtraInformation;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private extraInformationService: ExtraInformationService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInExtraInformations();
    }

    load(id) {
        this.extraInformationService.find(id)
            .subscribe((extraInformationResponse: HttpResponse<ExtraInformation>) => {
                this.extraInformation = extraInformationResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInExtraInformations() {
        this.eventSubscriber = this.eventManager.subscribe(
            'extraInformationListModification',
            (response) => this.load(this.extraInformation.id)
        );
    }
}
