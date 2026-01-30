# Railway deployment (backend)

This backend is a Node.js (Express) service located in the `backend/` folder.

## Deploy from GitHub

1. Push this repo to GitHub.
2. In Railway: **New Project** → **Deploy from GitHub repo**
3. Set **Root Directory** to `backend`
4. Railway should auto-detect Node.js and run:
   - Build: `npm install` (or `npm ci` if you prefer)
   - Start: `npm start`

## Environment variables

Add the variables from `backend/.env.example` in Railway → **Project** → **Variables**.

Minimum required:
- `MONGODB_URI`
- `JWT_SECRET`
- `PORT` (Railway usually injects this automatically)

Optional (for push notifications / reminders):
- `FIREBASE_PROJECT_ID`
- `FIREBASE_CLIENT_EMAIL`
- `FIREBASE_PRIVATE_KEY` (store with `\n` newlines; many providers require escaping newlines)

## Health check

The service exposes `GET /health`.
