import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { ExtraInformation } from './extra-information.model';
import { ExtraInformationService } from './extra-information.service';

@Injectable()
export class ExtraInformationPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private extraInformationService: ExtraInformationService

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
                this.extraInformationService.find(id)
                    .subscribe((extraInformationResponse: HttpResponse<ExtraInformation>) => {
                        const extraInformation: ExtraInformation = extraInformationResponse.body;
                        this.ngbModalRef = this.extraInformationModalRef(component, extraInformation);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.extraInformationModalRef(component, new ExtraInformation());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    extraInformationModalRef(component: Component, extraInformation: ExtraInformation): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.extraInformation = extraInformation;
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
