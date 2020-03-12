package com.example.weatherlogger.ui.base

import androidx.annotation.CallSuper
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.exceptions.CompositeException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

interface BaseContract {

    interface BaseView {
        fun showNoConnectivityError()
        fun showError(errorMessage: String)
        fun showLoading()
        fun hideLoading()
    }

    interface BasePresenter<T : BaseView> {
        var view: T?
        val compositeDisposable: CompositeDisposable

        fun handleError(
            throwable: Throwable,
            nonGenericErrorHandler: (() -> Unit)? = null,
            showConnectivityError: Boolean = false
        ) {
            when (throwable) {
                is SocketTimeoutException,
                is UnknownHostException -> {
                    if (showConnectivityError) {
                        view?.apply {
                            showNoConnectivityError()
                        }
                    }
                }
                is CompositeException -> {
                    val unknownHostException =
                        throwable.exceptions.takeWhile { it is UnknownHostException }
                    if (unknownHostException.isNotEmpty() && showConnectivityError) {
                        view?.apply {
                            showNoConnectivityError()
                        }
                    }
                }
                else -> nonGenericErrorHandler?.let { it() }
            }
        }

        @CallSuper
        fun attachView(view: T?) {
            this.view = view
        }

        @CallSuper
        fun detachView() {
            compositeDisposable.clear()
            view = null
        }
    }
}