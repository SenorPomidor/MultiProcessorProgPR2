package ru.bit.mipt;

import java.util.List;

public interface Graph {
    List<Integer> getNeighbors(int node);
    int getNumberOfNodes();
}
