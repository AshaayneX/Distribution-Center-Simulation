import { Component, OnInit } from '@angular/core';
import { HttpClientService } from '../service/http-client.service';
import { helper_model } from '../model/helper_model';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  selectedFile: any;
  step: helper_model;
  interval: any;


  constructor(private rs : HttpClientService) {}
  
  ngOnInit() {

    this.currentstep(); 
    this.interval = setInterval(() => {
      this.currentstep(); 
      }, 5000);
  }

  config(){
    this.rs.setConfig().subscribe(

      data  => {
      
      console.log("PUT Request is successful ", data);
      
      },
      
      error  => {
      
      console.log("Rrror", error);
      }

    )
  }
  startManagement(){
    this.rs.setStartManagement().subscribe(

      data  => {
      
      console.log("Post start Request is successful ", data);
      
      },
      
      error  => {
      
      console.log("Rrror", error);
      }

    )
  }

  resetServices(){
    this.rs.setResetServices().subscribe(

      data  => {
      
      console.log("Post start Request is successful ", data);
      
      },
      
      error  => {
      
      console.log("Rrror", error);
      }

    )
  }

  currentstep(){
  this.rs.getStep().subscribe
  (
    (response)=>
    {
      this.step = response;
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