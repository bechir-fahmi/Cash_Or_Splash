package ass.cashorsplash.huawei;

import android.app.Application;

import com.huawei.hms.ads.HwAds;

public class Adshuawei extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        HwAds.init(this);
    }
}
