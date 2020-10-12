package WolfPack.SimulatorService;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import java.awt.Color;

import com.mxgraph.layout.mxCompactTreeLayout;
import com.mxgraph.layout.mxEdgeLabelLayout;
import com.mxgraph.layout.mxGraphLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.layout.mxStackLayout;
import com.mxgraph.layout.orthogonal.mxOrthogonalLayout;
import com.mxgraph.shape.mxDefaultTextShape;
import com.mxgraph.util.*;
import java.awt.image.*;
import org.jgrapht.ext.JGraphXAdapter;

public class Map {

    private static String aisles;
    private static String sections;
    private static String shelves;
    private static String packagingAreas[];
    private static ArrayList<Vertex> detailedVerticeList;

    static Graph<String, DefaultEdge> floorPlan = new SimpleGraph<>(DefaultEdge.class);

    public static String getAisles() {
        return aisles;
    }

    public static void setAisles(String aisles) {
        Map.aisles = aisles;
    }

    public static String getSections() {
        return sections;
    }

    public static void setSections(String sections) {
        Map.sections = sections;
    }

    public static String getShelves() {
        return shelves;
    }

    public static void setShelves(String shelves) {
        Map.shelves = shelves;
    }

    public static String[] getPackagingAreas() {
        return packagingAreas;
    }

    public static void setPackagingAreas(String[] packagingAreas) {
        Map.packagingAreas = packagingAreas;
    }

    public static ArrayList<Vertex> getDetailedVerticeList() {
        return detailedVerticeList;
    }

    public static void setDetailedVerticeList(ArrayList<Vertex> detailedVerticeList) {
        Map.detailedVerticeList = detailedVerticeList;
    }

    public static void createMap() throws IOException {

        int a = Integer.parseInt(aisles);
        int s = Integer.parseInt(sections);
        int sh = Integer.parseInt(shelves);

        Graph<String, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
        String lastAddedVertex = "null";
        char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        // creating aisles with connected sections
        for (int i = 1; i <= a; i++) {

            for (int j = 0; j <= s + 1; j++) {
                String currentVertexName = "a" + String.valueOf(i) + "." + String.valueOf(j);
                if (j == 0) {
                    // adding initial node
                    VertexList.addVertex(new Vertex(currentVertexName));
                    graph.addVertex(currentVertexName);
                    lastAddedVertex = currentVertexName;
                } else {
                    // adding a normal section
                    VertexList.addVertex(new Vertex(currentVertexName));
                    graph.addVertex(currentVertexName);
                    graph.addEdge(lastAddedVertex, currentVertexName);

                    if (j != s + 1)// not final node
                    {
                        for (int k = 0; k < sh; k++) {
                            String shelfName = currentVertexName + "/" + Character.toString(alphabet[k]);
                            VertexList.getVertexByName(currentVertexName).addShelf(shelfName);
                        }
                    }
                    lastAddedVertex = currentVertexName;

                }
            }

        }
        // connecting first and last nodes across aisles
        if (a > 1) {
            String initialAisleStart = "a1.0";
            String initialAisleEnd = "null", nextAisleStart, nextAisleEnd;
            for (int i = 1; i <= a; i++) {
                if (i == 1) {
                    initialAisleEnd = "a1." + String.valueOf(s + 1);
                } else {
                    nextAisleStart = "a" + String.valueOf(i) + "." + "0";
                    nextAisleEnd = "a" + String.valueOf(i) + "." + String.valueOf(s + 1);
                    graph.addEdge(initialAisleStart, nextAisleStart);
                    graph.addEdge(initialAisleEnd, nextAisleEnd);
                    initialAisleStart = nextAisleStart;
                    initialAisleEnd = nextAisleEnd;
                }
            }
        }

        // allocate packaging points
        for (String areaName : packagingAreas) {
            Vertex v = VertexList.getVertexByName(areaName);
            v.setPackagingPoint(true);
        }

        JGraphXAdapter<String, DefaultEdge> graphAdapter = new JGraphXAdapter<String, DefaultEdge>(graph);
        mxCompactTreeLayout temp = new mxCompactTreeLayout(graphAdapter);
        temp.setNodeDistance(75);
        temp.setLevelDistance(5);
        mxIGraphLayout layout = temp;
        layout.execute(graphAdapter.getDefaultParent());

        BufferedImage image = mxCellRenderer.createBufferedImage(graphAdapter, null, 2, Color.WHITE, true, null);
        File imgFile = new File("../Web-GUI-Service/src/assets/map_image/graph.png");
        imgFile.createNewFile();
        ImageIO.write(image, "PNG", imgFile);
        Map.updateMap();

        floorPlan = graph;
    }

    public static void updateMap() {

        if (WorkerList.getWorkerList() != null) {
            // allocate workers to locations
            for (Worker w : WorkerList.getWorkerList()) {
                VertexList.setWorkerToVertex(w);
            }
        }
    }

};