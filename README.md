# 🎢 Theme Parks

**Theme Parks** is a modern Android application built in Kotlin that displays a list of amusement park rides and their current wait times. It utilizes clean architecture, Hilt for dependency injection, Retrofit for networking, and follows best practices like ViewModel and state-driven UI updates with Jetpack components.

---

## 📱 Features

- Fetches ride data from the [Queue-Times API](https://queue-times.com/)
- Displays live wait times for rides
- Indicates ride open/closed status
- Refreshes ride data using reactive state management
- Navigation to detailed view for each ride
- Built with modern Android stack: Kotlin, Hilt, Retrofit, ViewBinding, and more

---

## 🔧 Tech Stack

| Tech                  | Purpose                   |
|-----------------------|---------------------------|
| Kotlin                | Main programming language |
| Retrofit + Gson       | HTTP client for REST API  |
| Dagger Hilt           | Dependency Injection      |
| ViewModel + StateFlow | State management          |
| ViewBinding           | Type-safe view access     |
| RecyclerView          | Efficient list rendering  |

---

## 🚀 Getting Started

### Prerequisites

- Android Studio Hedgehog or later
- Android SDK 36
- Kotlin 1.9+
- Minimum SDK: 24

### Clone the repository

```bash
git clone https://github.com/denverhogan/theme-parks.git
cd theme-parks
