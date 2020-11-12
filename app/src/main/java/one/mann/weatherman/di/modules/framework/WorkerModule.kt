package one.mann.weatherman.di.modules.framework

import androidx.work.WorkerFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import one.mann.weatherman.di.annotations.keys.WorkerKey
import one.mann.weatherman.framework.service.workers.NotificationWorker
import one.mann.weatherman.framework.service.workers.factory.ChildWorkerFactory
import one.mann.weatherman.framework.service.workers.factory.ParentWorkerFactory

/* Created by Psmann. */

@Module
internal abstract class WorkerModule {

    @Binds
    abstract fun bindParentWorkerFactory(factory: ParentWorkerFactory): WorkerFactory

    @Binds
    @IntoMap
    @WorkerKey(NotificationWorker::class)
    abstract fun bindUpdateWeatherWorker(worker: NotificationWorker.Factory): ChildWorkerFactory
}