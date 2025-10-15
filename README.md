# 🎢 Park Pulse

**Park Pulse** is a modern Android application built in Kotlin that displays a list of amusement park rides and their current wait times. It utilizes Jetpack Compose for UI, clean architecture principles, Hilt for dependency injection, Retrofit for networking, and state-driven UI updates via `ViewModel` and `StateFlow`.

---

## 📱 Features

- Fetches ride data from the [Queue-Times API](https://queue-times.com/)
- Migrated to Jetpack Compose from XML layouts
- Displays live wait times for rides
- Indicates ride open/closed status
- Refreshes ride data using reactive state management
- Navigation between list and detail screens using NavHost
- Built with a modular, testable clean architecture

---

## 🔧 Tech Stack

| Tech                  | Purpose                         |
| --------------------- | ------------------------------- |
| Kotlin                | Primary language                |
| Jetpack Compose       | Declarative UI framework        |
| Navigation Compose    | Screen navigation               |
| Hilt                  | Dependency injection            |
| Retrofit + Gson       | REST API client                 |
| ViewModel + StateFlow | Reactive state management       |
| Material 3            | Modern Android UI design system |


---

## 🚀 Getting Started

### Prerequisites

- Android Studio Hedgehog or later
- Android SDK 36
- Kotlin 1.9+
- Minimum SDK: 24

### Clone the repository

```bash
git clone https://github.com/hogandenver05/park-pulse.git
cd theme-parks
