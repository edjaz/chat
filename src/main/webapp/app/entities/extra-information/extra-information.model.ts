import { BaseEntity } from './../../shared';

export class ExtraInformation implements BaseEntity {
    constructor(
        public id?: number,
        public extras?: string,
    ) {
    }
}
