package binauld.pierre.musictag.loader;

import android.os.AsyncTask;
import android.os.Build;

public class AsyncTaskExecutor {


    public static <Param, Progress, Result> AsyncTask<Param, Progress, Result> execute(AsyncTask<Param, Progress, Result> task, Param... params) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        } else {
            return task.execute();
        }
    }
}
