package binauld.pierre.musictag.service.task;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

public abstract class Task extends AsyncTask<Void, Void, Void> {

    private List<Runnable> onProgressUpdateCallbacks = new ArrayList<>();
    private List<Runnable> onPostExecuteCallbacks = new ArrayList<>();

    @Override
    protected void onProgressUpdate(Void... values) {
        for (Runnable callback : onProgressUpdateCallbacks) {
            callback.run();
        }
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        for (Runnable callback : onPostExecuteCallbacks) {
            callback.run();
        }
    }

    public void addOnProgressUpdateCallback(Runnable runnable) {
        if (null != runnable) {
            onProgressUpdateCallbacks.add(runnable);
        }
    }

    public void addOnPostExecuteCallback(Runnable runnable) {
        if (null != runnable) {
            onPostExecuteCallbacks.add(runnable);
        }
    }

}