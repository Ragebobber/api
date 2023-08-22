## About The Api

Rest api for desktop and web client

## Getting Started

### Prerequisites
1. Create DB 
2. Connect to DB (change props in application.properties)
```
spring.datasource.url=jdbc:postgresql://localhost:5432/blatant
spring.datasource.username=postgres
spring.datasource.password=postgres
```

### Installation
1. Create folder client in src/main/resources
2. Put to src/main/resources/client 
   * Loader.exe
   * libssl-3.dll
   * libcrypto-3.dll
3. Build the project

## Features
* Tls v1.3
* Key-store
* SSL
* JWT 
* Auth for desktop and web client
* JSON
* File uploader
* Zip array downloader
* Hash file checksum

## License

Distributed under the MIT License.See `LICENSE.txt` for more information.
