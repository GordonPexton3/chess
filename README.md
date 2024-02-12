# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

# Here is Gordon's Phase 2 Link
https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=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-H-A-fWij8y8+eUvPz4Y+PK9pq32bHmshz8OHbPAazWc+V-gm1qOM5GYY5vC5xAA

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared tests`     | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

### Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
