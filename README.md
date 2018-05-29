[![Release](https://jitpack.io/v/stanwood/Analytics_Framework_android.svg?style=flat-square)](https://jitpack.io/#stanwood/Analytics_Framework_android)

# Analytics Framework (Android)

This library contains a whole bunch of (mostly optional) Analytics and Reporting trackers for easy integration in your app.

## Import

The stanwood Analytics Framework is hosted on JitPack. Therefore you can simply import it by adding

```groovy
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```

to your project's `build.gradle`.

Then add this to you app's `build.gradle`:

```groovy
dependencies {
    // AAR versions are available for all artifacts as well!

    // base 
    implementation 'com.github.stanwood.Analytics_Framework_android:stanwoodanalytics:$latest_version'
   
    // fabric - non optional
    debugImplementation "com.github.stanwood.Analytics_Framework_android:fabrictrackingprovider-noop:$analytics_version"
    releaseImplementation "com.github.stanwood.Analytics_Framework_android:fabrictrackingprovider:$analytics_version"
    
    // firebase - non optional
    debugImplementation "com.github.stanwood.Analytics_Framework_android:firebasetrackingprovider-noop:$analytics_version"
    releaseImplementation "com.github.stanwood.Analytics_Framework_android:firebasetrackingprovider:$analytics_version"

    // Testfairy tracker
    debugImplementation 'com.github.stanwood.Analytics_Framework_android:testfairytrackingprovider:$latest_version'
    releaseImplementation 'com.github.stanwood.Analytics_Framework_android:testfairytrackingprovider-noop:$latest_version'

    // Google Analytics tracker - optional
    debugImplementation 'com.github.stanwood.Analytics_Framework_android:gatrackingprovider-noop:$latest_version'
    releaseImplementation 'com.github.stanwood.Analytics_Framework_android:gatrackingprovider:$latest_version'
    
    // Adjust tracker - optional
    debugImplementation 'com.github.stanwood.Analytics_Framework_android:adjusttrackingprovider-noop:$latest_version'
    releaseImplementation 'com.github.stanwood.Analytics_Framework_android:adjusttrackingprovider:$latest_version'

    // Bugfender tracker - optional & deprecated
    debugImplementation 'com.github.stanwood.Analytics_Framework_android:bugfendertrackingprovider-noop:$latest_version'
    releaseImplementation 'com.github.stanwood.Analytics_Framework_android:bugfendertrackingprovider:$latest_version'

    // Mixpanel Tracker - optional
    debugImplementation 'com.github.stanwood.Analytics_Framework_android:mixpaneltrackingprovider-noop:$latest_version'
    releaseImplementation 'com.github.stanwood.Analytics_Framework_android:mixpaneltrackingprovider:$latest_version'

    // Debugview Tracker - optional
    debugImplementation 'com.github.stanwood.Analytics_Framework_android:loggingtrackingprovider:$latest_version'
    releaseImplementation 'com.github.stanwood.Analytics_Framework_android:loggingtrackingprovider-noop:$latest_version'

    // Infoonline Tracker - optional
    debugImplementation "com.github.stanwood.Infonline_Analytics_Tracker_android:infoonlinetrackingprovider-noop:$infoonline_tracker_version"
    releaseImplementation "com.github.stanwood.Infonline_Analytics_Tracker_android:infoonlinetrackingprovider:$infoonline_tracker_version"

    // Facebook Tracker - optional
    debugImplementation "com.github.stanwood.Analytics_Framework_android:facebooktrackingprovider-noop:$analytics_version"
    releaseImplementation "com.github.stanwood.Analytics_Framework_android:facebooktrackingprovider:$analytics_version"
}
```

## Usage

The recommended way to integrate the library into an app is by subclassing the `BaseAnalyticsTracker` class and supplying it using the Singleton pattern:

```java
public class SimpleAppTracker extends BaseAnalyticsTracker {
    private static SimpleAppTracker instance;

    private SimpleAppTracker(@NonNull Context context, @NonNull FabricTracker fabricTracker, @NonNull FirebaseTracker firebaseTracker,
                             @NonNull TestfairyTracker testfairyTracker, @Nullable Tracker... optional) {
        super(context, fabricTracker, firebaseTracker, testfairyTracker, optional);
    }

    public static synchronized void init(Application application) {
        if (instance == null) {
            instance = new SimpleAppTracker(application, FabricTrackerImpl.builder(application).build(),
                    FirebaseTrackerImpl.builder(application).setExceptionTrackingEnabled(true).build(),
                    TestfairyTrackerImpl.builder(application, "KEY").build());
            FirebasePerformance.getInstance().setPerformanceCollectionEnabled(!BuildConfig.DEBUG);
            if (BuildConfig.DEBUG) {
                Timber.plant(new Timber.DebugTree());
            }
        }
    }

    public static SimpleAppTracker instance() {
        if (instance == null) {
            throw new IllegalArgumentException("Call init() first!");
        }
        return instance;
    }
}
```

Init it before calling through to it (best very early in the app, e.g. in your application's `onCreate()` method):

```java
AppTracker.init(getApplication());
```

Then you may call through to the `AppTracker` singleton from wherever you want to track something:

```java
AppTracker tracker = AppTracker.instance();

tracker.trackUser("alice", "alice@bob.com");
tracker.trackScreenView("home"); // this can be handy during migration from existing trackers, usually you should better define a more specific method like trackHome() in AppTracker
tracker.trackAdLoaded("123456");
tracker.trackShowDetails("id", "details of id");
tracker.trackException(new IllegalStateException("error"));
```

The `TrackingEvent` class contain predefined event names and keys you should use whenever possible when tracking events and keys.

For a more complete example refer to the `AdvancedAppTracker.java` class in the sample app module.

## Logging and catched exceptions

To log catched exceptions and arbitrary messages just use Timber's static methods (you need to use the default Timber instance, don't create a new one as this one won't be picked up by the Analytics library!).

## Map functions

All trackers support so-called _map functions_. These functions map `TrackerParams` to whatever the tracking module can work with.

_Every tracker has a default map function. In general it makes sense to take a look at the various tracker implementations (esp. the `*Tracker.java` classes) to understand how they work and what needs to be adapted to have them working as you want. Keep in mind that the default configurations have been carefully done and represent the stanwood defaults to be used for all apps if possible._

For example if you have custom labels for Firebase Analytics property keys you can set them on your own using a map function:

```java
FirebaseTracker firebaseTracker = FirebaseTracker.builder(application)
    .mapFunction(new io.stanwood.framework.analytics.firebase.MapFunction() {
        @Override
        public Bundle map(TrackerParams params) {
            Bundle bundle = new Bundle();
            bundle.putString("category", params.getEventName());
            bundle.putString("action", params.getName());
            bundle.putString("label", params.getItemId());
            return bundle;
        }
    }).build();
```

Or if you just want to track a specific token to Adjust:

```java
Tracker adjustTracker = AdjustTracker.builder(application, "KEY")
    .mapFunction(new io.stanwood.framework.analytics.adjust.MapFunction() {
        @Override
        public String mapContentToken(TrackerParams params) {
            if (params.getEventName().equals(TrackingEvent.VIEW_ITEM) && params.getName().equals("home")) {
                return "ADJUST_CONTENT_ID";
            }
            return null;
        }
    })
    .build();
```

## Opt-in/out

All trackers are ENABLED by default! 
Check [noop module's](#noop-modules) to disable tracking in debug builds.
Use BaseAnalyticsTrackers `enable(boolean)` function to change the state of your trackers.

To display an opt out message the provided context needs to be a FragmentActivity child.

e.g. Set all trackers to disabled:

```enable(context, false);```

Set Fabric and Firebase to enabled (This will not change the enabled state of any other tracker)

```enable(context, true, FabricTracker.TRACKER_NAME, FirebaseTracker.TRACKER_NAME);```

Trackers enabled state is persisted over app sessions. 

To check if there is currently at least one running tracker:

```hasEnabledTracker()```

for a specific tracker use:

```isTrackerEnabled(FabricTracker.TRACKER_NAME)```


**Always double-check your app by actively testing it after implementing opt-in/out to ensure that all trackers have been properly configured!**
#### _noop_ module`s
All tracking providers are also implemented as an `noop` version. 
They don`t execute any tracking code and doesn't pull in any dependencies or permissions.
Use this to e.g disable tracking in debug or qa builds.

You could configure the dependency in your app's `build.gradle` like so:

```groovy
debugImplementation 'com.github.stanwood.Analytics_Framework_android:testfairytrackingprovider:$latest_version'
releaseImplementation 'com.github.stanwood.Analytics_Framework_android:testfairytrackingprovider-noop:$latest_version'
```

#### Migration from other tracker frameworks

The first time a tracker gets initialized `migrateTrackerState(trackerName)` gets called , which per default returns true. Override this if needed to set the initial tracker enabled state from your previously used framework.


## Tracker specific documentation

### Testfairy

#### okhttp interceptor

The Testfairy module also contains an okhttp `Interceptor` called `TestfairyHttpInterceptor`.

This interceptor is needed to log calls to testfairy and is purely optional.

Add it to your okhttp client as an _app interceptor_:

```java
OkHttpClient client = new OkHttpClient.Builder()
    .addInterceptor(new TestfairyHttpInterceptor())
    .build();
```

You can also use this without modification in release builds, just make sure to use the _noop_ module for these builds instead of the regular one. The _noop_ version doesn't execute any own code and thus doesn't track network calls to Testfairy.


### Firebase Crashlytics (for future reference, currently this library only offers plain old Fabric)

#### opt-in/out

_It is **not** possible to reenable crash tracking for a running session. The user has to restart the app to get crash tracking back to work._

### Firebase Performance (not included in the library)

#### opt-in/out

To disable auto-intialisation of Firebase Performance at app start (e.g. because you want to wait for user-consent) you need to add this line to your manifest:

```xml
<meta-data android:name="firebase_performance_collection_enabled" android:value="false" />
```

Later on you can enable it with

```java
FirebasePerformance.getInstance().setPerformanceCollectionEnabled(true);
```

as outlined in the example above.

#### Debugging events

To enter debug mode execute the following on your terminal:

```
adb shell setprop debug.firebase.analytics.app <package_name>
```

This causes events to be sent to the server right away. Look out for requests to the `app-measurement.com` host.

You can disable debug mode by executing

```
adb shell setprop debug.firebase.analytics.app .none.
```

### Firebase Analytics

#### Debugging events

To ensure that events are sent check out the documentation over at https://firebase.google.com/docs/analytics/debugview. However this doesn't seem to always work properly.

What does work though is using the adb command mentioned at the beginning of the Firebase DebugView documentation. You will see the events being logged to the `app-measurement.com` host when proxying. This can be very helpful to ensure that your opt-out implementation really works.

### Infoonline
As the SDK is only available as AAR file the library needs to be copied into application's library folder and include via:
```groovy
releaseImplementation 'de.infonline.lib:infonlinelib_x.x.x@aar'
```
