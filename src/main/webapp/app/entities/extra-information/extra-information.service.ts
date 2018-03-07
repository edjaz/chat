import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { ExtraInformation } from './extra-information.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<ExtraInformation>;

@Injectable()
export class ExtraInformationService {

    private resourceUrl =  SERVER_API_URL + 'api/extra-informations';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/extra-informations';

    constructor(private http: HttpClient) { }

    create(extraInformation: ExtraInformation): Observable<EntityResponseType> {
        const copy = this.convert(extraInformation);
        return this.http.post<ExtraInformation>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(extraInformation: ExtraInformation): Observable<EntityResponseType> {
        const copy = this.convert(extraInformation);
        return this.http.put<ExtraInformation>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ExtraInformation>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<ExtraInformation[]>> {
        const options = createRequestOption(req);
        return this.http.get<ExtraInformation[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<ExtraInformation[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<ExtraInformation[]>> {
        const options = createRequestOption(req);
        return this.http.get<ExtraInformation[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<ExtraInformation[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: ExtraInformation = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<ExtraInformation[]>): HttpResponse<ExtraInformation[]> {
        const jsonResponse: ExtraInformation[] = res.body;
        const body: ExtraInformation[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to ExtraInformation.
     */
    private convertItemFromServer(extraInformation: ExtraInformation): ExtraInformation {
        const copy: ExtraInformation = Object.assign({}, extraInformation);
        return copy;
    }

    /**
     * Convert a ExtraInformation to a JSON which can be sent to the server.
     */
    private convert(extraInformation: ExtraInformation): ExtraInformation {
        const copy: ExtraInformation = Object.assign({}, extraInformation);
        return copy;
    }
}
