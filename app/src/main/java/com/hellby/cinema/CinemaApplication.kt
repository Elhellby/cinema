package com.hellby.cinema

import android.app.Application
import coil.Coil
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoder
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CinemaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val imageLoader = ImageLoader.Builder(this)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.25)
                    .build()
            }
            .diskCachePolicy(CachePolicy.ENABLED)
            .diskCache {
                DiskCache.Builder()
                    .directory(cacheDir.resolve("image_cache").apply { mkdirs() })
                    .maxSizePercent(0.02)
                    .build()
            }
            .components {
                add(GifDecoder.Factory())
                add(ImageDecoder.Factory())
            }
            .crossfade(true)
            .build()
        Coil.setImageLoader(imageLoader)
    }
}
