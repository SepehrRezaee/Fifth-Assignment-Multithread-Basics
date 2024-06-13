package sbu.cs;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MatrixMultiplication {

    // You are allowed to change all code in the BlockMultiplier class
    public static class BlockMultiplier implements Runnable {
        List<List<Integer>> matrixA, matrixB, result;
        int rowStart, rowEnd, colStart, colEnd;

        public BlockMultiplier(List<List<Integer>> matrixA, List<List<Integer>> matrixB, List<List<Integer>> result, int rowStart, int rowEnd, int colStart, int colEnd) {
            this.matrixA = matrixA;
            this.matrixB = matrixB;
            this.result = result;
            this.rowStart = rowStart;
            this.rowEnd = rowEnd;
            this.colStart = colStart;
            this.colEnd = colEnd;
        }

        @Override
        public void run() {
            for (int i = rowStart; i < rowEnd; i++) {
                for (int j = colStart; j < colEnd; j++) {
                    result.get(i).set(j, 0);
                    for (int k = 0; k < matrixA.get(0).size(); k++) {
                        result.get(i).set(j, result.get(i).get(j) + matrixA.get(i).get(k) * matrixB.get(k).get(j));
                    }
                }
            }
        }
    }

    public static List<List<Integer>> ParallelizeMatMul(List<List<Integer>> matrixA, List<List<Integer>> matrixB) {
        int p = matrixA.size();
        int q = matrixA.get(0).size();
        int r = matrixB.get(0).size();

        List<List<Integer>> result = new ArrayList<>();
        for (int i = 0; i < p; i++) {
            List<Integer> row = new ArrayList<>();
            for (int j = 0; j < r; j++) {
                row.add(0);
            }
            result.add(row);
        }

        ExecutorService executor = Executors.newFixedThreadPool(4);

        int halfP = p / 2;
        int halfR = r / 2;

        BlockMultiplier task1 = new BlockMultiplier(matrixA, matrixB, result, 0, halfP, 0, halfR);
        BlockMultiplier task2 = new BlockMultiplier(matrixA, matrixB, result, 0, halfP, halfR, r);
        BlockMultiplier task3 = new BlockMultiplier(matrixA, matrixB, result, halfP, p, 0, halfR);
        BlockMultiplier task4 = new BlockMultiplier(matrixA, matrixB, result, halfP, p, halfR, r);

        try {
            executor.submit(task1).get();
            executor.submit(task2).get();
            executor.submit(task3).get();
            executor.submit(task4).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        executor.shutdown();

        return result;
    }

    public static void main(String[] args) {
        // Test your code here
    }
}
