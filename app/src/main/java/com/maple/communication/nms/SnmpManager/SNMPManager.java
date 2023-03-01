package com.maple.communication.nms.SnmpManager;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

/**
 * Created by vineesh.c on 11/30/2015.
 */
public class SNMPManager {

    Snmp snmp = null;
    String address = "192.168.2.29";

    /**
     * Constructor
     *
     * @param add
     */
    public SNMPManager(String add) {
        address = add;
    }


    /**
     * Start the Snmp session. If you forget the listen() method you will not
     * get any answers because the communication is asynchronous
     * and the listen() method listens for answers.
     *
     *
     */
    public void start() {
        try {
            TransportMapping transport = new DefaultUdpTransportMapping();
            snmp = new Snmp(transport);

// Do not forget this line!
            transport.listen();
        }catch (Exception e) {}
    }

    /**
     * Method which takes a single OID and returns the response from the agent as a String.
     *
     * @param oid
     * @return
     *
     */
    public String getAsString(OID oid)  {
        ResponseEvent event = get(new OID[]{oid});
        return event.getResponse().get(0).getVariable().toString();
    }

    /**
     * This method is capable of handling multiple OIDs
     *
     * @param oids
     * @return

     */
    public ResponseEvent get(OID oids[]) {
        PDU pdu = new PDU();
        for (OID oid : oids) {
            pdu.add(new VariableBinding(oid));
        }
        pdu.setType(PDU.GET);
        try {
            ResponseEvent event = snmp.send(pdu, getTarget(), null);
            if (event != null) {
                return event;
            }

          //  throw new RuntimeException("GET timed out");
        }catch (Exception e) {}
        return null;
    }

    /**
     * This method returns a Target, which contains information about
     * where the data should be fetched and how.
     *
     * @return
     */
    private Target getTarget() {
        Address targetAddress = GenericAddress.parse(address);
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString("public"));
        target.setAddress(targetAddress);
        target.setRetries(2);
        target.setTimeout(1500);
        target.setVersion(SnmpConstants.version2c);
        return target;
    }
}
