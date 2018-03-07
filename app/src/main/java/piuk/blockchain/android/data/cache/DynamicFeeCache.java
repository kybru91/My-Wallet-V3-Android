package piuk.blockchain.android.data.cache;

import android.support.annotation.Nullable;

import info.blockchain.wallet.api.data.FeeOptions;

public class DynamicFeeCache {

    private FeeOptions btcFeeOptions;
    private FeeOptions ethFeeOptions;
    private FeeOptions bchFeeOptions;

    public DynamicFeeCache() {
        // No-op
    }

    @Nullable
    public FeeOptions getBtcFeeOptions() {
        return btcFeeOptions;
    }

    public void setBtcFeeOptions(FeeOptions btcFeeOptions) {
        this.btcFeeOptions = btcFeeOptions;
    }

    @Nullable
    public FeeOptions getEthFeeOptions() {
        return ethFeeOptions;
    }

    public void setEthFeeOptions(FeeOptions ethFeeOptions) {
        this.ethFeeOptions = ethFeeOptions;
    }

    @Nullable
    public FeeOptions getBchFeeOptions() {
        return bchFeeOptions;
    }

    public void setBchFeeOptions(FeeOptions bchFeeOptions) {
        this.bchFeeOptions = bchFeeOptions;
    }
}
