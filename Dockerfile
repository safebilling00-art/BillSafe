# syntax=docker/dockerfile:1

FROM node:20-alpine AS deps
WORKDIR /app

COPY backend/package.json backend/package-lock.json ./
RUN npm ci --omit=dev

FROM node:20-alpine AS runner
WORKDIR /app
ENV NODE_ENV=production

COPY --from=deps /app/node_modules ./node_modules
COPY backend/src ./src
COPY backend/package.json ./package.json

EXPOSE 5000
CMD ["npm", "start"]

