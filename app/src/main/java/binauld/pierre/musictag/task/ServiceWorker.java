package binauld.pierre.musictag.task;

import android.os.AsyncTask;
import android.util.Log;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;


public class ServiceWorker extends AsyncTask<Void, Runnable, Void> {

    BlockingQueue<Task> queue = new LinkedBlockingDeque<>();
    private boolean run = true;

    @Override
    protected Void doInBackground(Void... params) {

        try {
            while (run) {
                Task task = queue.take();
                task.run();
                publishProgress(task.getCallback());
            }
        } catch (InterruptedException e) {
            Log.i(getClass().toString(), e.getMessage());
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Runnable... values) {
        for (Runnable runnable : values) {
            runnable.run();
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        run = false;
    }

    public void addTask(Task task) {
        queue.add(task);
    }

    public abstract class Task implements Runnable {

        private Runnable callback;

        protected Task(Runnable callback) {
            this.callback = callback;
        }

        public Runnable getCallback() {
            return callback;
        }
    }
}
