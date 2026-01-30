# FlipFit API Testing Guide

## Starting the Server

```bash
# Build the project (if not already built)
mvn clean package -DskipTests -Dmaven.javadoc.skip=true

# Start the server
java -jar target/flipfit_d-1.0-SNAPSHOT.jar server config.yml
```

The server will start on:
- Application: http://localhost:8080
- Admin UI: http://localhost:8081

## Postman/cURL Examples

### 1. Register a Customer
```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "email": "john@example.com",
    "password": "password123",
    "roleChoice": 2
  }'
```

**Postman:**
- URL: `http://localhost:8080/api/users/register`
- Method: POST
- Headers: `Content-Type: application/json`
- Body (raw JSON):
```json
{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "password123",
  "roleChoice": 2
}
```

### 2. Login
```bash
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "password": "password123"
  }'
```

**Postman:**
- URL: `http://localhost:8080/api/users/login`
- Method: POST
- Headers: `Content-Type: application/json`
- Body (raw JSON):
```json
{
  "username": "john_doe",
  "password": "password123"
}
```

### 3. Admin Login (Default Credentials)
```bash
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'
```

### 4. View All Gym Centers
```bash
curl -X GET http://localhost:8080/api/centers
```

**Postman:**
- URL: `http://localhost:8080/api/centers`
- Method: GET

### 5. Book a Slot (Customer)
```bash
curl -X POST http://localhost:8080/api/customers/john_doe/bookings \
  -H "Content-Type: application/json" \
  -d '{
    "slotId": "SLOT1234"
  }'
```

### 6. Approve Gym Owner (Admin)
```bash
curl -X PUT http://localhost:8080/api/admin/owners/owner123/approve \
  -H "Content-Type: application/json" \
  -d '{}'
```

###7. Add Gym Center (Gym Owner)
```bash
curl -X POST http://localhost:8080/api/owners/owner123/centers \
  -H "Content-Type: application/json" \
  -d '{
    "name": "FitZone Gym",
    "location": "Downtown"
  }'
```

## Common Issues & Solutions

### Issue: 404 Not Found

**Possible Causes:**
1. Server is not running
2. Wrong URL (missing `/api` prefix)
3. Wrong HTTP method

**Solutions:**
- Ensure server is running: `java -jar target/flipfit_d-1.0-SNAPSHOT.jar server config.yml`
- Check URL includes `/api` prefix: `http://localhost:8080/api/users/login`
- Verify HTTP method matches endpoint (POST for login, GET for listings, etc.)

### Issue: 500 Internal Server Error

**Possible Causes:**
1. Database connection issues
2. .env file not properly configured

**Solutions:**
- Check `.env` file exists in project root
- Verify database is running: `mysql -u root -p`
- Check database name matches in `.env`: `DB_URL=jdbc:mysql://localhost:3306/Flipfit_schema`

### Issue: Connection Refused

**Possible Causes:**
1. Server not started
2. Port 8080 already in use

**Solutions:**
- Start the server first
- Check if another process is using port 8080: `lsof -i :8080`
- Kill the process if needed: `kill -9 <PID>`

## Complete Endpoint List

### User Endpoints
- `POST /api/users/login` - Login
- `POST /api/users/register` - Register
- `PUT /api/users/password` - Change password
- `GET /api/users/{userId}` - Get user by ID

### Customer Endpoints
- `POST /api/customers/{userId}/bookings` - Book slot
- `GET /api/customers/{userId}/plan` - View bookings
- `GET /api/customers/{userId}/payments/pending` - View pending payments
- `POST /api/customers/bookings/{bookingId}/confirm` - Confirm booking after payment
- `DELETE /api/customers/bookings/{bookingId}` - Cancel booking

### Admin Endpoints
- `GET /api/admin/owners/pending` - View pending owners
- `PUT /api/admin/owners/{ownerId}/approve` - Approve owner
- `GET /api/admin/centers/pending` - View pending centers
- `PUT /api/admin/centers/{centerId}/approve` - Approve center
- `GET /api/admin/slots/pending` - View pending slots
- `PUT /api/admin/slots/{slotId}/approve` - Approve slot

### Gym Owner Endpoints
- `POST /api/owners/onboard` - Onboard as gym owner
- `POST /api/owners/{ownerId}/centers` - Add gym center
- `GET /api/owners/{ownerId}/centers` - View my centers
- `POST /api/owners/centers/{centerId}/slots` - Add slot

### Gym Center & Slot Endpoints
- `GET /api/centers` - Get all centers
- `GET /api/centers/{centerId}` - Get center by ID
- `GET /api/slots/center/{centerId}` - Get slots for center
- `GET /api/slots/{slotId}` - Get slot by ID

### Payment Endpoints
- `POST /api/payments/process` - Process payment
- `GET /api/payments/history/{userId}` - Get payment history
- `GET /api/payments/revenue/{centerId}?userId={ownerId}` - Get gym revenue

### Notification Endpoints
- `GET /api/notifications/{userId}` - Get user notifications


