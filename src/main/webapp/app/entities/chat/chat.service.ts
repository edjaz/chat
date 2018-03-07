import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Chat } from './chat.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Chat>;

@Injectable()
export class ChatService {

    private resourceUrl =  SERVER_API_URL + 'api/chats';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/chats';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    create(chat: Chat): Observable<EntityResponseType> {
        const copy = this.convert(chat);
        return this.http.post<Chat>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(chat: Chat): Observable<EntityResponseType> {
        const copy = this.convert(chat);
        return this.http.put<Chat>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Chat>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Chat[]>> {
        const options = createRequestOption(req);
        return this.http.get<Chat[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Chat[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<Chat[]>> {
        const options = createRequestOption(req);
        return this.http.get<Chat[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Chat[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Chat = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Chat[]>): HttpResponse<Chat[]> {
        const jsonResponse: Chat[] = res.body;
        const body: Chat[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Chat.
     */
    private convertItemFromServer(chat: Chat): Chat {
        const copy: Chat = Object.assign({}, chat);
        copy.created = this.dateUtils
            .convertDateTimeFromServer(chat.created);
        copy.opened = this.dateUtils
            .convertDateTimeFromServer(chat.opened);
        copy.closed = this.dateUtils
            .convertDateTimeFromServer(chat.closed);
        return copy;
    }

    /**
     * Convert a Chat to a JSON which can be sent to the server.
     */
    private convert(chat: Chat): Chat {
        const copy: Chat = Object.assign({}, chat);

        copy.created = this.dateUtils.toDate(chat.created);

        copy.opened = this.dateUtils.toDate(chat.opened);

        copy.closed = this.dateUtils.toDate(chat.closed);
        return copy;
    }
}
