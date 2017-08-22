package com.fernandocejas.sample.framework.interactor

import com.fernandocejas.sample.framework.executor.ExecutionScheduler
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

abstract class UseCase<T, in P> where P : Any {

    private val disposables = CompositeDisposable()

    abstract fun buildObservable(params: P?): Observable<T>

    fun execute(observer: UseCaseObserver<T>, params: P? = null) {
        disposables.add(buildObservable(params).subscribeWith(observer))
    }

    fun dispose() = disposables.dispose()

    @Inject internal lateinit var scheduler: ExecutionScheduler
    internal fun <T> highPriorityScheduler() = scheduler.applyHighPriorityScheduler<T>()
    internal fun <T> lowPriorityScheduler() = scheduler.applyLowPriorityScheduler<T>()

    class None private constructor()
}
