const Bill = require('../models/Bill');
const User = require('../models/User');
const Reminder = require('../models/Reminder');

// Create a new bill
exports.createBill = async (req, res) => {
    try {
        const { uid } = req.params;
        const { billName, amount, dueDate, category, frequency, description, reminderDaysBefore } = req.body;

        const user = await User.findOne({ uid });
        if (!user) {
            return res.status(404).json({ success: false, error: 'User not found' });
        }

        const bill = new Bill({
            userId: user._id,
            billName,
            amount,
            dueDate,
            category,
            frequency,
            description,
            reminderDaysBefore: reminderDaysBefore || 3
        });

        await bill.save();

        // Create reminder
        const nextReminderDate = calculateNextReminderDate(dueDate, reminderDaysBefore || 3);
        const reminder = new Reminder({
            userId: user._id,
            billId: bill._id,
            reminderDate: nextReminderDate
        });
        await reminder.save();

        res.status(201).json({ success: true, data: bill });
    } catch (error) {
        res.status(500).json({ success: false, error: error.message });
    }
};

// Get all bills for a user
exports.getUserBills = async (req, res) => {
    try {
        const { uid } = req.params;
        const user = await User.findOne({ uid });

        if (!user) {
            return res.status(404).json({ success: false, error: 'User not found' });
        }

        const bills = await Bill.find({ userId: user._id }).sort({ dueDate: 1 });
        res.status(200).json({ success: true, data: bills });
    } catch (error) {
        res.status(500).json({ success: false, error: error.message });
    }
};

// Get active bills for a user
exports.getActiveBills = async (req, res) => {
    try {
        const { uid } = req.params;
        const user = await User.findOne({ uid });

        if (!user) {
            return res.status(404).json({ success: false, error: 'User not found' });
        }

        const bills = await Bill.find({ userId: user._id, isActive: true }).sort({ dueDate: 1 });
        res.status(200).json({ success: true, data: bills });
    } catch (error) {
        res.status(500).json({ success: false, error: error.message });
    }
};

// Update a bill
exports.updateBill = async (req, res) => {
    try {
        const { billId } = req.params;
        const updates = req.body;

        const bill = await Bill.findByIdAndUpdate(billId, updates, { new: true });

        if (!bill) {
            return res.status(404).json({ success: false, error: 'Bill not found' });
        }

        res.status(200).json({ success: true, data: bill });
    } catch (error) {
        res.status(500).json({ success: false, error: error.message });
    }
};

// Delete a bill
exports.deleteBill = async (req, res) => {
    try {
        const { billId } = req.params;

        const bill = await Bill.findByIdAndDelete(billId);

        if (!bill) {
            return res.status(404).json({ success: false, error: 'Bill not found' });
        }

        // Delete associated reminders
        await Reminder.deleteMany({ billId });

        res.status(200).json({ success: true, data: { message: 'Bill deleted successfully' } });
    } catch (error) {
        res.status(500).json({ success: false, error: error.message });
    }
};

// Mark bill as paid
exports.markBillPaid = async (req, res) => {
    try {
        const { billId } = req.params;

        const bill = await Bill.findByIdAndUpdate(
            billId,
            { lastPaidDate: new Date() },
            { new: true }
        );

        if (!bill) {
            return res.status(404).json({ success: false, error: 'Bill not found' });
        }

        res.status(200).json({ success: true, data: bill });
    } catch (error) {
        res.status(500).json({ success: false, error: error.message });
    }
};

function calculateNextReminderDate(dueDate, reminderDaysBefore) {
    const today = new Date();
    let reminderDate = new Date(today.getFullYear(), today.getMonth(), dueDate);
    
    reminderDate.setDate(reminderDate.getDate() - reminderDaysBefore);

    // If reminder date is in the past, schedule for next month
    if (reminderDate < today) {
        reminderDate.setMonth(reminderDate.getMonth() + 1);
    }

    return reminderDate;
}
