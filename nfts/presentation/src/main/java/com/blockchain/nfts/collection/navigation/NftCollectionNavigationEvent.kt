package com.blockchain.nfts.collection.navigation

import com.blockchain.coincore.CryptoAccount
import com.blockchain.commonarch.presentation.mvi_v2.NavigationEvent

sealed interface NftCollectionNavigationEvent : NavigationEvent {
    /**
     * Currently opens in OpenSea
     */
    data class ShopExternal(val url: String) : NftCollectionNavigationEvent

    data class ShowReceiveAddress(val account: CryptoAccount) : NftCollectionNavigationEvent

    object ShowHelp : NftCollectionNavigationEvent

    data class ShowDetail(
        val nftId: String,
        val pageKey: String?,
        val address: String
    ) : NftCollectionNavigationEvent
}
