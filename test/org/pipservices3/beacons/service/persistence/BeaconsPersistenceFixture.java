package org.pipservices3.beacons.service.persistence;

import org.pipservices3.beacons.service.data.version1.BeaconTypeV1;
import org.pipservices3.beacons.service.data.version1.BeaconV1;
import org.pipservices3.commons.data.FilterParams;
import org.pipservices3.commons.data.PagingParams;
import org.pipservices3.commons.errors.ApplicationException;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class BeaconsPersistenceFixture {

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

    BeaconV1 BEACON3 = new BeaconV1(
            "3",
            "00003",
            BeaconTypeV1.iBeacon,
            "2",
            "TestBeacon3",
            Map.of("type", "Point", "coordinates", List.of(10, 10)),
            50
    );
    private final IBeaconsPersistence _persistence;

    public BeaconsPersistenceFixture(IBeaconsPersistence persistence) {
        assertNotNull(persistence);
        this._persistence = persistence;
    }

    private void testCreateBeacons() throws ApplicationException {
        // Create the first beacon
        var beacon = this._persistence.create(
                null,
                BEACON1
        );

        assertEquals(BEACON1.udi, beacon.udi);
        assertEquals(BEACON1.siteId, beacon.siteId);
        assertEquals(BEACON1.type, beacon.type);
        assertEquals(BEACON1.label, beacon.label);
        assertNotNull(beacon.center);

        // Create the second beacon
        beacon = this._persistence.create(
                null,
                BEACON2
        );

        assertEquals(BEACON2.udi, beacon.udi);
        assertEquals(BEACON2.siteId, beacon.siteId);
        assertEquals(BEACON2.type, beacon.type);
        assertEquals(BEACON2.label, beacon.label);
        assertNotNull(beacon.center);

        // Create the third beacon
        beacon = this._persistence.create(
                null,
                BEACON3
        );

        assertEquals(BEACON3.udi, beacon.udi);
        assertEquals(BEACON3.siteId, beacon.siteId);
        assertEquals(BEACON3.type, beacon.type);
        assertEquals(BEACON3.label, beacon.label);
        assertNotNull(beacon.center);
    }

    public void testCrudOperations() throws ApplicationException {
        // Create items
        this.testCreateBeacons();

        // Get all beacons
        var page = this._persistence.getPageByFilter(
                null,
                new FilterParams(),
                new PagingParams()
        );

        assertEquals(page.getData().size(), 3);

        var beacon1 = page.getData().get(0);

        // Update the beacon
        beacon1.label = "ABC";

        var beacon = this._persistence.update(
                null,
                beacon1
        );

        assertEquals(beacon1.getId(), beacon.getId());
        assertEquals("ABC", beacon.label);

        // Get beacon by udi
        beacon = this._persistence.getOneByUdi(
                null,
                beacon1.udi
        );

        assertEquals(beacon1.getId(), beacon.getId());

        // Delete the beacon
        beacon = this._persistence.deleteById(
                null,
                beacon1.getId()
        );

        assertEquals(beacon1.getId(), beacon.getId());

        // Try to get deleted beacon
        beacon = this._persistence.getOneById(
                null,
                beacon1.getId()
        );
        assertNull(beacon);
    }

    public void testGetWithFilters() throws ApplicationException {
        // Create items
        this.testCreateBeacons();

        // Filter by id
        var page = this._persistence.getPageByFilter(
                null,
                FilterParams.fromTuples(
                        "id", "1"
                ),
                new PagingParams()
        );
        assertEquals(page.getData().size(), 1);

        // Filter by udi
        page = this._persistence.getPageByFilter(
                null,
                FilterParams.fromTuples(
                        "udi", "00002"
                ),
                new PagingParams()
        );
        assertEquals(page.getData().size(), 1);

        // Filter by udis
        page = this._persistence.getPageByFilter(
                null,
                FilterParams.fromTuples(
                        "udis", "00001,00003"
                ),
                new PagingParams()
        );
        assertEquals(page.getData().size(), 2);

        // Filter by site_id
        page = this._persistence.getPageByFilter(
                null,
                FilterParams.fromTuples(
                        "site_id", "1"
                ),
                new PagingParams()
        );
        assertEquals(page.getData().size(), 2);
    }
}
