package com.blockchain.analytics.events

import com.blockchain.analytics.AnalyticsEvent

@Deprecated("Analytics events should be defined near point of use")
enum class AnalyticsEvents(
    override val event: String,
    override val params: Map<String, String> = emptyMap()
) : AnalyticsEvent {

    AccountsAndAddresses("accounts_and_addresses"),
    Backup("backup"),
    Dashboard("dashboard"),
    Exchange("exchange"),
    ExchangeCreate("exchange_create"),
    ExchangeDetailConfirm("exchange_detail_confirm"),
    ExchangeDetailOverview("exchange_detail_overview"),
    ExchangeExecutionError("exchange_execution_error"),
    ExchangeHistory("exchange_history"),
    KycEmail("kyc_email"),
    KycAddress("kyc_address"),
    KycComplete("kyc_complete"),
    SwapTiers("swap_tiers"),
    KycTiersLocked("kyc_tiers_locked"),
    KycTier1Complete("kyc_tier1_complete"),
    KycTier2Complete("kyc_tier2_complete"),
    KycCountry("kyc_country"),
    KycProfile("kyc_profile"),
    KycStates("kyc_states"),
    KycVerifyIdentity("kyc_verify_identity"),
    KycWelcome("kyc_welcome"),
    KycResubmission("kyc_resubmission"),
    KycSunriverStart("kyc_sunriver_start"),
    KycBlockstackStart("kyc_blockstack_start"),
    KycSimpleBuyStart("kyc_simple_buy_start"),
    KycFiatFundsStart("kyc_fiat_funds_start"),
    KycInterestStart("kyc_interest_start"),
    KycMoreInfo("kyc_more_info"),
    KycTiers("kyc_tiers"),
    Logout("logout"),
    Settings("settings"),
    Support("support"),
    WebLogin("web_login"),
    SwapErrorDialog("swap_error_dialog"),
    WalletCreation("wallet_creation"),
    WalletManualLogin("wallet_manual_login"),
    PITDEEPLINK("pit_deeplink"),
    WalletAutoPairing("wallet_auto_pairing"),
    ChangeFiatCurrency("currency"),
    OpenAssetsSelector("asset_selector_open"),
    CloseAssetsSelector("asset_selector_open"),
    CameraSystemPermissionApproved("permission_sys_camera_approve"),
    CameraSystemPermissionDeclined("permission_sys_camera_decline"),

    WalletSignupOpen("wallet_signup_open"),
    WalletSignupClickCreate("wallet_signup_create"),
    WalletSignupClickEmail("wallet_signup_email"),
    WalletSignupClickPasswordFirst("wallet_signup_password_first"),
    WalletSignupCreated("wallet_signup_wallet_created"),
    WalletSignupPINFirst("wallet_signup_pin_first"),
    WalletSignupPINSecond("wallet_signup_pin_second"),
    WalletSignupFirstLogIn("wallet_signup_login")
}

fun kycTierStart(tier: Int): AnalyticsEvent = object : AnalyticsEvent {
    override val event: String = "kyc_tier${tier}_start"
    override val params: Map<String, String> = emptyMap()
}

fun networkError(host: String, path: String, message: String): AnalyticsEvent = object : AnalyticsEvent {
    override val event: String
        get() = "network_error"
    override val params: Map<String, String>
        get() = mapOf("host" to host, "message" to message, "path" to path)
}

fun apiError(host: String, path: String, body: String?, requestId: String?, errorCode: Int): AnalyticsEvent =
    object : AnalyticsEvent {
        override val event: String
            get() = "api_error"
        override val params: Map<String, String>
            get() = mapOf(
                "host" to host,
                "body" to body,
                "path" to path,
                "error_code" to errorCode.toString(),
                "request_id" to requestId
            ).mapNotNull { it.value?.let { value -> it.key to value } }.toMap()
    }

enum class AnalyticsNames(val eventName: String) {
    // NEW EVENTS BUY
    BUY_ASSET_SCREEN_VIEWED("Buy Asset Screen Viewed"),
    BUY_ASSET_SELECTED("Buy Asset Selected"),
    BUY_QUICK_FILL_BUTTON_CLICKED("Buy Quick Fill Clicked"),
    BUY_AMOUNT_SCREEN_VIEWED("Buy Amount Screen Viewed"),
    BUY_CHANGE_PAYMENT_METHOD_CLICKED("Buy Change Payment Method Clicked"),
    BUY_PAYMENT_METHOD_CHANGED("Buy Payment Method Changed"),
    BUY_PAYMENT_ADD_NEW_CLICKED("Buy Payment Method Add New Clicked"),
    BUY_AMOUNT_SCREEN_NEXT_CLICKED("Buy Amount Screen Next Clicked"),
    BUY_CHECKOUT_SCREEN_VIEWED("Buy Checkout Screen Viewed"),
    BUY_PRICE_TOOLTIP_CLICKED("Buy Price Tooltip Clicked"),
    BUY_BLOCKCHAIN_COM_FEE_CLICKED("Buy Blockchain Com Fee Clicked"),
    BUY_CHECKOUT_SCREEN_SUBMITTED("Buy Checkout Screen Submitted"),
    BUY_CHECKOUT_SCREEN_BACK_CLICKED("Buy Checkout Screen Back Clicked"),
    BUY_AMOUNT_SCREEN_BACK_CLICKED("Buy Amount Screen Back Clicked"),
    BUY_ASSET_UPSELL_PAGE_VIEWED("Buy Flow Buy Other Crypto Page Viewed"),
    BUY_ASSET_UPSELL_PAGE_DISMISSED("Buy Flow Buy Other Crypto Page Dismissed"),
    BUY_ASSET_UPSELL_MAYBE_LATER_CLICKED("Buy Flow Buy Other Crypto Maybe Later Clicked"),
    BUY_ASSET_UPSELL_MOST_POPULAR_ASSET_CLICKED("Buy Flow Buy Other Crypto Most Popular Asset Clicked"),

    // BUY ENTRY
    FAB_BUY_CLICKED("FAB Buy Clicked"),
    COIN_VIEW_BUY_CLICKED("Coin View Buy Clicked"),
    COIN_VIEW_ACCOUNT_BUY_CLICKED("Coin View Account Buy Clicked"),
    WALLET_BUY_SELL_VIEWED("Wallet Buy Sell Viewed"),

    // NEW EVENTS SELL
    SELL_ASSET_SCREEN_VIEWED("Sell Asset Screen Viewed"),
    SELL_ASSET_SELECTED("Sell Asset Selected"),
    SELL_QUICK_FILL_BUTTON_CLICKED("Sell Quick Fill Clicked"),
    SELL_AMOUNT_SCREEN_VIEWED("Sell Amount Screen Viewed"),
    SELL_FIAT_CRYPTO_SWITCHER_CLICKED("Sell Fiat Crypto Switcher Clicked"),
    SELL_AMOUNT_SCREEN_NEXT_CLICKED("Sell Amount Screen Next Clicked"),
    SELL_CHECKOUT_SCREEN_VIEWED("Sell Checkout Viewed"),
    SELL_PRICE_TOOLTIP_CLICKED("Sell Price Tooltip Clicked"),
    SELL_CHECKOUT_NETWORK_FEES_CLICKED("Sell Checkout Network Fees Clicked"),
    SELL_CHECKOUT_SCREEN_SUBMITTED("Sell Checkout Screen Submitted"),
    SELL_CHECKOUT_SCREEN_BACK_CLICKED("Sell Checkout Screen Back Clicked"),
    SELL_AMOUNT_SCREEN_BACK_CLICKED("Sell Amount Screen Back Clicked"),

    // SELL ENTRY
    FAB_SELL_CLICKED("FAB Sell Clicked"),
    COIN_VIEW_SELL_CLICKED("Coin View Sell Clicked"),
    COIN_VIEW_ACCOUNT_SELL_CLICKED("Coin View Account Sell Clicked"),

    // NEW SWAP EVENTs
    SWAP_FROM_WALLET_PAGE_VIEWED("Swap From Wallet Page Viewed"),
    SWAP_FROM_WALLET_PAGE_CLICKED("Swap From Wallet Page Clicked"),
    SWAP_RECEIVE_WALLET_PAGE_VIEWED("Swap Receive Wallet Page Viewed"),
    SWAP_RECEIVE_WALLET_PAGE_CLICKED("Swap Receive Wallet Page Clicked"),
    SWAP_ACCOUNTS_SELECTED("Swap Accounts Selected"),
    SWAP_QUICK_FILL_CLICKED("Swap Quick Fill Clicked"),
    SWAP_FIAT_CRYPTO_CLICKED("Swap Fiat Crypto Clicked"),
    SWAP_AMOUNT_SCREEN_BACK_CLICKED("Swap Amount Screen Back Clicked"),
    SWAP_AMOUNT_SCREEN_NEXT_CLICKED("Swap Amount Screen Next Clicked"),
    SWAP_CHECKOUT_VIEWED("Swap Checkout Viewed"),
    SWAP_PRICE_TOOLTIP_CLICKED("Swap Price Tooltip Clicked"),
    SWAP_CHECKOUT_NETWORK_FEES_CLICKED("Swap Checkout Network Fees Clicked"),
    SWAP_CHECKOUT_SCREEN_SUBMITTED("Swap Checkout Screen Submitted"),
    SWAP_CHECKOUT_SCREEN_BACK_CLICKED("Swap Checkout Screen Back Clicked"),
    SWAP_STATE_TRIGGERED("Swap State Triggered"),

    // SWAP ENTRY
    FAB_SWAP_CLICKED("FAB Swap Clicked"),
    COIN_VIEW_SWAP_CLICKED("Coin View Swap Clicked"),
    COIN_VIEW_ACCOUNT_SWAP_CLICKED("Coin View Account Swap Clicked"),

    // EXCHANGE AWARENESS CAMPAIGN
    EXCHANGE_AWARENESS_PROMPT_SHOWN("Exchange Awareness Prompt Shown"),
    EXCHANGE_AWARENESS_PROMPT_CLICKED("Exchange Awareness Prompt Clicked"),
    EXCHANGE_AWARENESS_PROMPT_DISMISSED("Exchange Awareness Prompt Dismissed"),

    // App Events
    APP_INSTALLED("Application Installed"),
    APP_UPDATED("Application Updated"),
    APP_DEEP_LINK_OPENED("Deep Link Opened"),
    APP_BACKGROUNDED("Application Backgrounded"),
    BUY_FREQUENCY_SELECTED("Buy Frequency Selected"),
    BUY_SELL_CLICKED("Buy Sell Clicked"),
    BUY_SELL_VIEWED("Buy Sell Viewed"),
    BUY_METHOD_OPTION_VIEWED("Buy Method Option Viewed"),
    DEPOSIT_METHOD_OPTION_VIEWED("Deposit Method Option Viewed"),
    WITHDRAWAL_METHOD_OPTION_VIEWED("Withdrawal Method Option Viewed"),
    SIGNED_IN("Signed In"),
    SIGNED_OUT("Signed Out"),
    SWAP_CLICKED("Swap Clicked"),
    SWAP_RECEIVE_SELECTED("Swap Receive Selected"),
    SWAP_MAX_CLICKED("Swap Amount Max Clicked"),
    AMOUNT_SWITCHED("Amount Switched"),
    SWAP_REQUESTED("Swap Requested"),
    SEND_MAX_CLICKED("Send Amount Max Clicked"),
    SEND_RECEIVE_CLICKED("Send Receive Clicked"),
    SEND_FROM_SELECTED("Send From Selected"),
    SEND_SUBMITTED("Send Submitted"),
    SEND_RECEIVE_VIEWED("Send Receive Viewed"),
    RECEIVE_ACCOUNT_SELECTED("Receive Currency Selected"),
    RECEIVE_ADDRESS_COPIED("Receive Details Copied"),
    WITHDRAWAL_AMOUNT_ENTERED("Withdrawal Amount Entered"),
    WITHDRAWAL_MAX_CLICKED("Withdrawal Amount Max Clicked"),
    WITHDRAWAL_CLICKED("Withdrawal Clicked"),
    WITHDRAWAL_METHOD_SELECTED("Withdrawal Method Selected"),
    LINK_BANK_CONDITIONS_APPROVED("Link Bank Conditions Approved"),
    LINK_BANK_CLICKED("Link Bank Clicked"),
    BANK_SELECTED("Link Bank Selected"),
    SELL_SOURCE_SELECTED("Sell From Selected"),
    SELL_AMOUNT_MAX_CLICKED("Sell Amount Max Clicked"),
    DEPOSIT_CLICKED("Deposit Clicked"),
    DEPOSIT_AMOUNT_ENTERED("Deposit Amount Entered"),
    DEPOSIT_METHOD_SELECTED("Deposit Method Selected"),
    BANK_TRANSFER_VIEWED("Bank Transfer Viewed"),
    BANK_TRANSFER_CLICKED("Bank Transfer Clicked"),
    INTEREST_CLICKED("Interest Clicked"),
    INTEREST_DEPOSIT_AMOUNT_ENTERED("Interest Deposit Amount Entered"),
    INTEREST_DEPOSIT_CLICKED("Interest Deposit Clicked"),
    INTEREST_MAX_CLICKED("Interest Deposit Max Amount Clicked"),
    INTEREST_DEPOSIT_VIEWED("Interest Deposit Viewed"),
    INTEREST_VIEWED("Interest Viewed"),
    INTEREST_WITHDRAWAL_CLICKED("Interest Withdrawal Clicked"),
    INTEREST_WITHDRAWAL_VIEWED("Interest Withdrawal Viewed"),
    STAKING_DEPOSIT_CLICKED("Staking Deposit Clicked"),
    STAKING_WITHDRAWAL_CLICKED("Staking Withdrawal Clicked"),
    ACTIVE_REWARDS_DEPOSIT_CLICKED("Active Rewards Deposit Clicked"),
    ACTIVE_REWARDS_WITHDRAWAL_CLICKED("Active Rewards Withdrawal Clicked"),
    ACCOUNT_PASSWORD_CHANGED("Account Password Changed"),
    CHANGE_PIN_CODE_CLICKED("Change Pin Clicked"),
    CHANGE_EMAIL_CLICKED("Email Change Clicked"),
    BIOMETRICS_OPTION_UPDATED("Biometrics Updated"),
    PIN_CODE_CHANGED("Mobile Pin Code Changed"),
    RECOVERY_PHRASE_SHOWN("Recovery Phrase Shown"),
    TWO_STEP_VERIFICATION_CODE_CLICKED("Two Step Verification Option Clicked"),
    TWO_STEP_VERIFICATION_CODE_SUBMITTED("Verification Code Submitted"),
    UPGRADE_KYC_VERIFICATION_CLICKED("Upgrade Verification Clicked"),
    EMAIL_VERIF_SKIPPED("Email Verification Skipped"),
    REMOVE_CARD_CLICKED("Remove Linked Card Clicked"),
    SETTINGS_HYPERLINK_DESTINATION("Settings Hyperlink Clicked"),
    NOTIFICATION_PREFS_UPDATED("Notification Preferences Updated"),
    LINK_CARD_CLICKED("Link Card Clicked"),
    CHANGE_MOBILE_NUMBER_CLICKED("Change Mobile Number Clicked"),
    EMAIL_VERIFF_REQUESTED("Email Verification Requested"),
    RECURRING_BUY_CANCEL_CLICKED("Cancel Recurring Buy Clicked"),
    RECURRING_BUY_CLICKED("Recurring Buy Clicked"),
    RECURRING_BUY_INFO_VIEWED("Recurring Buy Info Viewed"),
    RECURRING_BUY_LEARN_MORE_CLICKED("Recurring Buy Learn More Clicked"),
    RECURRING_BUY_DETAILS_CLICKED("Recurring Buy Details Clicked"),
    RECURRING_BUY_SUGGESTION_SKIPPED("Recurring Buy Suggestion Skipped"),
    RECURRING_BUY_VIEWED("Recurring Buy Viewed"),
    RECURRING_BUY_UNAVAILABLE_SHOWN("Recurring Buy Unavailable Shown"),
    WALLET_SIGN_UP("Wallet Signed Up"),
    WALLET_SIGN_UP_COUNTRY_SELECTED("Sign Up Country Selected"),
    WALLET_SIGN_UP_STATE_SELECTED("Sign Up Country State Selected"),
    LOGIN_DEVICE_VERIFIED("Device Verified"),
    LOGIN_CTA_CLICKED("Login Clicked"),
    LOGIN_HELP_CLICKED("Login Help Clicked"),
    LOGIN_ID_ENTERED("Login Identifier Entered"),
    LOGIN_ID_FAILED("Login Identifier Failed"),
    LOGIN_LEARN_MORE_CLICKED("Login Learn More Clicked"),
    LOGIN_METHOD_SELECTED("Login Method Selected"),
    LOGIN_PASSWORD_DENIED("Login Password Denied"),
    LOGIN_PASSWORD_ENTERED("Login Password Entered"),
    LOGIN_REQUEST_APPROVED("Login Request Approved"),
    LOGIN_REQUEST_DENIED("Login Request Denied"),
    LOGIN_2FA_DENIED("Login Two Step Verification Denied"),
    LOGIN_2FA_ENTERED("Login Two Step Verification Entered"),
    LOGIN_VIEWED("Login Viewed"),
    LOGIN_FAILED("Login Request Failed"),
    LOGIN_EMAIL_FAILED("Login Identifier Failed"),
    RECOVERY_PASSWORD_RESET("Account Password Reset"),
    RECOVERY_FAILED("Account Recovery Failed"),
    RECOVERY_CLOUD_BACKUP_SCANNED("Cloud Backup Code Scanned"),
    RECOVERY_NEW_PASSWORD("New Account Password Entered"),
    RECOVERY_OPTION_SELECTED("Recovery Option Selected"),
    RECOVERY_MNEMONIC_ENTERED("Recovery Phrase Entered"),
    RECOVERY_RESET_CANCELLED("Reset Account Cancelled"),
    RECOVERY_RESET_CLICKED("Reset Account Clicked"),
    LANDING_CTA_LOGIN_CLICKED("Login Clicked"),
    LANDING_CTA_SIGNUP_CLICKED("Sign Up Clicked"),
    DASHBOARD_ONBOARDING_VIEWED("Peeksheet Viewed"),
    DASHBOARD_ONBOARDING_DISMISSED("Peeksheet Dismissed"),
    DASHBOARD_ONBOARDING_CARD_CLICKED("Peeksheet Process Clicked"),
    DASHBOARD_ONBOARDING_STEP_LAUNCHED("Peeksheet Selection Clicked"),
    CURRENCY_SELECTION_TRADING_CURRENCY_CHANGED("Fiat Currency Selected"),
    CAMERA_PERMISSION_CHECKED("Camera Permission Checked"),
    CAMERA_PERMISSION_REQUESTED("Camera Permission Requested Actioned"),
    CONNECTED_DAPP_ACTIONED("Connected Dapp Actioned"),
    CONNECTED_DAPP_CLICKED("Connected Dapp Clicked"),
    CONNECTED_DAPPS_LIST_CLICKED("Connected Dapps List Clicked"),
    CONNECTED_DAPPS_LIST_VIEWED("Connected Dapps List Viewed"),
    DAPP_CONNECTION_ACTIONED("Dapp Connection Actioned"),
    DAPP_CONNECTION_CONFIRMED("Dapp Connection Confirmed"),
    DAPP_CONNECTION_REJECTED("Dapp Connection Rejected"),
    DAPP_REQUEST_ACTIONED("Dapp Request Actioned"),
    QR_CODE_CLICKED("Qr Code Clicked"),
    QR_CODE_SCANNED("Qr Code Scanned"),
    TERMS_CONDITIONS_VIEWED("T&C Viewed"),
    VERIFICATION_SUBMISSION_FAILED("Verification Submission Failed"),
    TERMS_CONDITIONS_ACCEPTED("T&C Accepted"),
    COINVIEW_REWARDS_WITHDRAW_ADD_CLICKED("Rewards Withdraw Add Clicked"),
    COINVIEW_WALLETS_ACCOUNTS_VIEWED("Wallets Accounts Viewed"),
    COINVIEW_WALLETS_ACCOUNTS_CLICKED("Wallets Accounts Clicked"),
    COINVIEW_TRANSACTION_CLICKED("Transaction Type Clicked"),
    COINVIEW_SEND_RECEIVE_CLICKED("Send Receive Clicked"),
    COINVIEW_BUY_RECEIVE_CLICKED("Buy Receive Clicked"),
    COINVIEW_CHART_INTERVAL_SELECTED("Chart Time Interval Selected"),
    COINVIEW_CHART_ENGAGED("Chart Engaged"),
    COINVIEW_CHART_DISENGAGED("Chart Disengaged"),
    COINVIEW_PAST_TRANSACTION_CLICKED("Past Transaction Clicked"),
    COINVIEW_HYPERLINK_CLICKED("Hyperlink Clicked"),
    COINVIEW_EXPLAINER_ACCEPTED("Explainer Accepted"),
    COINVIEW_EXPLAINER_VIEWED("Explainer Viewed"),
    COINVIEW_CONNECT_EXCHANGE_ACTIONED("Connect To The Exchange Actioned"),
    COINVIEW_COINVIEW_OPEN("Coin View Open"),
    COINVIEW_COINVIEW_CLOSE("Coin View Closed"),
    COINVIEW_REMOVED_FROM_WATCHLIST("Coin Removed From Watchlist"),
    COINVIEW_ADDED_WATCHLIST("Coin Added To Watchlist"),
    TX_INFO_KYC_UPSELL_CLICKED("Get More Access When You Verify Clicked"),
    TX_INFO_KYC_UPSELL_DISMISSED("Get More Access When You Verify Dismissed"),
    KYC_MORE_INFO_VIEWED("Pre Verification Viewed"),
    KYC_MORE_INFO_CTA_CLICKED("Pre Verification CTA Clicked"),
    KYC_MORE_INFO_DISMISSED("Pre Verification Dismissed"),
    KYC_UPGRADE_NOW_VIEWED("Trading Limits Viewed"),
    KYC_UPGRADE_NOW_GET_BASIC_CLICKED("Trading Limits Get Basic CTA Clicked"),
    KYC_UPGRADE_NOW_GET_VERIFIED_CLICKED("Trading Limits Get Verified CTA Clicked"),
    KYC_UPGRADE_NOW_DISMISSED("Trading Limits Dismissed"),
    KYC_QUESTIONNAIRE_VIEWED("Account Info Screen Viewed"),
    KYC_QUESTIONNAIRE_SUBMITTED("Account Info Submitted"),
    PUSH_NOTIFICATION_RECEIVED("Push Notification Received"),
    PUSH_NOTIFICATION_TAPPED("Push Notification Tapped"),
    PUSH_NOTIFICATION_MISSING_DATA("Push Notification Missing Data"),
    CUSTOMER_SUPPORT_CLICKED("Customer Support Clicked"),
    CUSTOMER_SUPPORT_CONTACT_US_CLICKED("Contact Us Clicked"),
    CUSTOMER_SUPPORT_FAQ_CLICKED("View FAQs Clicked"),
    CLIENT_ERROR("Client Error"),
    NOTIFICATION_CLICKED("Notification Clicked"),
    NOTIFICATION_VIEWED("Notification Viewed"),
    NOTIFICATION_PREFERENCES_CLICKED("Notification Preferences Clicked"),
    NOTIFICATION_PREFERENCES_VIEWED("Notification Preferences Viewed"),
    NOTIFICATIONS_CLOSED("Notifications Closed"),
    NOTIFICATION_NEWS_SET_UP("News Set Up"),
    NOTIFICATION_PRICE_ALERTS_SET_UP("Price Alerts Set Up"),
    NOTIFICATION_SECURITY_ALERTS_SET_UP("Security Alerts Set Up"),
    NOTIFICATION_WALLET_ACTIVITY_SET_UP("Wallet Activity Set Up"),
    NOTIFICATION_STATUS_CHANGE_ERROR("Status Change Error"),
    WALLET_ACTIVITY_VIEWED("Wallet Activity Viewed"),

    WALLET_FAB_VIEWED("Wallet FAB Viewed"),
    WALLET_HOME_VIEWED("Wallet Home Viewed"),
    WALLET_PRICES_VIEWED("Wallet Prices Viewed"),
    WALLET_REWARDS_VIEWED("Wallet Rewards Viewed"),
    WALLET_NFT_VIEWED("Wallet NFT Viewed"),
    REFERRAL_PROGRAM_CLICKED("Wallet Referral Program Clicked"),
    REFERRAL_VIEW_REFERRAL("View Referrals Page"),
    REFERRAL_CODE_FILLED("Referral Code Filled"),
    REFERRAL_SHARE_CODE("Share Referrals Code"),
    REFERRAL_COPY_CODE("Referral Code Copied"),
    COWBOYS_VERIFY_EMAIL_ANNOUNCEMENT_CLICKED("Cowboys Verify Email Announcement Clicked"),
    COWBOYS_WELCOME_INTERSTITIAL_VIEWED("Cowboys Welcome Interstitial Viewed"),
    COWBOYS_WELCOME_INTERSTITIAL_CONTINUE_CLICKED("Cowboys Welcome Interstitial Continue Clicked"),
    COWBOYS_WELCOME_INTERSTITIAL_CLOSED("Cowboys Welcome Interstitial Closed"),
    COWBOYS_COMPLETE_SIGNUP_ANNOUNCEMENT_CLICKED("Cowboys Complete Sign-Up Annoucement Clicked"),
    COWBOYS_KYC_PERSONAL_INFO_VIEWED("Cowboys Personal Info Viewed"),
    COWBOYS_KYC_ADDRESS_VIEWED("Cowboys Address Viewed"),
    COWBOYS_KYC_PERSONAL_INFO_CONFIRMED("Cowboys Personal Info Confirmed"),
    COWBOYS_KYC_ADDRESS_CONFIRMED("Cowboys Address Confirmed"),
    COWBOYS_RAFFLE_INTERSTITIAL_VIEWED("Cowboys Raffle Interstitial Viewed"),
    COWBOYS_RAFFLE_INTERSTITIAL_CLOSED("Cowboys Raffle Interstitial Closed"),
    COWBOYS_RAFFLE_INTERSTITIAL_BUY_CLICKED("Cowboys Raffle Interstitial Buy Crypto Clicked"),
    COWBOYS_VERIFY_ANNOUNCEMENT_CLICKED("Cowboys Verify Identity Announcement Clicked"),
    COWBOYS_KYC_IN_PROGRESS_ANNOUNCEMENT_CLICKED("Cowboys Kyc In Progress Announcement Clicked"),
    COWBOYS_VERIFY_INTERSTITIAL_VIEWED("Cowboys Verify Identity Interstitial Viewed"),
    COWBOYS_VERIFY_INTERSTITIAL_CLOSED("Cowboys Verify Identity Interstitial Closed"),
    COWBOYS_VERIFY_INTERSTITIAL_CTA_CLICKED("Cowboys Verify Identity Interstitial Verify ID Clicked"),
    COWBOYS_REFER_FRIEND_ANNOUNCEMENT_CLICKED("Cowboys Refer Friends Announcement Clicked"),
    ENABLE_DEFI_CLICKED("Enable DeFi Clicked"),

    // superapp
    SUPERAPP_MODE_CUSTODIAL_CLICKED("BCDC Account App Mode Clicked"),
    SUPERAPP_MODE_NON_CUSTODIAL_CLICKED("DeFi App Mode Clicked"),
    SUPERAPP_EMPTY_BUY_BTC_CLICKED("Wallet Dashboard Empty State Buy Bitcoin Clicked"),
    SUPERAPP_EMPTY_BUY_OTHER_CLICKED("Wallet Dashboard Empty State Buy Different Clicked"),
    SUPERAPP_EMPTY_RECEIVE_CLICKED("Wallet Dashboard Empty State Deposit In DeFi Wallet Clicked"),
    SUPERAPP_QUICK_ACTION_BUY_CLICKED("Wallet Dashboard Buy QA Clicked"),
    SUPERAPP_QUICK_ACTION_SWAP_CLICKED("Wallet Dashboard Swap QA Clicked"),
    SUPERAPP_QUICK_ACTION_SELL_CLICKED("Wallet Dashboard Sell QA Clicked"),
    SUPERAPP_QUICK_ACTION_RECEIVE_CLICKED("Wallet Dashboard Receive QA Clicked"),
    SUPERAPP_QUICK_ACTION_SEND_CLICKED("Wallet Dashboard Send QA Clicked"),
    SUPERAPP_QUICK_ACTION_ADD_CASH_CLICKED("Wallet Dashboard Add Cash QA Clicked"),
    SUPERAPP_QUICK_ACTION_CASH_OUT_CLICKED("Wallet Dashboard Cash Out QA Clicked"),
    SUPERAPP_MODE_CUSTODIAL_LONG_CLICK("BCDC Account App Mode Long Pressed"),
    SUPERAPP_MODE_NON_CUSTODIAL_LONG_CLICK("DeFi App Mode Long Pressed"),
    SUPERAPP_MODE_CUSTODIAL_VIEWED("BCDC Account App Mode Viewed"),
    SUPERAPP_MODE_NON_CUSTODIAL_VIEWED("DeFi App Mode Viewed"),
    SUPERAPP_ASSETS_SEE_ALL_CLICKED("Wallet Dashboard Assets See All Clicked"),
    SUPERAPP_CRYPTO_ASSET_CLICKED("Wallet Dashboard Assets Balance Clicked"),
    SUPERAPP_FIAT_ASSET_CLICKED("Wallet Dashboard Fiat Balance Clicked"),
    SUPERAPP_FIAT_ADD_CASH_CLICKED("Wallet Fiat Detail Add Cash Shortcut Clicked"),
    SUPERAPP_FIAT_CASH_OUT_CLICKED("Wallet Fiat Detail Cash Out Shortcut Clicked"),
    SUPERAPP_EARN_GET_STARTED_CLICKED("Wallet Dashboard Earn Get Started Clicked"),
    SUPERAPP_EARN_MANAGE_CLICKED("Wallet Dashboard Earn Manage Clicked"),
    SUPERAPP_EARN_DISCOVER_CLICKED("Wallet Earn Discover Clicked"),
    SUPERAPP_EARN_LEARN_MORE_CLICKED("Wallet Earn Discover Learn More Clicked"),
    SUPERAPP_EARN_ASSET_CLICKED("Wallet Dashboard Earn Asset Clicked"),
    SUPERAPP_EARN_DETAIL_WITHDRAW_CLICKED("Wallet Earn Asset Detail Withdraw Clicked"),
    SUPERAPP_EARN_DETAIL_ADD_CLICKED("Wallet Earn Asset Detail Add Clicked"),
    SUPERAPP_ACTIVITY_SEE_ALL_CLICKED("Wallet Dashboard Activity See All Clicked"),
    SUPERAPP_SUPPORT_CLICKED("Wallet Dashboard Need Help Support Center Clicked"),
    SUPERAPP_DEFI_ONBOARDING_VIEWED("Introducing DeFi Wallet Viewed"),
    SUPERAPP_DEFI_ONBOARDING_CONTINUE_CLICKED("Go To DeFi Wallet Clicked"),
    SUPERAPP_DEFI_BACKUP_SKIP_CLICKED("Back Up Flow Skipped"),
    SUPERAPP_DEFI_BACKUP_NOW_CLICKED("Back Up Flow Back Up Now Clicked"),
    SUPERAPP_DEFI_BACKUP_TO_CLOUD_CLICKED("Back Up Flow Back Up To Cloud Clicked"),
    SUPERAPP_DEFI_BACKUP_MANUALLY_CLICKED("Back Up Flow Backup Manually Clicked"),
    SUPERAPP_DEFI_BACKUP_SUCCESSFUL_VIEWED("Back Up Flow DeFi Wallet Back Up Successful Viewed"),
    SUPERAPP_DEFI_DCA_LEARN_MORE_CLICKED("Wallet Coinview X Sell Learn More Clicked"),
    TOP_MOVER_DASHBOARD_CLICKED("Wallet Dashboard top mover card clicked"),
    TOP_MOVER_PRICES_CLICKED("Prices Tab Top Mover Card Clicked"),
    TOP_MOVER_BUY_CLICKED("Buy List Top Mover Card Clicked"),

    // recurring buys
    RB_COINVIEW_CTA_CLICKED("Wallet Coinview Recurring Buy CTA clicked"),
    RB_HOME_CTA_CLICKED("Wallet Home Recurring Buy CTA clicked"),
    RB_ONBOARDING_VIEWED("Wallet Recurring Buy Onboarding viewed"),
    RB_HOME_DETAIL_CLICKED("Wallet Home Recurring Buy Detail Clicked"),
    RB_DASHBOARD_DETAIL_CLICKED("Wallet Recurring Buys Dashboard Detail Clicked"),
    RB_HOME_MANAGE_CLICKED("Wallet Home Recurring Buy Manage Clicked"),
    RB_DASHBOARD_ADD_CLICKED("Wallet Recurring Buys Dashboard CTA Clicked"),
    RB_DETAIL_VIEWED("Wallet Recurring Buys Detail Viewed"),
    RB_DETAIL_CANCEL_CLICKED("Wallet Recurring Buys Detail Cancel"),
    RB_BUY_TOGGLE_CLICKED("Wallet Buys Confirmation Screen Recurring Buy Toggle Clicked"),
    RB_BUY_FREQUENCY_CLICKED("Wallet Buys Recurring Frequency Selection Clicked"),
    RB_BUY_FREQUENCY_VIEWED("Wallet Buys Recurring Frequency Selection Viewed"),

    // new swap
    SWAP_ENTER_AMOUNT_VIEWED("Wallet Swap Amount Screen Viewed"),
    SWAP_ENTER_AMOUNT_MAX_CLICKED("Wallet Swap Amount Screen Max Clicked"),
    SWAP_ENTER_AMOUNT_PREVIEW_CLICKED("Wallet Swap Amount Screen Preview Swap Clicked"),
    SWAP_ENTER_AMOUNT_SOURCE_CLICKED("Wallet Swap Amount Screen Change Source Clicked"),
    SWAP_ENTER_AMOUNT_TARGET_CLICKED("Wallet Swap Amount Screen Change Target Clicked"),
    SWAP_SOURCE_SELECTED("Wallet Swap Source List Token Selected"),
    SWAP_TARGET_SELECTED("Wallet Swap Target List Token Selected"),
    SWAP_CONFIRMATION_VIEWED("Wallet Swap Confirmation Screen Viewed"),
    SWAP_CONFIRMATION_SWAP_CLICKED("Wallet Swap Confirmation Screen Confirm Swap Clicked"),
    SWAP_CONFIRMATION_PENDING_VIEWED("Wallet Swap Post Confirmation Pending Viewed"),
    SWAP_CONFIRMATION_SUCCESS_VIEWED("Wallet Swap Post Confirmation Success Viewed"),

    // dex
    DEX_ONBOARDING_VIEWED("DEX Onboarding Viewed"),
    DEX_SWAP_AMOUNT_ENTERED("DEX Swap Amount Entered"),
    DEX_COUNTRY_INELIGIBLE_VIEWED("DEX Country Ineligible Viewed"),
    DEX_SWAP_SELECT_SOURCE_OPENED("DEX Swap Input Opened"),
    DEX_SWAP_SELECT_DESTINATION_OPENED("DEX Swap Output Opened"),
    DEX_SWAP_DESTINATION_NOT_FOUND("DEX Swap Output Not Found"),
    DEX_SWAP_OUTPUT_SELECTED("DEX Swap Output Selected"),
    DEX_SWAP_APPROVE_TOKEN_CLICKED("DEX Swap Approve token clicked"),
    DEX_SWAP_APPROVE_TOKEN_CONFIRMED("DEX Swap Approve token confirmed"),
    DEX_SWAP_PREVIEW_VIEWED("DEX Swap Preview Viewed"),
    DEX_SWAP_CONFIRM_CLICKED("DEX Swap Confirm Clicked"),
    DEX_SWAP_IN_PROGRESS_VIEWED("DEX Swapping Viewed"),
    DEX_SWAP_EXECUTED_VIEWED("DEX Swap Executed Viewed"),
    DEX_SWAP_FAILED_VIEWED("DEX Swap Failed Viewed"),
    DEX_SETTINGS_OPENED("DEX Settings opened"),
    DEX_SLIPPAGE_CHANGED("DEX Slippage changed"),

    ;
}

enum class LaunchOrigin {
    NAVIGATION,
    SEND,
    SWAP,
    AIRDROP,
    RESUBMISSION,
    SIMPLETRADE,
    DASHBOARD_PROMO,
    DASHBOARD,
    TRANSACTION_LIST,
    TRANSACTION_DETAILS,
    DEPOSIT,
    BUY,
    WITHDRAW,
    CURRENCY_PAGE,
    DEEPLINK,
    NOTIFICATION,
    SAVINGS,
    FIAT_FUNDS,
    SIGN_UP,
    SETTINGS,
    SAVINGS_PAGE,
    VERIFICATION,
    FAB,
    PRICES,
    HOME,
    DCA_DETAILS_LINK,
    BUY_CONFIRMATION,
    RECURRING_BUY_DETAILS,
    RECURRING_BUY,
    APPS_LIST,
    QR_CODE,
    LAUNCH_SCREEN,
    COIN_VIEW,
    NUX_LAUNCH_PROMO_LOG_IN,
    NUX_LAUNCH_PROMO_BUY_CRYPTO;
}
