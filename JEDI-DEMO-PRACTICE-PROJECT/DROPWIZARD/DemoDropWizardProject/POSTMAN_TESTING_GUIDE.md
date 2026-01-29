# FlipFit API - Postman Testing Guide

## Important: Password Handling

### ✅ DO: Send Plain Text Passwords

All FlipFit API endpoints accept **plain text passwords** in JSON requests. The server handles password hashing automatically using SHA-256 with username as salt.

**Example:**
```json
{
  "username": "admin",
  "password": "admin123"
}
```

### ❌ DON'T: Pre-hash Passwords

Do NOT hash passwords before sending them to the API. The server will handle all cryptographic operations.

**Incorrect (Don't do this):**
```json
{
  "username": "admin",
  "password": "240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9"
}
```

---

## API Endpoints - Postman Examples

### 1. Login (POST)

**Endpoint:** `http://localhost:8080/api/auth/login`

**Method:** POST

**Headers:**
```
Content-Type: application/json
```

**Body (raw JSON):**
```json
{
  "username": "admin",
  "password": "admin123"
}
```

**Expected Response (200 OK):**
```json
{
  "userId": "admin",
  "username": "admin",
  "role": "ADMIN",
  "message": "Login successful"
}
```

**Error Response (401 Unauthorized):**
```json
{
  "success": false,
  "message": "Invalid username or password"
}
```

---

### 2. Register Gym Owner (POST)

**Endpoint:** `http://localhost:8080/api/auth/register/gymowner`

**Method:** POST

**Headers:**
```
Content-Type: application/json
```

**Body (raw JSON):**
```json
{
  "username": "john_doe",
  "password": "mypassword123",
  "panCard": "ABCDE1234F",
  "gstNumber": "22ABCDE1234F1Z5",
  "aadhaarNumber": "123456789012",
  "location": "Mumbai"
}
```

**Expected Response (201 Created):**
```json
{
  "success": true,
  "message": "Gym Owner registered successfully. Awaiting approval."
}
```

---

### 3. Change Password (PUT)

**Endpoint:** `http://localhost:8080/api/auth/change-password`

**Method:** PUT

**Headers:**
```
Content-Type: application/json
```

**Body (raw JSON):**
```json
{
  "username": "admin",
  "oldPassword": "admin123",
  "newPassword": "newpassword456"
}
```

**Expected Response (200 OK):**
```json
{
  "success": true,
  "message": "Password changed successfully"
}
```

**Error Response (400 Bad Request):**
```json
{
  "success": false,
  "message": "Password change failed"
}
```

---

### 4. View Pending Gym Owners (GET) - Admin Only

**Endpoint:** `http://localhost:8080/api/admin/pending-owners`

**Method:** GET

**Expected Response (200 OK):**
```json
[
  {
    "userId": "john_doe",
    "name": "john_doe",
    "email": "john_doe@flipfit.com",
    "role": "GYM_OWNER",
    "panNumber": "ABCDE1234F",
    "gstNumber": "22ABCDE1234F1Z5",
    "aadhaarNumber": "123456789012",
    "location": "Mumbai",
    "approved": false
  }
]
```

---

### 5. Approve Gym Owner (PUT) - Admin Only

**Endpoint:** `http://localhost:8080/api/admin/approve-owner/{ownerId}`

**Method:** PUT

**Example:** `http://localhost:8080/api/admin/approve-owner/john_doe`

**Expected Response (200 OK):**
```json
{
  "success": true,
  "message": "Gym Owner approved successfully"
}
```

---

### 6. Book Workout Slot (POST) - Customer Only

**Endpoint:** `http://localhost:8080/api/customer/bookings`

**Method:** POST

**Headers:**
```
Content-Type: application/json
```

**Body (raw JSON):**
```json
{
  "userId": "customer1",
  "slotId": "SLOT001",
  "bookingDate": "2024-02-01"
}
```

**Expected Response (201 Created):**
```json
{
  "success": true,
  "message": "Slot booked successfully",
  "bookingId": "BOOK123456"
}
```

---

### 7. View My Bookings (GET) - Customer Only

**Endpoint:** `http://localhost:8080/api/customer/bookings?userId={userId}`

**Method:** GET

**Example:** `http://localhost:8080/api/customer/bookings?userId=customer1`

**Expected Response (200 OK):**
```json
[
  {
    "bookingId": "BOOK123456",
    "scheduleId": "SLOT001",
    "userId": "customer1",
    "status": "PENDING_PAYMENT",
    "createdAt": "2024-02-01T10:30:00"
  }
]
```

---

### 8. Add Gym Center (POST) - Gym Owner Only

**Endpoint:** `http://localhost:8080/api/gymowner/centers`

**Method:** POST

**Headers:**
```
Content-Type: application/json
```

**Body (raw JSON):**
```json
{
  "ownerId": "john_doe",
  "centerName": "FitZone Gym",
  "location": "Andheri, Mumbai"
}
```

**Expected Response (201 Created):**
```json
{
  "success": true,
  "message": "Gym Center added successfully. Awaiting approval."
}
```

---

## Testing Workflow

### Step 1: Start the Server
```bash
cd /path/to/DemoDropWizardProject
java -jar target/flipfit-dropwizard-1.0.0.jar server src/main/resources/application.yml
```

### Step 2: Import to Postman

1. Open Postman
2. Create a new Collection named "FlipFit API"
3. Add the requests above as examples
4. Test each endpoint with the provided JSON payloads

### Step 3: Test Authentication Flow

1. **Register a Gym Owner** using plain text password (e.g., "password123")
2. **Login as Admin** using plain text password
3. **Approve the Gym Owner**
4. **Login as Gym Owner** using the same plain text password you registered with
5. **Add a Gym Center**

### Step 4: Verify Password Changes

1. **Login** with original password
2. **Change Password** to a new plain text password
3. **Logout**
4. **Login** with the new password (should succeed)
5. **Try Login** with old password (should fail)

---

## Common Testing Scenarios

### Scenario 1: New User Registration and Login

```
1. POST /api/auth/register/gymowner (password: "test123")
2. Admin approves the owner
3. POST /api/auth/login (password: "test123") ✓ Success
```

### Scenario 2: Password Change

```
1. POST /api/auth/login (password: "oldpass")
2. PUT /api/auth/change-password (oldPassword: "oldpass", newPassword: "newpass")
3. POST /api/auth/login (password: "newpass") ✓ Success
4. POST /api/auth/login (password: "oldpass") ✗ Fail
```

### Scenario 3: Invalid Password

```
1. POST /api/auth/login (password: "wrongpassword")
   Response: 401 Unauthorized
```

---

## Security Notes

1. **HTTPS Recommended**: In production, always use HTTPS to protect passwords in transit
2. **Server-Side Hashing**: All password hashing happens on the server using SHA-256 with username as salt
3. **No Client Hashing**: Clients should never hash passwords - send them as plain text over HTTPS
4. **Testing Convenience**: Plain text passwords make API testing with Postman straightforward and intuitive

---

## Troubleshooting

### Issue: 401 Unauthorized on Login

**Possible Causes:**
- Incorrect username or password (check for typos)
- User account not yet approved (for Gym Owners)
- User does not exist in database

**Solution:** 
- Verify credentials
- Check admin approval status
- Register the user first if needed

### Issue: 500 Internal Server Error

**Possible Causes:**
- Database connection issue
- Missing .env configuration
- Database schema mismatch

**Solution:**
- Check server logs
- Verify .env file exists and has correct DB credentials
- Ensure database is running and schema is set up

---

## Summary

✅ **Always use plain text passwords** in all API requests  
✅ Server handles all password hashing automatically  
✅ Testing with Postman is simple and intuitive  
✅ Security is maintained through server-side cryptography  

For more information, see the main README.md file.
