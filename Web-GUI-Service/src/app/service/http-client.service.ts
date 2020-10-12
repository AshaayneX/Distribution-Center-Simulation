import { Injectable } from '@angular/core';

import { HttpClient } from '@angular/common/http';
import { items } from '../model/items_model';
import { workers } from '../model/workers_model';
import { orders } from '../model/orders_model';
import { helper_model } from '../model/helper_model';

@Injectable({
  providedIn: 'root'
})
export class HttpClientService {

  constructor(private http: HttpClient) { }

  //simulator
  items_url: string = "http://localhost:8082/items";
  worker_url: string = "http://localhost:8082/workerlist";
  config_url: string = "http://localhost:8082/configuration";
  step_url: string = "http://localhost:8082/step";

  //OrderGen
  order_url: string = "http://localhost:8083/orders";

  //Management service
  startManagement_url: string = "http://localhost:8086/start";
  resetServices_url: string = "http://localhost:8086/reset";

  getItems() {
    return this.http.get<items[]>(this.items_url);

  }

  getWorkers() {
    return this.http.get<workers[]>(this.worker_url);

  }

  getOrders() {
    return this.http.get<orders[]>(this.order_url);

  }

  getStep() {
    return this.http.get<helper_model>(this.step_url);
  }

  setConfig() {
    return this.http.put("http://localhost:8082/configuration",
      {
        "aisles": 3,
        "sections": 2,
        "shelves": 1,
        "packagingAreas": ["a1.3", "a2.3"],
        "workers": [
          {
            "name": "rem0",
            "location": "a1.1",
            "capacity": 20
          }, 
        ],
        "items": [
          {
            "id": "mars",
            "name": "Mars",
            "supplier": "Nestle",
            "weight": 1
          },
          {
            "id": "kitkat",
            "name": "Kit Kat",
            "supplier": "Nestle",
            "weight": 1
          },
          {
            "id": "dd",
            "name": "Double Decker",
            "supplier": "Nestle",
            "weight": 1
          }
        ]
      }
    )
  }
  setStartManagement() {
    return this.http.post(this.startManagement_url, {});
  }

  setResetServices() {
    return this.http.post(this.resetServices_url, {
      "type":"test"
  });
  }
}