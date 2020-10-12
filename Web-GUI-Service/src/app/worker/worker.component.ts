import { Component, OnInit } from '@angular/core';
import { workers, NextAction } from '../model/workers_model';
import { HttpClientService } from '../service/http-client.service';
import { items } from '../model/items_model';

@Component({
  selector: 'app-worker',
  templateUrl: './worker.component.html',
  styleUrls: ['./worker.component.css']
})
export class WorkerComponent implements OnInit {
  title = 'Web-GUI-Service';
  interval: any;
  workers: workers[];

  constructor(private rs: HttpClientService) { }

  columns = ["Name", "Location", "Capacity", "Actions", "Next Action", "Weight", "Notification Uri", "Holding Items"];

  index = ["name", "location", "capacity", "actions", "nextAction", "weight", "notificationUri", "holdingItems"];

  ngOnInit() {

    this.CurrentWorkers();
    this.interval = setInterval(() => {
      this.CurrentWorkers();
    }, 5000);
  }

  CurrentWorkers() {
    this.rs.getWorkers().subscribe
      (
        (response) => {
          this.workers = response;
        },

        (error) => {
          console.log("Error Occured :" + error);
        }

      )
  }

  displayHoldingItemsInWorker(items: items[]) {
    // debugger;
    if (items && items.length > 0) {
      return items.map(i => i).join(', ');
    } else {
      return 'Empty Holding Items';
    }
  }

  displayAction(actions: NextAction[]) {
    let result: string = '';
    actions.forEach(action => {
      result += `<li> step : ${action.step} <br/> type: ${action.type} <br/> arguments: ${action.arguments} <br/> success:s ${action.success} </li>  <br/>`
    });
    return '<ul>' + result + '</ul>';
  }


  displayNextAction(action: NextAction) {
    let result: string = `<li> step : ${action.step} <br/> type: ${action.type} <br/> arguments: ${action.arguments} <br/> success:s ${action.success} </li>`;

    return '<ul>' + result + '</ul>';
  }

  ngOnDestroy() {
    if (this.interval) {
      clearInterval(this.interval);
    }
  }
}