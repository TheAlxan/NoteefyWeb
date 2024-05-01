# NoteefyWeb

`NoteefyWeb` builds upon the functionality of `Noteefy` to provide robust event transportation capabilities using the
Publisher and Subscriber Pattern across networked environments. Unlike traditional event systems, NoteefyWeb not only
ensures the reliable transmission of events but also maintains the order of events, crucial for applications requiring
strict sequencing.

#### Key Features:

- <b>Event Transportation:</b> Seamlessly transport events between different components of your distributed system,
  enabling efficient communication and coordination.
- <b>Publisher and Subscriber Pattern:</b> Utilize familiar patterns for event handling, simplifying integration and
  enhancing flexibility.
- <b>Order Preservation:</b> Preserve the order of events, ensuring that subscribers receive events in the same sequence
  as they were published, essential for maintaining application logic and consistency.
- <b>Asynchronous Publishing:</b> Support for asynchronous publishing allows publishers to emit events without blocking,
  optimizing performance and responsiveness.
- <b>Scalability:</b> Designed with scalability in mind, NoteefyWeb can handle large volumes of events across
  distributed architectures, enabling your application to grow without compromising performance.

## Table of Contents

- [Download](#download)
- [Milestones](#milestones)
- [Components](#components)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)

## Download

### Maven
```
<dependency>
    <groupId>io.github.thealxan</groupId>
    <artifactId>NoteefyWeb</artifactId>
    <version>0.1</version>
</dependency>
```

### Gradle
```
implementation group: 'io.github.thealxan', name: 'NoteefyWeb', version: '0.1'
```

## Milestones

- [ ] <b>Add documentation</b>
- [ ] <b>Add configuration file</b>
- [ ] <b>Use custom `ByteBuff` instead of `VertxBuff`</b>
- [ ] <b>Implement default HTTP `Server` and `Bridge`</b>
- [ ] <b> Add support for publishing on topic</b>

## Components

Here are the key components of `NoteefyWeb`:

- `ReadSource<I>`: Reads data from a source.
- `WriteSource<O>`: Writes data to a source.
- `DataSource<I,O>`: Contains a `ReadSource<I>` and a `WriteSource<O>`.
- `DataSourceInfo<I,O>`: Defines the types `I` and `O`.
- `Bridge<I,O>`: Establishes a connection between two `NoteefyWeb` instances over a network.
- `BridgeReader<I>`: Reads data from a `ReadSource<I>` into the Bridge.
- `BridgeWriter<O>`: Writes data from the Bridge into a `WriteSource<O>`.

## Usage

**Check out the example package for server and client connections.**

### Server

```java
NoteefyWeb noteefyWeb = new NoteefyWeb();

noteefyWeb.createServer(dataSourceInfo, remoteAddress);
noteefyWeb.createServer(TcpDataSourceInfo.getInstance(), otherRemoteAddress);

noteefyWeb.registerWebPublisher(publisher);
publisher.publish(256);
publisher.publish("256");
```

By executing the above code, the publishers' events will be published to connected network Bridges.

### Client

```java
NoteefyWeb noteefyWeb = new NoteefyWeb();

noteefyWeb.connectToRemoteBridge(dataSourceInfo, remoteAddress);

noteefyWeb.registerToRemotePublisher(publisherAddress, subscriber);
subscriber.doOnEvent(Integer.class, event->{ someActions(); });
subscriber.doOnEvent(String.class, new StringEventHandler());
```

This setup enables receiving events from remote publishers through the Bridge and dispatching them to registered
subscribers.

### Data Types

The `<I,O>` types represent the input and output types of the Bridge, respectively. They define the content type sent
between bridges.

### Custom Servers

`NoteefyWeb` allows users to create custom servers to suit their specific needs. While the library comes with a default
TCP server implemented using `Verx`, users have the flexibility to develop their own server implementations.

#### Implementation Process

- <b>Interface Implementation:</b> Users can create custom servers by implementing the `Server<I,O>` interface. This
  interface defines the methods necessary for handling incoming connections and processing data.
- <b>Factory Creation:</b> To instantiate custom server objects, users need to develop new server factories by
  implementing the `ServerFactory<I,O>` interface. This factory is responsible for creating instances of the custom
  servers.
- <b>Registration:</b> Once the server factory is implemented, users can register it to the `ServerAbstractFactory`.
  This registration process enables `NoteefyWeb` to access and utilize the custom server factory when needed.

### Custom Bridges

In addition to the default TCP Bridge provided by NoteefyWeb using Verx Socket, users have the flexibility to create
custom bridges tailored to their specific networking requirements.

#### Implementation Process

- <b>Interface Implementation:</b> Users can develop custom bridges by implementing the `Bridge<I,O>` interface. This
  interface outlines the necessary methods for establishing communication between different network endpoints and
  facilitating data transfer.
- <b>Factory Creation:</b> To instantiate instances of custom bridges, users need to create new bridge factories by
  implementing the `BridgeFactory<I,O>` interface. This factory is responsible for producing instances of the custom
  bridges.
- <b>Registration:</b> After implementing the bridge factory, users can register it with the `BridgeAbstractFactory`.
  This registration process enables `NoteefyWeb` to access and utilize the custom bridge factory as needed.

### Custom ReadSource and BridgeReader

`ReadSource` encapsulates the input object, such as sockets or streams, and is responsible for reading data from the
source. The `BridgeReader<I>` acts as a handler for `ReadSource<I>`, processing incoming data. When new data is
received, the `BridgeReader` utilizes its `Serializer` to deserialize the data, making it usable within the system.

### Custom WriteSource and BridgeWriter

`WriteSource` wraps the output object, such as sockets or streams, and facilitates writing data to the destination.
The `BridgeWriter<O>` functions as a writer for `WriteSource<I>`, responsible for transmitting data. When new data is to
be sent, the `BridgeWriter` employs its `Serializer` to serialize the data before writing it to the `WriteSource`,
ensuring compatibility and efficient transmission.

## Contributing

We welcome contributions to `NoteefyWeb`! Here's how you can contribute:

- Report bugs or suggest features by opening an issue.
- Submit pull requests to address issues or implement new features.
- Help improve documentation, code quality, and test coverage.

## License

`NoteefyWeb` is licensed under the MIT License.