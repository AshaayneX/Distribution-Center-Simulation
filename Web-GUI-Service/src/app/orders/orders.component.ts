import { Component, OnInit } from '@angular/core';
import { orders } from '../model/orders_model';
import { HttpClientService } from '../service/http-client.service';
import { items } from '../model/items_model';

@Component({
  selector: 'app-orders',
  templateUrl: './orders.component.html',
  styleUrls: ['./orders.component.css']
})
export class OrdersComponent implements OnInit {
  title = 'Web-GUI-Service';
  orders: orders[];
  interval: any;

  constructor(private rs : HttpClientService) {}

  ngOnInit() {
    this.currentOrderGenerated();
    this.interval = setInterval(() => {
      this.currentOrderGenerated(); 
      }, 5000);
    
  }
  
  currentOrderGenerated(){
    this.rs.getOrders().subscribe
    (
      (response)=>
      {
        this.orders = response;
      },

      (error)=>
      {
        console.log("Error Occured :" +error);
      }

    )
  }

  refresh(): void {
    window.location.reload();
  }

  displayItemsInOrder(items:items[]){
    // debugger;
    if(items && items.length> 0){
      return items.map(i=>i.id).join(', ');
    }else{
      return 'Empty Order';
    }
  }

  ngOnDestroy() {
    if (this.interval) {
      clearInterval(this.interval);
    }
 }
}