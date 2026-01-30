# FlipFit DropWizard Application

A complete port of the FlipFit Gym Booking System to DropWizard REST API with CLI client.

## Project Structure

```
DemoDropWizardProject/
├── src/main/java/com/flipfit/
│   ├── FlipFitApplication.java       # Main DropWizard Application
│   ├── FlipFitConfiguration.java     # DropWizard Configuration
│   ├── bean/                          # Domain models/entities
│   ├── business/                      # Business logic services
│   ├── dao/                           # Data Access Objects
│   ├── resources/                     # REST API endpoints
│   ├── utils/                         # Utility classes
│   ├── exception/                     # Custom exceptions
│   └── client/                        # CLI client application
│       ├── FlipFitCLIClient.java     # Main CLI entry point
│       ├── HttpClientUtil.java       # HTTP client utility
│       ├── CustomerCLIMenu.java      # Customer operations
│       ├── AdminCLIMenu.java         # Admin operations
│       └── GymOwnerCLIMenu.java      # Gym Owner operations
├── config.yml                         # DropWizard server configuration
├── .env                               # Database configuration
└── example.env                        # Example environment file
```

## Features

### REST API Endpoints

#### User Management
- `POST /api/users/login` - User login
- `POST /api/users/register` - User registration
- `PUT /api/users/password` - Change password
- `GET /api/users/{userId}` - Get user by ID
- `GET /api/users` - Get all users

#### Customer Operations
- `POST /api/customers/{userId}/bookings` - Book a slot
- `DELETE /api/customers/bookings/{bookingId}` - Cancel booking
- `GET /api/customers/{userId}/plan` - View customer plan
- `GET /api/customers/{userId}/payments/pending` - Get pending payments
- `POST /api/customers/bookings/{bookingId}/confirm` - Confirm booking after payment

#### Admin Operations
- `PUT /api/admin/owners/{ownerId}/approve` - Approve gym owner
- `PUT /api/admin/centers/{centerId}/approve` - Approve gym center
- `PUT /api/admin/slots/{slotId}/approve` - Approve slot
- `GET /api/admin/owners/pending` - View pending gym owners
- `GET /api/admin/centers/pending` - View pending gym centers
- `GET /api/admin/slots/pending` - View pending slots
- `GET /api/admin/owners?approved={true|false}` - View owners by status
- `GET /api/admin/centers?approved={true|false}` - View centers by status

#### Gym Owner Operations
- `POST /api/owners/onboard` - Onboard new gym owner
- `POST /api/owners/{ownerId}/centers` - Add gym center
- `GET /api/owners/{ownerId}/centers` - View owner's centers
- `POST /api/owners/centers/{centerId}/slots` - Add slot to center

#### Gym Centers & Slots
- `GET /api/centers` - Get all approved centers
- `GET /api/centers/{centerId}` - Get center by ID
- `GET /api/slots/center/{centerId}` - Get slots by center
- `GET /api/slots/{slotId}` - Get slot by ID
- `PUT /api/slots/{slotId}/availability` - Update slot availability

#### Payments
- `POST /api/payments/process` - Process payment
- `GET /api/payments/history/{userId}` - Get customer payment history
- `GET /api/payments/revenue/{centerId}?userId={userId}` - Get gym revenue
- `GET /api/payments/method/{bookingId}` - Get payment method

#### Notifications
- `GET /api/notifications/{userId}` - Get user notifications

### Security & Authorization
- **Bearer Token Auth**: Uses `username+admin` strategy for authentication.
- **Role-Based Access Control**: `@RolesAllowed` annotations (e.g., ADMIN only) are used to secure sensitive endpoints.
- **CORS Support**: Configured to handle Authorization headers.

### CLI Enhancements
- **Interactive UI**: Clear screen refreshes and colorful output.
- **Session Info**: Welcome message with username and current timestamp.

## Setup Instructions

### Prerequisites
- Java 8 or higher
- Maven 3.6+
- MySQL 8.0+
- Two terminal windows

### Database Setup

1. **Create the database:**
   ```sql
   CREATE DATABASE Flipfit_schema;
   ```

2. **Configure database connection:**
   - Copy `example.env` to `.env`
   - Update database credentials in `.env`:
     ```
     DB_URL=jdbc:mysql://localhost:3306/Flipfit_schema
     DB_USER=root
     DB_PASSWORD=your_password
     ADMIN_USER=admin
     ADMIN_PASSWORD=admin123
     ```

3. **Run database schema** (use the schema from the original project)

### Building the Application

```bash
# Clean and build the project
mvn clean package

# This creates an uber JAR in the target/ directory
```

## Running the Application

### Terminal 1: Start the DropWizard Server

```bash
# Run the server with the config file
java -jar target/flipfit_d-1.0-SNAPSHOT.jar server config.yml
```

The server will start on:
- **Application**: http://localhost:8080
- **Admin Interface**: http://localhost:8081

You should see:
```
============================================================
FlipFit REST API Server Started
Server running on: http://localhost:8080
============================================================
```

### Terminal 2: Run the CLI Client

```bash
# Run the CLI client (make sure server is running first)
java -cp target/flipfit_d-1.0-SNAPSHOT.jar com.flipfit.client.FlipFitCLIClient
```

You should see:
```
============================================================
FlipFit CLI Client - Connected to http://localhost:8080
============================================================

--- Welcome to FlipFit ---
1. Login
2. Register as Customer
3. Register as Gym Owner
4. Change Password
5. Exit
Choice:
```

## Usage Guide

### For Customers:
1. Register as Customer (option 2)
2. Login with credentials (option 1)
3. Browse and book slots
4. Make payments
5. View booking history
6. Cancel bookings

### For Gym Owners:
1. Register as Gym Owner (option 3)
2. Wait for admin approval
3. Login (option 1)
4. Add gym centers
5. Add slots to centers
6. View revenue

### For Admins:
1. Login with default credentials:
   - Username: `admin`
   - Password: `admin123`
2. Approve pending gym owners
3. Approve pending centers
4. Approve pending slots
5. View revenue reports

## Architecture

### Server (DropWizard)
- **DropWizard 2.0.33** - REST framework
- **Jersey** - RESTful web services
- **Jackson** - JSON processing
- **MySQL Connector** - Database connectivity

### Client (CLI)
- **OkHttp** - HTTP client for REST API calls
- **Jackson** - JSON parsing
- **Scanner** - Console input handling

### Data Flow
```
CLI Client → HTTP Request → DropWizard Server → Business Layer → DAO Layer → MySQL Database
```

## Key Differences from Original

1. **Separation of Concerns**: Server and client are completely separate
2. **Stateless**: No direct database access from CLI, all via REST
3. **Network Communication**: All interactions happen over HTTP
4. **Scalability**: Server can handle multiple concurrent CLI clients
5. **Deployment**: Server and client can run on different machines

## Environment Variables

| Variable | Description | Example |
|----------|-------------|---------|
| DB_URL | MySQL connection URL | `jdbc:mysql://localhost:3306/Flipfit_schema` |
| DB_USER | Database username | `root` |
| DB_PASSWORD | Database password | `password` |
| ADMIN_USER | Default admin username | `admin` |
| ADMIN_PASSWORD | Default admin password | `admin123` |

## Troubleshooting

### Server won't start
- Check if port 8080 is already in use
- Verify database connection in `.env`
- Ensure MySQL is running

### CLI can't connect
- Make sure server is running on localhost:8080
- Check for firewall issues
- Verify baseURL in HttpClientUtil.java

### Database errors
- Verify .env file is in project root directory
- Check MySQL credentials
- Ensure database schema is created

## API Testing

You can also test the API using tools like:
- **curl**: `curl -X GET http://localhost:8080/api/centers`
- **Postman**: Import the endpoints and test
- **Browser**: For GET requests

Example:
```bash
# Login example
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"password123"}'
```

## Project Dependencies

See `pom.xml` for complete list. Key dependencies:
- DropWizard Core
- MySQL Connector
- Jackson Databind
- OkHttp
- DropWizard Auth

## License

This is an educational project for learning DropWizard and REST API development.
