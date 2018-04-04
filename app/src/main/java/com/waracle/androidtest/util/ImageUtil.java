package com.waracle.androidtest.util;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.ImageView;

import com.waracle.androidtest.network.tasks.DownloadImageTask;

import java.lang.ref.SoftReference;
import java.security.InvalidParameterException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ImageUtil {

    private static final Map<String, SoftReference<Bitmap>> cache = new ConcurrentHashMap<>();

    private ImageUtil() {
    }

    public static void load(String url, ImageView imageView) {
        if (TextUtils.isEmpty(url)) {
            throw new InvalidParameterException("URL is empty!");
        }

        if (cache.containsKey(url)) {
            final SoftReference<Bitmap> reference = cache.get(url);

            if (reference != null && reference.get() != null) {
                imageView.setImageBitmap(reference.get());
            }
        } else {
            new DownloadImageTask(imageView).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
        }
    }

    public static void cacheImage(String key, Bitmap image) {
        if (!cache.containsKey(key)) {
            cache.put(key, new SoftReference<>(image));
        }
    }
}
