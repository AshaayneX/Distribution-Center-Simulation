export class workers
{
    name : string;
    location : string;
    capacity : string;
    actions : NextAction[];
    nextAction : NextAction;
    weight : string;
    notificationUri : string;
    holdingItems : string;

    constructor(name, location, actions, capacity, nextAction, weight, notificationUri, holdingItems)

    {
        this.name = name;
        this.location = location;
        this.capacity = capacity;
        this.actions = actions;
        this.nextAction = nextAction;
        this.weight = weight;
        this.notificationUri = notificationUri;
        this.holdingItems = holdingItems;
    }
}

export class NextAction {
    type: string;
    arguments: string;
    success: boolean;
    step: number;
}