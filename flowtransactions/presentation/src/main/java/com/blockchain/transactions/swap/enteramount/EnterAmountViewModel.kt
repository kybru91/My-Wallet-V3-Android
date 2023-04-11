package com.blockchain.transactions.swap.enteramount

import androidx.lifecycle.viewModelScope
import com.blockchain.coincore.Coincore
import com.blockchain.coincore.CryptoAsset
import com.blockchain.commonarch.presentation.mvi_v2.EmptyNavEvent
import com.blockchain.commonarch.presentation.mvi_v2.ModelConfigArgs
import com.blockchain.commonarch.presentation.mvi_v2.MviViewModel
import com.blockchain.componentlib.control.CurrencyValue
import com.blockchain.transactions.swap.SwapService
import info.blockchain.balance.CryptoCurrency
import info.blockchain.balance.FiatCurrency
import kotlinx.coroutines.launch

/**
 * @property fromTicker if we come from Coinview we should have preset FROM
 */
class EnterAmountViewModel(
    private val fromTicker: String? = null,
    private val coincore: Coincore,
    private val swapService: SwapService
) : MviViewModel<EnterAmountIntent, EnterAmountViewState, EnterAmountModelState, EmptyNavEvent, ModelConfigArgs.NoArgs>(
    EnterAmountModelState()
) {

    override fun viewCreated(args: ModelConfigArgs.NoArgs) {}

    override fun reduce(state: EnterAmountModelState): EnterAmountViewState {
        return with(state) {
            EnterAmountViewState(
                fromAsset = fromAsset?.toViewState(),
                toAsset = toAsset?.toViewState(),
                fiatAmount = fiatAmount,
                cryptoAmount = cryptoAmount
            )
        }
    }

    override suspend fun handleIntent(modelState: EnterAmountModelState, intent: EnterAmountIntent) {
        when (intent) {
            EnterAmountIntent.LoadData -> {
                updateState {
                    it.copy(
                        fromAsset = coincore["BTC"] as? CryptoAsset,
                        toAsset = coincore["ETH"] as? CryptoAsset,
                        fiatAmount = CurrencyValue(
                            value = "100", ticker = "$", isPrefix = true, separateWithSpace = false
                        ),
                        cryptoAmount = CurrencyValue(
                            value = "200", ticker = "BTC", isPrefix = false, separateWithSpace = true
                        ),
                    )
                }
            }
            is EnterAmountIntent.FiatAmountChanged -> {
                check(modelState.fiatAmount != null)
                check(modelState.cryptoAmount != null)

                swapService.fiatToCrypto(intent.amount, FiatCurrency.Dollars, CryptoCurrency.ETHER)
                viewModelScope.launch {
                    val cryptoAmount = swapService.fiatToCrypto(
                        intent.amount, FiatCurrency.Dollars, CryptoCurrency.ETHER
                    )
                    updateState {
                        it.copy(
                            fiatAmount = it.fiatAmount?.copy(value = intent.amount),
                            cryptoAmount = it.cryptoAmount?.copy(value = cryptoAmount)
                        )
                    }
                }
            }
            is EnterAmountIntent.CryptoAmountChanged -> {
                check(modelState.fiatAmount != null)
                check(modelState.cryptoAmount != null)

                viewModelScope.launch {
                    val fiatAmount = swapService.cryptoToFiat(
                        intent.amount, FiatCurrency.Dollars, CryptoCurrency.ETHER
                    )

                    updateState {
                        it.copy(
                            cryptoAmount = it.cryptoAmount?.copy(value = intent.amount),
                            fiatAmount = it.fiatAmount?.copy(value = fiatAmount),
                        )
                    }
                }
            }
        }
    }
}

private fun CryptoAsset.toViewState() = EnterAmountAssetState(
    iconUrl = currency.logo,
    ticker = currency.displayTicker
)