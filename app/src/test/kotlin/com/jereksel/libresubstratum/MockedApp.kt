package com.jereksel.libresubstratum

import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import com.jereksel.libresubstratum.activities.installed.InstalledContract
import com.jereksel.libresubstratum.activities.priorities.PrioritiesContract
import com.jereksel.libresubstratum.activities.prioritiesdetail.PrioritiesDetailContract
import com.jereksel.libresubstratum.dagger.components.BaseComponent
import com.jereksel.libresubstratum.domain.Metrics
import com.jereksel.libresubstratum.domain.PrivacyPolicySettings
import com.jereksel.libresubstratum.domain.usecases.ICleanUnusedOverlays
import dagger.Component
import dagger.Module
import dagger.Provides
import io.kotlintest.mock.mock
import org.mockito.Mockito
import javax.inject.Named
import javax.inject.Singleton

class MockedApp : App() {

    companion object {
        @JvmStatic
        val mockedInstalledPresenter: InstalledContract.Presenter = Mockito.mock(InstalledContract.Presenter::class.java)
        @JvmStatic
        val mockedPrioritiesPresenter: PrioritiesContract.Presenter = Mockito.mock(PrioritiesContract.Presenter::class.java)
        @JvmStatic
        val mockedPrioritiesDetailPresenter: PrioritiesDetailContract.Presenter = Mockito.mock(PrioritiesDetailContract.Presenter::class.java)
        @JvmStatic
        val viewModelFactory: ViewModelProvider.Factory = Mockito.mock(ViewModelProvider.Factory::class.java)

        @Module
        class TestModule {

            @Provides
            fun installed(): InstalledContract.Presenter = mockedInstalledPresenter
            @Provides
            fun priorities(): PrioritiesContract.Presenter = mockedPrioritiesPresenter
            @Provides
            fun proritiesDetail(): PrioritiesDetailContract.Presenter = mockedPrioritiesDetailPresenter
            @Provides
            @Named("persistent")
            fun metrics(): Metrics = mock()
            @Provides
            fun factory() = viewModelFactory
            @Provides
            fun iCleanUnusedOverlays(): ICleanUnusedOverlays = mock()
            @Provides
            fun privacySettings(): PrivacyPolicySettings = object: PrivacyPolicySettings {
                override fun isPrivacyPolicyRequired() = TODO()
            }

            @Provides
            fun detailedPresenter(): com.jereksel.libresubstratum.activities.detailed.DetailedPresenter = mock()

        }

        @Singleton
        @Component(modules = [TestModule::class])
        interface TestComponent: BaseComponent

    }

    override fun getAppComponent(context: Context): BaseComponent {
        return DaggerMockedApp_Companion_TestComponent.create()
    }

}
