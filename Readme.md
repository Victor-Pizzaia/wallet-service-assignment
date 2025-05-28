# Wallet Service Assignment

A modular virtual wallet system developed with Spring Boot and Spring Modulith, following Domain-Driven Design (DDD) principles and Hexagonal Architecture.

---

## 🛠️ Technologies Used
* Java 21
* Spring Boot
* Spring Modulith
* Spring Data JPA
* PostgreSQL
* Docker
* Docker compose
* Kafka
* Redis
* Grafana

---

## 📦 Installation

### Prerequisites

* Java 21
* Maven
* Docker and Docker Compose

### Setup

```bash
# Clone the repository
$ git clone https://github.com/Victor-Pizzaia/wallet-service-assignment.git
$ cd wallet-service-assignment

# Build Wallet Docker image
$ docker build -t wallet-assignment .

# Start project with dependencies (PostgreSQL, Kafka, Redis, Grafana, Promtail, Loki)
$ docker-compose up -d
```

---

## ▶️ Running Unit Tests

```bash
# To run all tests
$ ./mvnw test
```

---

## 🛠️ Features

| Method | Endpoint                 | Function                          |
|--------|--------------------------|-----------------------------------|
| POST   | `/users`                 | Create a new User and wallet      |
| POST   | `/auth `                 | Authentication                    |
| GET    | `/wallets`               | Get actual balance                |
| POST   | `/wallets/deposit`       | Deposit                           |
| POST   | `/wallets/withdraw`      | Withdraw                          |
| POST   | `/wallets/transaction`   | Transfer for other wallet         |
| GET    | `/statement`             | Historical balance                |

---

## 🧠 Design Decisions

### Domain Decomposition

The system is divided into three main domains:

* **Wallets**: Manages balance and wallet operations.
* **Users**: Manages user identity and registration.
* **Transactions**: Manages transfers and financial flows.

These domains follow DDD principles and are encapsulated using the module boundaries enforced by Spring Modulith. Communication between domains is done strictly via domain events to avoid coupling.

### Architecture

* **Hexagonal (Ports & Adapters)**: Enables separation of core logic and infrastructure concerns.
* **Spring Modulith**: Used to handle modularization and internal event-driven communication.
* **Outbox Pattern**: Ensures reliable delivery of domain events through a persistent table, preparing the system for distributed communication via Kafka.
* **PostgreSQL**: Chosen for its consistency, relational capabilities, and compatibility with JPA/Hibernate.
* **Redis**: Used for in-memory caching to improve read performance.
* **Grafana + Promtail + Loki**: Used for log capture, monitoring and visualization.

### Security

Authentication and authorization are managed by a dedicated **Auth** module, which implements JWT-based authentication using email or CPF and password, isolating all security concerns from the business logic.

---

## ✅ Requirements

### Functional Requirements

* Wallet creation and user registration.
* The wallet is capable of receiving withdrawals and deposits.
* Transactions between wallets are processed asynchronously through events.
* Wallet balances and statements are available for viewing.

### Non-Functional Requirements

* **Performance**: Redis caching is used to reduce database load.
* **Observability**: Logs are centralized with Promtail and visualized using Grafana dashboards.
* **Scalability**: Modular architecture allows independent scaling and deployment.
* **Maintainability**: Hexagonal architecture and DDD make the system easy to extend.
* **Idempotency**: Idempotency key filter that allows the application to receive multiple requests with greater atomicity and ensures that a process will not occur with duplication

---

## 🚀 Future Improvements

* 🧪 **Integration Tests**: For better security in development, it is necessary to create integration tests, which can be used using Cucumber.
* 🔐 **Security**: The security layer can be better implemented by generating more consistent jwt tokens.
* 🔄 **Cache**: It can be better explored within the application.
* 📊 **Advanced Monitoring**: Log collection can be better built based on JSON's and the connection with Grafana can be better explored.

## 🧪 How to Test

### Create User (/api/v1/users):
User:
```
{
    "fullname": "John Doe",
    "cpf": "12345678910"
    "email": "jhon.doe@teste.com",
    "plainPassword": "test123",
}
```
User 2:
```
{
    "fullname": "Jane Doe",
    "cpf": "12345678900"
    "email": "jane.doe@teste.com",
    "plainPassword": "456test",
}
```
### Authentication (/api/v1/auth):
```
{
  "identifier": "12345678910", // Cpf or Email
  "password": "test123"
}
```
### Balance (/api/v1/wallets/balance):
Requires JWT token on header Authorization Bearer token (Returns on Auth Request)
### Deposit (/api/v1/wallets/deposit):
Requires JWT and Idempotency-Key header
```
{
    "amount": 500
}
```
### Withdraw (/api/v1/wallets/withdraw):
Requires JWT and Idempotency-Key header
```
{
    "amount": 100
}
```
### Transfer (/api/v1/wallets/transaction):
Requires JWT and Idempotency-Key header
```
{
    "payeeKey": "12345678900",
    "amount": 200
}
```
### Statement (/api/v1/statement?page=0&size=10):
Requires JWT

---

### Scripts
Here is a script I created to facilitate the authorization header and idempotency-key.
Change 'yourKey' for [withdraw, deposit or transfer].
Postman:
```
if (!pm.collectionVariables.get("token")) {
    pm.sendRequest({
        url: pm.collectionVariables.get("LOCAL") + "/api/v1/auth",
        method: 'POST',
        header: { 'Content-Type': 'application/json' },
        body: {
            mode: 'raw',
            raw: JSON.stringify({
                identifier: pm.collectionVariables.get("identifier"),
                password: pm.collectionVariables.get("password")
            })
        }
    }, function (err, res) {
        const json = res.json();
        pm.collectionVariables.set("Authorization", json.token);
    });
}

pm.collectionVariables.set("Idempotency-key", "yourKey");
```
