const express = require('express');
const userController = require('../controllers/userController');

const router = express.Router();

// User routes
router.post('/:uid', userController.createUser);
router.get('/:uid', userController.getUser);
router.get('/:uid/stats', userController.getUserStats);

module.exports = router;
