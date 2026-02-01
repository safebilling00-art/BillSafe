const cron = require('node-cron');
const Reminder = require('../models/Reminder');
const Bill = require('../models/Bill');
const User = require('../models/User');
const firebaseUtil = require('./firebaseUtil');

// Run reminder checks every day at 9 AM
const initializeReminderScheduler = (options = {}) => {
    const { notificationsEnabled = true } = options;

    if (!notificationsEnabled) {
        console.log('Reminder scheduler disabled (push notifications not configured)');
        return;
    }

    cron.schedule('0 9 * * *', async () => {
        console.log('Running reminder check...');
        
        try {
            const today = new Date();
            today.setHours(0, 0, 0, 0);

            // Find reminders that should be sent today
            const pendingReminders = await Reminder.find({
                sent: false,
                reminderDate: { $lte: today }
            }).populate('userId').populate('billId');

            for (const reminder of pendingReminders) {
                try {
                    const user = reminder.userId;
                    const bill = reminder.billId;

                    if (user.fcmToken && user.notificationsEnabled) {
                        await firebaseUtil.sendNotification(
                            user.fcmToken,
                            `Bill Reminder: ${bill.billName}`,
                            `Payment of ₹${bill.amount} is due on ${bill.dueDate}th`,
                            {
                                billId: bill._id.toString(),
                                amount: bill.amount.toString()
                            }
                        );

                        // Mark reminder as sent
                        reminder.sent = true;
                        reminder.sentAt = new Date();
                        await reminder.save();
                    }
                } catch (error) {
                    console.error('Error sending reminder:', error);
                }
            }

            console.log(`Sent ${pendingReminders.length} reminders`);
        } catch (error) {
            console.error('Error in reminder scheduler:', error);
        }
    });
};

module.exports = {
    initializeReminderScheduler
};
