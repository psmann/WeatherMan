package one.mann.weatherman.application.di.module.framework

import androidx.work.WorkerFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import one.mann.weatherman.application.di.annotation.WorkerKey
import one.mann.weatherman.framework.service.workers.UpdateWeatherWorker
import one.mann.weatherman.framework.service.workers.factory.ChildWorkerFactory
import one.mann.weatherman.framework.service.workers.factory.ParentWorkerFactory

@Module
internal abstract class WorkerModule {

    @Binds
    abstract fun bindParentWorkerFactory(factory: ParentWorkerFactory): WorkerFactory

    @Binds
    @IntoMap
    @WorkerKey(UpdateWeatherWorker::class)
    abstract fun bindUpdateWeatherWorker(worker: UpdateWeatherWorker.Factory): ChildWorkerFactory
}