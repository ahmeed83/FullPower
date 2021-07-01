
# Rabobank assignment Ahmed Aziz

Rabobank assignment Ahmed Aziz

## Tech Stack

**Server:** Java 11, Spring boot, Spring WEB, Lombok, MongoDB

## Run Locally

Clone the project
```bash
  git clone https://github.com/ahmeed83/RabobankAssignment
```
Go to the project directory
```bash
  cd RabobankAssignment
```
Build the application
```bash
  mvn clean package
```
Start the server
```bash
  mvn spring-boot:run
```
Application will start on
[localhost:8080](http://localhost:8080)

## API Reference

| Rest Method | EndPoint                                                            | Description                       |
| :-------    | :--------                                                           | :--------------                   |
| `GET`       | `/rabobank/api/v1/customer-account-details/{customerId}`            | Get all customer accounts         |
| `POST  `    | `/rabobank/api/v1/authorize-account`                                | Authorize given account           |

## Sequence Diagrams
![Rabo-Ahmed-SD](docs/rabo-sd-v1.jpg)

## Features
1- Customers can call the server with their user ID (when they log in). They will get a list of their account details. These accounts consist of the account itself, and the authorization type that this account has. 

2- The authorization type can consist of three types:
  1) OWNER: means this account belongs to the logged-in user.
  2) READ: means this account is not belong to the logged-in user, but he has the permission to see it. 
  3) Write: means this account is not belong to the logged-in user, but he has the permission to change it. 

3- Customers can authorize their accounts to other customers. They can permit READ/WRITE permissions of their accounts to others. However, customers can not permit OWNER permission to other customers.

4- If an account is already been assigned to a customer, this account can not be assigned again. However, the same account can be assigned to a different customers if the authorization type is different. For example,
if a customer has authorization for an account with type READ, the account can be authorized again for a different authorization type (in this case WRITE). 

## Postman collection
[Postman Collection](docs/RaboAhmedAssignment.postman_collection.json)

## Author: Ahmed Aziz
- [Linkedin](https://www.linkedin.com/in/ahmedaziz83/)
- [GitHub](https://github.com/ahmeed83/)
- [Twitter](https://twitter.com/AA_ziz/)
