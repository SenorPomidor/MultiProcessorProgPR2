package ru.bit.mipt;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class ParallelBFS extends BFS {
    private final int numThreads;

    public ParallelBFS(Graph graph, int source, int numThreads) {
        super(graph, source);
        this.numThreads = numThreads;
    }

    @Override
    public int[] execute() {
        int n = graph.getNumberOfNodes();
        AtomicIntegerArray visited = new AtomicIntegerArray(n);
        AtomicIntegerArray distances = new AtomicIntegerArray(n);

        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        try {
            List<Integer> currentFrontier = new ArrayList<>();
            currentFrontier.add(source);
            visited.set(source, 1);
            distances.set(source, 0);

            while (!currentFrontier.isEmpty()) {
                List<Future<List<Integer>>> futures = new ArrayList<>();

                int chunkSize = (int) Math.ceil((double) currentFrontier.size() / numThreads);
                for (int i = 0; i < numThreads; i++) {
                    int start = i * chunkSize;
                    int end = Math.min(start + chunkSize, currentFrontier.size());
                    if (start >= end) break;
                    List<Integer> subList = currentFrontier.subList(start, end);
                    futures.add(executor.submit(new BFSWorker(subList, visited, distances)));
                }

                List<Integer> nextFrontier = new ArrayList<>();
                for (Future<List<Integer>> future : futures) {
                    try {
                        nextFrontier.addAll(future.get());
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }

                currentFrontier = nextFrontier;
            }
        } finally {
            executor.shutdown();
        }

        int[] distancesArray = new int[n];
        for (int i = 0; i < n; i++) {
            distancesArray[i] = distances.get(i);
        }

        return distancesArray;
    }

    private class BFSWorker implements Callable<List<Integer>> {
        private final List<Integer> batch;
        private final AtomicIntegerArray visited;
        private final AtomicIntegerArray distances;

        public BFSWorker(List<Integer> batch, AtomicIntegerArray visited, AtomicIntegerArray distances) {
            this.batch = batch;
            this.visited = visited;
            this.distances = distances;
        }

        @Override
        public List<Integer> call() {
            List<Integer> localNextFrontier = new ArrayList<>();
            for (int node : batch) {
                int currentDistance = distances.get(node);
                List<Integer> neighbors = graph.getNeighbors(node);
                for (int neighbor : neighbors) {
                    if (visited.compareAndSet(neighbor, 0, 1)) {
                        distances.set(neighbor, currentDistance + 1);
                        localNextFrontier.add(neighbor);
                    }
                }
            }
            return localNextFrontier;
        }
    }
}
