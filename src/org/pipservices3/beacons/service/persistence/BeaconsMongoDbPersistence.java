package org.pipservices3.beacons.service.persistence;

import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.pipservices3.beacons.service.data.version1.BeaconV1;
import org.pipservices3.commons.data.DataPage;
import org.pipservices3.commons.data.FilterParams;
import org.pipservices3.commons.data.PagingParams;
import org.pipservices3.mongodb.persistence.IdentifiableMongoDbPersistence;

import java.util.ArrayList;
import java.util.List;

public class BeaconsMongoDbPersistence extends IdentifiableMongoDbPersistence<BeaconV1, String>
        implements IBeaconsPersistence {

    public BeaconsMongoDbPersistence(){
        super("beacons", BeaconV1.class);
        this._maxPageSize = 1000;
    }

    private Bson composeFilter(FilterParams filter) {
        filter = filter != null ? filter : new FilterParams();

        List<Bson> criteria = new ArrayList<>();

        var id = filter.getAsNullableString("id");
        if (id != null) {
            criteria.add(Filters.eq("_id", id));
        }

        var siteId = filter.getAsNullableString("site_id");
        if (siteId != null)
            criteria.add(Filters.eq("site_id", siteId));

        var label = filter.getAsNullableString("label");
        if (label != null)
            criteria.add(Filters.eq("label", label));

        var udi = filter.getAsNullableString("udi");
        if (udi != null)
            criteria.add(Filters.eq("udi", udi));

        var udis = filter.getAsObject("udis");

        if (udis instanceof String) {
            udis = ((String) udis).split(",");
            var udisList = List.of((String[])udis);
            criteria.add(Filters.in("udi",  udisList));
        }

        return criteria.size() > 0 ? Filters.and(criteria) : null;
    }
    @Override
    public DataPage<BeaconV1> getPageByFilter(String correlationId, FilterParams filter, PagingParams paging) {
        return super.getPageByFilter(correlationId, this.composeFilter(filter), paging, null, null);
    }

    @Override
    public BeaconV1 getOneByUdi(String correlationId, String udi) {
        Bson criteria = new Document("udi", udi);

        var item =  _collection.find(criteria).first();
//        var item =  _collection.find(criteria, BeaconV1.class).first();
        if (item != null) this._logger.trace(correlationId, "Found beacon by %s", udi);
        else this._logger.trace(correlationId, "Cannot find beacon by %s", udi);

        return this.convertToPublic(item);
    }
}
