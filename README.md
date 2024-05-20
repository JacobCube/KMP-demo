This is a Kotlin Multiplatform project targeting Android, iOS, Web, Desktop.

* `/composeApp` is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - `commonMain` is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    `iosMain` would be the right folder for such calls.

* `/iosApp` contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform, 
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.


Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html),
[Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform/#compose-multiplatform),
[Kotlin/Wasm](https://kotl.in/wasm/)…

**Note:** Compose/Web is Experimental and may be changed at any time. Use it only for evaluation purposes.
We would appreciate your feedback on Compose/Web and Kotlin/Wasm in the public Slack channel [#compose-web](https://slack-chats.kotlinlang.org/c/compose-web).
If you face any issues, please report them on [GitHub](https://github.com/JetBrains/compose-multiplatform/issues).

You can open the web application by running the `:composeApp:wasmJsBrowserDevelopmentRun` Gradle task.


**Resources které by se mohli hodit:**
* https://github.com/JetBrains/compose-multiplatform/tree/master/tutorials/Native_distributions_and_local_execution
* https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-lifecycle.html#mapping-android-lifecycle-to-other-platforms
* https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-navigation-routing.html
* https://developer.android.com/guide/topics/large-screens/support-different-screen-sizes
* https://ktor.io/docs/client-create-multiplatform-application.html
* https://github.com/arkivanov/Decompose


**Co bude v rámci konceptu vyzkoušeno:**
* Základní stylizace, která zapadne na desktopové zobrazení i mobilní
* Funkční navigace na všech platformách (v první verzi jsem udělal navigace v Decompose, před 2 týdny ale Google přidal podporu pro navigaci stejnou co máme v Android, tak jsem implementoval Jetpack Compose Navigation)
* Responsibilita - UI upravené speciálně pro každou platformu a různé zařízení
* Textové hledání v navigaci - specifické pro Tipsys, kde by to dost zjednodušilo práci
* Získání dat přes RestApi (velké množství dat - http://games.casino.czd1.k8s.tipsport.it/api/v1/games, menší množství dat - http://games.casino.czd1.k8s.tipsport.it/api/v1/vendors)
* Otevření detailu nějakého záznamu - tedy navigace s argumenty
* Lokální historie - na úvodní stránce je historie naposledy navštívených destinací, která se dá vymazat
* Hledání v záznamech
* Poslouchání na životní cyklus aplikací https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-lifecycle.html#lifecycle-implementation