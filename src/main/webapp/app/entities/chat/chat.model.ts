import { BaseEntity } from './../../shared';

export const enum ChatStatus {
    'WAITTING',
    'OPENED',
    'CLOSED'
}

export class Chat implements BaseEntity {
    constructor(
        public id?: number,
        public status?: ChatStatus,
        public created?: any,
        public opened?: any,
        public closed?: any,
        public clientId?: number,
        public conseillers?: BaseEntity[],
    ) {
    }
}
