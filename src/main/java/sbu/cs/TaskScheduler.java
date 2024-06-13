package sbu.cs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TaskScheduler {
    public static class Task implements Runnable {
        String taskName;
        int processingTime;

        public Task(String taskName, int processingTime) {
            this.taskName = taskName;
            this.processingTime = processingTime;
        }

        @Override
        public void run() {
            try {
                System.out.println("Task " + taskName + " started, processing time: " + processingTime);
                Thread.sleep(processingTime);
                System.out.println("Task " + taskName + " finished");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static ArrayList<String> doTasks(ArrayList<Task> tasks) {
        ArrayList<String> finishedTasks = new ArrayList<>();

        // Sort tasks by processing time in descending order
        Collections.sort(tasks, Comparator.comparingInt(task -> -task.processingTime));

        for (Task task : tasks) {
            Thread thread = new Thread(task);
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            finishedTasks.add(task.taskName);
        }

        return finishedTasks;
    }

    public static void main(String[] args) {
        // Test your code here
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(new Task("Task1", 2000));
        tasks.add(new Task("Task2", 1000));
        tasks.add(new Task("Task3", 3000));
        tasks.add(new Task("Task4", 500));

        ArrayList<String> finishedTasks = doTasks(tasks);

        System.out.println("Finished tasks in order:");
        for (String taskName : finishedTasks) {
            System.out.println(taskName);
        }
    }
}
