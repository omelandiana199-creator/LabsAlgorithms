import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;


// @param <T> тип даних, що представляє вершину графу.

public class Graph<T> {

    private final Map<T, List<T>> adjacencyList;

    public Graph() {
        this.adjacencyList = new HashMap<>();
    }

    /* Додає неорієнтоване ребро між двома вершинами
     */
    public void addEdge(T u, T v) {
        adjacencyList.computeIfAbsent(u, k -> new ArrayList<>()).add(v);
        adjacencyList.computeIfAbsent(v, k -> new ArrayList<>()).add(u);
    }

    //Перевіряє наявність циклу в неорієнтованому графі.
    public boolean hasCycle() {
        Set<T> visited = new HashSet<>();
        for (T vertex : adjacencyList.keySet()) {
            if (!visited.contains(vertex)) {
                if (hasCycleDFS(vertex, visited, null)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hasCycleDFS(T vertex, Set<T> visited, T parent) {
        visited.add(vertex);
        for (T neighbor : adjacencyList.getOrDefault(vertex, Collections.emptyList())) {
            if (!visited.contains(neighbor)) {
                if (hasCycleDFS(neighbor, visited, vertex)) {
                    return true;
                }
            } else if (!neighbor.equals(parent)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Знаходить найкоротший шлях між двома вершинами за допомогою BFS.
     * Повертає пустий список, якщо шляху немає або вершини не існують.
     */
    public List<T> shortestPathBfs(T start, T target) {
        //  якщо вершин немає в графі, немає сенсу запускати пошук
        if (!adjacencyList.containsKey(start) || !adjacencyList.containsKey(target)) {
            return Collections.emptyList();
        }

        Map<T, T> predecessors = new HashMap<>();
        Queue<T> queue = new LinkedList<>();
        Set<T> visited = new HashSet<>();

        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            T current = queue.poll();
            if (current.equals(target)) {
                return buildPath(predecessors, target);
            }
            for (T neighbor : adjacencyList.getOrDefault(current, Collections.emptyList())) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    predecessors.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }
        return Collections.emptyList();
    }

    private List<T> buildPath(Map<T, T> predecessors, T target) {
        LinkedList<T> path = new LinkedList<>();
        T step = target;
        while (step != null) {
            path.addFirst(step);
            step = predecessors.get(step);
        }
        return path;
    }

    //Повертає мапу з найкоротшими відстанями від стартової вершини до всіх інших.
    public Map<T, Integer> bfsDistances(T startVertex) {
        if (!adjacencyList.containsKey(startVertex)) {
            return Collections.emptyMap();
        }

        Map<T, Integer> distances = new HashMap<>();
        Queue<T> queue = new LinkedList<>();

        distances.put(startVertex, 0);
        queue.add(startVertex);

        while (!queue.isEmpty()) {
            T current = queue.poll();
            int currentDistance = distances.get(current);

            for (T neighbor : adjacencyList.getOrDefault(current, Collections.emptyList())) {
                if (!distances.containsKey(neighbor)) {
                    distances.put(neighbor, currentDistance + 1);
                    queue.add(neighbor);
                }
            }
        }
        return distances;
    }

    //Знаходить будь-який шлях між двома вершинами за допомогою DFS.
    public List<T> dfsPath(T start, T target) {
        if (!adjacencyList.containsKey(start) || !adjacencyList.containsKey(target)) {
            return Collections.emptyList();
        }

        List<T> path = new ArrayList<>();
        Set<T> visited = new HashSet<>();
        if (dfsPathRecursive(start, target, visited, path)) {
            return path;
        }
        return Collections.emptyList();
    }

    private boolean dfsPathRecursive(T current, T target, Set<T> visited, List<T> path) {
        visited.add(current);
        path.add(current);

        if (current.equals(target)) {
            return true;
        }

        for (T neighbor : adjacencyList.getOrDefault(current, Collections.emptyList())) {
            if (!visited.contains(neighbor)) {
                if (dfsPathRecursive(neighbor, target, visited, path)) {
                    return true;
                }
            }
        }
        path.remove(path.size() - 1);
        return false;
    }

    //Перевіряє, чи є граф двочастковим (може бути розфарбований у 2 кольори).
    public boolean isBipartite() {
        Map<T, Integer> colors = new HashMap<>();

        for (T startVertex : adjacencyList.keySet()) {
            if (!colors.containsKey(startVertex)) {
                Queue<T> queue = new LinkedList<>();
                queue.add(startVertex);
                colors.put(startVertex, 0);

                while (!queue.isEmpty()) {
                    T current = queue.poll();
                    int currentColor = colors.get(current);
                    int nextColor = 1 - currentColor;

                    for (T neighbor : adjacencyList.getOrDefault(current, Collections.emptyList())) {
                        if (!colors.containsKey(neighbor)) {
                            colors.put(neighbor, nextColor);
                            queue.add(neighbor);
                        } else if (colors.get(neighbor) == currentColor) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
}