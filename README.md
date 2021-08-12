# wheelie
Collection of libraries to make it easy to bootstrap and develop an existing Android app.

# Libraries

## Chronicle
It is a provider agnostic analytics library. It currently supports firebase at the moment.

## Firebase
Add the library to your project:

`implementation group: 'com.github.ankitbatra11.wheelie', name: 'firebase', version: WHEELIE_VERSION`

### Chronicle

If you are using `Chronicle`, add the following line to initialize the `Firebase` flavour of `Chronicle`:

```
<provider
  android:name="androidx.startup.InitializationProvider"
  android:authorities="${applicationId}.androidx-startup"
  android:exported="false"
  tools:node="merge">
  <meta-data
    android:name="com.abatra.android.wheelie.firebase.chronicle.FirebaseChronicleInitializer"
    android:value="androidx.startup" />
</provider>
```
### Timber
If you are using `Timber`, you can use `CrashlyticsTimberTree.newInstance()` (a `Tree` implementation) for the release version of your app.

### Remote Config

#### Initialize
```
DynamicConfig dynamicConfig = new FirebaseDynamicConfig();
dynamicConfig.initialize(application);
dynamicConfig.initialize(getFirebaseDynamicConfigSettings());
dynamicConfig.fetchAndActivate();
```
```
private FirebaseDynamicConfigSettings getFirebaseDynamicConfigSettings() {
  FirebaseDynamicConfigSettings.Builder builder = FirebaseDynamicConfigSettings.builder();
  if (BuildConfig.DEBUG) {
    builder.debug();
  }
  builder.addDefaultValue(REMOTE_CONFIG_KEY, REMOTE_CONFIG_VALUE);
  return builder.build();
}
```
#### Access

Get a string value

`dynamicConfig.getStringValue(REMOTE_CONFIG_KEY)`

Get a long value

`dynamicConfig.getLongValue(REMOTE_CONFIG_KEY)`

# Jitpack Repo
https://jitpack.io/#ankitbatra11/wheelie
