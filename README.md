# Chat OpenAI Backend

This project is a backend service for a chat application that integrates with OpenAI's API. It is built using Java and Spring Boot, providing WebSocket communication for real-time chat functionality.

## Project Structure

- **src/main/java/com/demo/chat/config**: Contains configuration files, such as `WebSocketConfig.java` for WebSocket setup.
- **src/main/java/com/demo/chat/controller**: Contains the controller classes like `ChatController.java` that handle HTTP requests.
- **src/main/java/com/demo/chat/handler**: Includes WebSocket handlers like `ChatWebSocketHandler.java`.
- **src/main/java/com/demo/chat/model**: Defines the data models, including `ChatMessage.java`.
- **src/main/java/com/demo/chat/repository**: Contains repository interfaces such as `ChatMessageRepository.java` for database operations.
- **src/main/java/com/demo/chat/service**: Implements service logic with classes like `ChatMessageService.java` and `OpenAIClient.java`.
- **src/main/java/com/demo/chat/thread**: Manages threading with `PersistenceThreadPoolManager.java`.

## Prerequisites

- Java 11 or higher
- Maven 3.6+

## Setup and Running

1. Clone the repository:
   ```bash
   git clone <repository-url>
   ```

2. Navigate to the project directory:
   ```bash
   cd chat-openai-backend
   ```

3. Build the project using Maven:
   ```bash
   ./mvnw clean install
   ```

4. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

## Usage

Once the application is running, it will be accessible at `http://localhost:8080`. You can connect to the WebSocket endpoint for real-time chat functionality.

## Contributing

Feel free to submit issues or pull requests if you have suggestions or improvements.

## License

This project is licensed under the MIT License.
