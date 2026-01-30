require('dotenv').config();
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

const app = express();

// Middleware
app.use(helmet());
app.use(cors());
app.use(express.json());
app.use(express.urlencoded({ extended: true }));

// Rate limiting
const limiter = rateLimit({
    windowMs: 15 * 60 * 1000, // 15 minutes
    max: 100 // limit each IP to 100 requests per windowMs
});
app.use(limiter);

// Database connection
mongoose.connect(process.env.MONGODB_URI, {
    useNewUrlParser: true,
    useUnifiedTopology: true
}).then(() => {
    console.log('Connected to MongoDB');
}).catch(error => {
    console.error('MongoDB connection error:', error);
    process.exit(1);
});

// Routes
app.use('/api/users', userRoutes);
app.use('/api/bills', billRoutes);
app.use('/api/subscriptions', subscriptionRoutes);

// Health check
app.get('/health', (req, res) => {
    res.status(200).json({ status: 'OK', message: 'BillSafe API is running' });
});

// Initialize reminder scheduler
initializeReminderScheduler();

// Error handling middleware
app.use((err, req, res, next) => {
    console.error('Error:', err);
    res.status(500).json({ success: false, error: err.message });
});

// Start server
const PORT = process.env.PORT || 5000;
app.listen(PORT, () => {
    console.log(`BillSafe server running on port ${PORT}`);
});

module.exports = app;
