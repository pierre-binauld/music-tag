package binauld.pierre.musictag.io;


import android.util.LruCache;

public class Cache<Resource> {

    private LruCache<String, Resource> cache;

    public Cache() {
        // Use 1/8th of the available memory for this memory cache.
        this.cache = new LruCache<String, Resource>((int) (Runtime.getRuntime().maxMemory() / 1024 / 8));
    }

    public void put(String key, Resource resource) {
        if (cache.get(key) == null) {
            cache.put(key, resource);
        }
    }

    public Resource get(String key) {
        return cache.get(key);
    }
    public Resource get(int key) {
        return cache.get(String.valueOf(key));
    }


}
