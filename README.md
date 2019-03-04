
# Mintegral聚合集成指南（Android）

## 概要

    
本文档介绍了Android开发者如何通过Mintegral聚合其他第三方的广告SDK，目前仅支持聚合ironsource的Rewarded Video、Interstitial，其中Interstitial对应Mintegral的Interstitial Video。
 

## 配置Mintegral
### 获取账户信息    

**App Key**   

开发者每个账号都有对应的App Key，请求广告时需要用到该参数，它可以从Mintegral开发者后台获取，在**APP Setting -> App Key**界面，可以查看到该账号的App Key，如图所示：  
![](https://github.com/Vivi012/MTG/blob/master/apikey.png?raw=true) 

**App Id**        

开发者每创建一个应用后，系统会自动生成App Id，可在**APP Setting -> App Id**界面查看到已创建的应用以及对应的App Id，如图所示：  
![](https://github.com/Vivi012/MTG/blob/master/appid.png?raw=true)    

**Unit Id**       

开发者每创建一个广告位后，系统会自动生成Unit Id，可在**APP Setting -> AD Unit -> AD Unit Id**界面查看到已创建的广告位以及对应的Unit Id，如图所示：  
![](https://github.com/Vivi012/MTG/blob/master/unitid.png?raw=true)   

### 获取SDK

[点击下载](http://cdn-adn.rayjump.com/cdn-adn/v2/portal/19/02/22/11/49/5c6f715dba0c6.zip)最新版本的国内版本9.8.0 

[点击下载](http://cdn-adn.rayjump.com/cdn-adn/v2/portal/19/02/22/11/49/5c6f7156c5290.zip)最新版本的非国内版本9.8.0  


### 将Mintegral SDK添加至您的项目   

**通过JAR/AAR包集成**     
#### Interstitial需要导入以下文件

mintegral_alphab.aar<br>
mintegral_common.aar/mintegral_chinacommon.aar<br>
mintegral_intersitialvideo.aar<br>
mintegral_videocommon.aar<br>
mintegral_videojs.aar<br>
mintegral_playercommon.aar<br>
mintegral_reward.aar<br>
mintegral_mtgjscommon.aar<br>
mintegral_mtgdownloads.aar<br>
或者对应的jar包和res文件。  



#### Rewarded Video需要导入以下文件

mintegral_alphab.aar<br>
mintegral_common.aar/mintegral_chinacommon.aar<br>
mintegral_reward.aar<br> 
mintegral_videocommon.aar<br>
mintegral_videojs.aar<br>
mintegral_playercommon.aar<br>
mintegral_mtgjscommon.aar<br>
mintegral_mtgdownloads.aar<br>
或者对应的jar包和res文件。  



### AndroidManifest.xml 配置

1、必要权限 

**注：如果使用了国内版的SDK ，“READ_PHONE_STATE”此条权限必须加上,如果是Android系统6.0以上，需要动态申请此权限。**   

```java
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES"/>
    <!-- 如果是国内版本SDK ，则此条权限必须加上 -->
    <uses-permission android:name="android.permission.READ_PHONE_STATE"/>
```


2、非必要权限

```java
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
```

3、添加组件 

```xml
    <activity
            android:name="com.mintegral.msdk.activity.MTGCommonActivity"
            android:configChanges="keyboard|orientation"
            android:screenOrientation="portrait"
            android:exported="true"
            android:theme="@android:style/Theme.Translucent.NoTitleBar">
    </activity>
    
    
    <activity
            android:name="com.mintegral.msdk.reward.player.MTGRewardVideoActivity"
            android:configChanges="orientation|keyboardHidden|screenSize"
            android:theme="@android:style/Theme.NoTitleBar.Fullscreen" />
    

    <receiver android:name="com.mintegral.msdk.click.AppReceiver" >
        <intent-filter>
            <action android:name="android.intent.action.PACKAGE_ADDED" />
            <data android:scheme="package" />
        </intent-filter>
    </receiver>
    <service android:name="com.mintegral.msdk.shell.MTGService" >
        <intent-filter>
            <action android:name="com.mintegral.msdk.download.action" />
        </intent-filter>
    </service>
```


4、下载器适配  
 

（1）**将Android Support v4包升级到26.0.0或以上。**

（2）**如果targetSDKVersion >= 24，需要适配FileProvider。**  
   在xml文件下添加mtg_provider_paths.xml

```xml
	<?xml version="1.0" encoding="utf-8"?>
	<paths xmlns:android="http://schemas.android.com/apk/res/android">
	 <external-path name="external_files" path="."/>
	</paths>
```

   在AndroidManifest.xml文件中添加如下代码  


```xml
	      <provider
              android:name="com.mintegral.msdk.base.utils.MTGFileProvider"
              android:authorities="${applicationId}.mtgFileProvider"
              android:exported="false"
              android:grantUriPermissions="true">
            <meta-data
              android:name="android.support.FILE_PROVIDER_PATHS"
              android:resource="@xml/mtg_provider_paths"/>
        </provider>
```



5、硬件加速   
在application节点下修改"android:hardwareAccelerated"的属性值：

```xml
<application
        ...
        android:hardwareAccelerated="true">
        ...
        ...
        ...
        ...

</application>
```



6、混淆配置

```java
    -keepattributes Signature   
    -keepattributes *Annotation*   
    -keep class com.mintegral.** {*; }  
    -keep interface com.mintegral.** {*; }  
    -keep class android.support.v4.** { *; }  
    -dontwarn com.mintegral.**   
    -keep class **.R$* { public static final int mintegral*; }
    -keep class com.alphab.** {*; }
    -keep interface com.alphab.** {*; }
```

7、Android9.0兼容注意事项   

**目前Android SDK暂不支持Android9.0以上操作系统，如果在Android9.0以上系统出现的崩溃，可以通过以下两个方法之一解决。**    
（1）在AndroidManifest.xml中添加以下内容：  

```xml
<uses-library android:name="org.apache.http.legacy" android:required="false"/>
```
（2）将targaetSDKveriosn设置为27或者27以下。  
  


## 配置ironsource

### 创建账号
####  [注册](https://platform.ironsrc.com/partners/signup)并[登录](https://platform.ironsrc.com/partners/tour)您的ironSource帐户。     
#### 创建App 
要将您的应用程序添加到ironSource dashboard，请单击**New App** 按钮。

![](https://github.com/Vivi012/MTG/blob/master/ir1.png?raw=true)

#### 进入应用详情

选择**Mobile App**，输入**应用的Google Play URL**，然后点击**Import App Info**。显示应用信息后，单击**Add App**按钮。

如果您的应用程序不可用，请选择 **App Not Live in the Application Store**并提供**Temporary Name**。选择Android平台，然后单击**Add App**。

![](https://github.com/Vivi012/MTG/blob/master/ir2.png?raw=true)

####  广告位配置
记录下您添加应用后得到的**App Key**，初始化SDK时会用到此值。并在此处设置您所需要的广告形式状态，然后单击**Done**。
![](https://developers.google.com/admob/images/mediation/ironsource/ad_format_select_android.png)


###  集成ironsource

[ironsource集成文档]()参考链接。

#### 导入ironsource文件

1、在build.gradle文件中加入

```java
repositories {
    maven {
        url "https://dl.bintray.com/ironsource-mobile/android-sdk"
   }
}    
```
2、然后在dependencies中添加   

```java
dependencies {

  implementation 'com.ironsource.sdk:mediationsdk:6.8.1@jar' 

}
```
#### 配置AndroidManifest.xml 

```java
 <activity
            android:name="com.ironsource.sdk.controller.ControllerActivity"
            android:configChanges="orientation|screenSize"
            android:hardwareAccelerated="true" />
        <activity
            android:name="com.ironsource.sdk.controller.InterstitialActivity"
            android:configChanges="orientation|screenSize"
            android:hardwareAccelerated="true"
            android:theme="@android:style/Theme.Translucent" />
        <activity
            android:name="com.ironsource.sdk.controller.OpenUrlActivity"
            android:configChanges="orientation|screenSize"
            android:hardwareAccelerated="true"
            android:theme="@android:style/Theme.Translucent" 
/>
```
#### ironsource混淆配置

```java
-keepclassmembers class com.ironsource.sdk.controller.IronSourceWebView$JSInterface {
    public *;
}
-keepclassmembers class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}
-keep public class com.google.android.gms.ads.** {
   public *;
}
-keep class com.ironsource.adapters.** { *;
}
-dontwarn com.ironsource.mediationsdk.**
-dontwarn com.ironsource.adapters.**
-dontwarn com.moat.**
-keep class com.moat.** { public protected private *; }
```


### 导入Adapter文件
点击[这里]()中获取Mediation包，并将其中的全部文档拷贝到您的项目中。

## Interstitial接入
###  初始化
####  创建MediationInterstitialHandler对象

```java
manager = new MediationInterstitialHandler();
```   

#### 设置MediationAdapterInitListener 
需要在初始化前调用

```java
      manager.setMediationAdapterInitListener(new MediationAdapterInitListener() {
           /**
           * 初始化成功
           */
           
            @Override
            public void onInitSucceed() {
                Toast.makeText(InterstitialActivity.this,"onInitSucceed",Toast.LENGTH_LONG).show();
                Log.e("interstitial","onInitSucceed");
            }
            
           /**
           * 初始化失败
           */

            @Override
            public void onInitFailed() {
                Toast.makeText(InterstitialActivity.this,"onInitFailed",Toast.LENGTH_LONG).show();
                Log.e("interstitial","onInitFailed");
            }
        });
```

#### 调用init方法 

初始化时需要在Map中配置广告id，Adapter路径及超时时间等参数。
   
示例代码：

```java
	Map<String,Object> paramsMap = new HashMap<>();
        //IronSource
        AdSource adSource = new AdSource();
        Map<String,Object> ironsourceMap = new HashMap<>();
        ironsourceMap.put("local","88f1a7f5");//该值为ironsource的AppKey.
        adSource.setLocalParams(ironsourceMap);
        adSource.setTargetClass("com.mintegral.mediation.adapter.iron.IronInterstitialAdapter");//这里的Adapter路径要对应您项目中该文件的全路径
        adSource.setTimeOut(10000);//请求广告超时时间
        paramsMap.put("2",adSource);//设置广告优先级，1为优先展示，默认优先请求Mintegral广告。

        //Mintegral
        Map<String,Object> map = new HashMap<>();
        AdSource mtgAdSource = new AdSource();
        map.put(CommonConst.KEY_APPID, "your AppId");
        map.put(CommonConst.KEY_APPKEY, "your AppKey");
        map.put(CommonConst.KEY_INTERSTITIALUNITID, "your unitId");
        map.put(CommonConst.KEY_MUTE, false);//是否静音，默认为非静音
        mtgAdSource.setLocalParams(map);
        mtgAdSource.setTargetClass("com.mintegral.mediation.adapter.mtg.MTGInterstitialAdapter");
        mtgAdSource.setTimeOut(10000);
        paramsMap.put("1",mtgAdSource);
        
 	 /**
         * 初始化
         */
	 manager.init(this,paramsMap);   
 
```

#### 设置MediationAdapterInterstitialListener
```java

      manager.setMediationAdapterInterstitialListener(new MediationAdapterInterstitialListener() {
      
	    /**
	     * 加载广告成功
	     */
	     
            @Override
            public void loadSucceed() {
                Toast.makeText(InterstitialActivity.this,"loadSucceed",Toast.LENGTH_LONG).show();
            }
	    
            /**
	     * 加载广告失败
	     */

            @Override
            public void loadFailed(String msg) {
                Toast.makeText(InterstitialActivity.this,"loadFailed:"+msg,Toast.LENGTH_LONG).show();
	    }
	    
            /**
	     * 展示广告成功
	     */

            @Override
            public void showSucceed() {
                Toast.makeText(InterstitialActivity.this,"showSucceed:",Toast.LENGTH_LONG).show();
            }
            
	     /**
	     * 展示广告失败
	     */

            @Override
            public void showFailed(String msg) {
                Toast.makeText(InterstitialActivity.this,"showFailed:"+msg,Toast.LENGTH_LONG).show();
            }
	    
            /**
	     * 点击广告
	     */

            @Override
            public void clicked(String msg) {
                Toast.makeText(InterstitialActivity.this,"clicked:"+msg,Toast.LENGTH_LONG).show();
            }
	    
            /**
	     * 关闭广告
	     */

            @Override
            public void closed() {
                Toast.makeText(InterstitialActivity.this,"closed:",Toast.LENGTH_LONG).show();
            }

        });
```
#### 获取Activity生命周期的监听
ironsource要求需要调用该方法。

```java

  lifecycleListener = manager.getLifecycleListener();
```
在Activity的生命周期中调用其中方法。     
       
```java

    @Override
    protected void onPause() {
        super.onPause();
        if(lifecycleListener != null){
            lifecycleListener.onPause(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(lifecycleListener != null){
            lifecycleListener.onResume(this);
        }
    }
```


### 加载广告
建议开发者在展示广告之前，提前进行广告加载（如初始化时或每次关闭广告后），进而给素材等资源的下载提供时间，减少用户在展示广告时等待的时间，提高用户体验和广告时间。    

```java
                /**
                 * @param 可置空
                 */
                 
                if (mediationInterstitialHandler != null) {
                    mediationInterstitialHandler.load("");
                }
```
### 展示广告
```java
                /**
                 * @param 可置空
                 */
                 
                if (mediationInterstitialHandler != null) {
                    mediationInterstitialHandler.show("");
                }
```
### 判断广告是否可播放
返回boolean值。    

```java
                /**
                 * @param 可置空
                 */
                 
 		if (mediationInterstitialHandler != null) {
                    mediationInterstitialHandler.isReady("");
                }
```



## Rewarded Video接入

### 初始化
#### 创建一个MediationRewardVideoHandler对象

```java
  	manager = new MediationRewardVideoHandler();
```

#### 设置MediationAdapterInitListener 
需要在初始化前调用

```java
      	manager.setMediationAdapterInitListener(new MediationAdapterInitListener() {
           /**
           * 初始化成功
           */
            @Override
            public void onInitSucceed() {
                Toast.makeText(InterstitialActivity.this,"onInitSucceed",Toast.LENGTH_LONG).show();
                Log.e("interstitial","onInitSucceed");
            }
            
           /**
           * 初始化失败
           */

            @Override
            public void onInitFailed() {
                Toast.makeText(InterstitialActivity.this,"onInitFailed",Toast.LENGTH_LONG).show();
                Log.e("interstitial","onInitFailed");
            }
        });
```
#### 调用init方法 


初始化时需要在Map中配置广告id，Adapter路径及超时时间等参数。         
示例代码：

```java
	Map<String,Object> paramsMap = new HashMap<>();
        //IronSource
        AdSource adSource = new AdSource();
        Map<String,Object> ironsourceMap = new HashMap<>();
        ironsourceMap.put("local","88f1a7f5");//该值为ironsource的AppKey.
        adSource.setLocalParams(ironsourceMap);
        adSource.setTargetClass("com.mintegral.mediation.adapter.iron.IronRewardAdapter");//这里的Adapter路径要对应您项目中该文件的全路径
        adSource.setTimeOut(10000);//请求广告超时时间
        paramsMap.put("2",adSource);//设置广告优先级，1为优先展示，默认优先请求Mintegral广告。

        //Mintegral
        Map<String,Object> map = new HashMap<>();
        AdSource mtgAdSource = new AdSource();
        map.put(CommonConst.KEY_APPID, "your AppId");
        map.put(CommonConst.KEY_APPKEY, "your AppKey");
        map.put(CommonConst.KEY_REWARDUNITID, "your unitId");
        map.put(CommonConst.KEY_USERID, "your userId");////userId在服务器回调中用到
        map.put(CommonConst.KEY_REWARDID, "your rewardId");//rewardId默认可以传1
        map.put(CommonConst.KEY_MUTE, false);
        mtgAdSource.setLocalParams(map);
        mtgAdSource.setTargetClass("com.mintegral.mediation.adapter.mtg.MTGRewardAdapter");
        mtgAdSource.setTimeOut(20000);
        paramsMap.put("1",mtgAdSource);

 	 /**
         * 初始化
         */ 
	 manager.init(this,paramsMap);

```

#### 设置MediationAdapterRewardListener
```java

    manager.setMediationAdapterRewardListener(new MediationAdapterRewardListener() {
	    /**
	     * 加载广告成功
	     */

            @Override
            public void loadSucceed() {
                Toast.makeText(RewardActivity.this,"loadSucceed",Toast.LENGTH_LONG).show();
            }

	    /**
	     * 加载广告失败
	     */

            @Override
            public void loadFailed(String msg) {
                Toast.makeText(RewardActivity.this,"loadFailed:"+msg,Toast.LENGTH_LONG).show();
            }
	    
            /**
	     * 展示广告成功
	     */

            @Override
            public void showSucceed() {
                Toast.makeText(RewardActivity.this,"showSucceed:",Toast.LENGTH_LONG).show();
            }
	    
            /**
	     * 展示广告失败
	     */

            @Override
            public void showFailed(String msg) {
                Toast.makeText(RewardActivity.this,"showFailed:"+msg,Toast.LENGTH_LONG).show();
            }
	    
            /**
	     * 点击广告
	     */

            @Override
            public void clicked(String msg) {
                Toast.makeText(RewardActivity.this,"clicked:"+msg,Toast.LENGTH_LONG).show();
            }
	    
             /**
	      * 关闭广告
	      */

            @Override
            public void closed() {
                Toast.makeText(RewardActivity.this,"closed:",Toast.LENGTH_LONG).show();
            }
	    
            /**
	     * 奖励回调
	     */


            @Override
            public void rewarded(String name, int amount) {
                Toast.makeText(RewardActivity.this,"rewarded:"+name+"-amount:"+amount,Toast.LENGTH_LONG).show();
            }
        });
```
#### 获取Activity生命周期的监听
ironsource要求需要调用该方法。

```java
   lifecycleListener = manager.getLifecycleListener();
```
在Activity的生命周期中调用其中方法。     
       
```java

    @Override
    protected void onPause() {
        super.onPause();
        if(lifecycleListener != null){
            lifecycleListener.onPause(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(lifecycleListener != null){
            lifecycleListener.onResume(this);
        }
    }
```



### 加载广告
建议开发者在展示广告之前，提前进行广告加载（如初始化时或每次关闭广告后），进而给素材等资源的下载提供时间，减少用户在展示广告时等待的时间，提高用户体验和广告时间。    

```java
                /**
                 * @param 可置空
                 */
                 
                if (mediationRewardVideoHandler != null) {
                    mediationRewardVideoHandler.load("");
                }
```
### 展示广告   

```java
                /**
                 * @param 可置空
                 */
                 
                if (mediationRewardVideoHandler != null) {
                    mediationRewardVideoHandler.show("");
                }
```
### 判断广告是否可播放
返回boolean值。     

```java
                /**
                 * @param 可置空
                 */
                 
                if (mediationRewardVideoHandler != null) {
                    mediationRewardVideoHandler.isReady("");
                }
```



## 设置拦截器
您可以重写BaseInterceptor，该拦截器的作用是设置SDK请求广告优先级，需要在init之前调用该方法。

```java
    /**
     * 设置拦截器，如果不设置或设置为null，将使用默认
     * @param interceptor
     */
     
    public void setInterceptor(BaseInterceptor interceptor){
        if (interceptor != null) {
            mInterceptor = interceptor;
        }
    }
```

示例代码：    

```java

public class TestInterceptor extends BaseInterceptor {  
            @Override 

            public LinkedList<AdSource> onInterceptor(String unitId, Map<String, Object> localParams, String serviceParams) { 
                LinkedList<AdSource> linkedList = new LinkedList<>(); 
                if (localParams != null) { Object o = localParams.get("a"); 
                    if (o instanceof AdSource) { linkedList.add((AdSource) o); } 
                    Object o1 = localParams.get("b"); 
                    if (o1 instanceof AdSource) { linkedList.add((AdSource) o1); } } 
                return linkedList; } 
        }

        /**
         * 初始化前调用
         */
        
        interceptor_demo = new TestInterceptor();      
        mediationInterstitialHandler.setInterceptor(interceptor_demo);



```

## Error Code
```java
public class MediationMTGErrorCode {


    public static String INTERNAL_ERROR = "INTERNAL_ERROR";//网络错误
    public static String ADAPTER_CONFIGURATION_ERROR = "ADAPTER_CONFIGURATION_ERROR";//配置错误
    public static String VIDEO_CACHE_ERROR = "VIDEO_CACHE_ERROR";//广告缓存失败
    public static String NETWORK_NO_FILL = "NETWORK_NO_FILL";//广告无填充
    public static String ADSOURCE_IS_INVALID = "adsources is invalid";//adsources 无效
    public static String NO_CONNECTION = "NO_CONNECTION";//无网络连接
    public static String UNSPECIFIED = "UNSPECIFIED";//未知错误
    public static String ACTIVITY_IS_NULL= "ACTIVITY_IS_NULL";//Activity为空
    public static String ADSOURCE_IS_TIMEOUT = "ADSOURCE_IS_TIMEOUT";//广告加载超时


}
```





		 
		 

