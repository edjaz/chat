import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Conseiller } from './conseiller.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Conseiller>;

@Injectable()
export class ConseillerService {

    private resourceUrl =  SERVER_API_URL + 'api/conseillers';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/conseillers';

    constructor(private http: HttpClient) { }

    create(conseiller: Conseiller): Observable<EntityResponseType> {
        const copy = this.convert(conseiller);
        return this.http.post<Conseiller>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(conseiller: Conseiller): Observable<EntityResponseType> {
        const copy = this.convert(conseiller);
        return this.http.put<Conseiller>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Conseiller>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Conseiller[]>> {
        const options = createRequestOption(req);
        return this.http.get<Conseiller[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Conseiller[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<Conseiller[]>> {
        const options = createRequestOption(req);
        return this.http.get<Conseiller[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Conseiller[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Conseiller = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Conseiller[]>): HttpResponse<Conseiller[]> {
        const jsonResponse: Conseiller[] = res.body;
        const body: Conseiller[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Conseiller.
     */
    private convertItemFromServer(conseiller: Conseiller): Conseiller {
        const copy: Conseiller = Object.assign({}, conseiller);
        return copy;
    }

    /**
     * Convert a Conseiller to a JSON which can be sent to the server.
     */
    private convert(conseiller: Conseiller): Conseiller {
        const copy: Conseiller = Object.assign({}, conseiller);
        return copy;
    }
}
