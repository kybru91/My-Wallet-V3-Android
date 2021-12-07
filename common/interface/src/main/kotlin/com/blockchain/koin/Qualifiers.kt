package com.blockchain.koin

import org.koin.core.qualifier.StringQualifier
import org.koin.core.qualifier.named

val unifiedSignInFeatureFlag = StringQualifier("ff_unified_sign_in")
val walletRedesignFeatureFlag = StringQualifier("ff_wallet_redesign")
val pricingQuoteFeatureFlag = StringQualifier("ff_pricing_quote")
val stripeAndCheckoutPaymentsFeatureFlag = StringQualifier("ff_stripe_checkout_payments")
val ssoSignInPolling = StringQualifier("ff_sso_polling")
val buyCryptoDashboardButton = StringQualifier("ff_dashboard_buy_crypto")
val nabu = StringQualifier("nabu")
val status = StringQualifier("status")
val kotlinApiRetrofit = StringQualifier("kotlin-api")
val explorerRetrofit = StringQualifier("explorer")
val everypayRetrofit = StringQualifier("everypay")
val apiRetrofit = StringQualifier("api")
val moshiExplorerRetrofit = StringQualifier("moshi_explorer")
val gbp = StringQualifier("GBP")
val usd = StringQualifier("USD")
val eur = StringQualifier("EUR")
val priorityFee = StringQualifier("Priority")
val regularFee = StringQualifier("Regular")
val bigDecimal = StringQualifier("BigDecimal")
val bigInteger = StringQualifier("BigInteger")
val interestLimits = StringQualifier("InterestLimits")
val kyc = StringQualifier("kyc")
val uniqueId = StringQualifier("unique_id")
val uniqueUserAnalytics = StringQualifier("unique_user_analytics")
val userAnalytics = StringQualifier("user_analytics")
val walletAnalytics = StringQualifier("wallet_analytics")
val payloadScopeQualifier = named("Payload")
val ioDispatcher = named("io_dispatcher")
