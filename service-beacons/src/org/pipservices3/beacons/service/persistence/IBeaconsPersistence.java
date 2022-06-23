package org.pipservices3.beacons.service.persistence;

import org.pipservices3.beacons.data.version1.BeaconV1;
import org.pipservices3.commons.data.DataPage;
import org.pipservices3.commons.data.FilterParams;
import org.pipservices3.commons.data.PagingParams;
import org.pipservices3.commons.errors.ApplicationException;

public interface IBeaconsPersistence {
    DataPage<BeaconV1> getPageByFilter(String correlationId, FilterParams filter,
                                       PagingParams paging);

    BeaconV1 getOneById(String correlationId, String id);

    BeaconV1 getOneByUdi(String correlationId, String udi);

    BeaconV1 create(String correlationId, BeaconV1 item) throws ApplicationException;

    BeaconV1 update(String correlationId, BeaconV1 item) throws ApplicationException;

    BeaconV1 deleteById(String correlationId, String id) throws ApplicationException;
}
