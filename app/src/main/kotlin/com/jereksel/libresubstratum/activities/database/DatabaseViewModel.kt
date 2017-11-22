package com.jereksel.libresubstratum.activities.database

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.jereksel.libresubstratum.domain.SubsDatabaseDownloader
import com.jereksel.libresubstratum.domain.SubstratumDatabaseTheme
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.http.GET

class DatabaseViewModel(
        val downloader: SubsDatabaseDownloader
): ViewModel() {

    var compositeDisposable = CompositeDisposable()

//    @Volatile
//    private var appsInvoked = false
    private var apps : MutableLiveData<List<SubstratumDatabaseTheme>>? = null
    val clearTheme = MutableLiveData<List<String>>()
    val darkTheme = MutableLiveData<List<String>>()
    val lightThemes = MutableLiveData<List<String>>()
    val plugin = MutableLiveData<List<String>>()
    val samsung = MutableLiveData<List<String>>()
    val wallpapers = MutableLiveData<List<String>>()

    fun getApps(): MutableLiveData<List<SubstratumDatabaseTheme>> {

        val apps = apps

        if (apps == null) {
            val newApps = MutableLiveData<List<SubstratumDatabaseTheme>>()
            this.apps = newApps
            asyncGetApps()
            return newApps
        } else {
            return apps
        }

    }

    private fun asyncGetApps() {
        compositeDisposable += downloader.getApps()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe { v ->
                    apps?.value = v
                }
    }

//    init {
////        computeApps()
//        computeApps()
//    }
//
//    fun computeApps() {
//        compositeDisposable += downloader.getApps()
//                .observeOn(Schedulers.io())
//                .subscribeOn(Schedulers.io())
//                .subscribe { v ->
//                    apps.postValue(v)
//                }
//    }

    override fun onCleared() {
        compositeDisposable.dispose()
    }

    fun computeApps2() {

//        Schedulers.io().scheduleDirect {
//            Thread.sleep(10000)
//            val list = (1..10).map { "app$it" }
//            apps.postValue(list)
//        }


//        val module = JacksonXmlModule()
//        module.setDefaultUseWrapper(false)
//        val xmlMapper = XmlMapper(module)
//
//        xmlMapper.registerModule(KotlinModule())

        val retrofit = Retrofit.Builder()
                .baseUrl("https://raw.githubusercontent.com/")
//                .addConverterFactory(JacksonConverterFactory.create(xmlMapper))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        val api = retrofit.create(Api::class.java)

        api.getApps()
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe {
//                    apps.postValue(it.themes)
                }


    }

    interface Api {

        @GET("substratum/database/master/substratum_tab_apps.xml")
        fun getApps(): Observable<substratum>

    }

    data class substratum(
//        @JsonProperty
//        @JacksonXmlProperty(localName = "event")
        val themes: List<theme>
    )

    data class theme(
            val author: String
    )

//    fun getApps() {
//        apps.observe()
//    }

}