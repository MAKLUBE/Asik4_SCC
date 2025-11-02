package graph;

import com.google.gson.JsonObject;
import graph.scc.TarjanSCC;
import graph.topo.TopologicalSort;
import graph.dagsp.DAGShortestPath;
import graph.dagsp.DAGShortestPath.Edge;
import util.GraphLoader;
import util.Metrics;

import java.io.FileWriter;
import java.util.*;

public class Main {
    public static void main(String[] args) {

        String path = "src/main/resources/graphs.json";
        List<JsonObject> allGraphs = GraphLoader.loadAllGraphs(path);

        try (FileWriter w = new FileWriter("metrics.csv")) {
            w.write("");
        } catch (Exception e) {
            System.err.println("Error initializing metrics.csv: " + e.getMessage());
        }

        for (JsonObject dataset : allGraphs) {
            String name = dataset.get("name").getAsString();
            String source = GraphLoader.getSource(dataset);
            var weightedGraph = GraphLoader.parseWeighted(dataset);
            var unweightedGraph = GraphLoader.parseUnweighted(dataset);

            try (FileWriter w = new FileWriter("metrics.csv", true)) {
                w.write(String.format("=== %s ===\n", name));
                w.write("Algorithm,Time(ms),DFS,Edges,QueueOps,Relaxations\n");
            } catch (Exception e) {
                System.err.println("Error writing header for dataset " + name + ": " + e.getMessage());
            }

            TarjanSCC scc = new TarjanSCC(unweightedGraph);
            var components = scc.findSCCs();
            writeMetrics(name, "TarjanSCC", scc.getMetrics());

            var condensation = TarjanSCC.toCondensationGraph(components, unweightedGraph);
            TopologicalSort.kahnSort(condensation);
            writeMetrics(name, "KahnTopo", TopologicalSort.getMetrics());

            DAGShortestPath dag = new DAGShortestPath(weightedGraph);
            var simple = toSimple(weightedGraph);
            var topoForSP = TopologicalSort.dfsSort(simple);

            dag.shortestPath(source, topoForSP);
            writeMetrics(name, "DAGSP_Shortest", DAGShortestPath.getLastMetrics());

            dag.longestPath(source, topoForSP);
            writeMetrics(name, "DAGSP_Longest", DAGShortestPath.getLastMetrics());

            try (FileWriter w = new FileWriter("metrics.csv", true)) {
                w.write("\n");
            } catch (Exception ignored) {}
        }

        System.out.println("✅ All graphs processed. Metrics written to metrics.csv");
    }

    private static void writeMetrics(String dataset, String algo, Metrics m) {
        try (FileWriter w = new FileWriter("metrics.csv", true)) {
            w.write(String.format("%s,%.3f,%d,%d,%d,%d\n",
                    algo,
                    m.getTimeMs(),
                    m.getDFSVisits(),
                    m.getEdgeChecks(),
                    m.getQueueOps(),
                    m.getRelaxations()));
        } catch (Exception e) {
            System.err.println("Error writing metrics for " + dataset + ": " + e.getMessage());
        }
    }

    private static Map<String, List<String>> toSimple(Map<String, List<Edge>> g) {
        Map<String, List<String>> simple = new HashMap<>();
        for (var e : g.entrySet()) {
            List<String> adj = new ArrayList<>();
            for (Edge edge : e.getValue()) adj.add(edge.to);
            simple.put(e.getKey(), adj);
        }
        return simple;
    }
}
