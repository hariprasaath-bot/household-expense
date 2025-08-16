# User Profile Management API Documentation

This document provides information about the User Profile Management API endpoints and how to use them.

## Models

### User Profile

The User Profile contains the following information:

```json
{
  "id": "string",
  "name": "string",
  "email": "string",
  "memberSince": "date",
  "bankAccounts": [
    {
      "id": "string",
      "bankName": "string",
      "accountNumber": "string",
      "accountType": "string"
    }
  ],
  "categories": [
    {
      "id": "string",
      "name": "string",
      "isCustom": "boolean"
    }
  ]
}
```

## API Endpoints

### Get Current User Profile

Retrieves the profile for the currently authenticated user.

- **URL**: `/user/profile`
- **Method**: `GET`
- **Auth Required**: Yes
- **Response**: User Profile object

Example Response:
```json
{
  "id": "123456",
  "name": "John Doe",
  "email": "john.doe@example.com",
  "memberSince": "2023-01-01T00:00:00.000Z",
  "bankAccounts": [
    {
      "id": "bank1",
      "bankName": "ICICI Bank",
      "accountNumber": "1234567890",
      "accountType": "Savings"
    }
  ],
  "categories": [
    {
      "id": "cat1",
      "name": "FOOD",
      "isCustom": false
    },
    {
      "id": "cat2",
      "name": "TRANSPORT",
      "isCustom": false
    }
  ]
}
```

### Get User Profile by ID

Retrieves the profile for a specific user by ID.

- **URL**: `/user/profile/{userId}`
- **Method**: `GET`
- **Auth Required**: Yes
- **URL Params**: `userId=[string]`
- **Response**: User Profile object

### Update User Profile

Updates the profile for the currently authenticated user.

- **URL**: `/user/profile/update`
- **Method**: `POST`
- **Auth Required**: Yes
- **Data Params**: User Profile object (only name and email are updated)
- **Response**: Success/failure message

Example Request:
```json
{
  "name": "John Smith",
  "email": "john.smith@example.com"
}
```

Example Response:
```json
{
  "message": "Profile updated successfully"
}
```

### Add Bank Account

Adds a bank account to the currently authenticated user.

- **URL**: `/user/bank-account/add`
- **Method**: `POST`
- **Auth Required**: Yes
- **Data Params**: Bank Account object
- **Response**: Success/failure message

Example Request:
```json
{
  "bankName": "HDFC Bank",
  "accountNumber": "9876543210",
  "accountType": "Current"
}
```

Example Response:
```json
{
  "message": "Bank account added successfully"
}
```

### Remove Bank Account

Removes a bank account from the currently authenticated user.

- **URL**: `/user/bank-account/remove/{bankAccountId}`
- **Method**: `DELETE`
- **Auth Required**: Yes
- **URL Params**: `bankAccountId=[string]`
- **Response**: Success/failure message

Example Response:
```json
{
  "message": "Bank account removed successfully"
}
```

### Add Category

Adds a category to the currently authenticated user.

- **URL**: `/user/category/add`
- **Method**: `POST`
- **Auth Required**: Yes
- **Data Params**: Category object
- **Response**: Success/failure message

Example Request:
```json
{
  "name": "GROCERIES"
}
```

Example Response:
```json
{
  "message": "Category added successfully"
}
```

### Remove Category

Removes a category from the currently authenticated user. Only custom categories can be removed.

- **URL**: `/user/category/remove/{categoryId}`
- **Method**: `DELETE`
- **Auth Required**: Yes
- **URL Params**: `categoryId=[string]`
- **Response**: Success/failure message

Example Response:
```json
{
  "message": "Category removed successfully"
}
```

## Integration with Angular UI

The API endpoints are designed to work with the provided Angular UI. Here's how they map to the UI functionality:

1. When the user profile page loads, the UI calls `GET /user/profile` to retrieve the user's profile information.
2. When the user edits their profile information (name and email), the UI calls `POST /user/profile/update` with the updated information.
3. When the user adds a bank account, the UI calls `POST /user/bank-account/add` with the bank account information.
4. When the user removes a bank account, the UI calls `DELETE /user/bank-account/remove/{bankAccountId}` with the bank account ID.
5. When the user adds a category, the UI calls `POST /user/category/add` with the category information.
6. When the user removes a category, the UI calls `DELETE /user/category/remove/{categoryId}` with the category ID.

## Default Categories

The system comes with the following default categories that cannot be removed:

1. FOOD
2. TRANSPORT
3. BILLS
4. SHOPPING
5. ENTERTAINMENT
6. HEALTH
7. TRAVEL
8. INCOME
9. OTHER

Users can add custom categories in addition to these default categories.