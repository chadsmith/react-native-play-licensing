## React Native Play Licensing

Adds [Client-Side License Verification](https://developer.android.com/google/play/licensing/client-side-verification) to React Native applications. Works on both Android and iOS, but only verifies licenses on Android.

## Installing

```
npm install react-native-play-licensing
```

## Linking Native Dependencies

### Automatic Linking

```
react-native link react-native-play-licensing
```

### Manual Linking

1. In `android/settings.gradle`

    ```
    ...
    include ':react-native-play-licensing'
    project(':react-native-play-licensing').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-play-licensing/android')
    ```

2. In `android/app/build.gradle`

    ```
    ...
    dependencies {
        ...
        implementation project(':react-native-play-licensing')
    }
    ```

3. Register module in `MainApplication.java`

    ```java
    import com.github.chadsmith.playlicensing.PlayLicensingPackage;  // <--- import

    public class MainApplication extends Application implements ReactApplication {
      ......

      @Override
      protected List<ReactPackage> getPackages() {
          return Arrays.<ReactPackage>asList(
              new MainReactPackage(),
              ......
              new PlayLicensingPackage(),  // <--- add package
          );
      }

      ......

    }
    ```

## Usage

To use the `react-native-play-licensing` package in your codebase, use the `PlayLicensing` module:

```javascript
import PlayLicensing from 'react-native-play-licensing';
```

```javascript
export default function(App) {
  // ...
  const [licensed, setLicensed] = useState(null);
  useEffect(() => {
    PlayLicensing.verify('MIIBIjANBgkqhkiG...')
      .then(setLicensed);
  }, []);

  // do something if app is not licensed

  // ...
}
```

## API

- `verify(BASE64_PUBLIC_KEY, STRING_FOR_SALT)`

```javascript
PlayLicensing.verify(BASE64_PUBLIC_KEY)
  .then(licensed => {
    if(licensed === true) {
      // app was purchased from Google Play
    }
    else if (licensed === false) {
      // app was not purchased from Google Play
    }
    else {
      // verification failed, retry later
    }
  })
```
