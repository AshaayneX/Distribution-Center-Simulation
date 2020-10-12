package WolfPack.SimulatorService;

import java.util.ArrayList;

public class WorkerList {
private static ArrayList<Worker> workerList;

    public static ArrayList<Worker> getWorkerList() {
        return workerList;
    }

    public static void setWorkerList(ArrayList<Worker> workerList) {
        WorkerList.workerList = workerList;
    }
    
    public static Worker getWorkerByName(String name) throws NoSuchWorker {
        for(Worker w:workerList){
           if(w.getName().equals(name))
            return w;       
        }
        throw new NoSuchWorker();
    }

    public static String[] getWorkerNames() {
        String[] name = new String[workerList.size()];
        
        for(int i =0; i<workerList.size();i++){
           name[i]= workerList.get(i).getName();    
        }
        return name; 
        
    }

    public static void process() throws NoSuchItem, NoSuchVertex {
        for(Worker w:workerList){
           w.work();      
        }   
    }
}