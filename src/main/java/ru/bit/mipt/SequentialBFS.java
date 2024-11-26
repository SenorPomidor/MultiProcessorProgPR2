package ru.bit.mipt;

import java.util.BitSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class SequentialBFS extends BFS {

    public SequentialBFS(Graph graph, int source) {
        super(graph, source);
    }

    @Override
    public int[] execute() {
        int n = graph.getNumberOfNodes();
        BitSet visited = new BitSet(n);
        int[] distances = new int[n];
        Queue<Integer> queue = new LinkedList<>();

        queue.add(source);
        visited.set(source);
        distances[source] = 0;

        while (!queue.isEmpty()) {
            int current = queue.poll();
            int currentDistance = distances[current];
            List<Integer> neighbors = graph.getNeighbors(current);
            for (int neighbor : neighbors) {
                if (!visited.get(neighbor)) {
                    visited.set(neighbor);
                    distances[neighbor] = currentDistance + 1;
                    queue.add(neighbor);
                }
            }
        }

        return distances;
    }
}



