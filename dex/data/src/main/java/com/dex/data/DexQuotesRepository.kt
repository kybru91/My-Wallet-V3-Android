package com.dex.data

import com.blockchain.api.NabuApiException
import com.blockchain.api.dex.DexQuotesApiService
import com.blockchain.api.dex.FromCurrency
import com.blockchain.api.dex.ToCurrency
import com.blockchain.coincore.AssetFilter
import com.blockchain.coincore.Coincore
import com.blockchain.outcome.Outcome
import com.blockchain.outcome.flatMap
import com.blockchain.outcome.map
import com.blockchain.outcome.mapError
import com.blockchain.utils.asFlow
import com.blockchain.utils.awaitOutcome
import com.dex.domain.DexAccount
import com.dex.domain.DexBalanceService
import com.dex.domain.DexQuote
import com.dex.domain.DexQuoteParams
import com.dex.domain.DexQuotesService
import com.dex.domain.DexTxError
import com.dex.domain.OutputAmount
import info.blockchain.balance.AssetCatalogue
import info.blockchain.balance.Currency
import info.blockchain.balance.Money
import java.math.BigInteger
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.web3j.utils.Convert

class DexQuotesRepository(
    private val dexQuotesApiService: DexQuotesApiService,
    private val coincore: Coincore,
    private val assetCatalogue: AssetCatalogue
) : DexQuotesService, DexBalanceService {
    override suspend fun quote(
        dexQuoteParams: DexQuoteParams
    ): Outcome<DexTxError, DexQuote> {
        val address = dexQuoteParams.sourceAccount.receiveAddress()

        val nativeCurrency = dexQuoteParams.sourceAccount.currency.coinNetwork?.nativeAssetTicker?.let {
            assetCatalogue.fromNetworkTicker(it)
        } ?: return Outcome.Failure(
            DexTxError.FatalTxError(IllegalStateException("Unknown native asset ticker"))
        )

        return address.flatMap {
            dexQuotesApiService.quote(
                fromCurrency = FromCurrency(
                    chainId = dexQuoteParams.sourceAccount.chainId,
                    symbol = dexQuoteParams.sourceAccount.currency.networkTicker,
                    address = dexQuoteParams.sourceAccount.contractAddress,
                    amount = dexQuoteParams.amount.toBigInteger().toString()
                ),
                toCurrency = ToCurrency(
                    chainId = dexQuoteParams.destinationAccount.chainId,
                    symbol = dexQuoteParams.destinationAccount.currency.networkTicker,
                    address = dexQuoteParams.destinationAccount.contractAddress,
                ),
                slippage = dexQuoteParams.slippage,
                address = it
            ).map { resp ->
                DexQuote.ExchangeQuote(
                    amount = dexQuoteParams.amount,
                    outputAmount = OutputAmount(
                        expectedOutput = Money.fromMinor(
                            currency = dexQuoteParams.destinationAccount.currency,
                            value = resp.quote.buyAmount.amount.toBigInteger()
                        ),
                        minOutputAmount = Money.fromMinor(
                            currency = dexQuoteParams.destinationAccount.currency,
                            value = resp.quote.buyAmount.minAmount?.toBigInteger()
                                ?: resp.quote.buyAmount.amount.toBigInteger()
                        )
                    ),
                    fees = calculateEstimatedQuoteFee(
                        nativeCurrency,
                        resp.transaction.gasLimit.toBigInteger(),
                        resp.transaction.gasPrice.toBigInteger()
                    )
                )
            }
        }.mapError {
            if (it is NabuApiException) {
                DexTxError.QuoteError(
                    title = it.getServerSideErrorInfo()?.title.orEmpty().plus(" --- ").plus(
                        it.getErrorType()
                    ),
                    message = it.getErrorDescription().plus(" ").plus(" ")
                        .plus(it.getServerSideErrorInfo()?.description.orEmpty())
                )
            } else {
                DexTxError.FatalTxError(it)
            }
        }
    }

    private fun calculateEstimatedQuoteFee(
        nativeCurrency: Currency,
        gasLimit: BigInteger,
        gasPriceWei: BigInteger
    ): Money {

        val gasPriceInGwei = Convert.fromWei(
            gasPriceWei.toBigDecimal(), Convert.Unit.GWEI
        )

        val feeInWei = Convert.toWei(
            gasPriceInGwei.multiply(gasLimit.toBigDecimal()),
            Convert.Unit.GWEI
        ).toBigInteger()

        return Money.fromMinor(
            nativeCurrency,
            feeInWei
        )
    }

    private suspend fun DexAccount.receiveAddress(): Outcome<Exception, String> {
        val nativeAssetTicker = currency.coinNetwork?.nativeAssetTicker ?: currency.networkTicker
        val currency = assetCatalogue.fromNetworkTicker(nativeAssetTicker) ?: return Outcome.Failure(
            IllegalStateException("Unknown currency")
        )
        return coincore[currency].defaultAccount(AssetFilter.NonCustodial).flatMap { acc ->
            acc.receiveAddress.map { receiveAddress ->
                receiveAddress.address
            }
        }.awaitOutcome()
    }

    override suspend fun networkBalance(account: DexAccount): Money {
        val nativeCurrency = account.currency.coinNetwork?.nativeAssetTicker?.let {
            assetCatalogue.fromNetworkTicker(it)
        } ?: throw IllegalArgumentException("Unsupported currency")
        val coincoreAccount = coincore[nativeCurrency].defaultAccount(AssetFilter.NonCustodial).asFlow().first()
        return coincoreAccount.balance().map { it.total }.first()
    }
}
