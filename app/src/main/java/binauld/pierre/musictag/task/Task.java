package binauld.pierre.musictag.task;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

abstract class Task extends AsyncTask<Void, Void, Void> {

    private List<Runnable> onProgressUpdateCallbacks = new ArrayList<>();
    private List<Runnable> onPostExecuteCallbacks = new ArrayList<>();
    private Integer token;

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

    public void addOnProgressUpdateCallbacks(Runnable runnable) {
        onProgressUpdateCallbacks.add(runnable);
    }

    public void addOnPostExecuteCallbacks(Runnable runnable) {
        onPostExecuteCallbacks.add(runnable);
    }


    public Integer getToken() {
        return token;
    }

    public void setToken(Integer token) {
        this.token = token;
    }
}