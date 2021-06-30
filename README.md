
# Mancala Game

Rabobank assignment Ahmed Aziz

## Tech Stack

**Server:** Java 11, Spring boot, Spring WEB, Lombok, JPA, MongoDB

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

| Rest Method | EndPoint                                                     | Description              |
| :-------    | :--------                                                    | :--------------          |
| `GET`       | `/rabobank/api/v1/customer-account-details/{customerId}`            | Get all accounts         |
| `POST  `    | `/rabobank/api/v1/authorize-account`                                 | Authorize given account  |

## Sequence Diagrams
![Mancala Game](docs/rabo-sd-v1.jpg)

## Features
1- get accounts
2- authorize read account
2- authorize write account

## Author: Ahmed Aziz
- [Linkedin](https://www.linkedin.com/in/ahmedaziz83/)
- [GitHub](https://github.com/ahmeed83/)
- [Twitter](https://twitter.com/AA_ziz/)
