package org.pipservices3.beacons.service.persistence;

import org.pipservices3.beacons.service.data.version1.BeaconV1;
import org.pipservices3.commons.config.ConfigParams;
import org.pipservices3.commons.errors.ConfigException;
import org.pipservices3.data.persistence.JsonFilePersister;

public class BeaconsFilePersistence extends BeaconsMemoryPersistence {
    protected JsonFilePersister<BeaconV1> _persister;

    public BeaconsFilePersistence() {
        this(null);
    }
    public BeaconsFilePersistence(String path) {
        super();

        this._persister = new JsonFilePersister<>(BeaconV1.class, path);
        this._loader = this._persister;
        this._saver = this._persister;
    }

    @Override
    public void configure(ConfigParams config) throws ConfigException {
        super.configure(config);
        this._persister.configure(config);
    }
}
