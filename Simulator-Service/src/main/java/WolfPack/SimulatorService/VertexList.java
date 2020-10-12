package WolfPack.SimulatorService;

import java.util.ArrayList;

public class VertexList{
    private static ArrayList<Vertex> vertexList = new ArrayList<Vertex>();

    public static ArrayList<Vertex> getVertexList() {
        return vertexList;
    }

    public static void setVertexList(ArrayList<Vertex> vertexList) {
        VertexList.vertexList = vertexList;
    }

    public static void addVertex(Vertex v) {
        vertexList.add(v);
    }

    public static void setWorkerToVertex(Worker w){
        for(Vertex v:vertexList){
            if(w.getLocation().equals(v.getName())){
                v.addWorker(w);;
            }
        }
        
    }

    public static Vertex getVertexByName(String name) throws NoSuchVertex {
        for(Vertex v:vertexList){
           if(v.getName().equals(name))
             return v;
        }
        throw new NoSuchVertex();
    }

    

};