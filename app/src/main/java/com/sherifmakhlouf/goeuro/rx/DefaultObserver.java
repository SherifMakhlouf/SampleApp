package com.sherifmakhlouf.goeuro.rx;

/**
 * Default Observer To clean up code
 */
public abstract  class DefaultObserver<T> implements rx.Observer<T> {

    @Override public void onCompleted() {
    }

    @Override public void onError(Throwable throwable) {
    }
}
