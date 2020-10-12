export class items
{
    id : string;
    name : string;
    supplier : string;
    weight : string;
    location: string;

    constructor(id,name, supplier, weight,location)

    {
        this.id= id;
        this.name = name;
        this.supplier = supplier;
        this.weight = weight;
        this.location = location;
    }
}