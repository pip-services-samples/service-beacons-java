package org.pipservices3.beacons.service.services;

import org.pipservices3.commons.refer.Descriptor;
import org.pipservices3.rpc.services.CommandableHttpService;

public class BeaconsHttpServiceV1 extends CommandableHttpService {
    public BeaconsHttpServiceV1() {
        super("v1/beacons");
        this._dependencyResolver.put("controller", new Descriptor("beacons", "controller", "*", "*", "1.0"));
    }
}
