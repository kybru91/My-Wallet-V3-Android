package piuk.blockchain.android.ui.transactionflow.plugin

import android.content.Context
import android.util.AttributeSet
import com.blockchain.componentlib.viewextensions.visibleIf
import com.blockchain.core.limits.TxLimits
import com.blockchain.core.price.ExchangeRate
import com.blockchain.presentation.complexcomponents.QuickFillButtonData
import com.blockchain.presentation.complexcomponents.QuickFillDisplayAndAmount
import com.blockchain.presentation.complexcomponents.QuickFillRowView
import info.blockchain.balance.Currency
import info.blockchain.balance.CurrencyType
import info.blockchain.balance.Money
import java.math.BigDecimal
import kotlin.math.floor
import piuk.blockchain.android.R
import piuk.blockchain.android.ui.transactionflow.analytics.TxFlowAnalytics
import piuk.blockchain.android.ui.transactionflow.engine.PrefillAmounts
import piuk.blockchain.android.ui.transactionflow.engine.TransactionIntent
import piuk.blockchain.android.ui.transactionflow.engine.TransactionModel
import piuk.blockchain.android.ui.transactionflow.engine.TransactionState
import piuk.blockchain.android.ui.transactionflow.flow.convertFiatToCrypto
import piuk.blockchain.android.ui.transactionflow.flow.customisations.EnterAmountCustomisations

class QuickFillRowView @JvmOverloads constructor(
    ctx: Context,
    attr: AttributeSet? = null,
    defStyle: Int = 0
) : QuickFillRowView(ctx, attr, defStyle),
    EnterAmountWidget {

    private lateinit var model: TransactionModel
    private lateinit var customiser: EnterAmountCustomisations
    private lateinit var analytics: TxFlowAnalytics

    override fun initControl(
        model: TransactionModel,
        customiser: EnterAmountCustomisations,
        analytics: TxFlowAnalytics
    ) {
        check(this::model.isInitialized.not()) { "QuickFillTxFlowWidget already initialised" }

        this.model = model
        this.customiser = customiser
        this.analytics = analytics
    }

    override fun update(state: TransactionState) {
        state.pendingTx?.limits?.let { limits ->
            state.fiatRate?.let { fiatRate ->
                when (state.currencyType) {
                    CurrencyType.CRYPTO -> {
                        renderCryptoButtons(limits, state, fiatRate)
                    }
                    CurrencyType.FIAT -> {
                        renderFiatButtons(state, fiatRate, limits)
                    }
                    null -> {
                        // do nothing - screen is initialising
                    }
                }
            }
            maxButtonText = customiser.quickFillRowMaxButtonLabel(state)

            onMaxItemClick = { maxAmount ->
                state.fiatRate?.let { rate ->
                    model.process(
                        TransactionIntent.UpdatePrefillAmount(
                            PrefillAmounts(
                                cryptoValue = maxAmount,
                                fiatValue = state.convertBalanceToFiat(maxAmount, rate)
                            )
                        )
                    )
                }
            }
        }
    }

    private fun renderFiatButtons(
        state: TransactionState,
        fiatRate: ExchangeRate,
        limits: TxLimits
    ) {
        val spendableFiatBalance = state.convertBalanceToFiat(state.maxSpendable, fiatRate)
        val fiatCurrency = spendableFiatBalance.currency

        val listOfAmounts = mutableListOf<Money>()
        val spendableBalanceWithinLimits = limits.getSpendableBalanceWithinLimits(
            amount = state.maxSpendable,
            currency = fiatCurrency
        )

        val lowestPrefillValues = getRoundedFiatAndCryptoValues(
            state = state,
            spendableBalance = spendableBalanceWithinLimits,
            fiatRate = fiatRate,
            multiplicationFactor = TWENTY_FIVE_PERCENT
        )

        if (limits.isAmountInRange(lowestPrefillValues.second)) {
            listOfAmounts.add(lowestPrefillValues.first)
        }

        val mediumPrefillValues = getRoundedFiatAndCryptoValues(
            state = state,
            spendableBalance = spendableBalanceWithinLimits,
            fiatRate = fiatRate,
            multiplicationFactor = FIFTY_PERCENT
        )

        if (limits.isAmountInRange(mediumPrefillValues.second)) {
            listOfAmounts.add(mediumPrefillValues.first)
        }

        val largestPrefillValues =
            getRoundedFiatAndCryptoValues(
                state = state,
                spendableBalance = spendableBalanceWithinLimits,
                fiatRate = fiatRate,
                multiplicationFactor = SEVENTY_FIVE_PERCENT
            )

        if (limits.isAmountInRange(largestPrefillValues.second)) {
            listOfAmounts.add(state.convertBalanceToFiat(largestPrefillValues.first, fiatRate))
        }

        quickFillButtonData = QuickFillButtonData(
            maxAmount = state.maxSpendable,
            quickFillButtons = listOfAmounts.map { amount ->
                QuickFillDisplayAndAmount(
                    displayValue = amount.toStringWithSymbol(includeDecimalsWhenWhole = false),
                    amount = amount
                )
            }
        )

        onQuickFillItemClick = { quickFillData ->
            model.process(
                TransactionIntent.UpdatePrefillAmount(
                    PrefillAmounts(
                        cryptoValue = quickFillData.amount.convertFiatToCrypto(fiatRate, state),
                        fiatValue = quickFillData.amount
                    )
                )
            )
        }
    }

    private fun renderCryptoButtons(
        limits: TxLimits,
        state: TransactionState,
        fiatRate: ExchangeRate
    ) {
        val adjustedBalance =
            limits.getSpendableBalanceWithinLimits(state.maxSpendable, state.maxSpendable.currency)

        val listOfAmounts = mutableListOf<QuickFillDisplayAndAmount>()

        val lowestPrefillAmount = adjustedBalance.times(TWENTY_FIVE_PERCENT)

        if (limits.isAmountInRange(lowestPrefillAmount)) {
            listOfAmounts.add(
                QuickFillDisplayAndAmount(
                    displayValue = resources.getString(R.string.enter_amount_quickfill_25),
                    amount = lowestPrefillAmount
                )
            )
        }

        val mediumPrefillAmount = adjustedBalance.times(FIFTY_PERCENT)

        if (limits.isAmountInRange(mediumPrefillAmount)) {
            listOfAmounts.add(
                QuickFillDisplayAndAmount(
                    displayValue = resources.getString(R.string.enter_amount_quickfill_50),
                    amount = mediumPrefillAmount
                )
            )
        }

        val largestPrefillAmount = adjustedBalance.times(SEVENTY_FIVE_PERCENT)

        if (limits.isAmountInRange(largestPrefillAmount)) {
            listOfAmounts.add(
                QuickFillDisplayAndAmount(
                    displayValue = resources.getString(R.string.enter_amount_quickfill_75),
                    amount = largestPrefillAmount
                )
            )
        }

        quickFillButtonData = QuickFillButtonData(
            maxAmount = state.maxSpendable,
            quickFillButtons = listOfAmounts
        )

        onQuickFillItemClick = { quickFillData ->
            model.process(
                TransactionIntent.UpdatePrefillAmount(
                    PrefillAmounts(
                        cryptoValue = quickFillData.amount,
                        fiatValue = state.convertBalanceToFiat(quickFillData.amount, fiatRate)
                    )
                )
            )
        }
    }

    private fun getRoundedFiatAndCryptoValues(
        state: TransactionState,
        spendableBalance: Money,
        fiatRate: ExchangeRate,
        multiplicationFactor: Float
    ): Pair<Money, Money> {

        val lowestPrefillCrypto = spendableBalance.times(multiplicationFactor)
        val lowestPrefillFiat = state.convertBalanceToFiat(lowestPrefillCrypto, fiatRate)
        val lowestPrefillFiatParts = lowestPrefillFiat.toStringParts()

        val lowestPrefillRoundedFiat = when (
            lowestPrefillFiatParts.major.filterNot {
                it == lowestPrefillFiatParts.groupingSeparator
            }.length
        ) {
            0,
            1 -> {
                roundToNearest(lowestPrefillFiat, NEAREST_ONE)
            }
            2 -> {
                roundToNearest(lowestPrefillFiat, NEAREST_TEN)
            }
            3 -> {
                roundToNearest(lowestPrefillFiat, NEAREST_TWENTY_FIVE)
            }
            4 -> {
                roundToNearest(lowestPrefillFiat, NEAREST_HUNDRED)
            }
            5 -> {
                roundToNearest(lowestPrefillFiat, NEAREST_FIVE_HUNDRED)
            }
            else -> {
                roundToNearest(lowestPrefillFiat, NEAREST_THOUSAND)
            }
        }

        val lowestPrefillRoundedCrypto = lowestPrefillRoundedFiat.convertFiatToCrypto(fiatRate, state)

        return Pair(lowestPrefillRoundedFiat, lowestPrefillRoundedCrypto)
    }

    private fun TxLimits.getSpendableBalanceWithinLimits(amount: Money, currency: Currency): Money {
        val isMaxLimited = isAmountOverMax(amount)
        val isMinLimited = isAmountUnderMin(amount)

        return when {
            isMinLimited && isMaxLimited -> Money.fromMajor(currency, BigDecimal.ZERO)
            isMinLimited -> minAmount
            isMaxLimited -> maxAmount
            else -> amount
        }
    }

    private fun roundToNearest(lastAmount: Money, nearest: Int): Money {
        return Money.fromMajor(
            lastAmount.currency, (nearest * (floor(lastAmount.toFloat() / nearest))).toBigDecimal()
        )
    }

    override fun setVisible(isVisible: Boolean) {
        this.visibleIf { isVisible }
    }

    companion object {
        private const val TWENTY_FIVE_PERCENT = 0.25f
        private const val FIFTY_PERCENT = 0.5f
        private const val SEVENTY_FIVE_PERCENT = 0.75f
        private const val NEAREST_ONE = 1
        private const val NEAREST_TEN = 10
        private const val NEAREST_TWENTY_FIVE = 25
        private const val NEAREST_HUNDRED = 100
        private const val NEAREST_FIVE_HUNDRED = 500
        private const val NEAREST_THOUSAND = 1000
    }
}
