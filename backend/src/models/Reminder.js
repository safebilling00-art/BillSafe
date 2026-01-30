const mongoose = require('mongoose');

const reminderSchema = new mongoose.Schema({
    userId: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'User',
        required: true
    },
    billId: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'Bill',
        required: true
    },
    reminderDate: {
        type: Date,
        required: true
    },
    sent: {
        type: Boolean,
        default: false
    },
    sentAt: Date,
    createdAt: {
        type: Date,
        default: Date.now
    }
});

// Index for querying pending reminders
reminderSchema.index({ userId: 1, sent: 1, reminderDate: 1 });

module.exports = mongoose.model('Reminder', reminderSchema);
