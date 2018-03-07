import { BaseEntity } from './../../shared';

export const enum MessageStatus {
    'IN_PROGRESS',
    'DONE',
    'REMOVED'
}

export class Message implements BaseEntity {
    constructor(
        public id?: number,
        public text?: string,
        public status?: MessageStatus,
        public created?: any,
        public updated?: any,
        public sent?: any,
        public chatId?: number,
        public writeByClientId?: number,
        public writeByConseillerId?: number,
    ) {
    }
}
