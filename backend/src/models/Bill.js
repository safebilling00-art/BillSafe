const mongoose = require('mongoose');

const billSchema = new mongoose.Schema({
    userId: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'User',
        required: true
    },
    billName: {
        type: String,
        required: true
    },
    amount: {
        type: Number,
        required: true
    },
    dueDate: {
        type: Number,
        required: true,
        min: 1,
        max: 31
    },
    category: {
        type: String,
        enum: ['electricity', 'water', 'phone', 'credit_card', 'internet', 'subscription', 'other'],
        default: 'other'
    },
    frequency: {
        type: String,
        enum: ['monthly', 'quarterly', 'yearly', 'one-time'],
        default: 'monthly'
    },
    description: String,
    isActive: {
        type: Boolean,
        default: true
    },
    reminderDaysBefore: {
        type: Number,
        default: 3
    },
    lastPaidDate: Date,
    source: {
        type: String,
        enum: ['manual', 'sms', 'email'],
        default: 'manual'
    },
    createdAt: {
        type: Date,
        default: Date.now
    },
    updatedAt: {
        type: Date,
        default: Date.now
    }
});

// Index for efficient queries
billSchema.index({ userId: 1, dueDate: 1 });
billSchema.index({ userId: 1, isActive: 1 });

module.exports = mongoose.model('Bill', billSchema);
