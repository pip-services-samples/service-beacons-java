package org.pipservices3.beacons.service.persistence;

import org.pipservices3.beacons.data.version1.BeaconV1;
import org.pipservices3.commons.data.DataPage;
import org.pipservices3.commons.data.FilterParams;
import org.pipservices3.commons.data.PagingParams;
import org.pipservices3.commons.errors.ApplicationException;
import org.pipservices3.data.persistence.IdentifiableMemoryPersistence;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class BeaconsMemoryPersistence extends IdentifiableMemoryPersistence<BeaconV1, String>
        implements IBeaconsPersistence {

    public BeaconsMemoryPersistence() {
        super(BeaconV1.class);

        this._maxPageSize = 1000;
    }

    private Predicate<BeaconV1> composeFilter(FilterParams filter) {
        filter = filter != null ? filter : new FilterParams();

        var id = filter.getAsNullableString("id");
        var siteId = filter.getAsNullableString("site_id");
        var label = filter.getAsNullableString("label");
        var udi = filter.getAsNullableString("udi");
        var udis = filter.getAsObject("udis");

        if (udis != null)
            udis = Arrays.stream(((String) udis).split(",")).toList();

        if (udis != null && ((List<?>) udis).isEmpty())
            udis = null;

        List<String> finalUdis = (List<String>) udis;
        return (BeaconV1 item) -> {
            if (id != null && !Objects.equals(item.getId(), id))
                return false;
            if (siteId != null && !Objects.equals(item.siteId, siteId))
                return false;
            if (label != null && !Objects.equals(item.label, label))
                return false;
            if (udi != null && !Objects.equals(item.udi, udi))
                return false;
            if (finalUdis != null && !finalUdis.contains(item.udi))
                return false;
            return true;
        };
    }

    @Override
    public DataPage<BeaconV1> getPageByFilter(String correlationId, FilterParams filter, PagingParams paging) {
        try {
            return super.getPageByFilter(correlationId, this.composeFilter(filter), paging, null, null);
        } catch (ApplicationException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public BeaconV1 getOneByUdi(String correlationId, String udi) {
        var item = this._items.stream().filter((b) -> Objects.equals(b.udi, udi)).findFirst();

        if (item.isPresent()) this._logger.trace(correlationId, "Found beacon by %s", udi);
        else this._logger.trace(correlationId, "Cannot find beacon by %s", udi);

        return item.get();
    }
}
