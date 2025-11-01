package graph.scc;

import java.util.*;

public class TarjanSCC {

    private final Map<String, List<String>> graph;
    private int time = 0;
    private final Map<String, Integer> disc = new HashMap<>();
    private final Map<String, Integer> low = new HashMap<>();
    private final Deque<String> stack = new ArrayDeque<>();
    private final Set<String> onStack = new HashSet<>();
    private final List<List<String>> components = new ArrayList<>();

    public TarjanSCC(Map<String, List<String>> graph) {
        this.graph = graph;
    }

    public List<List<String>> findSCCs() {
        for (String v : graph.keySet()) {
            if (!disc.containsKey(v)) {
                dfs(v);
            }
        }
        return components;
    }

    private void dfs(String u) {
        disc.put(u, time);
        low.put(u, time);
        time++;
        stack.push(u);
        onStack.add(u);

        for (String v : graph.getOrDefault(u, Collections.emptyList())) {
            if (!disc.containsKey(v)) {
                dfs(v);
                low.put(u, Math.min(low.get(u), low.get(v)));
            } else if (onStack.contains(v)) {
                low.put(u, Math.min(low.get(u), disc.get(v)));
            }
        }

        // root of SCC
        if (Objects.equals(low.get(u), disc.get(u))) {
            List<String> comp = new ArrayList<>();
            String w;
            do {
                w = stack.pop();
                onStack.remove(w);
                comp.add(w);
            } while (!w.equals(u));
            components.add(comp);
        }
    }

    public static Map<Integer, List<String>> toCondensationGraph(List<List<String>> sccList,
                                                                 Map<String, List<String>> graph) {
        // Map vertex -> componentId
        Map<String, Integer> compIndex = new HashMap<>();
        for (int i = 0; i < sccList.size(); i++) {
            for (String v : sccList.get(i)) compIndex.put(v, i);
        }

        Map<Integer, List<String>> condensed = new HashMap<>();
        for (int i = 0; i < sccList.size(); i++) condensed.put(i, new ArrayList<>());

        for (var entry : graph.entrySet()) {
            String u = entry.getKey();
            for (String v : entry.getValue()) {
                int cu = compIndex.get(u);
                int cv = compIndex.get(v);
                if (cu != cv && !condensed.get(cu).contains("C" + cv))
                    condensed.get(cu).add("C" + cv);
            }
        }
        return condensed;
    }
}
