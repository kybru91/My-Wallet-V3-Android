package piuk.blockchain.android.identity

import info.blockchain.balance.AssetInfo
import io.reactivex.Single

interface UserIdentity {
    fun isEligibleFor(feature: Feature): Single<Boolean>
    fun isVerifiedFor(feature: Feature): Single<Boolean>
}

sealed class Feature {
    data class TierLevel(val tier: Tier) : Feature()
    object SimplifiedDueDiligence : Feature()
    data class Interest(val currency: AssetInfo) : Feature()
    object SimpleBuy : Feature()
}

enum class Tier {
    BRONZE, SILVER, GOLD
}