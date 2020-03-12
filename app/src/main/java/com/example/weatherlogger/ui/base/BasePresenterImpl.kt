package com.example.weatherlogger.ui.base

import io.reactivex.disposables.CompositeDisposable

open class BasePresenterImpl<T : BaseContract.BaseView> : BaseContract.BasePresenter<T> {

    override var view: T? = null
    override val compositeDisposable: CompositeDisposable by lazy { CompositeDisposable() }
}