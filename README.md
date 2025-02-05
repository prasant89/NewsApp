# Modern Android News App

A modern Android News App built using Jetpack Compose, Hilt, Room, and MVVM architecture. Fetch the latest news from an API, view offline news, and explore articles in a sleek UI.

## 🚀 Features
- ✅ Fetch live news articles using an API.
- ✅ View news details with images, title, and content.
- ✅ Offline support using Room Database.
- ✅ Modern UI with Jetpack Compose.
- ✅ Dependency Injection with Hilt.
- ✅ Kotlin Coroutines + Flow for async data handling.

## 🛠️ Tech Stack
- **Language**: Kotlin
- **UI**: Jetpack Compose
- **Architecture**: MVVM
- **Dependency Injection**: Hilt
- **Networking**: Retrofit + Gson
- **Local Database**: Room
- **Coroutines & Flow**: Async operations
- **Image Loading**: Coil

## 📸 Screenshots
![News List Page](https://github.com/user-attachments/assets/5b1ec785-8a3c-45a6-8644-0effb7653ac4)
![News Details](https://github.com/user-attachments/assets/57157acb-13ef-4288-9aa7-9a0c08957251)

## 🔧 Setup & Installation
1. **Clone the Repository**  
   ```bash

    git clone https://github.com/prasant89/NewsApp.git
   cd NewsApp

Open in Android Studio
Open the project in Android Studio (latest version recommended).
Ensure you have Gradle Sync completed successfully.
Set Up API Key
The app fetches news from an external API (e.g., NewsAPI.org).
Add your API Key in local.properties (or directly in Constants.kt):
NEWS_API_KEY="your_api_key_here"
Run the App
Select a device/emulator and hit Run ▶ in Android Studio.

## 📡 API Integration
This app fetches data from NewsAPI. To change the API endpoint, modify FetchNewsUseCase.kt and NewsRepository.kt.

## 📜 License
This project is open-source. Feel free to contribute!

## 👨‍💻 Author
Prasant Pradhan | GitHub
