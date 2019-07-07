# FeliCaZ

Save history of FeliCa (Suica, PASMO) to Zaim.

## Usage

### Login with Zaim

![login](img/login.png)

### Read FeliCa card (Suica, PASMO)

![home](img/home.png)

### Save history to Zaim.

![history](img/history.png)

## Debug run

```bash
$ ./gradlew assembleDebug
$ adb install -r ./app/build/outputs/apk/debug/app-debug.apk
$ adb shell monkey -p work.deka.felicaz -c android.intent.category.LAUNCHER 1
```