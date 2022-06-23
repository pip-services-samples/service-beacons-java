package org.pipservices3.beacons.service.logic;

import com.fasterxml.jackson.core.type.TypeReference;
import org.pipservices3.beacons.data.version1.BeaconV1;
import org.pipservices3.beacons.data.version1.BeaconV1Schema;
import org.pipservices3.commons.commands.Command;
import org.pipservices3.commons.commands.CommandSet;
import org.pipservices3.commons.commands.ICommand;
import org.pipservices3.commons.convert.TypeCode;
import org.pipservices3.commons.data.FilterParams;
import org.pipservices3.commons.data.PagingParams;
import org.pipservices3.commons.run.Parameters;
import org.pipservices3.commons.validate.ArraySchema;
import org.pipservices3.commons.validate.FilterParamsSchema;
import org.pipservices3.commons.validate.ObjectSchema;
import org.pipservices3.commons.validate.PagingParamsSchema;

import java.util.List;

public class BeaconsCommandSet extends CommandSet {
    private final IBeaconsController _controller;

    public BeaconsCommandSet(IBeaconsController controller) {
        this._controller = controller;

        this.addCommand(this.makeGetBeaconsCommand());
        this.addCommand(this.makeGetBeaconByIdCommand());
        this.addCommand(this.makeGetBeaconByUdiCommand());
        this.addCommand(this.makeCalculatePositionCommand());
        this.addCommand(this.makeCreateBeaconCommand());
        this.addCommand(this.makeUpdateBeaconCommand());
        this.addCommand(this.makeDeleteBeaconByIdCommand());
    }

    private ICommand makeGetBeaconsCommand() {
        return new Command(
                "get_beacons",
                new ObjectSchema().allowUndefined(true)
                        .withOptionalProperty("filter", new FilterParamsSchema())
                        .withOptionalProperty("paging", new PagingParamsSchema()),
                (String correlationId, Parameters args) -> {
                    var filter = FilterParams.fromValue(args.get("filter"));
                    var paging = PagingParams.fromValue(args.get("paging"));
                    return this._controller.getBeacons(correlationId, filter, paging);
                }
        );
    }

    private ICommand makeGetBeaconByIdCommand() {
        return new Command(
                "get_beacon_by_id",
                new ObjectSchema().allowUndefined(true)
                        .withRequiredProperty("beacon_id", TypeCode.String),
                (String correlationId, Parameters args) -> {
                    var beaconId = args.getAsString("beacon_id");
                    return this._controller.getBeaconById(correlationId, beaconId);
                }
        );
    }

    private ICommand makeGetBeaconByUdiCommand() {
        return new Command(
                "get_beacon_by_udi",
                new ObjectSchema().allowUndefined(true)
                        .withRequiredProperty("udi", TypeCode.String),
                (String correlationId, Parameters args) -> {
                    var udi = args.getAsString("udi");
                    return this._controller.getBeaconByUdi(correlationId, udi);
                }
        );
    }

    private ICommand makeCalculatePositionCommand() {
        return new Command(
                "calculate_position",
                new ObjectSchema().allowUndefined(true)
                        .withRequiredProperty("site_id", TypeCode.String)
                        .withRequiredProperty("udis", new ArraySchema(TypeCode.String)),
                (String correlationId, Parameters args) -> {
                    var siteId = args.getAsString("site_id");
                    var type = new TypeReference<List<String>>() {
                    };
                    var udis = args.getAsObject(type, "udis");
                    return this._controller.calculatePosition(correlationId, siteId, udis);
                }
        );
    }

    private ICommand makeCreateBeaconCommand() {
        return new Command(
                "create_beacon",
                new ObjectSchema().allowUndefined(true)
                        .withRequiredProperty("beacon", new BeaconV1Schema()),
                (String correlationId, Parameters args) -> {
                    var beacon = args.getAsObject(BeaconV1.class, "beacon");
                    return this._controller.createBeacon(correlationId, beacon);
                }
        );
    }

    private ICommand makeUpdateBeaconCommand() {
        return new Command(
                "update_beacon",
                new ObjectSchema().allowUndefined(true)
                        .withRequiredProperty("beacon", new BeaconV1Schema()),
                (String correlationId, Parameters args) -> {
                    var beacon = args.getAsObject(BeaconV1.class, "beacon");
                    return this._controller.updateBeacon(correlationId, beacon);
                }
        );
    }

    private ICommand makeDeleteBeaconByIdCommand() {
        return new Command(
                "delete_beacon_by_id",
                new ObjectSchema().allowUndefined(true)
                        .withRequiredProperty("beacon_id", TypeCode.String),
                (String correlationId, Parameters args) -> {
                    var beaconId = args.getAsString("beacon_id");
                    return this._controller.deleteBeaconById(correlationId, beaconId);
                }
        );
    }
}
