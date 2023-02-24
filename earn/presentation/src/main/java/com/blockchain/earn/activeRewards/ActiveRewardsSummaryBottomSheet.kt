package com.blockchain.earn.activeRewards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import com.blockchain.coincore.EarnRewardsAccount
import com.blockchain.commonarch.presentation.mvi_v2.MVIBottomSheet
import com.blockchain.commonarch.presentation.mvi_v2.NavigationRouter
import com.blockchain.commonarch.presentation.mvi_v2.bindViewModel
import com.blockchain.earn.activeRewards.viewmodel.ActiveRewardsError
import com.blockchain.earn.activeRewards.viewmodel.ActiveRewardsSummaryArgs
import com.blockchain.earn.activeRewards.viewmodel.ActiveRewardsSummaryNavigationEvent
import com.blockchain.earn.activeRewards.viewmodel.ActiveRewardsSummaryViewModel
import com.blockchain.earn.activeRewards.viewmodel.ActiveRewardsSummaryViewState
import com.blockchain.earn.staking.SummarySheetLoading
import com.blockchain.koin.payloadScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinScopeComponent
import org.koin.core.scope.Scope

class ActiveRewardsSummaryBottomSheet :
    MVIBottomSheet<ActiveRewardsSummaryViewState>(),
    KoinScopeComponent,
    NavigationRouter<ActiveRewardsSummaryNavigationEvent> {

    interface Host : MVIBottomSheet.Host {
        fun openExternalUrl(url: String)
        fun launchActiveRewardsWithdrawal(account: EarnRewardsAccount.Active)
        fun launchActiveRewardsDeposit(account: EarnRewardsAccount.Active)
        fun showActiveRewardsLoadingError(error: ActiveRewardsError)
    }

    override val host: Host by lazy {
        super.host as? Host ?: throw IllegalStateException(
            "Host fragment is not a ActiveRewardsSummaryBottomSheet.Host"
        )
    }

    override val scope: Scope
        get() = payloadScope

    private val viewModel by viewModel<ActiveRewardsSummaryViewModel>()

    private val cryptoTicker by lazy {
        arguments?.getString(ActiveRewardsSummaryBottomSheet.ASSET_TICKER) ?: throw IllegalStateException(
            "StakingSummaryBottomSheet requires a ticker to start"
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setContent {
                bindViewModel(
                    viewModel = viewModel,
                    navigator = this@ActiveRewardsSummaryBottomSheet,
                    args = ActiveRewardsSummaryArgs(cryptoTicker)
                )

                ActiveRewardsSummaryScreen(
                    viewModel = viewModel,
                    onClosePressed = this@ActiveRewardsSummaryBottomSheet::dismiss,
                    onLoadError = { error ->
                        dismiss()
                        host.showActiveRewardsLoadingError(error)
                    },
                    onWithdrawPressed = { account ->
                        dismiss()
                        host.launchActiveRewardsWithdrawal(account)
                    },
                    onDepositPressed = { account ->
                        dismiss()
                        host.launchActiveRewardsDeposit(account)
                    },
                    withdrawDisabledLearnMore = {
                        // TODO (labreu): add withdrawal disabled messaging
                    },
                )
            }
        }
    }

    override fun onStateUpdated(state: ActiveRewardsSummaryViewState) { }

    override fun route(navigationEvent: ActiveRewardsSummaryNavigationEvent) { }

    companion object {
        private const val ASSET_TICKER = "ASSET_TICKER"

        fun newInstance(cryptoTicker: String) = ActiveRewardsSummaryBottomSheet().apply {
            arguments = Bundle().apply {
                putString(ASSET_TICKER, cryptoTicker)
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ActiveRewardsSummaryScreen(
    viewModel: ActiveRewardsSummaryViewModel,
    onClosePressed: () -> Unit,
    onLoadError: (ActiveRewardsError) -> Unit,
    onWithdrawPressed: (currency: EarnRewardsAccount.Active) -> Unit,
    onDepositPressed: (currency: EarnRewardsAccount.Active) -> Unit,
    withdrawDisabledLearnMore: () -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val stateFlowLifecycleAware = remember(viewModel.viewState, lifecycleOwner) {
        viewModel.viewState.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
    }
    val viewState: ActiveRewardsSummaryViewState? by stateFlowLifecycleAware.collectAsState(null)

    viewState?.let { state ->
        Column(modifier = Modifier.nestedScroll(rememberNestedScrollInteropConnection())) {
            when {
                state.isLoading -> {
                    SummarySheetLoading()
                }
                state.errorState != ActiveRewardsError.None -> {
                    onLoadError(state.errorState)
                }
                else -> {
                    ActiveRewardsSummarySheet(
                        state = state,
                        onWithdrawPressed = onWithdrawPressed,
                        onDepositPressed = onDepositPressed,
                        withdrawDisabledLearnMore = withdrawDisabledLearnMore,
                        onClosePressed = onClosePressed,
                    )
                }
            }
        }
    }
}
