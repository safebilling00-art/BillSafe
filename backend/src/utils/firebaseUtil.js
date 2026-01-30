const admin = require('firebase-admin');

// Initialize Firebase Admin
const initializeFirebase = (serviceAccountKey) => {
    if (!admin.apps.length) {
        admin.initializeApp({
            credential: admin.credential.cert(serviceAccountKey)
        });
    }
    return admin;
};

// Send notification via Firebase Cloud Messaging
const sendNotification = async (deviceToken, title, body, data = {}) => {
    try {
        const message = {
            notification: {
                title,
                body
            },
            data,
            token: deviceToken
        };

        const response = await admin.messaging().send(message);
        console.log('Notification sent:', response);
        return response;
    } catch (error) {
        console.error('Error sending notification:', error);
        throw error;
    }
};

// Send multicast notification
const sendMulticastNotification = async (deviceTokens, title, body, data = {}) => {
    try {
        const message = {
            notification: {
                title,
                body
            },
            data
        };

        const response = await admin.messaging().sendMulticast({
            ...message,
            tokens: deviceTokens
        });

        console.log('Multicast notification sent:', response);
        return response;
    } catch (error) {
        console.error('Error sending multicast notification:', error);
        throw error;
    }
};

module.exports = {
    initializeFirebase,
    sendNotification,
    sendMulticastNotification
};
