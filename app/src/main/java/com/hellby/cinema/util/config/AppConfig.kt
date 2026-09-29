package com.hellby.cinema.util.config

import com.hellby.cinema.BuildConfig
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppConfig @Inject constructor() {
    val baseUrl: String = BuildConfig.BASE_URL
    val enableLogging: Boolean = BuildConfig.ENABLE_LOGGING
    val videoSampleUrl: String = BuildConfig.VIDEO_SAMPLE_URL
}
