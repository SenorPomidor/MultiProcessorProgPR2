package ru.bit.mipt;

public class Main {
    public static void main(String[] args) {
        int N = 500;
        int source = 0;
        int numThreads = 4;
        int runs = 5;

        Graph graph = new CubeGraph(N);

        System.out.println("Количество узлов: " + graph.getNumberOfNodes());

        System.out.println("Выполнение разогревочных запусков...");
        SequentialBFS warmupSeqBFS = new SequentialBFS(graph, source);
        warmupSeqBFS.execute();

        ParallelBFS warmupParBFS = new ParallelBFS(graph, source, numThreads);
        warmupParBFS.execute();
        System.out.println("Разогревочные запуски завершены.\n");

        SequentialBFS seqBFS = new SequentialBFS(graph, source);
        long seqTotalTime = 0;
        int[] seqDistances = null;

        for (int i = 0; i < runs; i++) {
            long startTime = System.nanoTime();
            seqDistances = seqBFS.execute();
            long endTime = System.nanoTime();
            long elapsed = (endTime - startTime) / 1_000_000;
            seqTotalTime += elapsed;
            System.out.println("Последовательный запуск " + (i + 1) + ": " + elapsed + " мс");
        }

        double seqAvgTime = seqTotalTime / (double) runs;
        System.out.println("Среднее время последовательного BFS: " + seqAvgTime + " мс\n");

        ParallelBFS parBFS = new ParallelBFS(graph, source, numThreads);
        long parTotalTime = 0;
        int[] parDistances = null;

        for (int i = 0; i < runs; i++) {
            long startTime = System.nanoTime();
            parDistances = parBFS.execute();
            long endTime = System.nanoTime();
            long elapsed = (endTime - startTime) / 1_000_000;
            parTotalTime += elapsed;
            System.out.println("Параллельный запуск " + (i + 1) + ": " + elapsed + " мс");
        }

        double parAvgTime = parTotalTime / (double) runs;
        System.out.println("Среднее время параллельного BFS: " + parAvgTime + " мс\n");

        System.out.println("\nПроверка корректности результатов...");
        if (compareDistances(seqDistances, parDistances)) {
            System.out.println("Результаты последовательного и параллельного BFS совпадают.");
        } else {
            System.out.println("Результаты последовательного и параллельного BFS НЕ совпадают!");
        }
    }

    /**
     * Сравнивает два массива расстояний.
     */
    private static boolean compareDistances(int[] seqDistances, int[] parDistances) {
        if (seqDistances == null || parDistances == null) {
            System.out.println("Один из массивов расстояний равен null.");
            return false;
        }

        if (seqDistances.length != parDistances.length) {
            System.out.println("Длины массивов расстояний не совпадают.");
            return false;
        }

        for (int i = 0; i < seqDistances.length; i++) {
            if (seqDistances[i] != parDistances[i]) {
                System.out.println("Несовпадение на индексе " + i + ": последовательный BFS = " + seqDistances[i]
                        + ", параллельный BFS = " + parDistances[i]);
                return false;
            }
        }
        return true;
    }
}


