package graph;

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

        try (FileWriter w = new FileWriter("metrics.csv")) {
            w.write("Dataset,Algorithm,Time(ms),DFS,Edges,QueueOps,Relaxations\n");
        } catch (Exception e) {
            System.err.println("Failed to initialize metrics.csv: " + e.getMessage());
        }

        String[] datasets = {
                "small_1.json", "small_2.json", "small_3.json",
                "medium_1.json", "medium_2.json", "medium_3.json",
                "large_1.json"
        };

        for (String file : datasets) {
            String path = "src/main/resources/" + file;

            var weightedGraph = GraphLoader.loadWeightedGraph(path);
            var unweightedGraph = GraphLoader.loadUnweightedGraph(path);
            String source = GraphLoader.getSource(path);

            TarjanSCC scc = new TarjanSCC(unweightedGraph);
            scc.findSCCs();
            writeMetricsCSV(file, "TarjanSCC", scc.getMetrics());

            var components = scc.findSCCs();
            var condensation = TarjanSCC.toCondensationGraph(components, unweightedGraph);
            TopologicalSort.kahnSort(condensation);
            writeMetricsCSV(file, "KahnTopo", TopologicalSort.getMetrics());

            DAGShortestPath dag = new DAGShortestPath(weightedGraph);
            var simple = toSimple(weightedGraph);
            var topoForSP = TopologicalSort.dfsSort(simple);

            dag.shortestPath(source, topoForSP);
            writeMetricsCSV(file, "DAGSP_Shortest", DAGShortestPath.getLastMetrics());

            dag.longestPath(source, topoForSP);
            writeMetricsCSV(file, "DAGSP_Longest", DAGShortestPath.getLastMetrics());
        }

        System.out.println("✅ All datasets processed. Results written to metrics.csv");
    }

    private static void writeMetricsCSV(String dataset, String algo, Metrics m) {
        try (FileWriter w = new FileWriter("metrics.csv", true)) {
            w.write(String.format("%s,%s,%.3f,%d,%d,%d,%d\n",
                    dataset, algo, m.getTimeMs(),
                    m.getDFSVisits(), m.getEdgeChecks(),
                    m.getQueueOps(), m.getRelaxations()));
        } catch (Exception e) {
            System.err.println("Error writing metrics: " + e.getMessage());
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
