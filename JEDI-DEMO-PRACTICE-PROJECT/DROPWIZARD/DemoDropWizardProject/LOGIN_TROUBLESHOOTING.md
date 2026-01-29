# Login Troubleshooting Guide

## Common Login Issues and Solutions

### Issue 1: "Could not find .env file" error

**Cause**: The application cannot locate the database configuration file.

**Solution**:
1. Copy `example.env` to `.env` in the project root:
   ```bash
   cd JEDI-DEMO-PRACTICE-PROJECT/DROPWIZARD/DemoDropWizardProject
   cp example.env .env
   ```

2. Update `.env` with your MySQL credentials:
   ```properties
   DB_URL=jdbc:mysql://localhost:3306/Flipfit_schema?useSSL=false&serverTimezone=UTC
   DB_USER=root
   DB_PASSWORD=your_mysql_password
   ```

### Issue 2: Database connection errors

**Cause**: MySQL is not running or credentials are incorrect.

**Solution**:
1. Ensure MySQL is running:
   ```bash
   sudo systemctl status mysql
   # or
   sudo service mysql status
   ```

2. Create the database and schema:
   ```bash
   mysql -u root -p < JEDI-DEMO-PRACTICE-PROJECT/DROPWIZARD/JEDI_ZETA_DEVELOPMENT_JAVA_POS_DAO/flipfit_schema.sql
   ```

3. Verify the database exists:
   ```bash
   mysql -u root -p -e "SHOW DATABASES LIKE 'Flipfit_schema';"
   ```

### Issue 3: "Invalid username or password" for admin

**Cause**: Default admin user not inserted or password hash mismatch.

**Solution**:
1. The default admin credentials are:
   - Username: `admin`
   - Password: `admin123`

2. Verify admin user exists in database:
   ```sql
   USE Flipfit_schema;
   SELECT userId, name, role FROM User WHERE userId='admin';
   ```

3. If admin doesn't exist, insert manually:
   ```sql
   INSERT INTO User (userId, name, email, password, role) 
   VALUES ('admin', 'Admin User', 'admin@flipfit.com', 
           '057d6ab44a20179a4bb213682beff1ddb9e60f4413e03f0e0df4d84b7b057459', 
           'ADMIN');
   ```

### Issue 4: Gym Owner cannot login

**Cause**: Gym Owner account is pending approval.

**Symptoms**: Login returns "Invalid username or password" even with correct credentials.

**Solution**:
1. Login as admin first
2. Approve the Gym Owner account through admin panel or SQL:
   ```sql
   UPDATE GymOwner SET isApproved = TRUE WHERE userId = 'owner_username';
   ```

### Issue 5: "NullPointerException" or "Connection refused"

**Cause**: Dropwizard server is not running.

**Solution**:
1. Start the Dropwizard server first:
   ```bash
   cd JEDI-DEMO-PRACTICE-PROJECT/DROPWIZARD/DemoDropWizardProject
   java -jar target/flipfit_d-1.0-SNAPSHOT.jar server src/main/resources/application.yml
   ```

2. Wait for the server to start (you should see "Server started" message)

3. Then run the CLI client or test with Postman

### Issue 6: JSON parsing errors in Postman

**Cause**: Incorrect JSON format or Content-Type header.

**Solution**:
1. Ensure Content-Type header is set:
   ```
   Content-Type: application/json
   ```

2. Use correct JSON format:
   ```json
   {
     "username": "admin",
     "password": "admin123"
   }
   ```

## Testing Login with cURL

Test if the server is running and login works:

```bash
# Test server health
curl http://localhost:8080/

# Test login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

Expected response:
```json
{
  "userId": "admin",
  "username": "Admin User",
  "role": "ADMIN",
  "message": "Login successful"
}
```

## Login Flow

1. Client sends POST request to `/api/auth/login` with JSON:
   ```json
   {"username": "admin", "password": "admin123"}
   ```

2. `AuthResource.login()` receives the request

3. `UserService.login()` is called with plain text password

4. Password is hashed: SHA-256(password + username)
   - Example: SHA-256("admin123" + "admin") = "057d6ab..."

5. `GymUserDAOImpl.loginUser()` queries database with hashed password

6. If found, user object is returned

7. Special check: If user is GymOwner and not approved, return null

8. `LoginResponse` is created and returned to client

## Server Logs

Check server logs for detailed error messages:
- Database connection errors
- SQL exceptions
- Authentication failures

The logs will show the actual error that's occurring.
