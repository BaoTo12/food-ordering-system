# Hexagonal Architecture in the Order Service

This document maps the concepts of Hexagonal Architecture (also known as Ports and Adapters) to the structure of the `order-service` in this project.

The primary goal of this architecture is to isolate the core business logic (the "domain") from outside concerns like databases, messaging queues, or web frameworks. This makes the core logic independent, easier to test, and adaptable to changing technology.

Imagine the core logic is inside a "hexagon". Each side of the hexagon is a "port" that allows communication with the outside world.

---

## 1. The Hexagon: Core Business Logic

This is the center of your application. It contains the "what" of your application (the business rules) but not the "how" (the implementation details like databases). In your project, this is the `order-domain` module.

### a. Domain Model (The Absolute Center)

This is the heart of your business logic. It contains the entities, value objects, and domain events that represent your business concepts. It has no dependencies on any other layer.

**Mapped to your code:**
*   **Directory:** `order-service/order-domain/order-domain-core/src/main/java/com/chibao/edu/domain/`
*   **Examples:**
    *   `entity/Order.java`: The main business object (Aggregate Root).
    *   `entity/OrderItem.java`, `entity/Customer.java`: Other business entities.
    *   `value_object/Money.java`, `value_object/StreetAddress.java`: Objects that represent values, defined by their attributes.
    *   `event/OrderCreatedEvent.java`: Represents something important that happened in the domain.
    *   `OrderDomainServiceImpl.java`: Encapsulates complex business rules that don't naturally fit into a single entity.

### b. Application Services (Your Use Cases)

This layer orchestrates the domain model to perform specific application tasks, or "use cases". **This is the direct answer to your question about use cases.** A use case is an action the system can perform, like creating an order or tracking an order.

This layer defines the *ports* (interfaces) for interacting with the application.

**Mapped to your code:**
*   **Directory:** `order-service/order-domain/order-application-service/src/main/java/com/chibao/edu/domain/`
*   **Use Case Implementations:**
    *   `OrderCreateCommandHandler.java`: This class handles the entire logic for the "Create Order" use case.
    *   `OrderTrackCommandHandler.java`: This class handles the logic for the "Track Order" use case.
*   **Data Transfer Objects (DTOs):** These are objects that carry data into and out of the use cases.
    *   `dto/create/CreateOrderCommand.java`: The input data for creating an order.
    *   `dto/track/TrackOrderQuery.java`: The input data for tracking an order.
    *   `dto/create/CreateOrderResponse.java`: The output data after creating an order.

---

## 2. Ports: The Boundaries of the Hexagon

Ports are the interfaces that define how the core application communicates with the outside world. They are defined *inside* the application layer but implemented by *adapters* outside of it.

### a. Input Ports (Driving Ports)

These define how the outside world can *drive* the application. They are the public API of your core logic.

**Mapped to your code:**
*   **Directory:** `order-service/order-domain/order-application-service/src/main/java/com/chibao/edu/domain/ports/input/`
*   **Examples:**
    *   `service/OrderApplicationService.java`: This interface defines all the available use cases (`createOrder`, `trackOrder`). This is the primary input port for synchronous calls (like from a REST controller).
    *   `message/listener/payment/PaymentResponseMessageListener.java`: This is also an input port, but for asynchronous communication. It defines how the application listens for payment responses from a message queue.

### b. Output Ports (Driven Ports)

These define what the application *needs* from the outside world to do its job. They are interfaces for things like databases, payment gateways, or message publishers.

**Mapped to your code:**
*   **Directory:** `order-service/order-domain/order-application-service/src/main/java/com/chibao/edu/domain/ports/output/`
*   **Examples:**
    *   `repository/OrderRepository.java`: An interface that defines the methods needed for saving and retrieving orders (e.g., `save`, `findById`). The core logic doesn't know or care *how* this is implemented (SQL, NoSQL, etc.).
    *   `message/publisher/payment/OrderCreatedPaymentRequestMessagePublisher.java`: An interface that defines how to publish a message when an order is created.

---

## 3. Adapters: The Outside World

Adapters are the components that connect the ports to specific technologies. They are "outside" the hexagon and are interchangeable. You could swap a PostgreSQL adapter for a MongoDB adapter without changing a single line of code inside the hexagon.

### a. Input Adapters (Driving Adapters)

These are the components that call the *input ports*. They adapt an external request into a method call on an application service.

**Mapped to your code:**
*   **`order-application` module:** This module would typically contain the REST controllers. A `OrderController` class here would be an input adapter. It would receive an HTTP request, convert it to a `CreateOrderCommand` DTO, and call the `orderApplicationService.createOrder()` method (the input port).
*   **`order-messaging` module:** A Kafka consumer class would be an input adapter. It would listen to a Kafka topic, receive a message, and call the `paymentResponseMessageListener.paymentCompleted()` method (the input port).

### b. Output Adapters (Driven Adapters)

These are the concrete implementations of the *output ports*.

**Mapped to your code:**
*   **`order-data-access` module:** This module provides the implementation for the repository output ports. It contains the JPA entities, and the `OrderRepositoryImpl` class that actually interacts with the database. It *adapts* the application's need for data persistence into SQL queries.
*   **`order-messaging` module:** This module provides the implementation for the message publisher output ports. It contains the Kafka producer logic that sends messages to the appropriate Kafka topics. It *adapts* the application's need to send a message into the specifics of the Kafka client library.

---

## Summary Diagram

Here is a simplified view of how it all connects:

```
[External World]      <-- Adapters -->      [Ports]      <-- Core Application -->      [Domain Model]

  (REST API,          (order-application,   (Interfaces in    (Use Cases like             (Order, Customer,
   Kafka, UI)          order-data-access,    order-domain/      OrderCreateCommandHandler)   Money)
                       order-messaging)       ports/)
```

I hope this clears things up! This structure is designed to protect your most valuable asset—your business logic—from being tightly coupled to technology choices that will inevitably change over time.
