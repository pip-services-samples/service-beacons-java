package org.pipservices3.beacons.service.logic;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.pipservices3.beacons.data.version1.BeaconTypeV1;
import org.pipservices3.beacons.data.version1.BeaconV1;
import org.pipservices3.beacons.service.persistence.BeaconsMemoryPersistence;
import org.pipservices3.commons.config.ConfigParams;
import org.pipservices3.commons.data.FilterParams;
import org.pipservices3.commons.data.PagingParams;
import org.pipservices3.commons.errors.ApplicationException;
import org.pipservices3.commons.refer.Descriptor;
import org.pipservices3.commons.refer.References;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class BeaconsControllerTest {

    BeaconsMemoryPersistence persistence;
    BeaconsController controller;

    BeaconV1 BEACON1 = new BeaconV1(
            "1",
            "00001",
            BeaconTypeV1.AltBeacon,
            "1",
            "TestBeacon1",
            Map.of("type", "Point", "coordinates", List.of(0, 0)),
            50
    );

    BeaconV1 BEACON2 = new BeaconV1(
            "2",
            "00002",
            BeaconTypeV1.iBeacon,
            "1",
            "TestBeacon2",
            Map.of("type", "Point", "coordinates", List.of(2, 2)),
            70
    );

    @Before
    public void setup() throws ApplicationException {
        persistence = new BeaconsMemoryPersistence();
        persistence.configure(new ConfigParams());

        controller = new BeaconsController();
        controller.configure(new ConfigParams());

        var references = References.fromTuples(
                new Descriptor("beacons", "persistence", "memory", "default", "1.0"), persistence,
                new Descriptor("beacons", "controller", "default", "default", "1.0"), controller
        );

        controller.setReferences(references);

        persistence.open(null);
    }

    @After
    public void teardown() throws ApplicationException {
        persistence.close(null);
    }

    @Test
    public void testCrudOperations() {
        // Create the first beacon
        var beacon = controller.createBeacon(
                null,
                BEACON1
        );

        assertEquals(BEACON1.udi, beacon.udi);
        assertEquals(BEACON1.siteId, beacon.siteId);
        assertEquals(BEACON1.type, beacon.type);
        assertEquals(BEACON1.label, beacon.label);
        assertNotNull(beacon.center);

        // Create the second beacon
        beacon = controller.createBeacon(
                null,
                BEACON2
        );

        assertEquals(BEACON2.udi, beacon.udi);
        assertEquals(BEACON2.siteId, beacon.siteId);
        assertEquals(BEACON2.type, beacon.type);
        assertEquals(BEACON2.label, beacon.label);
        assertNotNull(beacon.center);

        // Get all beacons
        var page = controller.getBeacons(
                null,
                new FilterParams(),
                new PagingParams()
        );

        assertEquals(page.getData().size(), 2);

        var beacon1 = page.getData().get(0);

        // Update the beacon
        beacon1.label = "ABC";

        beacon = controller.updateBeacon(
                null,
                beacon1
        );

        assertEquals(beacon1.getId(), beacon.getId());
        assertEquals("ABC", beacon.label);

        // Get beacon by udi
        beacon = controller.getBeaconByUdi(
                null,
                beacon1.udi
        );

        assertEquals(beacon1.getId(), beacon.getId());

        // Delete the beacon
        beacon = controller.deleteBeaconById(
                null,
                beacon1.getId()
        );

        assertEquals(beacon1.getId(), beacon.getId());

        // Try to get deleted beacon
        beacon = controller.getBeaconById(
                null,
                beacon1.getId()
        );

        assertNull(beacon);
    }

    @Test
    public void testCalculatePosition() {
        // Create the first beacon
        var beacon =  controller.createBeacon(
                null,
                BEACON1
        );

        assertEquals(BEACON1.udi, beacon.udi);
        assertEquals(BEACON1.siteId, beacon.siteId);
        assertEquals(BEACON1.type, beacon.type);
        assertEquals(BEACON1.label, beacon.label);
        assertNotNull(beacon.center);

        // Create the second beacon
        beacon = controller.createBeacon(
                null,
                BEACON2
        );

        assertEquals(BEACON2.udi, beacon.udi);
        assertEquals(BEACON2.siteId, beacon.siteId);
        assertEquals(BEACON2.type, beacon.type);
        assertEquals(BEACON2.label, beacon.label);
        assertNotNull(beacon.center);

        // Calculate position for one beacon
        var position = controller.calculatePosition(
                null, "1", List.of("00001")
        );

        assertEquals("Point", position.get("type"));
        assertEquals(((List<?>)position.get("coordinates")).size(), 2);
        assertEquals(0, ((List<?>)position.get("coordinates")).get(0));
        assertEquals(0, ((List<?>)position.get("coordinates")).get(1));

        // Calculate position for two beacons
        position = controller.calculatePosition(
                null, "1", List.of("00001", "00002")
        );

        assertEquals("Point", position.get("type"));
        assertEquals(((List<?>)position.get("coordinates")).size(), 2);
        assertEquals(1, ((List<?>) position.get("coordinates")).get(0));
        assertEquals(1, ((List<?>) position.get("coordinates")).get(1));
    }
}
