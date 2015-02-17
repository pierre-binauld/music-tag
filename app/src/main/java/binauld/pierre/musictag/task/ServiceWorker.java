package binauld.pierre.musictag.task;

import android.os.AsyncTask;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class ServiceWorker extends AsyncTask<Void, Task, Void> {

    private BlockingQueue<Task> queue = new LinkedBlockingDeque<>();
    private BlockingQueue<Integer> tokens = new ArrayBlockingQueue<>(1);
    private Map<Integer, Task> currentTasks = new HashMap<>();
    private boolean run = true;

    @Override
    protected Void doInBackground(Void... params) {
        int taskId = 0;

        try {
            while (run) {
                Integer token = tokens.take();
                TaskEndingHandler taskEndingHandler = new TaskEndingHandler(taskId, token);

                Task task = queue.take();
                task.addOnPostExecuteCallbacks(taskEndingHandler);

                currentTasks.put(taskId, task);

                publishProgress(task);

                taskId++;
            }
        } catch (InterruptedException e) {
            Log.i(getClass().toString(), e.getMessage());
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Task... values) {
        for (Task task : values) {
            AsyncTaskExecutor.execute(task);
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        for (Task task : currentTasks.values()) {
            task.cancel(true);
        }
        run = false;
    }

    public void addTask(Task task) {
        queue.add(task);
    }

    class TaskEndingHandler implements Runnable {

        private int taskId;
        private Integer token;

        TaskEndingHandler(int taskId, Integer token) {
            this.taskId = taskId;
            this.token = token;
        }

        @Override
        public void run() {
            currentTasks.remove(taskId);
            tokens.add(token);
        }
    }
}
