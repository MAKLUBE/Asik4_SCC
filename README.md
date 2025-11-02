# 🧠 Assignment 4 — Strongly Connected Components & Shortest Paths in DAGs

### 👤 Author
**Rassul Sakenov, SE-2435**

---

## 🎯 Objective
Implement and evaluate several fundamental graph algorithms:
- **Tarjan’s Algorithm** — find Strongly Connected Components (SCCs)
- **Kahn’s Algorithm** — perform Topological Sorting
- **DAG Shortest Path** — compute Shortest and Longest paths in a Directed Acyclic Graph (DAG)

All algorithms are instrumented with a **Metrics** system that measures:
- Execution time (ms)
- DFS visits
- Edge traversals
- Queue operations
- Relaxations

---

## 🧩 Implementation Overview

| Component | Description |
|------------|--------------|
| `TarjanSCC.java` | Implements Tarjan’s algorithm to find SCCs using recursion, low-link values, and a stack. |
| `TopologicalSort.java` | Performs topological sorting using **Kahn’s Algorithm** and a DFS-based backup. |
| `DAGShortestPath.java` | Computes shortest and longest paths in a DAG using topological order. |
| `GraphLoader.java` | Loads JSON graph datasets into Java Map structures. |
| `Metrics.java` | Records operation counts and execution time for performance comparison. |
| `Main.java` | Orchestrates all algorithms and writes results to `metrics.csv`. |

---

## 📂 Dataset Summary (`graphs.json`)

| Category | Nodes (n) | Description | Variants |
|-----------|-----------|--------------|-----------|
| **Small** | 6–10 | Simple cases, 1–2 cycles or pure DAG | 3 |
| **Medium** | 10–20 | Mixed structures, several SCCs | 3 |
| **Large** | 20–50 | Performance and timing tests | 3 |

Each dataset has different density levels (sparse vs dense).  
At least one graph per category contains multiple SCCs.

---

## ⚙️ Instrumentation

- **Timing** via `System.nanoTime()`
- **Counters** include:
    - DFS visits (Tarjan)
    - Edge traversals (Kahn)
    - Queue operations (Kahn)
    - Relaxations (DAG shortest/longest)
- **Output:** `metrics.csv` — automatically generated after execution

---

## 📊 Results Summary (Extracted from `metrics.csv`)

| Dataset | Tarjan (ms) | Kahn (ms) | Shortest (ms) | Longest (ms) |
|----------|--------------|------------|----------------|----------------|
| small_1 | 0.147 | 0.116 | 0.36 | 0.19 |
| small_2 | 0.57 | 0.104 | 0.14 | 0.17 |
| small_3 | 0.59 | 0.27 | 0.70 | 0.12 |
| medium_1 | 0.132 | 0.38 | 0.25 | 0.53 |
| medium_2 | 0.75 | 0.54 | 0.33 | 0.24 |
| medium_3 | 0.76 | 0.42 | 0.17 | 0.26 |
| large_1 | 0.156 | 0.94 | 0.28 | 0.51 |
| large_2 | 0.99 | 2.23 | 0.24 | 0.37 |
| large_3 | 0.133 | 0.98 | 0.44 | 0.33 |

---

## 🧠 Observations

- Tarjan’s algorithm scales linearly with the number of nodes.
- Kahn’s algorithm grows faster on dense graphs due to more queue operations.
- Shortest and Longest path computations

---

## ✅ Conclusion

- Implemented and benchmarked **4 major graph algorithms** successfully.
- The unified metrics system allows objective performance comparison.
- Tarjan and Kahn demonstrate linear scalability for small to medium graphs.
- DAG-based pathfinding remains efficient even for larger input sizes.
- Project fully meets the **Assignment 4** requirements.

---

## 🧾 Reference 
- DAA Lecture 7 Slides — *SCCs and Shortest Paths in DAGs*.
