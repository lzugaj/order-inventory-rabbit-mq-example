# 🛒 Order Processing System with RabbitMQ

## 📌 Overview
This project demonstrates an **asynchronous order processing system** using **RabbitMQ** as a message broker. Orders are placed through an **Order Service**, sent to a message queue, and processed by an **Inventory Service** to update stock.

---

## 🏗️ Technologies Used
- **Java 21+** – Backend development
- **Spring Boot** – Framework for building microservices
- **RabbitMQ** – Message broker for handling asynchronous communication
- **Docker & Docker Compose** – Containerization
- **Maven** – Dependency management
- **PostgreSQL** – Relational database for storing orders and inventory
- **Testcontainers** – For running RabbitMQ and PostgreSQL in isolation during tests
- **Swagger** – API documentation for the Order and Inventory services

---

## 🏛️ Architecture

1️⃣ **Order Service** 📝 
- Accepts new orders via REST API
- Sends messages to RabbitMQ

2️⃣ **RabbitMQ (Message Broker) 📩**
- Routes order messages to the correct queue

3️⃣ **Inventory Service 📦**
- Listens for messages from RabbitMQ
- Updates inventory stock

---

## 🚀 Getting Started

### 1️⃣ Clone the Repository
```bash
git clone https://github.com/lzugaj/order-inventory-rabbit-mq-example.git
cd order-inventory-rabbit-mq-example
```

### 2️⃣ Start RabbitMQ (Using Docker)
```bash
docker-compose up -d
```

### 3️⃣ Build and Run the Services

#### Order Service:
```bash
cd order-service
mvn clean install
java -jar target/order-service.jar
```

#### Inventory Service:
```bash
cd inventory-service
mvn clean install
java -jar target/inventory-service.jar
```

---

## 🔧 Configuration
Modify `application.properties` to customize:
- **RabbitMQ Connection** (host, port, credentials)
- **Database Connection** (PostgreSQL)

---

## 🔍 Testing the System

1️⃣ **Place an Order**  
Send a `POST` request to the Order Service:
   ```bash
   curl -X POST http://localhost:8080/orders -H "Content-Type: application/json" -d '{"productId": "321e6543-a21b-43c1-b879-625189837800","productName": "Jeans","productCategory": "CLOTHING","quantity": 1,"price": 19.98}'
   ```

2️⃣ **Check the Logs**
- The **Order Service** should log the order creation.
- The **Inventory Service** should log the inventory update.

---

## 📖 API Documentation with Swagger
This project integrates Swagger to document and visualize the APIs for both the Order Service and Inventory Service.

Once the services are running, access Swagger UI at:
http://localhost:8081/swagger-ui.html

---

## 🎯 Why Use This?
✔️ **Decoupled architecture** – Services operate independently  
✔️ **Asynchronous processing** – Orders are handled efficiently  
✔️ **Scalable** – Easily add more services if needed  
✔️ **Testable** – Uses Testcontainers for isolated testing environments   
✔️ **Self-Documenting** – Swagger UI for easy API exploration

---

🚀 **Enjoy working with this project!** Contributions and issues are welcome on GitHub.