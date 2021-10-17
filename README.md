# Table of Contents
- [Table of Contents](#table-of-contents)
- [About the Project](#about-the-project)
- [Functional Requirements](#functional-requirements)
- [Built With](#built-with)
- [Getting Started](#getting-started)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Usage](#usage)
    - [To run (docker ready)](#to-run-docker-ready)
- [Development Support Commands](#development-support-commands)
    - [To build the app](#to-build-the-app)
    - [To run locally tests](#to-run-locally-tests)
    - [To run Test Development Mode](#to-run-test-development-mode)
    - [To run development](#to-run-development)
    - [To run production](#to-run-production)
- [Roadmap](#roadmap)
- [Contributing](#contributing)
- [License](#license)
- [Contact](#contact)
- [Acknowledgements](#acknowledgements)

# About the Project
E-Commerce is really easy, in theory. You have x stock for a given product, and ideally, you only sell as much as you have, not more, not less. As a company, we just made the decision to externalise the logic for managing, reserving and ultimately selling inventory into its own micro service, and it's your task to create a proof of concept of said service.
Solution architecture has provided a rough outline on how the service is supposed to be working:
* For each product (do not consider individual sizes, we just assume for simplicity that a product only ever has one size), there's three stock levels: IN_STOCK, RESERVED and SOLD. Stock starts being IN_STOCK, it can be reserved, and once reserved, moved to SOLD. Stock in RESERVED should at one point be automatically freed up, but that's out of scope for this PoC.

# Functional Requirements
This system consists of the following endpoints:
```
- PATCH /product/:id/stock, with a payload consisting of a JSON Object that is formed as follows:
```
```
{"stock": 123}
```
This endpoint sets the stock that is available to be sold (i.e. IN_STOCK), overwriting any existing values for IN_STOCK. If a record for the product does not exist, a new one is created.
```
- GET /product/:id This endpoint returns a JSON Object returning the stock level for the product given in the following form:
```
```
{"IN_STOCK": 123, "RESERVED": 4, "SOLD": 12}
```
If there is no record for the given Product ID, the request returns a status code of 404 with an arbitrary response body that must be ignored, otherwise the status code is a 200.
```
- POST /product/:id/reserve, without any payload. This call reserves one item of stock. On success, a 200 status code is returned, with a response JSON that is having the following form
```
```
{"reservationToken": "22489339-5462-458b-b184-fc1f55eedab5"}
```
This call moves stock from IN_STOCK to reserved. Should the IN_STOCK column have a value lower than 1, this call returns a status code of 400 (feel free to suggest a semantically more fitting one) with an arbitrary response body that must be ignored.
The Reservation Token is unique for one item of stock and is required to later mark an item of stock as sold – without this token, both the unreserve (i.e. the inverse operation) and the final sold move cannot be performed. It is the callers responsibility to keep track of this reservationToken.
```
- POST /product/:id/unreserve, with a JSON payload like this:
```
```
{"reservationToken": "22489339-5462-458b-b184-fc1f55eedab5"}
```
This endpoint returns a 200 status code and moves one item of stock from RESERVED to IN_STOCK if, and only if, the reservation token is recognised and belonging to the product ID indicated. After the stock item has been moved to IN_STOCK again, it is not possible to use the reservation token again, it can be destroyed by the caller.
```
- POST /product/:id/sold, with a JSON payload of:
```
```
{"reservationToken": "22489339-5462-458b-b184-fc1f55eedab5"}
```
This endpoint moves a stock unit from RESERVED to the state of SOLD, and only if the reservation token is valid for this product. If successful, a 200 status code is returned, otherwise the response is 400. After this operation is complete, the reservation token can no longer be used and can be destroyed by the caller.
This API is a minimal set of functions that are needed by our web frontend to make stock reservations, show available stock and convert units to being sold.
A special focus in the implementation of the proof of concept should be put on two aspects: The data should be held in a database that is external to the application. Secondly, this service will need to scale significantly and still perform robustly, with a special regard for correctness of reservations. Ensure that even if the reserve endpoint is called twice at exactly the same time, and only one unit of stock is left, only one reservation is returned – there should be robustness in regards to concurrency, with no race conditions or double reservations happening.

# Built With
* [Node.js](https://nodejs.org/en/about/)
* [MongoDB](https://www.mongodb.com)
* [Mocha](https://mochajs.org)
* [Chai](https://chaijs.com)
* [Express](https://expressjs.com)

# Getting Started
# Prerequisites
* [Docker Desktop](https://www.docker.com/products/docker-desktop)

# Installation
```
git clone https://github.com/jeortizquan/ecommerce-app.git
```

# Usage
### To run (docker ready)
go to the cloned folder of the app, and run the following commands to get the service up and running.
```
cp .env.example .env && npm install
npm run build
docker-compose up -d
```

Default address and port
```
http://localhost:5000
```
a postman collection is included to test the endpoints
```
ecommerce-app.postman_collection.json
```
# Development Support Commands
go to the cloned folder and run the command of your choice
### To build the app
```
npm run build
```

### To run locally tests
if the first time
```
cp .env.example .env && npm install
npm run build
```
otherwise just run
```
docker-compose -f docker-compose.test.yaml up -d
npm run test
docker-compose -f docker-compose.test.yaml down
```

### To run Test Development Mode

```
npm run start:test-dev
```

### To run development

```
npm run start:dev
```
### To run production

```
npm run start:prod
```

# Roadmap
# Contributing
Any contributions you make are appreciated.

1. Fork the Project
2. Create your Feature Branch (git checkout -b feature/AmazingFeature)
3. Commit your Changes (git commit -m 'Add some AmazingFeature')
4. Push to the Branch (git push origin feature/AmazingFeature)
5. Open a Pull Request

# License
private

# Contact
jeortiz.quan@gmail.com

# Acknowledgements
