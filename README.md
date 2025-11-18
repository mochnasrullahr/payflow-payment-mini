# Payflow Payment Mini — Payment API (v0.1.0)

Simple payment orchestration API built with **Spring Boot 3**, featuring
asynchronous processing, structured logging, correlation-ID tracing, and unit-tested
business logic.

---

## 🚀 Features

- **POST /api/payments/charge** → create payment (PENDING) + async processing
- **PaymentProcessorService** → updates status to SUCCESS/FAILED
- **Correlation-ID** (`X-Correlation-ID`) logged automatically
- **PII Masking** for email & card numbers via `LogMasker`
- **Unit Tests**: PaymentService, Async processor, Log masking
- Lightweight, dependency-free, ideal for demos/testing

---

## ▶️ Run

./mvnw clean package
./mvnw spring-boot:run


Service runs at: `http://localhost:8080`

---

## 📘 API Overview

### 1) POST `/api/payments/charge`
Creates a payment and triggers async processing.

**Request**
```json
{
  "userId": "U1",
  "amount": 10000,
  "currency": "IDR",
  "referenceId": "REF123"
}
```
### 2) GET /api/payments/{id}

Returns payment status.

### 3) GET /api/health

Health check.

## 🔎 Logging

Automatic request/response logging

CID is injected from the header or auto-generated

Sensitive fields masked for safety

## 🧪 Tests
./mvnw test
Covers:

Payment creation rules

Async processor logic

Log masking behavior

## 📂 Structure
controller/
service/
logging/
config/
repository/
entity/

## 📌 Version

This is v0.1.0 — first stable minimal payment API draft.