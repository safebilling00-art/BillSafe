const express = require('express');
const billController = require('../controllers/billController');

const router = express.Router();

// Bill routes
router.post('/:uid/create', billController.createBill);
router.get('/:uid/all', billController.getUserBills);
router.get('/:uid/active', billController.getActiveBills);
router.put('/:billId', billController.updateBill);
router.delete('/:billId', billController.deleteBill);
router.put('/:billId/mark-paid', billController.markBillPaid);

module.exports = router;
