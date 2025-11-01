package graph.dagsp;

import java.util.*;

public class DAGShortPath {

    private final Map<String, List<Edge>> graph;

    public static class Edge {
        public final String to;
        public final int weight;
        public Edge(String to, int weight) {
            this.to = to;
            this.weight = weight;
        }
    }

    public DAGShortPath(Map<String, List<Edge>> graph) {
        this.graph = graph;
    }

    public Map<String, Integer> shortestPath(String source, List<String> topoOrder) {
        Map<String, Integer> dist = new HashMap<>();
        for (String v : graph.keySet()) dist.put(v, Integer.MAX_VALUE);
        dist.put(source, 0);

        for (String u : topoOrder) {
            if (dist.get(u) == Integer.MAX_VALUE) continue;
            for (Edge e : graph.getOrDefault(u, Collections.emptyList())) {
                if (dist.get(e.to) > dist.get(u) + e.weight) {
                    dist.put(e.to, dist.get(u) + e.weight);
                }
            }
        }
        return dist;
    }

    public Map<String, Integer> longPath(String source, List<String> topoOrder) {
        Map<String, Integer> dist = new HashMap<>();
        for (String v : graph.keySet()) dist.put(v, Integer.MIN_VALUE);
        dist.put(source, 0);

        for (String u : topoOrder) {
            if (dist.get(u) == Integer.MIN_VALUE) continue;
            for (Edge e : graph.getOrDefault(u, Collections.emptyList())) {
                if (dist.get(e.to) < dist.get(u) + e.weight) {
                    dist.put(e.to, dist.get(u) + e.weight);
                }
            }
        }
        return dist;
    }
}

