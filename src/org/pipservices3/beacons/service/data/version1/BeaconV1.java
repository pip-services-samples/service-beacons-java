package org.pipservices3.beacons.service.data.version1;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.pipservices3.commons.data.IStringIdentifiable;
import org.pipservices3.commons.data.IdGenerator;

import java.util.Map;

public class BeaconV1 implements IStringIdentifiable {
    private String _id;
    public BeaconV1() {}

    public BeaconV1(String id, String udi, String type, String siteId, String label, Map<String, Object> center, float radius) {
        this._id = id;
        this.siteId = siteId;
        this.type = type;
        this.udi = udi;
        this.label = label;
        this.center = center;
        this.radius = radius;
    }

    @JsonProperty("site_id")
    public String siteId;

    @JsonProperty("type")
    public String type;

    @JsonProperty("udi")
    public String udi;

    @JsonProperty("label")
    public String label;

    @JsonProperty("center")
    public Map<String, Object> center; // GeoJson

    @JsonProperty("radius")
    public float radius;

    @JsonProperty("id")
    @Override
    public String getId() {
        return _id;
    }

    @Override
    public void setId(String value) {
        _id = value;
    }

    @Override
    public String withGeneratedId() {
        _id = IdGenerator.nextLong();
        return _id;
    }
}
