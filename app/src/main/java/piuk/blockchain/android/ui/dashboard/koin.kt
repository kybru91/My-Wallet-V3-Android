package piuk.blockchain.android.ui.dashboard

import com.blockchain.core.announcements.DismissClock
import com.blockchain.core.announcements.DismissRecorder
import com.blockchain.domain.onboarding.CompletableDashboardOnboardingStep
import com.blockchain.koin.assetOrderingFeatureFlag
import com.blockchain.koin.defaultOrder
import com.blockchain.koin.exchangeWAPromptFeatureFlag
import com.blockchain.koin.payloadScopeQualifier
import com.blockchain.koin.sellOrder
import com.blockchain.koin.swapSourceOrder
import com.blockchain.koin.swapTargetOrder
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import org.koin.dsl.bind
import org.koin.dsl.module
import piuk.blockchain.android.domain.usecases.ShouldShowExchangeCampaignUseCase
import piuk.blockchain.android.ui.cowboys.CowboysPromoDataProvider
import piuk.blockchain.android.ui.dashboard.assetdetails.StateAwareActionsComparator
import piuk.blockchain.android.ui.dashboard.onboarding.DashboardOnboardingInteractor
import piuk.blockchain.android.ui.dashboard.onboarding.DashboardOnboardingModel
import piuk.blockchain.android.ui.transfer.AccountsSorting
import piuk.blockchain.android.ui.transfer.DefaultAccountsSorting
import piuk.blockchain.android.ui.transfer.SellAccountsSorting
import piuk.blockchain.android.ui.transfer.SwapSourceAccountsSorting
import piuk.blockchain.android.ui.transfer.SwapTargetAccountsSorting

val dashboardModule = module {

    scope(payloadScopeQualifier) {
        factory {
            StateAwareActionsComparator()
        }.bind(Comparator::class)

        factory {
            BalanceAnalyticsReporter(
                analytics = get()
            )
        }

        factory(defaultOrder) {
            DefaultAccountsSorting(currencyPrefs = get())
        }.bind(AccountsSorting::class)

        factory(swapSourceOrder) {
            SwapSourceAccountsSorting(
                assetListOrderingFF = get(assetOrderingFeatureFlag),
                dashboardAccountsSorter = get(defaultOrder),
                sellAccountsSorting = get(sellOrder),
            )
        }.bind(AccountsSorting::class)

        factory(swapTargetOrder) {
            SwapTargetAccountsSorting(
                currencyPrefs = get(),
                exchangeRatesDataManager = get(),
                watchlistDataManager = get()
            )
        }.bind(AccountsSorting::class)

        factory(sellOrder) {
            SellAccountsSorting(
                coincore = get()
            )
        }.bind(AccountsSorting::class)

        factory { params ->
            DashboardOnboardingModel(
                initialSteps = params.getOrNull<List<CompletableDashboardOnboardingStep>>() ?: emptyList(),
                interactor = get(),
                fiatCurrenciesService = get(),
                uiScheduler = AndroidSchedulers.mainThread(),
                environmentConfig = get(),
                remoteLogger = get()
            )
        }

        factory {
            DashboardOnboardingInteractor(
                getDashboardOnboardingUseCase = get(),
                bankService = get(),
                getAvailablePaymentMethodsTypesUseCase = get()
            )
        }

        scoped { WalletModeBalanceCache(coincore = get()) }

        scoped {
            CowboysPromoDataProvider(
                config = get(),
                json = get()
            )
        }

        factory {
            ShouldShowExchangeCampaignUseCase(
                exchangeWAPromptFF = get(exchangeWAPromptFeatureFlag),
                exchangeCampaignPrefs = get(),
                mercuryExperimentsService = get()
            )
        }
    }

    single {
        DismissRecorder(
            prefs = get(),
            clock = get()
        )
    }

    single {
        object : DismissClock {
            override fun now(): Long = System.currentTimeMillis()
        }
    }.bind(DismissClock::class)
}
