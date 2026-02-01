const { GoogleAuth } = require('google-auth-library');
const axios = require('axios');

let firebaseProjectId = null;
let firebaseAuth = null;
let firebaseAuthClient = null;

const FIREBASE_MESSAGING_SCOPE = 'https://www.googleapis.com/auth/firebase.messaging';

// Initialize Firebase (FCM HTTP v1) using a service account
const initializeFirebase = (serviceAccountKey) => {
    if (!serviceAccountKey) {
        throw new Error('Missing Firebase service account credentials');
    }

    const { projectId, clientEmail, privateKey } = serviceAccountKey;
    if (!projectId || !clientEmail || !privateKey) {
        throw new Error('Firebase credentials must include projectId, clientEmail, and privateKey');
    }

    firebaseProjectId = projectId;
    firebaseAuth = new GoogleAuth({
        credentials: {
            client_email: clientEmail,
            private_key: privateKey
        },
        scopes: [FIREBASE_MESSAGING_SCOPE]
    });

    firebaseAuthClient = null;
};

const getAccessToken = async () => {
    if (!firebaseAuth) {
        throw new Error('Firebase is not initialized');
    }

    if (!firebaseAuthClient) {
        firebaseAuthClient = await firebaseAuth.getClient();
    }

    const tokenResponse = await firebaseAuthClient.getAccessToken();
    const token = typeof tokenResponse === 'string' ? tokenResponse : tokenResponse?.token;

    if (!token) {
        throw new Error('Failed to acquire Google access token for FCM');
    }

    return token;
};

const normalizeDataPayload = (data) => {
    const normalized = {};
    Object.entries(data || {}).forEach(([key, value]) => {
        normalized[key] = value == null ? '' : String(value);
    });
    return normalized;
};

// Send notification via Firebase Cloud Messaging HTTP v1
const sendNotification = async (deviceToken, title, body, data = {}) => {
    if (!firebaseProjectId) {
        throw new Error('Firebase is not initialized');
    }

    const accessToken = await getAccessToken();
    const url = `https://fcm.googleapis.com/v1/projects/${firebaseProjectId}/messages:send`;

    const payload = {
        message: {
            token: deviceToken,
            notification: {
                title,
                body
            },
            data: normalizeDataPayload(data)
        }
    };

    try {
        const response = await axios.post(url, payload, {
            headers: {
                Authorization: `Bearer ${accessToken}`
            }
        });

        console.log('Notification sent:', response.data?.name || response.status);
        return response.data;
    } catch (error) {
        const status = error?.response?.status;
        const message = error?.response?.data?.error?.message || error.message;
        console.error('Error sending notification:', status ? `${status} ${message}` : message);
        throw error;
    }
};

// Basic multicast helper: send one-by-one and return a summary
const sendMulticastNotification = async (deviceTokens, title, body, data = {}) => {
    const tokens = Array.isArray(deviceTokens) ? deviceTokens : [];
    const results = await Promise.allSettled(
        tokens.map((token) => sendNotification(token, title, body, data))
    );

    const successCount = results.filter((r) => r.status === 'fulfilled').length;
    const failureCount = results.length - successCount;

    console.log('Multicast notification sent:', { successCount, failureCount });
    return { successCount, failureCount, results };
};

module.exports = {
    initializeFirebase,
    sendNotification,
    sendMulticastNotification
};
