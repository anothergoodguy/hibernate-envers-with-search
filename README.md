# store

This application was generated using JHipster 7.9.3, you can find documentation and help at [https://www.jhipster.tech/documentation-archive/v7.9.3](https://www.jhipster.tech/documentation-archive/v7.9.3).

## Start

1. First bring up the ecosystem with

```
docker-compose -f src/main/docker/ecosystem.yml up -d && docker-compose -f src/main/docker/ecosystem.yml logs -f
```

2. Build the application with

```agsl
./gradlew clean -Pdev bootJar
```

3. Run the application with

```agsl
java -jar build/libs/store-0.0.1-SNAPSHOT.jar
```

4. Wait for the application to start listening on 8080 and ensure that liquibase has populated the data structure
5. Login to the application on [http://localhost:8080](http://localhost:8080) with user: `admin` and password `admin`
6. To load the test data -> Navigate to [http://localhost:8080/admin/docs](http://localhost:8080/admin/docs) and execute the request on `data-loader-resource` under `http://localhost:8080/api/load`

# To Browse The Database:

7. To browse the MYSQL data open PHPMyAdmin on [http://localhost:8010/](http://localhost:8010/) and select `store` database

# To Browse The Elasticsearch Instance:

8. To Browse the instance and index(s) structure use `elastichq` on [http://localhost:5000/](http://localhost:5000/)
9. To Browse the data on the index(s) use `elasticvue` on [http://localhost:8090/](http://localhost:8090/)
