package android.rezkyaulia.com.reactive_programming

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.functions.Function
import io.reactivex.functions.Predicate
import io.reactivex.schedulers.Schedulers
import io.reactivex.observers.DisposableObserver




class MainActivity : AppCompatActivity(){
    val TAG: String = MainActivity::class.java.name
    var animalObs: Observable<String> = getAnimalsObservable()
    val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val animalObserver = getAnimalsObserver()
        compositeDisposable.add(animalObs
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(Predicate<String>() {
                    it.toLowerCase().startsWith("b");

                })
                .subscribeWith(animalObserver)
        )
        val animalsCapObserver = getAnimalsAllCapsObserver()
        compositeDisposable.add(animalObs
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter { t ->
                    t.toLowerCase().startsWith("c")
                }
                .map { s -> s.toUpperCase() }
                .subscribeWith(animalsCapObserver)
        )
    }


    private fun getAnimalsObservable(): Observable<String> {
        return Observable.fromArray(
                "Ant", "Ape",
                "Bat", "Bee", "Bear", "Butterfly",
                "Cat", "Crab", "Cod",
                "Dog", "Dove",
                "Fox", "Frog")
    }

    private fun getAnimalsAllCapsObserver(): DisposableObserver<String> {
        return object : DisposableObserver<String>() {


            override fun onNext(s: String) {
                Log.e(TAG, "Name: $s")
            }

            override fun onError(e: Throwable) {
                Log.e(TAG, "onError: " + e.message)
            }

            override fun onComplete() {
                Log.e(TAG, "All items are emitted!")
            }
        }
    }
    private fun getAnimalsObserver(): DisposableObserver<String> {
        return object : DisposableObserver<String>() {

            override fun onNext(s: String) {
                Log.d(TAG, "Name: $s")
            }

            override fun onError(e: Throwable) {
                Log.e(TAG, "onError: " + e.message)
            }

            override fun onComplete() {
                Log.d(TAG, "All items are emitted!")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}
