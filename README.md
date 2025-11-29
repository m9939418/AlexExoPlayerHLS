# AlexHLSExoPlayer

本專案以 **Jetpack Compose**、**Media3 ExoPlayer**、**Hilt DI** 與 **MVVM + UI State** 架構實作現代化的影音串流播放器。
專注於 **清晰架構、可維護性、高可擴充性**，示範如何在 Android 中正確地實作 HLS 播放、控制邏輯與自訂 UI。

---

## ✨ 功能特色

### 🎥 支援 HLS / m3u8 串流播放

* 使用 **Media3 ExoPlayer** 播放 HLS 串流
* 自動顯示載入中（Buffering）動畫
* 配合 App lifecycle 自動暫停 / 恢復播放

### 🎚️ 自訂播放器控制 UI（Compose）

* 自訂 **Slider（含圓角軌道、陰影 Thumb）**
* 顯示播放時間（current / total）
* 控制項按鈕：

    * ⏪ -10 秒
    * ⏮ -1 秒
    * ▶ / ⏸ 播放 / 暫停
    * ⏩ +10 秒
    * ⏭ +1 秒

### 🧠 ViewModel + StateFlow 狀態管理

* 使用 `PlayerUiState` 統一管理 UI 狀態
* ViewModel 負責：

    * 播放 / 暫停
    * 跳秒 / 快轉快退
    * 更新 duration / current position
    * 監聽 ExoPlayer 回傳事件

### 🔧 Hilt 依賴注入（DI）

* 正確以 Singleton 注入 **唯一 ExoPlayer 實例**
* 避免記憶體洩漏
* 便於後續擴充（小窗播放、全螢幕播放…）

### 🎨 100% Jetpack Compose UI

* 使用 AndroidView 整合 PlayerView
* 完整客製播放器 UI
* 無 XML，全部以 Compose 編寫

---

## 🏛 架構總覽

```
presentation/
│
├── PlayerScreen.kt        # UI + PlayerView + lifecycle 管理
├── PlayerSlider.kt        # 進度 Slider（自訂軌道 + Thumb）
├── PlayerActions.kt       # 播放控制（快轉、倒退、播放、暫停）
│
└── PlayerUiState.kt       # 所有 UI 狀態（immutable）
```

### ViewModel 負責：

* 控制 ExoPlayer
* 整理各種狀態吐給 UI
* 封裝所有播放邏輯

### UI 負責：

* 顯示畫面
* 回傳事件給 ViewModel
* 綁定 PlayerView（AndroidView）

---

## 📦 技術堆疊（Tech Stack）

| 技術                         | 用途                |
| -------------------------- | ----------------- |
| **Jetpack Compose**        | UI                |
| **Media3 ExoPlayer**       | HLS 播放            |
| **Hilt DI**                | 注入 ExoPlayer 與架構層 |
| **StateFlow / Coroutines** | UI 狀態管理           |
| **AndroidView Interop**    | 嵌入原生 PlayerView   |
| **Navigation Compose**     | 可擴充的頁面導航          |
| **Clean Architecture 心法**  | UI <-> VM 職責明確    |

---

## 📁 專案結構

```
app/
├── MainActivity.kt
├── navigation/
│     └── AppNavGraph.kt
│
├── player/
│     ├── PlayerViewModel.kt
│     ├── PlayerUiState.kt
│     ├── PlayerScreen.kt
│     ├── PlayerActions.kt
│     ├── PlayerSlider.kt
│
└── di/
      └── PlayerModule.kt
```

---
## 🔍 重要技術亮點

### ✔ Singleton ExoPlayer（避免 Memory Leak）

透過 Hilt Module 注入唯一實例，支援多頁面共用。

### ✔ 播放器邏輯集中於 ViewModel

提供乾淨的 API：

```
play()
pause()
togglePlayPause()
skipPrev1s()
skipPrev10s()
skipNext1s()
skipNext10s()
seekTo(ms)
```

### ✔ 自訂 Compose Slider

* 圓角底色
* 黃色進度條
* 陰影 Thumb（提升視覺質感）

### ✔ Lifecycle Aware PlayerScreen

* 使用 `DisposableEffect` 監聽生命週期
* App 進入背景自動 `pause()`
* 回到前景自動 `play()`（可自由調整）

---

## 🧪 TODO / 未來可擴充項目

* [ ] 倍速播放（1.25x / 1.5x / 2.0x）
* [ ] 畫中畫 PiP（Picture-in-Picture）
* [ ] 全螢幕橫向播放
* [ ] HLS 畫質切換（Variant / Tracks）
* [ ] 錯誤提示與重新整理 UI
* [ ] 手勢控制（亮度、音量、快轉拖曳）

---
## 👤 Author

**Alex Yang**  
Android Engineer
🌐 [github.com/m9939418](https://github.com/m9939418)

---
