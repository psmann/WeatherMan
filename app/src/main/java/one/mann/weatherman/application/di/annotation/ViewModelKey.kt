package one.mann.weatherman.application.di.annotation

import androidx.lifecycle.ViewModel
import dagger.MapKey
import kotlin.annotation.AnnotationTarget.*
import kotlin.reflect.KClass

@Target(FUNCTION, PROPERTY_GETTER, PROPERTY_SETTER)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
internal annotation class ViewModelKey(val value: KClass<out ViewModel>)