package binauld.pierre.musictag.task;

import android.os.AsyncTask;
import android.util.Log;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class ServiceWorker extends AsyncTask<Void, Runnable, Void> implements Task.Publisher {

    BlockingQueue<Task> queue = new LinkedBlockingDeque<>();
    private boolean run = true;

    @Override
    protected Void doInBackground(Void... params) {

        try {
            while (run) {
                Task task = queue.take();
                task.run();
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
        task.setPublisher(this);
        queue.add(task);
    }

    public void publish(Runnable runnable) {
        publishProgress(runnable);
    }

}
