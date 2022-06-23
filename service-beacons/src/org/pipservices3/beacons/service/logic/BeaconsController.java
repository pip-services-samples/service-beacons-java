package org.pipservices3.beacons.service.logic;

import org.pipservices3.beacons.data.version1.BeaconTypeV1;
import org.pipservices3.beacons.data.version1.BeaconV1;
import org.pipservices3.beacons.service.persistence.IBeaconsPersistence;
import org.pipservices3.commons.commands.CommandSet;
import org.pipservices3.commons.commands.ICommandable;
import org.pipservices3.commons.config.ConfigParams;
import org.pipservices3.commons.config.IConfigurable;
import org.pipservices3.commons.data.DataPage;
import org.pipservices3.commons.data.FilterParams;
import org.pipservices3.commons.data.PagingParams;
import org.pipservices3.commons.errors.ApplicationException;
import org.pipservices3.commons.refer.Descriptor;
import org.pipservices3.commons.refer.IReferenceable;
import org.pipservices3.commons.refer.IReferences;
import org.pipservices3.commons.refer.ReferenceException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BeaconsController implements IBeaconsController, IConfigurable, IReferenceable, ICommandable {
    private IBeaconsPersistence _persistence;
    private BeaconsCommandSet _commandSet;

    @Override
    public void configure(ConfigParams config) {
    }

    @Override
    public void setReferences(IReferences references) throws ReferenceException {
        this._persistence = references.getOneRequired(IBeaconsPersistence.class,
                new Descriptor("beacons", "persistence", "*", "*", "1.0")
        );
    }

    @Override
    public CommandSet getCommandSet() {
        if (this._commandSet == null)
            this._commandSet = new BeaconsCommandSet(this);

        return this._commandSet;
    }

    @Override
    public DataPage<BeaconV1> getBeacons(String correlationId, FilterParams filter, PagingParams paging) {
        return this._persistence.getPageByFilter(correlationId, filter, paging);
    }

    @Override
    public BeaconV1 getBeaconById(String correlationId, String beaconId) {
        return this._persistence.getOneById(correlationId, beaconId);
    }

    @Override
    public BeaconV1 getBeaconByUdi(String correlationId, String beaconId) {
        return this._persistence.getOneByUdi(correlationId, beaconId);
    }

    @Override
    public Map<String, ?> calculatePosition(String correlationId, String siteId, List<String> udis) {
        if (udis == null || udis.size() == 0)
            return null;

        var page = this._persistence.getPageByFilter(
                correlationId,
                FilterParams.fromTuples(
                        "site_id", siteId,
                        "udis", udis
                ),
                null
        );

        var beacons = page.getData() != null ? page.getData() : new ArrayList<BeaconV1>();

        var lat = 0;
        var lng = 0;
        var count = 0;
        for (var beacon : beacons) {
            if (beacon.center != null
                    && beacon.center.get("type").equals("Point")
                    && beacon.center.get("coordinates") instanceof List) {
                lng += ((List<Integer>) beacon.center.get("coordinates")).get(0);
                lat += ((List<Integer>) beacon.center.get("coordinates")).get(1);
                count += 1;
            }
        }

        if (count == 0)
            return null;

        return Map.of(
                "type", "Point",
                "coordinates", List.of(lng / count, lat / count)
        );
    }

    @Override
    public BeaconV1 createBeacon(String correlationId, BeaconV1 beacon) {
        if (beacon.getId() == null)
            beacon.withGeneratedId();
        beacon.type = beacon.type != null ? beacon.type : BeaconTypeV1.Unknown;

        try {
            return this._persistence.create(correlationId, beacon);
        } catch (ApplicationException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public BeaconV1 updateBeacon(String correlationId, BeaconV1 beacon) {
        beacon.type = beacon.type != null ? beacon.type : BeaconTypeV1.Unknown;

        try {
            return this._persistence.update(correlationId, beacon);
        } catch (ApplicationException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public BeaconV1 deleteBeaconById(String correlationId, String beaconId) {
        try {
            return this._persistence.deleteById(correlationId, beaconId);
        } catch (ApplicationException ex) {
            throw new RuntimeException(ex);
        }
    }
}
