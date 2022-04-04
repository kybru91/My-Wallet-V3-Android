package com.blockchain.koin

import org.koin.core.qualifier.StringQualifier
import org.koin.core.qualifier.named

val featureFlagsPrefs = StringQualifier("FeatureFlagsPrefs")
val redesignPart2CoinViewFeatureFlag = StringQualifier("ff_redesign_part_2coinview")
val uiTourFeatureFlag = StringQualifier("ff_ui_tour")
val googlePayFeatureFlag = StringQualifier("ff_gpay")
val ethMemoHotWalletFeatureFlag = StringQualifier("ff_eth_memo")
val intercomChatFeatureFlag = StringQualifier("ff_intercom_chat")
val blockchainCardFeatureFlag = StringQualifier("ff_blockchain_card")
val enableKotlinSerializerFeatureFlag = StringQualifier("ff_kotlin_serializer")
val disableMoshiSerializerFeatureFlag = StringQualifier("ff_disable_moshi")
val termsAndConditionsFeatureFlag = StringQualifier("ff_terms_and_conditions")
val entitySwitchSilverEligibilityFeatureFlag = StringQualifier("ff_entity_switch_silver_eligibility")
val deeplinkingFeatureFlag = StringQualifier("ff_deeplinking")
val kycAdditionalInfoFeatureFlag = StringQualifier("ff_kyc_additional_info")
val sendToDomainsAnnouncementFeatureFlag = StringQualifier("ff_send_domain_announcement")
val embraceFeatureFlag = StringQualifier("ff_embrace")
val nabu = StringQualifier("nabu")
val status = StringQualifier("status")
val kotlinApiRetrofit = StringQualifier("kotlin-api")
val explorerRetrofit = StringQualifier("explorer")
val everypayRetrofit = StringQualifier("everypay")
val apiRetrofit = StringQualifier("api")
val kotlinXApiRetrofit = StringQualifier("kotlinx-api")
val moshiExplorerRetrofit = StringQualifier("moshi_explorer")
val gbp = StringQualifier("GBP")
val usd = StringQualifier("USD")
val eur = StringQualifier("EUR")
val priorityFee = StringQualifier("Priority")
val regularFee = StringQualifier("Regular")
val bigDecimal = StringQualifier("BigDecimal")
val kotlinJsonConverterFactory = StringQualifier("KotlinJsonConverterFactory")
val bigInteger = StringQualifier("BigInteger")
val interestLimits = StringQualifier("InterestLimits")
val kyc = StringQualifier("kyc")
val uniqueId = StringQualifier("unique_id")
val uniqueUserAnalytics = StringQualifier("unique_user_analytics")
val userAnalytics = StringQualifier("user_analytics")
val walletAnalytics = StringQualifier("wallet_analytics")
val payloadScopeQualifier = named("Payload")
val ioDispatcher = named("io_dispatcher")
