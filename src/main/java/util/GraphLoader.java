package util;

import com.google.gson.*;
import java.io.FileReader;
import java.util.*;
import graph.dagsp.DAGShortPath.Edge;

public class GraphLoader {

    public static Map<String, List<Edge>> loadWeightedGraph(String path) {
        Map<String, List<Edge>> graph = new HashMap<>();
        try (FileReader reader = new FileReader(path)) {
            JsonObject obj = JsonParser.parseReader(reader).getAsJsonObject();
            JsonArray edges = obj.getAsJsonArray("edges");
            for (JsonElement e : edges) {
                JsonObject edge = e.getAsJsonObject();
                String from = edge.get("from").getAsString();
                String to = edge.get("to").getAsString();
                int w = edge.has("weight") ? edge.get("weight").getAsInt() : 1;
                graph.computeIfAbsent(from, k -> new ArrayList<>()).add(new Edge(to, w));
                graph.putIfAbsent(to, new ArrayList<>());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return graph;
    }

    public static Map<String, List<String>> loadUnweightedGraph(String path) {
        Map<String, List<String>> graph = new HashMap<>();
        try (FileReader reader = new FileReader(path)) {
            JsonObject obj = JsonParser.parseReader(reader).getAsJsonObject();
            JsonArray edges = obj.getAsJsonArray("edges");
            for (JsonElement e : edges) {
                JsonObject edge = e.getAsJsonObject();
                String from = edge.get("from").getAsString();
                String to = edge.get("to").getAsString();
                graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
                graph.putIfAbsent(to, new ArrayList<>());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return graph;
    }
}
