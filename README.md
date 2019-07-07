# FeliCaZ

Save history of FeliCa (Suica, PASMO) to Zaim.

## Usage

### Login with Zaim

<img src="img/login.png" alt="login" width="240" />

### Read FeliCa card (Suica, PASMO)

<img src="img/home.png" alt="home" width="240" />

### Save history to Zaim.

<img src="img/history.png" alt="history" width="240" />

## Debug run

```bash
$ ./gradlew assembleDebug
$ adb install -r ./app/build/outputs/apk/debug/app-debug.apk
$ adb shell monkey -p work.deka.felicaz -c android.intent.category.LAUNCHER 1
```
