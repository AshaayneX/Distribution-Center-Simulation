<h1> Distribution Center Model </h1>

Application that provides a fully-fledged functionality for a worker in a distribution center to perform the daily activities with minimum fatigue and maximum efficiency, This is implemented using complex order processing strategies<br>
<B>Find video demonstrations here - </B><ul><li><a>https://www.youtube.com/watch?v=qH_AD3unf2Q&feature=youtu.be</a></li><li><a>https://drive.google.com/drive/folders/1_zZVnTT5Li3-GNF-mUQecwObL47-hD1L?usp=sharing</a></li></ul>
<h2>Technology Stack</h2>

<table style="width:50%">
  <tr>
    <th>Component</th>
    <th>Technology</th>
  </tr>
  <tr>
    <td>Frontend</td>
    <td>Angular 9</td> 
  </tr>
  <tr>
    <td>Backend</td>
    <td>Rest, Spring boot , AKKA</td> 
  </tr>
  <tr>
    <td>Security</td>
    <td>Token based security</td> 
  </tr>
  <tr>
    <td>Client build </td>
    <td>Angular CLI</td> 
  </tr>
   <tr>
    <td>Server build </td>
    <td>Maven</td> 
  </tr>
</table>

<h2>Prerequisites</h2>

Ensure you have this installed before proceeding further

• Java 11 <br>
• Maven 3.6.3  <br>
• Spring boot 2.3.1 <br>
• Angular 9 <br>

<h2>About Distribution Center Model</h2>

We have implemented a fully-fledged distribution center model which allows the worker in the Distribution Center to carry out the day to day task in the simplest and most efficient manner, although the system seems simple for the user the team has implemented a complex and reliable architecture through microservices with additional functionalities, technologies, and complex order processing strategies to ensure high performance and easy maintenance.
Once any configuration with the item details is sent to the system the simulator generates the  workers, Map, Item List and Location of items, the Order generator service will generate the order, Management Service will obtain the processed orders from the Order Generator and assign the orders as a task to workers, Where worker service will act as the brain of the workers and receives the tasks from management service and display it to the web page where the worker will be able to view with a simple interface, All these activities are time discrete and happen in correlation with the clock service which allows us to implement a high performance and efficient Simulator model for distribution center

<h2>How to Initialize the project</h2>

<h3>1. Clone the Master branch into your local computer</h3>

Clone the https://gitlab.com/comp3006l/2020/wolfpack/dc-model.git into your local folder in your computer 

<h3>2. Locate the project folder and build the following services in the order as mentioned below </h3>

1. Euereka Server  <br>
2. Clock Server  <br>
3. Web GUI  <br>
4. Simulator Service (and load your configuration)<br>
5. Management Service <br>
6. Worker Service  <br>
7. Order Generator Service <br>

<h3>2.1 Execute the following commands in the command prompt or power shell opened in the service folder</h3>

<h4>Example from clock service </h4> 
 E:\Final Year Project - DC Model all fils\Project\dc-model\Clock-Service> mvn package <br>

 E:\Final Year Project - DC Model all fils\Project\dc-model\Clock-Service> mvn clean compile spring-boot:run<br>

 
 
 <h4>For Worker Service which is built in AKKA you may execute the above OR  </h4> 

  E:\Final Year Project - DC Model all fils\Project\dc-model\Worker-Service> mvn clean compile exec:java<br>

  <h4> For WEB GUI Service which is built in Angular CLI use the following </h4> 

  E:\Final Year Project - DC Model all fils\Project\dc-model\Web-GUI-Service> ng serve -o <br>

  <h4>Configuring the system</h4> 

  Perform the following actions in the web interface to start and use the system<br>
  
  1.Config<br>
  2.Reset<br>
  3.Start Management service <br>

  

  Et Voila there you have it! But there are more cool things :) <br>


<h2>Services and its End points</h2>

<table style="width:50%">
  <tr>
    <th>Component</th>
    <th>URL</th>
  </tr>
  <tr>
    <td>Simulator Service</td>
    <td>"http://localhost:8082"</td> 
  </tr>
  <tr>
    <td>Euerka Server </td>
    <td>"http://localhost:8761"</td> 
  </tr>
  <tr>
    <td>Clock Service</td>
    <td>"http://localhost:9000"</td> 
  </tr>
  <tr>
    <td>Management Service </td>
    <td>"http://localhost:8086"</td> 
  </tr>
   <tr>
    <td>Worker Service </td>
    <td>"http://localhost:8090"</td> 
  </tr>
    </tr>
   <tr>
    <td>Order Generator Service </td>
    <td>"http://localhost:8083"</td> 
  </tr>

  <tr>
    <td>WEB GUI Service </td>
    <td>"http://localhost:4200"</td> 
  </tr>

</table>



