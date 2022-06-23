package org.pipservices3.beacons.service.persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.pipservices3.commons.config.ConfigParams;
import org.pipservices3.commons.errors.ApplicationException;
import org.pipservices3.commons.errors.ConfigException;

public class BeaconsFilePersistenceTest {
    BeaconsFilePersistence persistence;
    BeaconsPersistenceFixture fixture;

    @Before
    public void setup() throws ApplicationException {
        persistence = new BeaconsFilePersistence();
        persistence.configure(ConfigParams.fromTuples(
                "path", "data/beacons.test.json"
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
