package binauld.pierre.musictag.io;


import android.util.LruCache;

public class Cache<Resource> {

    private LruCache<String, Resource> cache;

    public Cache() {
        // Use 1/8th of the available memory for this memory cache.
        this.cache = new LruCache<>((int) (Runtime.getRuntime().maxMemory() / 1024 / 8));
    }

    /**
     * Put a resource in the cache.
     * @param key The resource key.
     * @param resource The resource to put in the cache.
     */
    public void put(String key, Resource resource) {
        if (cache.get(key) == null) {
            cache.put(key, resource);
        }
    }

    /**
     * Get a resource from the cache associated to the key.
     * @param key The resource key.
     * @return A resource.
     */
    public Resource get(String key) {
        return cache.get(key);
    }


    /**
     * Get a resource from the cache associated to the key.
     * @param key The resource key.
     * @return A resource.
     */
    public Resource get(int key) {
        return cache.get(String.valueOf(key));
    }


}
