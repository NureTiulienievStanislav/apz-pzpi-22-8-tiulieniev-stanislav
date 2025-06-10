
# TrackWay — Програмна система керування автопарком

TrackWay — це система, яка дозволяє диспетчерам керувати автопарком, а водіям — отримувати завдання та повідомлення про стан авто. Система включає:
- 🌐 Web-інтерфейс для диспетчерів
- 📱 Android-додаток для водіїв
- 📡 IoT-модуль для передачі даних (front/back distance)

---

## 📦 Структура проєкту

```
trackway/
├── backend/        # Node.js + Express + MongoDB + MQTT API
├── frontend/       # React + Tailwind веб-інтерфейс
├── android/        # Android-додаток на Kotlin
├── iot/            # IoT-емулятор ESP32
```

---

## 🚀 Запуск Web-платформи

### Backend (Node.js + MongoDB)
```bash
cd backend
npm install
npm run dev
```
✅ Сервер запуститься на http://localhost:3000

### Frontend (React)
```bash
cd frontend
npm install
npm run dev
```
✅ Веб-інтерфейс буде доступний на http://localhost:5173

> ⚠️ У файлі `.env` backend-сервера переконайтесь, що вказано `MONGODB_URI`, `JWT_SECRET`, `MQTT_BROKER_URL`

---

## 📱 Запуск Android-додатку

1. Відкрийте проєкт у Android Studio (`/android`)
2. У `ApiClient.kt` вкажіть адресу сервера: `http://10.0.2.2:3000` (для емулятора)
3. Підключіть Android-емулятор або реальний пристрій
4. Запустіть проєкт (`Run > app`)

✅ Водій зможе увійти, переглядати авто та отримувати повідомлення

---

## 📡 Запуск IoT-емулятора (front_iot / back_iot)

Введіть VIN автомобіля, і кожні 5 сек буде надсилатись front/back distance до backend через MQTT.

---

## 🧠 Авторизація

- Ролі: `admin`, `driver`
- JWT токен зберігається у LocalStorage (web) та SharedPreferences (android)
- Усі запити вимагають заголовок:
```
Authorization: Bearer <ваш токен>
```

---

## 🗂️ Технічний стек

- **Frontend**: React, Tailwind CSS, Axios
- **Backend**: Node.js, Express, MongoDB, Mongoose, MQTT.js, JWT
- **Mobile**: Kotlin, Retrofit, Coroutines
- **IoT**: ESP32 / MQTT 

---

## 🔐 Безпека

- Паролі зберігаються як bcrypt-хеші
- Запити захищені JWT
- Веб доступ через CORS
- MQTT обмежений за IP

---

