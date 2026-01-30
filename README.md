# BillSafe - Smart Bill & Subscription Manager

A comprehensive Android app that automatically detects, tracks, and reminds users about their bills and subscriptions. Designed specifically for the Indian market with support for various payment formats (UPI, bank transfers, utilities).

## 🎯 Overview

BillSafe solves the problem of forgotten bills and subscriptions by:
- **Automatic Detection**: Parses SMS & email for bill information
- **Smart Reminders**: Sends timely notifications before due dates
- **Expense Tracking**: Monthly dashboards with spending insights
- **Subscription Management**: Warns about unused subscriptions
- **Secure Storage**: End-to-end encrypted local & cloud storage

## 📱 Features (MVP)

### Core Features
- ✅ SMS parsing for automatic bill detection
- ✅ Manual bill addition
- ✅ Monthly expense dashboard
- ✅ Due date reminders (3, 5, 7 days before)
- ✅ Bill categories (electricity, water, phone, subscriptions, etc.)
- ✅ Firebase Cloud Messaging for push notifications

### Advanced Features (Phase 2)
- AI-powered subscription detection
- Predictive billing amounts
- Unused subscription alerts (30+ days)
- Spending analytics & reports
- Export to PDF/Excel
- Integration with payment apps

## 🏗️ Architecture

### Tech Stack

**Frontend (Android)**
- Kotlin + Jetpack Compose
- Room Database (local storage)
- Hilt (dependency injection)
- Retrofit (REST API)
- Firebase (auth, notifications)
- EncryptedSharedPreferences (security)

**Backend**
- Node.js + Express
- MongoDB (database)
- Firebase Admin SDK
- Node-cron (scheduling)

## 📂 Project Structure

```
BillSafe/
├── android/                          # Android app
│   ├── app/
│   │   └── src/main/
│   │       ├── java/com/billsafe/
│   │       │   ├── BillSafeApp.kt              # App entry point
│   │       │   ├── MainActivity.kt             # Main activity
│   │       │   ├── data/
│   │       │   │   ├── api/                    # Retrofit API definitions
│   │       │   │   ├── dao/                    # Room DAO interfaces
│   │       │   │   ├── database/               # Room database
│   │       │   │   └── entities/               # Data models
│   │       │   ├── di/                         # Dependency injection (Hilt)
│   │       │   ├── receivers/                  # SMS receiver
│   │       │   ├── services/                   # Firebase messaging service
│   │       │   ├── ui/
│   │       │   │   ├── screens/                # Compose screens
│   │       │   │   ├── theme/                  # UI theme
│   │       │   │   └── viewmodels/             # MVVM ViewModels
│   │       │   └── utils/                      # SMS parser, encryption
│   │       └── res/                            # Android resources
│   └── build.gradle.kts                        # Build configuration
│
├── backend/                          # Node.js backend
│   ├── src/
│   │   ├── controllers/              # Business logic
│   │   │   ├── billController.js
│   │   │   ├── subscriptionController.js
│   │   │   └── userController.js
│   │   ├── models/                   # MongoDB schemas
│   │   │   ├── Bill.js
│   │   │   ├── Subscription.js
│   │   │   ├── User.js
│   │   │   └── Reminder.js
│   │   ├── routes/                   # API routes
│   │   │   ├── billRoutes.js
│   │   │   ├── subscriptionRoutes.js
│   │   │   └── userRoutes.js
│   │   ├── utils/                    # Utilities
│   │   │   ├── firebaseUtil.js       # FCM notifications
│   │   │   └── reminderScheduler.js  # Cron jobs
│   │   └── index.js                  # Server entry point
│   ├── package.json
│   └── .env                          # Environment variables
│
└── README.md                         # This file
```

## 🚀 Getting Started

### Prerequisites

**Android Development**
- Android Studio (latest)
- Kotlin 1.9+
- Gradle 8.0+
- Firebase Account
- Min SDK: 28, Target SDK: 34

**Backend Development**
- Node.js 18+
- MongoDB 6+ (local or Atlas)
- Firebase Admin SDK
- Postman (for testing)

### Android Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/safebilling00-art/billsafe.git
   cd billsafe/android
   ```

2. **Open in Android Studio**
   ```bash
   File → Open → Select android folder
   ```

3. **Configure Firebase**
   - Create Firebase project at console.firebase.google.com
   - Download `google-services.json` and place in `android/app/` directory
   
   **Enable Authentication:**
   - Go to Firebase Console → Authentication
   - Click "Get Started"
   - Enable "Email/Password" sign-in method
   - Click "Save"
   
   **Enable Cloud Messaging:**
   - Go to Firebase Console → Cloud Messaging
   - Click "Enable Cloud Messaging"
   - Copy "Server API Key" for backend configuration
   - Note the Sender ID for the app
   
   **Enable Analytics:**
   - Go to Firebase Console → Analytics
   - Click "Enable Google Analytics"
   - Choose "Create new Google Analytics account" or link existing
   - Complete the setup wizard
   - Verify analytics is active in the Console

4. **Update local.properties**
   ```properties
   sdk.dir=/path/to/Android/sdk
   ndk.dir=/path/to/Android/ndk
   ```

5. **Build and Run**
   ```bash
   ./gradlew build
   # Or press Shift + F10 in Android Studio
   ```

### Backend Setup

1. **Clone backend folder**
   ```bash
   cd backend
   ```

2. **Install dependencies**
   ```bash
   npm install
   ```

3. **Configure environment**
   - Copy `.env.example` to `.env`
   - Update MongoDB URI
   - Add Firebase credentials
   
   ```bash
   cp .env.example .env
   ```

4. **Add Firebase Service Account**
   - Get JSON from Firebase Console → Project Settings
   - Save as `firebase-key.json` in backend root

5. **Start MongoDB**
   ```bash
   # If using local MongoDB
   mongod
   ```

6. **Run server**
   ```bash
   npm run dev    # Development with nodemon
   npm start      # Production
   ```

   Server runs at `http://localhost:5000`

7. **Test API**
   ```bash
   curl http://localhost:5000/health
   ```

## 📡 API Endpoints


### Users
```
POST   /api/users/:uid              - Create/update user
GET    /api/users/:uid              - Get user details
GET    /api/users/:uid/stats        - Get user statistics
```

### Bills
```
POST   /api/bills/:uid/create       - Create bill
GET    /api/bills/:uid/active       - Get active bills
GET    /api/bills/:uid/all          - Get all bills
PUT    /api/bills/:billId           - Update bill
DELETE /api/bills/:billId           - Delete bill
PUT    /api/bills/:billId/mark-paid - Mark bill as paid
```

### Subscriptions
```
POST   /api/subscriptions/:uid/create         - Create subscription
GET    /api/subscriptions/:uid/active         - Get active subscriptions
GET    /api/subscriptions/:uid/unused         - Get unused subscriptions
PUT    /api/subscriptions/:subscriptionId    - Update subscription
PUT    /api/subscriptions/:subscriptionId/cancel - Cancel subscription
```

## 🔐 Security Features

1. **Encrypted Storage**
   - EncryptedSharedPreferences for sensitive data
   - AES-256-GCM encryption
   - MasterKey protection

2. **Network Security**
   - HTTPS only (no cleartext traffic)
   - Certificate pinning ready
   - Request/response encryption

3. **Authentication**
   - Firebase Authentication
   - JWT tokens for API
   - Refresh token rotation

4. **Data Protection**
   - MongoDB encryption at rest
   - Secure credential storage
   - Audit logging

## 📊 Database Schemas

### Bill
```javascript
{
  userId: ObjectId,
  billName: String,
  amount: Number,
  dueDate: Number (1-31),
  category: String (electricity|water|phone|etc),
  frequency: String (monthly|quarterly|yearly),
  reminderDaysBefore: Number,
  lastPaidDate: Date,
  source: String (manual|sms|email),
  isActive: Boolean
}
```

### Subscription
```javascript
{
  userId: ObjectId,
  appName: String,
  amount: Number,
  billingCycle: String,
  renewalDate: Date,
  status: String (active|cancelled|expired),
  isUsed: Boolean,
  lastUsedDate: Date,
  paymentMethod: String
}
```

## 🔔 Reminder System

- **Scheduler**: Runs daily at 9 AM
- **Trigger**: Compares reminder date with current date
- **Notification**: Sends FCM push notification
- **Format**: "Bill Reminder: [Bill Name] - ₹[Amount] due on [Date]th"
- **Storage**: Marks reminder as sent in DB

## 📈 Monetization Strategy

### 1. Freemium Model
- **Free Tier**: 5 bills, basic reminders
- **Premium (₹99-199/month)**: Unlimited bills, AI detection, analytics

### 2. Affiliate Revenue
- Credit card recommendations
- Loan & insurance products
- Broadband/ISP upgrades
- Subscription cancellation services

### 3. B2B Version
- White-label for colleges
- Utility provider integrations
- SME expense management

## 🛣️ Roadmap

### Phase 1 (Current)
- [x] Core bill tracking
- [x] SMS parsing
- [x] Manual bill entry
- [x] Basic reminders
- [ ] Bug fixes & stability

### Phase 2 (Q1 2026)
- [ ] AI subscription detection
- [ ] Spending analytics
- [ ] Export reports (PDF)
- [ ] Multiple user accounts
- [ ] Dark mode

### Phase 3 (Q2 2026)
- [ ] Premium subscription model
- [ ] Affiliate integrations
- [ ] Offline sync
- [ ] Web dashboard
- [ ] Payment gateway integration

### Phase 4 (Q3 2026)
- [ ] ML-based bill prediction
- [ ] Chatbot support
- [ ] Smart categorization
- [ ] Multi-language support
- [ ] WhatsApp integration

## 🧪 Testing

### Android Tests
```bash
./gradlew test                  # Unit tests
./gradlew connectedAndroidTest  # Instrumented tests
```

### Backend Tests
```bash
npm test
```

### API Testing
```bash
# Using Postman collection (included in repo)
- Import: BillSafe.postman_collection.json
- Set variables and test endpoints
```

## 🐛 Troubleshooting

### Android Issues

**"build.gradle error"**
- Sync Gradle files
- Clear cache: Build → Clean Project
- Invalidate caches: File → Invalidate Caches

**"SMS permission denied"**
- Check manifest permissions
- Request runtime permissions (Android 6+)
- Check device SMS app defaults

**Firebase notifications not working**
- Verify google-services.json is present
- Check FCM token generation
- Test via Firebase Console

### Backend Issues

**"Cannot connect to MongoDB"**
```bash
# Check MongoDB is running
mongosh  # Connect to local instance
# Or update MONGODB_URI in .env
```

**"Firebase credentials error"**
- Verify firebase-key.json path
- Check environment variables loaded
- Validate JSON format

**"Port 5000 already in use"**
```bash
# Kill process using port
# Windows
netstat -ano | findstr :5000
taskkill /PID <PID> /F

# Linux/Mac
lsof -i :5000
kill -9 <PID>
```

## 📚 Documentation

- [Android Architecture](docs/ANDROID_ARCHITECTURE.md)
- [Backend API Docs](docs/API_REFERENCE.md)
- [SMS Parser Guide](docs/SMS_PARSER.md)
- [Security Implementation](docs/SECURITY.md)
- [Deployment Guide](docs/DEPLOYMENT.md)

## 🤝 Contributing

1. Fork the repository
2. Create feature branch: `git checkout -b feature/amazing-feature`
3. Commit changes: `git commit -m 'Add amazing feature'`
4. Push to branch: `git push origin feature/amazing-feature`
5. Open Pull Request

### Code Standards
- Kotlin style guide
- Meaningful variable/function names
- Comments for complex logic
- Unit tests for new features

## 📄 License

MIT License - see [LICENSE](LICENSE) file

## 👥 Authors

- Your Name (@github)

## 📞 Support

- **Issues**: GitHub Issues
- **Email**: support@billsafe.app
- **Discord**: [Join Server](https://discord.gg/billsafe)

## 🎓 Educational Value

This project demonstrates:
- ✅ Full-stack Android development
- ✅ Secure local & cloud storage
- ✅ REST API design
- ✅ Database modeling
- ✅ Real-time notifications
- ✅ Job scheduling & automation
- ✅ Encryption & security best practices
- ✅ MVVM architecture
- ✅ Dependency injection
- ✅ Firebase integration

Perfect for:
- Portfolio projects
- Interview preparation
- Startup MVP
- App Security Engineer profile building

---

**Built with ❤️ for financial awareness**
