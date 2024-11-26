package ru.bit.mipt;

public abstract class BFS {
    protected Graph graph;
    protected int source;

    public BFS(Graph graph, int source) {
        this.graph = graph;
        this.source = source;
    }

    public abstract int[] execute();
}




