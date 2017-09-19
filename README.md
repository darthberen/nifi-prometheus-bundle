# nifi-prometheus-bundle
Nifi reporting task that sends metrics to a Prometheus Push Gateway

## Requirements
* Java 8
* Nifi 1.3+

## Building
```
mvn install
```

## Deploying
Copy the nar file created in `nifi-prometheus-nar/target` to your NiFi's `lib` directory and restart NiFi.
