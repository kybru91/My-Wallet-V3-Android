package piuk.blockchain.android.coincore.erc20

import com.blockchain.annotations.CommonCode
import com.blockchain.featureflags.InternalFeatureFlagApi
import com.blockchain.logging.CrashLogger
import com.blockchain.preferences.CurrencyPrefs
import com.blockchain.nabu.datamanagers.CustodialWalletManager
import com.blockchain.preferences.WalletStatus
import com.blockchain.wallet.DefaultLabels
import info.blockchain.balance.AssetInfo
import info.blockchain.balance.isCustodialOnly
import info.blockchain.wallet.util.FormatsUtil
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import piuk.blockchain.android.coincore.AddressParseError
import piuk.blockchain.android.coincore.ReceiveAddress
import piuk.blockchain.android.coincore.SingleAccountList
import piuk.blockchain.android.coincore.impl.CryptoAssetBase
import piuk.blockchain.android.identity.UserIdentity
import piuk.blockchain.android.thepit.PitLinking
import piuk.blockchain.androidcore.data.ethereum.EthDataManager
import piuk.blockchain.androidcore.data.exchangerate.ExchangeRateDataManager
import piuk.blockchain.androidcore.data.exchangerate.ExchangeRateService
import piuk.blockchain.androidcore.data.fees.FeeDataManager
import piuk.blockchain.androidcore.data.payload.PayloadDataManager

internal class Erc20Asset(
    override val asset: AssetInfo,
    payloadManager: PayloadDataManager,
    private val ethDataManager: EthDataManager,
    private val feeDataManager: FeeDataManager,
    private val walletPreferences: WalletStatus,
    custodialManager: CustodialWalletManager,
    exchangeRates: ExchangeRateDataManager,
    historicRates: ExchangeRateService,
    currencyPrefs: CurrencyPrefs,
    labels: DefaultLabels,
    pitLinking: PitLinking,
    crashLogger: CrashLogger,
    identity: UserIdentity,
    features: InternalFeatureFlagApi
) : CryptoAssetBase(
    payloadManager,
    exchangeRates,
    historicRates,
    currencyPrefs,
    labels,
    custodialManager,
    pitLinking,
    crashLogger,
    identity,
    features
) {
    override val isCustodialOnly: Boolean = asset.isCustodialOnly
    override val multiWallet: Boolean = false

    override fun initToken(): Completable =
        ethDataManager.fetchErc20DataModel(asset)
                .ignoreElements()

    override fun loadNonCustodialAccounts(labels: DefaultLabels): Single<SingleAccountList> =
        Single.just(getNonCustodialAccount())
            .map { listOf(it) }

    private fun getNonCustodialAccount(): Erc20NonCustodialAccount {
        val erc20Address = ethDataManager.getEthWallet()?.account?.address
            ?: throw Exception("No ${asset.ticker} wallet found")

        return Erc20NonCustodialAccount(
            payloadManager,
            asset,
            ethDataManager,
            erc20Address,
            feeDataManager,
            labels.getDefaultNonCustodialWalletLabel(),
            exchangeRates,
            walletPreferences,
            custodialManager,
            identity
        )
    }

    @CommonCode("Exists in EthAsset")
    override fun parseAddress(address: String, label: String?): Maybe<ReceiveAddress> =
        Single.just(isValidAddress(address)).flatMapMaybe { isValid ->
            if (isValid) {
                ethDataManager.isContractAddress(address)
                    .flatMapMaybe { isContract ->
                        if (isContract) {
                            throw AddressParseError(AddressParseError.Error.ETH_UNEXPECTED_CONTRACT_ADDRESS)
                        } else {
                            Maybe.just(Erc20Address(asset, address, label ?: address))
                        }
                    }
            } else {
                Maybe.empty()
            }
        }

    override fun isValidAddress(address: String): Boolean =
        FormatsUtil.isValidEthereumAddress(address)
}
