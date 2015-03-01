//package binauld.pierre.musictag.task;
//
//
//import android.os.AsyncTask;
//
//public class AsyncServiceWorker extends AsyncTask<Void, Runnable, Void> implements Task.Publisher {
//
//    private Task task;
//
//    public AsyncServiceWorker(Task task) {
//        this.task = task;
//        this.task.setPublisher(this);
//    }
//
//    @Override
//    protected Void doInBackground(Void... params) {
//
//        task.run();
//
//        return null;
//    }
//
//    @Override
//    protected void onProgressUpdate(Runnable... values) {
//        for (Runnable runnable : values) {
//            runnable.run();
//        }
//    }
//
//    public void publish(Runnable runnable) {
//        publishProgress(runnable);
//    }
//
//}
