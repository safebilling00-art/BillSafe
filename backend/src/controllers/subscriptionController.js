const Subscription = require('../models/Subscription');
const User = require('../models/User');

// Create a new subscription
exports.createSubscription = async (req, res) => {
    try {
        const { uid } = req.params;
        const { appName, amount, billingCycle, startDate, renewalDate, paymentMethod } = req.body;

        const user = await User.findOne({ uid });
        if (!user) {
            return res.status(404).json({ success: false, error: 'User not found' });
        }

        const subscription = new Subscription({
            userId: user._id,
            appName,
            amount,
            billingCycle,
            startDate: new Date(startDate),
            renewalDate: new Date(renewalDate),
            paymentMethod
        });

        await subscription.save();
        res.status(201).json({ success: true, data: subscription });
    } catch (error) {
        res.status(500).json({ success: false, error: error.message });
    }
};

// Get active subscriptions
exports.getActiveSubscriptions = async (req, res) => {
    try {
        const { uid } = req.params;
        const user = await User.findOne({ uid });

        if (!user) {
            return res.status(404).json({ success: false, error: 'User not found' });
        }

        const subscriptions = await Subscription.find({
            userId: user._id,
            status: 'active'
        }).sort({ renewalDate: 1 });

        res.status(200).json({ success: true, data: subscriptions });
    } catch (error) {
        res.status(500).json({ success: false, error: error.message });
    }
};

// Get unused subscriptions (not used in 30+ days)
exports.getUnusedSubscriptions = async (req, res) => {
    try {
        const { uid } = req.params;
        const user = await User.findOne({ uid });

        if (!user) {
            return res.status(404).json({ success: false, error: 'User not found' });
        }

        const thirtyDaysAgo = new Date();
        thirtyDaysAgo.setDate(thirtyDaysAgo.getDate() - 30);

        const subscriptions = await Subscription.find({
            userId: user._id,
            status: 'active',
            isUsed: false,
            $or: [
                { lastUsedDate: null },
                { lastUsedDate: { $lt: thirtyDaysAgo } }
            ]
        });

        res.status(200).json({ success: true, data: subscriptions });
    } catch (error) {
        res.status(500).json({ success: false, error: error.message });
    }
};

// Update subscription
exports.updateSubscription = async (req, res) => {
    try {
        const { subscriptionId } = req.params;
        const updates = req.body;

        const subscription = await Subscription.findByIdAndUpdate(subscriptionId, updates, { new: true });

        if (!subscription) {
            return res.status(404).json({ success: false, error: 'Subscription not found' });
        }

        res.status(200).json({ success: true, data: subscription });
    } catch (error) {
        res.status(500).json({ success: false, error: error.message });
    }
};

// Cancel subscription
exports.cancelSubscription = async (req, res) => {
    try {
        const { subscriptionId } = req.params;

        const subscription = await Subscription.findByIdAndUpdate(
            subscriptionId,
            { status: 'cancelled' },
            { new: true }
        );

        if (!subscription) {
            return res.status(404).json({ success: false, error: 'Subscription not found' });
        }

        res.status(200).json({ success: true, data: subscription });
    } catch (error) {
        res.status(500).json({ success: false, error: error.message });
    }
};
