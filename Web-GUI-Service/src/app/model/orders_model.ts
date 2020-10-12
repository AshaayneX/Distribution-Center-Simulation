import { items } from './items_model';

export class orders
{
    orderId : string;
    status : string;
    itemsInOrder : items[];

    constructor(orderId, status, itemsInOrder)

    {
        this.orderId = orderId;
        this.status = status;
        this.itemsInOrder = itemsInOrder;
    }
}