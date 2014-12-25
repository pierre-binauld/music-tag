package binauld.pierre.musictag.service;

import android.graphics.Bitmap;


public class Locator {

    private static CacheService<Bitmap> cacheService;

    public static void provide(CacheService<Bitmap> cacheService) {
        Locator.cacheService = cacheService;
    }

    public static CacheService<Bitmap> getCacheService() {
        return Locator.cacheService;
    }
}
