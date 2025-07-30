# 📊 Personal Finance Tracker API

Ushbu loyiha foydalanuvchilarga o‘zining oylik moliyaviy harajat va daromadlarini boshqarish, ko‘rish, tahlil qilish va ularni PDF yoki Excel formatida eksport qilish imkonini beradi. REST API Java va Spring Boot yordamida yozilgan.

---

## 🚀 Texnologiyalar

- ⚙️ **Platforma:** Spring Boot 3+
- 💻 **Til:** Java 17+
- 🗃️ **Ma'lumotlar bazasi:** PostgreSQL
- 🛠️ **ORM:** Spring Data JPA
- 📄 **PDF generatsiya:** OpenPDF
- 📊 **Excel eksport:** Apache POI
- 🕐 **Vaqt hisoblash:** Java Time API
- 🔐 **Avtorizatsiya:** JWT (Bearer token)
- 📘 **API hujjatlari:** Springdoc Swagger / OpenAPI

---

## 📂 Loyihani ishga tushurish

### 1. Kodni yuklab oling
```bash
``
git clone https://github.com/anvarSharif/Personal_finance_tracker.git

```

### 2. Sozlamalar (`application.properties`)
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres_db
spring.datasource.username=postgres
spring.datasource.password=your-password
```

### 3. Maven orqali build
```bash
mvn clean
mvn package
```

### 4. Docker orqali ishga tushirish
```bash
docker-compose up --build
```

---

## 🔐 JWT orqali avtorizatsiya

1. `/api/users/login` endpoint orqali login qilinadi.
2. Javobda JWT token qaytadi.
3. Boshqa barcha so‘rovlar uchun `Authorization` header yuboring:

```
Authorization: Bearer <JWT_TOKEN>
```

---

## 🔍 Swagger UI

API hujjatlarini Swagger orqali quyidagi manzilda ko‘rishingiz mumkin:

```
http://localhost:8080/swagger-ui/index.html
```

Swagger OpenAPI konfiguratsiyasi orqali JWT token bilan ishlash ham qo‘llab-quvvatlanadi.

---

## 📮 Muhim Endpointlar

### 👤 Foydalanuvchi autentifikatsiyasi

| Method | Endpoint            | Tavsif             |
|--------|---------------------|---------------------|
| POST   | /api/users/register | Ro‘yxatdan o‘tish   |
| POST   | /api/users/login    | Login qilish        |

### 💰 Tranzaksiyalar

| Method | Endpoint               | Tavsif                        |
|--------|------------------------|-------------------------------|
| GET    | /api/transactions      | Oylik tranzaksiyalar ro‘yxati |
| POST   | /api/transactions      | Yangi tranzaksiya qo‘shish    |

### 📦 Eksport (hisobotlar)

| Method | Endpoint               | Tavsif                 |
|--------|------------------------|-------------------------|
| GET    | /api/summary/pdf       | Oylik PDF hisobot       |
| GET    | /api/summary/excel     | Oylik Excel hisobot     |

---

## 📁 Foydalanish bo‘yicha misol

Endpointlarda `month` parametri quyidagi formatda yuboriladi: `yyyy-MM`.

Transaction saqlashda type  `INCOME`,`EXPENSE` bo'lishi shart.

**Misol so‘rov:**
```
GET /api/transactions?month=2025-07
Authorization: Bearer eyJhbGciOiJIUzI1...
```

PDF yoki Excel eksport qilishda ham `month` parametri kerak bo‘ladi:
```
GET /api/summary/pdf?month=2025-07
GET /api/summary/excel?month=2025-07
```

---

## 👨‍💻 Muallif

- Ismi: **Anvar Hasanov**
- GitHub: [@anvarSharif](https://github.com/anvarSharif)
- Telegram: [@Anvarsharif](https://t.me/Anvarsharif)
