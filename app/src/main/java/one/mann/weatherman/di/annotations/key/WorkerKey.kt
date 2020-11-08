package one.mann.weatherman.di.annotations.key

import androidx.work.ListenableWorker
import dagger.MapKey
import kotlin.annotation.AnnotationTarget.*
import kotlin.reflect.KClass

/* Created by Psmann. */

@MapKey
@Target(FUNCTION, PROPERTY_GETTER, PROPERTY_SETTER)
@Retention(AnnotationRetention.RUNTIME)
internal annotation class WorkerKey(val value: KClass<out ListenableWorker>)