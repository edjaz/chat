import { BaseEntity } from './../../shared';

export class Conseiller implements BaseEntity {
    constructor(
        public id?: number,
        public userId?: number,
    ) {
    }
}
