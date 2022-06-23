package org.pipservices3.beacons.service.build;

import org.pipservices3.beacons.service.logic.BeaconsController;
import org.pipservices3.beacons.service.persistence.BeaconsFilePersistence;
import org.pipservices3.beacons.service.persistence.BeaconsMemoryPersistence;
import org.pipservices3.beacons.service.persistence.BeaconsMongoDbPersistence;
import org.pipservices3.beacons.service.services.BeaconsHttpServiceV1;
import org.pipservices3.commons.refer.Descriptor;
import org.pipservices3.components.build.Factory;

public class BeaconsServiceFactory extends Factory {
    public static final Descriptor MemoryPersistenceDescriptor = new Descriptor("beacons", "persistence", "memory", "*", "1.0");
    public static final Descriptor FilePersistenceDescriptor = new Descriptor("beacons", "persistence", "file", "*", "1.0");
    public static final Descriptor MongoDbPersistenceDescriptor = new Descriptor("beacons", "persistence", "mongodb", "*", "1.0");
    public static final Descriptor ControllerDescriptor = new Descriptor("beacons", "controller", "default", "*", "1.0");
    public static final Descriptor HttpServiceV1Descriptor = new Descriptor("beacons", "service", "http", "*", "1.0");

    public BeaconsServiceFactory() {
        super();

        this.registerAsType(BeaconsServiceFactory.MemoryPersistenceDescriptor, BeaconsMemoryPersistence.class);
        this.registerAsType(BeaconsServiceFactory.FilePersistenceDescriptor, BeaconsFilePersistence.class);
        this.registerAsType(BeaconsServiceFactory.MongoDbPersistenceDescriptor, BeaconsMongoDbPersistence.class);
        this.registerAsType(BeaconsServiceFactory.ControllerDescriptor, BeaconsController.class);
        this.registerAsType(BeaconsServiceFactory.HttpServiceV1Descriptor, BeaconsHttpServiceV1.class);
    }
}
