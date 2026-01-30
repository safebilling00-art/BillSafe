const mongoose = require('mongoose');

const subscriptionSchema = new mongoose.Schema({
    userId: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'User',
        required: true
    },
    appName: {
        type: String,
        required: true
    },
    amount: {
        type: Number,
        required: true
    },
    billingCycle: {
        type: String,
        enum: ['monthly', 'quarterly', 'yearly', 'one-time'],
        default: 'monthly'
    },
    startDate: {
        type: Date,
        required: true
    },
    renewalDate: {
        type: Date,
        required: true
    },
    status: {
        type: String,
        enum: ['active', 'cancelled', 'expired'],
        default: 'active'
    },
    isUsed: {
        type: Boolean,
        default: true
    },
    lastUsedDate: Date,
    paymentMethod: String,
    createdAt: {
        type: Date,
        default: Date.now
    }
});

// Index for efficient queries
subscriptionSchema.index({ userId: 1, status: 1 });
subscriptionSchema.index({ userId: 1, renewalDate: 1 });

module.exports = mongoose.model('Subscription', subscriptionSchema);
