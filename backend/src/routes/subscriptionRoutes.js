const express = require('express');
const subscriptionController = require('../controllers/subscriptionController');

const router = express.Router();

// Subscription routes
router.post('/:uid/create', subscriptionController.createSubscription);
router.get('/:uid/active', subscriptionController.getActiveSubscriptions);
router.get('/:uid/unused', subscriptionController.getUnusedSubscriptions);
router.put('/:subscriptionId', subscriptionController.updateSubscription);
router.put('/:subscriptionId/cancel', subscriptionController.cancelSubscription);

module.exports = router;
