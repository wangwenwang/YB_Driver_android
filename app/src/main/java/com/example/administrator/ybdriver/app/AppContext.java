package com.example.administrator.ybdriver.app;


import android.content.Context;
import android.content.Intent;

import com.baidu.mapapi.SDKInitializer;
import com.example.administrator.ybdriver.bean.Business;
import com.example.administrator.ybdriver.bean.User;
import com.example.administrator.ybdriver.servers.DaemonReceiver;
import com.example.administrator.ybdriver.servers.DaemonService;
import com.example.administrator.ybdriver.servers.TrackingReceiver;
import com.example.administrator.ybdriver.servers.TrackingService;
import com.example.administrator.ybdriver.utils.SharedPreferencesUtils;
import com.kaidongyuan.app.basemodule.utils.nomalutils.DensityUtil;

import com.kaidongyuan.app.basemodule.widget.MLog;
import com.marswin89.marsdaemon.DaemonApplication;
import com.marswin89.marsdaemon.DaemonConfigurations;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;


import okhttp3.OkHttpClient;



public class AppContext extends DaemonApplication {

    private static AppContext appContext;
    //	private String tempcoor = "bd09ll";//百度地图的编码模式
//	private LocationMode tempMode = LocationMode.Hight_Accuracy;//定位模式 选中高性能定位
    private final String prefName = "configs";//程序的SharedPreferences名称

    private User user;//用于存储用户信息
    private Business business; // 用于存储用户的业务信息

    private String down_url;//存储下载路径
    private String new_version;//存储最新版本

    public static boolean IS_LOGIN = false;//登录状态是否改变
    private OkHttpClient okHttpClient;
    public OkHttpUtils okHttpUtils;
    final Intent intent = new Intent();


    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
        intent.setAction("com.kaidongyuan.app.kdydriver.TrackingService");
        intent.setPackage(AppContext.getInstance().getPackageName());
//        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
//        Display display = wm.getDefaultDisplay();
//        MLog.i("屏幕尺寸: 宽度 = " + display.getWidth() + "高度 = :" + display.getHeight());
        //bugHD
//        FIR.init(this);
       // 百度地图  SDK初始化功能
      SDKInitializer.initialize(this);
//		initLocationClient();
       // initImageLoader();
        DensityUtil.setContext(getBaseContext());
        //okhttpclient 初始化设置连接超时15s,读取超时20s,写入超时20s
        createOkhttpUtils(15*1000L,20*1000L,20*1000L);
        MLog.i("appContext  onCreate()");

    }
/**
 *@auther: Tom
 *created at 2016/6/6 11:15
 * 初始化OkHttpUtils对象
 */
    private OkHttpUtils createOkhttpUtils(long connectTimeOut,long readTimeOut,long writeTimeOuts) {
        okHttpClient = new OkHttpClient.Builder().connectTimeout(connectTimeOut, TimeUnit.MILLISECONDS).readTimeout(readTimeOut,TimeUnit.MILLISECONDS).writeTimeout(writeTimeOuts,TimeUnit.MILLISECONDS).build();
        okHttpUtils = OkHttpUtils.initClient(okHttpClient);
        return okHttpUtils;
    }


    public static AppContext getInstance() {
        if (appContext == null) {
            appContext = new AppContext();
        }
        return appContext;
    }

    /**
     * 定位初始化
     */
    /*public void initLocationClient() {
		LocationClientOption option = new LocationClientOption();
		// option.setCoorType("bd09ll"); // 设置坐标类型
		option.setCoorType(tempcoor);// 返回的定位结果是百度经纬度，默认值gcj02
		option.setLocationMode(tempMode);// 设置定位模式
		// 设置发起定位请求的间隔时间
		option.setScanSpan(Constants.scanSpan);
		option.setOpenGps(true);
		option.setIsNeedAddress(true);
	}*/

    @Override
    public void attachBaseContextByDaemon(Context base) {
        super.attachBaseContextByDaemon(base);
    }

    /**
     *管理注册双进程守护功能
     *created at 2016/7/1 14:16
     *
     */
    @Override
    protected DaemonConfigurations getDaemonConfigurations() {
        DaemonConfigurations.DaemonConfiguration daemonConfiguration1=new DaemonConfigurations.DaemonConfiguration(
                "com.example.administrator.ybdriver:process1",
                TrackingService.class.getCanonicalName(),
                TrackingReceiver.class.getCanonicalName()
        );
        DaemonConfigurations.DaemonConfiguration daemonConfiguration2=new DaemonConfigurations.DaemonConfiguration(
                "com.example.administrator.ybdriver:push",
                DaemonService.class.getCanonicalName(),
                DaemonReceiver.class.getCanonicalName()
        );
        DaemonConfigurations.DaemonListener listener = new MyDaemonListener();

        return new DaemonConfigurations(daemonConfiguration1,daemonConfiguration2,listener);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }

    public void clearUser() {
        user = null;
       SharedPreferencesUtils.ClearSharedPreferences("account");
    }


    public void setIdentity(long id) {
        SharedPreferencesUtils.WriteSharedPreferences(prefName, "device_identity", id);
    }

    public Long getIdentity() {
        return SharedPreferencesUtils.getValueByName(prefName, "device_identity", 3);
    }




    public String getDown_url() {
        return down_url;
    }

    public void setDown_url(String down_url) {
        this.down_url = down_url;
    }

    public String getNew_version() {
        return new_version;
    }

    public void setNew_version(String new_version) {
        this.new_version = new_version;
    }

    class MyDaemonListener implements DaemonConfigurations.DaemonListener{
        @Override
        public void onPersistentStart(Context context) {
            MLog.e("*****AppContext.MyDaemonListener.onPersistentStart()******");

        }

        @Override
        public void onDaemonAssistantStart(Context context) {
            MLog.e("*****AppContext.MyDaemonListener.onDaemonAssistantStart()******");
        }

        @Override
        public void onWatchDaemonDaed() {
            MLog.e("*****AppContext.MyDaemonListener.onWatchDaemonDaed()******");
                return;
        }
    }

}
