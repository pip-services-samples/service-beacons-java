package org.pipservices3.beacons.service.persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.pipservices3.commons.config.ConfigParams;
import org.pipservices3.commons.errors.ApplicationException;

import java.util.Objects;

public class BeaconsMongoDbPersistenceTest {
    BeaconsMongoDbPersistence persistence;
    BeaconsPersistenceFixture fixture;

    String mongoUri = System.getenv("MONGO_SERVICE_URI");
    String mongoHost = System.getenv("MONGO_SERVICE_HOST") != null ? System.getenv("MONGO_SERVICE_HOST") : "localhost";
    int mongoPort = System.getenv("MONGO_SERVICE_PORT") != null ? Integer.parseInt(System.getenv("MONGO_SERVICE_PORT")) : 27017;
    String mongoDatabase = System.getenv("MONGO_SERVICE_DB") != null ? System.getenv("MONGO_SERVICE_DB") : "test";

    boolean _enabled = false;

    public BeaconsMongoDbPersistenceTest() {
        // Disable tests if mongo connection is not set
        if (Objects.equals(mongoUri, "") && Objects.equals(mongoHost, ""))
            _enabled = true;
    }

    @Before
    public void setup() throws ApplicationException {
        persistence = new BeaconsMongoDbPersistence();
        persistence.configure(ConfigParams.fromTuples(
                "connection.uri", mongoUri,
                "connection.host", mongoHost,
                "connection.port", mongoPort,
                "connection.database", mongoDatabase
        ));

        fixture = new BeaconsPersistenceFixture(persistence);

        persistence.open(null);
        persistence.clear(null);
    }

    @After
    public void teardown() throws ApplicationException {
        persistence.close(null);
    }

    @Test
    public void testCrudOperations() throws ApplicationException {
        fixture.testCrudOperations();
    }

    @Test
    public void testGetWithFilters() throws ApplicationException {
        fixture.testGetWithFilters();
    }
}
