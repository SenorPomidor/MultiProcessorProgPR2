package ru.bit.mipt;

import java.util.ArrayList;
import java.util.List;

public class CubeGraph implements Graph {
    private final int N;
    private final int totalNodes;

    public CubeGraph(int N) {
        this.N = N;
        this.totalNodes = N * N * N;
    }

    @Override
    public List<Integer> getNeighbors(int node) {
        List<Integer> neighbors = new ArrayList<>(6);
        int x = node / (N * N);
        int y = (node / N) % N;
        int z = node % N;

        if (x > 0) neighbors.add((x - 1) * N * N + y * N + z);
        if (x < N - 1) neighbors.add((x + 1) * N * N + y * N + z);
        if (y > 0) neighbors.add(x * N * N + (y - 1) * N + z);
        if (y < N - 1) neighbors.add(x * N * N + (y + 1) * N + z);
        if (z > 0) neighbors.add(x * N * N + y * N + (z - 1));
        if (z < N - 1) neighbors.add(x * N * N + y * N + (z + 1));

        return neighbors;
    }

    @Override
    public int getNumberOfNodes() {
        return totalNodes;
    }
}
