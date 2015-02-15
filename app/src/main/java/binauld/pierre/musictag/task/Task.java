package binauld.pierre.musictag.task;

abstract class Task implements Runnable {

    private Runnable callback;
    private Publisher publisher;

    public Task() {
        this.callback = emptyCallback;
    }

    protected void publish() {
        publisher.publish(callback);
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public void setCallback(Runnable callback) {
        this.callback = callback;
    }

    public static Runnable emptyCallback = new Runnable() {

        @Override
        public void run() {

        }
    };

    protected interface Publisher {
        void publish(Runnable runnable);
    }
}