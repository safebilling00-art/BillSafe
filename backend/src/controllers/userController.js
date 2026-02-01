const User = require('../models/User');
const Bill = require('../models/Bill');

// Create or update user
exports.createUser = async (req, res) => {
    try {
        const { uid } = req.params;
        const { email, name, phoneNumber, fcmToken } = req.body;

        if (!uid) {
            return res.status(400).json({ success: false, error: 'Missing uid' });
        }

        if (!email) {
            return res.status(400).json({ success: false, error: 'Missing email' });
        }

        let user = await User.findOne({ uid });

        // If the user exists in Mongo by email (e.g., Firebase uid changed), re-link it to the new uid.
        if (!user) {
            const userByEmail = await User.findOne({ email });
            if (userByEmail) {
                user = userByEmail;
                user.uid = uid;
            }
        }
        
        if (user) {
            // Update existing user
            user.email = email;
            user.name = name ?? user.name;
            user.phoneNumber = phoneNumber ?? user.phoneNumber;
            user.fcmToken = fcmToken || user.fcmToken;
            user.updatedAt = new Date();
            await user.save();
        } else {
            // Create new user
            user = new User({
                uid,
                email,
                name,
                phoneNumber,
                fcmToken
            });
            await user.save();
        }

        console.log(`Upserted user uid=${uid} email=${email} hasFcmToken=${Boolean(user.fcmToken)}`);
        res.status(200).json({ success: true, data: user });
    } catch (error) {
        if (error?.code === 11000) {
            return res.status(409).json({ success: false, error: 'User already exists' });
        }
        res.status(500).json({ success: false, error: error.message });
    }
};

// Get user details
exports.getUser = async (req, res) => {
    try {
        const { uid } = req.params;
        const user = await User.findOne({ uid });

        if (!user) {
            return res.status(404).json({ success: false, error: 'User not found' });
        }

        res.status(200).json({ success: true, data: user });
    } catch (error) {
        res.status(500).json({ success: false, error: error.message });
    }
};

// Get user's bill statistics
exports.getUserStats = async (req, res) => {
    try {
        const { uid } = req.params;
        const user = await User.findOne({ uid });

        if (!user) {
            return res.status(404).json({ success: false, error: 'User not found' });
        }

        const bills = await Bill.find({ userId: user._id, isActive: true });
        const monthlyTotal = bills
            .filter(b => b.frequency === 'monthly')
            .reduce((sum, b) => sum + b.amount, 0);

        const stats = {
            totalBills: bills.length,
            monthlyExpense: monthlyTotal,
            categories: {}
        };

        bills.forEach(bill => {
            if (!stats.categories[bill.category]) {
                stats.categories[bill.category] = 0;
            }
            stats.categories[bill.category]++;
        });

        res.status(200).json({ success: true, data: stats });
    } catch (error) {
        res.status(500).json({ success: false, error: error.message });
    }
};
