<div dir="rtl">

# تطبيق أذكار (Athkar App) - الإصدار 2.0.5

تطبيق أندرويد بسيط وخفيف للأذكار. الهدف من بناء التطبيق هو البساطة، الخصوصية، والاعتمادية؛ عشان كده الكود مكتوب بالكامل بلغة **Java** ومن غير أي مكتبات خارجية (Zero Dependencies)، وبيشتغل بالكامل بدون إنترنت أو أي أذونات مزعجة.

### مميزات التطبيق
* **خصوصية تامة:** لا يطلب أي أذونات (Permissions) ولا يحتاج للاتصال بالإنترنت.
* **أذكار عند فتح الشاشة:** يعرض الأذكار تلقائياً عند فتح قفل الهاتف، مع العمل تلقائياً بعد إعادة تشغيل الجهاز.
* **مشاركة الأذكار كصور:** إمكانية تحويل الذكر إلى صورة ومشاركتها بسهولة.
* **خفيف وسريع:** حجم صغير جداً وأداء سلس بفضل الاعتماد على أدوات الأندرويد الأساسية فقط.

### بيئة التطوير والأدوات المستخدمة
لو حابب تفتح المشروع وتشتغل عليه، دي الإعدادات الأساسية اللي اتبنى بيها:
* **اللغة:** Java (JDK 17)
* **نظام البناء:** Gradle 8.7 مع Android Gradle Plugin 8.3.2
* **إعدادات الأندرويد:**
  * `compileSdk`: 34
  * `targetSdk`: 33
  * `minSdk`: 23
* **المكتبات الخارجية:** لا يوجد، التطبيق مبني على أدوات الأندرويد الأساسية فقط.
* **الخطوط:** التطبيق بيستخدم خط **الأميري (Amiri)** بترخيص (OFL 1.1)، وهتلاقيه مرفق في مسار `app/src/main/assets`.

### إزاي تعمل Build للمشروع؟
1. اتأكد إن الـ **Android SDK** متثبت على جهازك، وعَرّف مساره في ملف `local.properties` بالشكل ده:
   ```properties
   sdk.dir=/path/to/your/Android/Sdk
   ```
2. ملف التوقيع الخاص بالإصدار النهائي (Release Keystore) مش مرفوع هنا لأسباب أمنية. لو حابب تطلع نسخة Release، هتحتاج تضيف إعدادات التوقيع الخاصة بيك (`signingConfig`) جوه ملف `app/build.gradle`.
3. بعد كده، تقدر تبني النسخة النهائية (APK) من خلال الأمر ده في الـ Terminal:
   ```bash
   gradle assembleRelease
   ```

### الرخصة (License)
هذا المشروع مفتوح المصدر ومتاح للجميع للاستخدام والتعديل، راجع ملف [LICENSE](LICENSE) للمزيد من التفاصيل. (خط Amiri يخضع لترخيص OFL 1.1 الخاص به).

</div>

---

# Athkar App (v2.0.5)

A simple, lightweight Athkar app for Android. The main goal behind this project is simplicity, privacy, and reliability—it is written purely in **Java** with zero third-party libraries, works completely offline, and requires zero permissions.

### Features
* **Privacy-First:** Requires no permissions and works 100% offline.
* **Screen-Unlock Athkar:** Displays Athkar seamlessly upon unlocking your device, with automatic startup on boot.
* **Share as Image:** Easily generate and share Athkar as clean images.
* **Lightweight & Fast:** Tiny footprint and smooth performance using native Android APIs only.

### Tech Stack & Environment
If you want to explore or work on the code, here is the setup used to build the project:
* **Language:** Java (JDK 17)
* **Build System:** Gradle 8.7 with Android Gradle Plugin 8.3.2
* **SDK Versions:**
  * `compileSdk`: 34
  * `targetSdk`: 33
  * `minSdk`: 23
* **Dependencies:** None. Built strictly using native Android APIs.
* **Typography:** Uses the **Amiri** font (licensed under OFL 1.1), included in `app/src/main/assets`.

### How to Build
1. Make sure you have the **Android SDK** installed on your machine, and set its path in your `local.properties` file:
   ```properties
   sdk.dir=/path/to/your/Android/Sdk
   ```
2. The release signing keystore is excluded for security reasons. To generate a signed release build, add your own `signingConfig` inside `app/build.gradle`.
3. Run the following command in your terminal to build the release APK:
   ```bash
   gradle assembleRelease
   ```

### License
This project is open-source. See the [LICENSE](LICENSE) file for details. (The bundled Amiri font is licensed under OFL 1.1).
