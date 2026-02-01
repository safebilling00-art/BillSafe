require('dotenv').config();
const http = require('http');
const express = require('express');
const mongoose = require('mongoose');
const cors = require('cors');
const helmet = require('helmet');
const rateLimit = require('express-rate-limit');

// Import routes
const userRoutes = require('./routes/userRoutes');
const billRoutes = require('./routes/billRoutes');
const subscriptionRoutes = require('./routes/subscriptionRoutes');

// Import utilities
const { initializeReminderScheduler } = require('./utils/reminderScheduler');
const { initializeFirebase } = require('./utils/firebaseUtil');

const app = express();
app.set('trust proxy', 1);

// Middleware
app.use(helmet());
app.use(cors());
app.use(express.json());
app.use(express.urlencoded({ extended: true }));

// Health check (not rate-limited)
const healthHandler = (req, res) => {
    res.status(200).json({ status: 'OK', message: 'BillSafe API is running' });
};
app.get('/', healthHandler);
app.get(['/health', '/Health', '/healthz'], healthHandler);

// Rate limiting
const limiter = rateLimit({
    windowMs: 15 * 60 * 1000, // 15 minutes
    max: 100 // limit each IP to 100 requests per windowMs
});
app.use('/api', limiter);

// Routes
app.use('/api/users', userRoutes);
app.use('/api/bills', billRoutes);
app.use('/api/subscriptions', subscriptionRoutes);

// Error handling middleware
app.use((err, req, res, next) => {
    console.error('Error:', err);
    res.status(500).json({ success: false, error: err.message });
});

async function startServer() {
    const mongoUri = process.env.MONGODB_URI;
    if (!mongoUri) {
        console.error('MongoDB connection error: MONGODB_URI is not set');
        process.exit(1);
    }

    try {
        await mongoose.connect(mongoUri);
        console.log('Connected to MongoDB');
    } catch (error) {
        console.error('MongoDB connection error:', error);
        process.exit(1);
    }

    // Firebase messaging init (optional)
    let firebaseNotificationsEnabled = false;
    const firebaseConfigured = Boolean(
        process.env.FIREBASE_PROJECT_ID &&
        process.env.FIREBASE_CLIENT_EMAIL &&
        process.env.FIREBASE_PRIVATE_KEY
    );

    if (firebaseConfigured) {
        try {
            initializeFirebase({
                projectId: process.env.FIREBASE_PROJECT_ID,
                clientEmail: process.env.FIREBASE_CLIENT_EMAIL,
                privateKey: process.env.FIREBASE_PRIVATE_KEY.replace(/\\n/g, '\n')
            });
            firebaseNotificationsEnabled = true;
            console.log('Firebase messaging configured');
        } catch (error) {
            console.error('Firebase messaging configuration error:', error.message);
        }
    } else {
        console.log('Firebase messaging not configured; push notifications disabled');
    }

    // Initialize reminder scheduler (requires MongoDB; uses FCM if configured)
    initializeReminderScheduler({ notificationsEnabled: firebaseNotificationsEnabled });

    const PORT = process.env.PORT || 5000;
    const server = http.createServer(app);
    server.on('error', (error) => {
        if (error.code === 'EADDRINUSE') {
            console.error(`Port ${PORT} is already in use. Stop the other process or set a different PORT in .env.`);
            process.exit(1);
        }
        console.error('Server error:', error);
        process.exit(1);
    });

    server.listen(PORT, () => {
        console.log(`BillSafe server running on port ${PORT}`);
    });
}

if (require.main === module) {
    startServer();
}

module.exports = app;
