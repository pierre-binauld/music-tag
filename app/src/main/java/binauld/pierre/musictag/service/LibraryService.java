package binauld.pierre.musictag.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import binauld.pierre.musictag.task.AsyncTaskExecutor;
import binauld.pierre.musictag.task.ServiceWorker;

public class LibraryService extends Service {

    private final IBinder binder = new LibraryServiceBinder();
    private ServiceWorker worker = new ServiceWorker();

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "Library Service created.", Toast.LENGTH_SHORT).show();
        AsyncTaskExecutor.execute(worker);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Library Service destroyed.", Toast.LENGTH_SHORT).show();
        worker.cancel(true);
    }


    public class LibraryServiceBinder extends Binder {
        public LibraryService getService() {
            return LibraryService.this;
        }

    }
}
