package org.pipservices3.beacons.service;

import org.pipservices3.beacons.service.containers.BeaconsProcess;

public class Program {
    public static void main(String[] args) {
        try {
            var proc = new BeaconsProcess();
            proc.run(args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
