## Aircall Technical Test

The goal of this test is to implement an in-house alert notification system.

## Prerequisites

- Java 11
- Maven 3.6.0

## How to run tests 
At the root project, run the following command :

    mvn clean test

All the tests are located in the class `PagerServiceTest`.

## Current limitations

- Persistence adapter is limited to operations needed for the use cases provided in the test description.
- Database persistence is out of scope. For this adapter, we need to correctly handle concurrency control. One example of such mechanism is pessimistic locking.
- Only one alert per monitored service is handled at a time.
- Little error handling.