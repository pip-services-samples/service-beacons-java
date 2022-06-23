package org.pipservices3.beacons.service.containers;

import org.pipservices3.beacons.service.build.BeaconsServiceFactory;
import org.pipservices3.container.ProcessContainer;
import org.pipservices3.rpc.build.DefaultRpcFactory;
import org.pipservices3.swagger.build.DefaultSwaggerFactory;

public class BeaconsProcess extends ProcessContainer {
    public BeaconsProcess() {
        super("beacons", "Beacons microservice");

        _configPath = "./config/config.yml";

        this.addFactory(new BeaconsServiceFactory());
        this.addFactory(new DefaultRpcFactory());
        this.addFactory(new DefaultSwaggerFactory());
    }
}
