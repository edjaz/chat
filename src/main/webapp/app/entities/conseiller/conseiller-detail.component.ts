import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Conseiller } from './conseiller.model';
import { ConseillerService } from './conseiller.service';

@Component({
    selector: 'jhi-conseiller-detail',
    templateUrl: './conseiller-detail.component.html'
})
export class ConseillerDetailComponent implements OnInit, OnDestroy {

    conseiller: Conseiller;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private conseillerService: ConseillerService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInConseillers();
    }

    load(id) {
        this.conseillerService.find(id)
            .subscribe((conseillerResponse: HttpResponse<Conseiller>) => {
                this.conseiller = conseillerResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInConseillers() {
        this.eventSubscriber = this.eventManager.subscribe(
            'conseillerListModification',
            (response) => this.load(this.conseiller.id)
        );
    }
}
