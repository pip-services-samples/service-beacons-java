package org.pipservices3.beacons.service.services;

import jakarta.ws.rs.core.GenericType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.pipservices3.beacons.data.version1.BeaconTypeV1;
import org.pipservices3.beacons.data.version1.BeaconV1;
import org.pipservices3.beacons.service.logic.BeaconsController;
import org.pipservices3.beacons.service.persistence.BeaconsMemoryPersistence;
import org.pipservices3.commons.config.ConfigParams;
import org.pipservices3.commons.data.DataPage;
import org.pipservices3.commons.data.FilterParams;
import org.pipservices3.commons.data.PagingParams;
import org.pipservices3.commons.errors.ApplicationException;
import org.pipservices3.commons.refer.Descriptor;
import org.pipservices3.commons.refer.References;
import org.pipservices3.rpc.test.TestCommandableHttpClient;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class BeaconsHttpServiceV1Test {

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

    BeaconsMemoryPersistence persistence;
    BeaconsController controller;
    BeaconsHttpServiceV1 service;
    TestCommandableHttpClient client;

    @Before
    public void setup() throws ApplicationException {
        var restConfig = ConfigParams.fromTuples(
                "connection.protocol", "http",
                "connection.port", 3000,
                "connection.host", "localhost"
        );

        persistence = new BeaconsMemoryPersistence();
        persistence.configure(new ConfigParams());

        controller = new BeaconsController();
        controller.configure(new ConfigParams());

        service = new BeaconsHttpServiceV1();
        service.configure(restConfig);

        client = new TestCommandableHttpClient("v1/beacons");
        client.configure(restConfig);

        var references = References.fromTuples(
                new Descriptor("beacons", "persistence", "memory", "default", "1.0"), persistence,
                new Descriptor("beacons", "controller", "default", "default", "1.0"), controller,
                new Descriptor("beacons", "service", "http", "default", "1.0"), service
        );

        controller.setReferences(references);
        service.setReferences(references);

        persistence.open(null);
        service.open(null);
        client.open(null);
    }

    @After
    public void teardown() throws ApplicationException {
        client.close(null);
        service.close(null);
        persistence.close(null);
    }

    @Test
    public void testCrudOperations() throws ApplicationException {
        BeaconV1 beacon1;

        // Create the first beacon
        var beacon = client.callCommand(BeaconV1.class,
                "create_beacon",
                null,
                Map.of("beacon", BEACON1)
        );

        assertEquals(BEACON1.udi, beacon.udi);
        assertEquals(BEACON1.siteId, beacon.siteId);
        assertEquals(BEACON1.type, beacon.type);
        assertEquals(BEACON1.label, beacon.label);
        assertNotNull(beacon.center);

        // Create the second beacon
        beacon = client.callCommand(BeaconV1.class,
                "create_beacon",
                null,
                Map.of("beacon", BEACON2)
        );

        assertEquals(BEACON2.udi, beacon.udi);
        assertEquals(BEACON2.siteId, beacon.siteId);
        assertEquals(BEACON2.type, beacon.type);
        assertEquals(BEACON2.label, beacon.label);
        assertNotNull(beacon.center);

        // Get all beacons
        var page = client.callCommand(new GenericType<DataPage<BeaconV1>>() {
                                      },
                "get_beacons",
                null,
                Map.of(
                        "filter", new FilterParams(),
                        "paging", new PagingParams()
                )
        );

        assertEquals(page.getData().size(), 2);

        beacon1 = page.getData().get(0);

        // Update the beacon
        beacon1.label = "ABC";

        beacon = client.callCommand(
                BeaconV1.class,
                "update_beacon",
                null,
                Map.of("beacon", beacon1)
        );

        assertEquals(beacon1.siteId, beacon.siteId);
        assertEquals("ABC", beacon.label);

        // Get beacon by udi
        beacon = client.callCommand(
                BeaconV1.class,
                "get_beacon_by_udi",
                null,
                Map.of("udi", beacon1.udi)
        );

        assertEquals(beacon1.siteId, beacon.siteId);

        // Calculate position for one beacon
        var position = client.callCommand(Map.class,
                "calculate_position",
                null,
                Map.of(
                        "site_id", "1",
                        "udis", List.of("00001")
                )
        );

        assertEquals("Point", position.get("type"));
        assertEquals(((List) position.get("coordinates")).size(), 2);
        assertEquals(0, ((List<?>) position.get("coordinates")).get(0));
        assertEquals(0, ((List<?>) position.get("coordinates")).get(1));

        // Delete the beacon
        beacon = client.callCommand(
                BeaconV1.class,
                "delete_beacon_by_id",
                null,
                Map.of("beacon_id", beacon1.getId())
        );

        assertEquals(beacon1.getId(), beacon.getId());

        // Try to get deleted beacon
        var res = client.callCommand(
                Object.class,
                "get_beacon_by_id",
                null,
                Map.of("beacon_id", beacon1.getId())
        );

        assertNull(res);
    }
}
