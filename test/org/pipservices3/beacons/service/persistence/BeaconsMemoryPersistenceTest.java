package org.pipservices3.beacons.service.persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.pipservices3.commons.config.ConfigParams;
import org.pipservices3.commons.errors.ApplicationException;

public class BeaconsMemoryPersistenceTest {
    BeaconsMemoryPersistence persistence;
    BeaconsPersistenceFixture fixture;

    @Before
    public void setup() throws ApplicationException {
        persistence = new BeaconsMemoryPersistence();
        persistence.configure(new ConfigParams());

        fixture = new BeaconsPersistenceFixture(persistence);

        persistence.open(null);
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
