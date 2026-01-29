# FlipFit Dropwizard REST API Application

## Overview
This project is a complete port of the FlipFit gym booking application from a CLI-based architecture to a **Dropwizard REST API** with a separate CLI client that communicates via HTTP requests.

## Architecture

### 1. Dropwizard REST API Server
- **Framework**: Dropwizard 2.1.6
- **Database**: MySQL with JDBC
- **Configuration**: YAML config + .env for database credentials
- **Port**: 8080 (application), 8081 (admin)

### 2. CLI Client
- **Technology**: Java with Apache HttpClient
- **Communication**: HTTP/REST calls to localhost:8080
- **User Interface**: Console-based menu system

## Project Structure

```
src/main/java/com/flipfit/
├── api/                          # REST API Resources
│   ├── AuthResource.java         # Login, registration, password change
│   ├── AdminResource.java        # Admin operations (11 endpoints)
│   ├── CustomerResource.java     # Customer operations (7 endpoints)
│   ├── GymOwnerResource.java     # Gym owner operations (8 endpoints)
│   └── dto/                      # Data Transfer Objects
│       ├── LoginRequest.java
│       ├── LoginResponse.java
│       ├── ApiResponse.java
│       └── ChangePasswordRequest.java
├── bean/                         # Entity/Model classes
│   ├── User.java
│   ├── Admin.java
│   ├── Customer.java
│   ├── GymOwner.java
│   ├── GymCenter.java
│   ├── SlotMaster.java
│   ├── Booking.java
│   ├── PaymentRecord.java
│   ├── Notification.java
│   └── [All enums: Role, BookingStatus, PaymentStatus, etc.]
├── business/                     # Service Layer
│   ├── UserService.java
│   ├── CustomerService.java
│   ├── GymOwnerService.java
│   ├── AdminService.java
│   ├── BookingService.java
│   ├── PaymentService.java
│   └── NotificationService.java
├── dao/                          # Data Access Layer
│   ├── GymUserDAO.java
│   ├── GymCustomerDAO.java
│   ├── GymOwnerDAO.java
│   ├── GymAdminDAO.java
│   ├── PaymentDAO.java
│   ├── NotificationDAO.java
│   └── impl/                     # DAO Implementations
├── exception/                    # Custom Exceptions
│   ├── UserNotFoundException.java
│   ├── BookingFailedException.java
│   ├── RegistrationFailedException.java
│   ├── InvalidApprovalException.java
│   └── SlotNotFoundException.java
├── utils/                        # Utility Classes
│   ├── DBConnection.java         # Database connection with .env support
│   ├── InputValidator.java       # Input validation
│   ├── InputSanitizer.java       # Input sanitization
│   └── PasswordHashUtil.java     # Password hashing with BCrypt
├── config/                       # Configuration
│   └── FlipFitConfiguration.java # Dropwizard configuration class
├── cli/                          # CLI Client
│   ├── FlipFitCLIClient.java    # Main CLI application
│   └── HttpClientUtil.java       # HTTP request helper
└── FlipFitApplication.java       # Main Dropwizard application

src/main/resources/
└── application.yml                # Dropwizard server configuration
```

## REST API Endpoints

### Authentication (AuthResource)
- `POST /api/auth/login` - User login
- `POST /api/auth/register/customer` - Customer registration
- `POST /api/auth/register/gymowner` - Gym owner registration
- `PUT /api/auth/change-password` - Change password

### Admin Operations (AdminResource)
- `GET /api/admin/pending-owners` - View pending gym owners
- `PUT /api/admin/approve-owner/{ownerId}` - Approve gym owner
- `GET /api/admin/pending-centers` - View pending gym centers
- `PUT /api/admin/approve-center/{centerId}` - Approve gym center
- `GET /api/admin/pending-slots` - View pending slots
- `PUT /api/admin/approve-slot/{slotId}` - Approve slot
- `GET /api/admin/center-revenue/{centerId}` - View center revenue
- `GET /api/admin/owners?status={approved|pending}` - View gym owners by status
- `GET /api/admin/centers?status={approved|pending}` - View centers by status
- `GET /api/admin/notifications/{userId}` - View notifications

### Customer Operations (CustomerResource)
- `POST /api/customer/book-slot` - Book a workout slot
- `GET /api/customer/plan/{customerId}` - View customer's plan
- `GET /api/customer/pending-bookings/{customerId}` - View pending bookings
- `POST /api/customer/pay-booking/{bookingId}` - Process payment
- `DELETE /api/customer/cancel-booking/{bookingId}` - Cancel booking
- `GET /api/customer/payment-history/{customerId}` - View payment history
- `GET /api/customer/notifications/{userId}` - View notifications
- `GET /api/customer/centers` - Browse gym centers
- `GET /api/customer/slots/{centerId}` - View slots for a center

### Gym Owner Operations (GymOwnerResource)
- `POST /api/gymowner/add-center` - Add new gym center
- `GET /api/gymowner/centers/{ownerId}` - View owner's centers
- `POST /api/gymowner/add-slot` - Add slot to center
- `GET /api/gymowner/slots/{centerId}` - View slots in center
- `PUT /api/gymowner/update-slot-capacity/{slotId}` - Update slot capacity
- `GET /api/gymowner/center-revenue/{centerId}` - View center revenue
- `GET /api/gymowner/notifications/{userId}` - View notifications

## Setup Instructions

### Prerequisites
- Java 11 or higher
- Maven 3.6+
- MySQL 8.0+
- IDE (IntelliJ IDEA, Eclipse, or VS Code)

### Database Setup
1. Create MySQL database:
```sql
CREATE DATABASE flipfit_db;
```

2. Run the database schema script (if available)

### Configuration

1. **Copy environment file**:
```bash
cp example.env .env
```

2. **Edit `.env` file**:
```properties
DB_URL=jdbc:mysql://localhost:3306/flipfit_db?useSSL=false&serverTimezone=UTC
DB_USER=your_database_user
DB_PASSWORD=your_database_password
```

3. **Application configuration** is in `src/main/resources/application.yml`

### Build & Run

#### Option 1: Using Maven

1. **Build the project**:
```bash
mvn clean package
```

2. **Run the server**:
```bash
java -jar target/flipfit-dropwizard-1.0.0.jar server src/main/resources/application.yml
```

3. **Run the CLI client** (in a separate terminal):
```bash
java -cp target/flipfit-dropwizard-1.0.0.jar com.flipfit.cli.FlipFitCLIClient
```

#### Option 2: Using IDE

1. **Run Server**: Run `FlipFitApplication.java` with arguments: `server src/main/resources/application.yml`
2. **Run CLI Client**: Run `FlipFitCLIClient.java`

### Testing the API

You can test the API using:
- **CLI Client** (recommended for full application experience)
- **cURL** commands
- **Postman** or similar API testing tools

Example cURL:
```bash
# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# View pending gym owners (Admin)
curl -X GET http://localhost:8080/api/admin/pending-owners
```

## CLI Client Features

### Main Menu
1. Login
2. Register as Customer
3. Register as Gym Owner
4. Change Password
5. Exit

### Role-Specific Menus

#### Admin Menu (11 Operations)
- View Pending Gym Owners
- Approve Gym Owner
- View Pending Gym Centers
- Approve Gym Center
- View Pending Slots
- Approve Slot
- View Center Revenue & History
- View Gym Owners by Status
- View Gym Centers by Status
- View My Notifications
- Back to Main Menu

#### Customer Menu (7 Operations)
- Book Workout Slot
- View My Plan
- Pay for Pending Bookings
- Cancel Booking
- View Payment History
- View My Notifications
- Back to Main Menu

#### Gym Owner Menu (8 Operations)
- Add Gym Center
- View My Centers
- Add Slot to Center
- View Slots in Center
- Update Slot Capacity
- View Center Revenue & History
- View My Notifications
- Back to Main Menu

## Key Features

### Security
- **Password Hashing**: BCrypt for secure password storage
- **Input Validation**: All inputs are validated before processing
- **Input Sanitization**: SQL injection prevention

### Database
- **Connection Pooling**: Efficient database connection management
- **Environment Variables**: Secure credential management via .env
- **Prepared Statements**: SQL injection prevention

### Error Handling
- **Custom Exceptions**: 5 domain-specific exceptions
- **Graceful Error Responses**: User-friendly error messages
- **HTTP Status Codes**: Proper REST API status codes

### Data Validation
- Email format validation
- Phone number validation
- Password strength validation
- Input length validation

## Dependencies

### Server Dependencies
- `io.dropwizard:dropwizard-core:2.1.6` - Web framework
- `mysql:mysql-connector-java:8.0.33` - MySQL driver
- `io.github.cdimascio:dotenv-java:2.3.2` - Environment variables
- `org.mindrot:jbcrypt:0.4` - Password hashing

### Client Dependencies
- `org.apache.httpcomponents:httpclient:4.5.14` - HTTP client
- `com.fasterxml.jackson.core:jackson-databind:2.14.2` - JSON processing

## Development

### Building
```bash
mvn clean compile
```

### Testing
```bash
mvn test
```

### Packaging
```bash
mvn package
```

### Running in Development Mode
```bash
mvn exec:java -Dexec.mainClass="com.flipfit.FlipFitApplication" -Dexec.args="server src/main/resources/application.yml"
```

## Monitoring

Dropwizard provides admin endpoints at `http://localhost:8081/`:
- `/healthcheck` - Application health status
- `/metrics` - Application metrics
- `/ping` - Simple ping endpoint
- `/threads` - Thread dump

## Troubleshooting

### Common Issues

1. **Connection Refused**
   - Ensure the server is running on port 8080
   - Check firewall settings

2. **Database Connection Failed**
   - Verify `.env` file configuration
   - Ensure MySQL is running
   - Check database credentials

3. **Compilation Errors**
   - Ensure Java 11+ is installed
   - Run `mvn clean install`

4. **Port Already in Use**
   - Change port in `application.yml`
   - Kill process using the port: `lsof -ti:8080 | xargs kill`

## License
[Specify your license here]

## Contributors
Zeta Development Team

## Support
For issues and questions, please contact the development team.
