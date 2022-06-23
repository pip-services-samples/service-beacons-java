package org.pipservices3.beacons.service.logic;

import org.pipservices3.beacons.data.version1.BeaconV1;
import org.pipservices3.commons.data.DataPage;
import org.pipservices3.commons.data.FilterParams;
import org.pipservices3.commons.data.PagingParams;

import java.util.List;
import java.util.Map;

public interface IBeaconsController {

    DataPage<BeaconV1> getBeacons(String correlationId, FilterParams filter,
                                  PagingParams paging);

    BeaconV1 getBeaconById(String correlationId, String beaconId);

    BeaconV1 getBeaconByUdi(String correlationId, String beaconId);

    Map<String, ?> calculatePosition(String correlationId, String siteId, List<String> udis);

    BeaconV1 createBeacon(String correlationId, BeaconV1 beacon);

    BeaconV1 updateBeacon(String correlationId, BeaconV1 beacon);

    BeaconV1 deleteBeaconById(String correlationId, String beaconId);
}
