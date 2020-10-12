import { Component, OnInit } from '@angular/core';
import { items } from '../model/items_model';
import { HttpClientService } from '../service/http-client.service';

@Component({
  selector: 'app-items',
  templateUrl: './items.component.html',
  styleUrls: ['./items.component.css']
})
export class ItemsComponent implements OnInit {
  title = 'Web-GUI-Service';
  items: items[];
  interval: any;

  constructor(private rs : HttpClientService) {}

  columns = ["ID","Name", "Suppliers","Weight","Location"];

  index = ["id", "name", "supplier","weight","location"];
    
  ngOnInit() {
  
  this.currentItems();
  this.interval = setInterval(() => {
    this.currentItems();
    }, 5000);
    
  }

  currentItems(){
    this.rs.getItems().subscribe
    (
      (response)=>
      {
        this.items = response;
      },

      (error)=>
      {
        console.log("Error Occured :" +error);
      }

    )
  }
  
  ngOnDestroy() {
    if (this.interval) {
      clearInterval(this.interval);
    }
 }
}