package com.maple.communication.nms.SnmpManager;

import android.util.Log;

import com.maple.communication.nms.Data.CMTSData;
import com.maple.communication.nms.Data.TransponderData;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.util.ArrayList;

import static java.lang.Boolean.FALSE;

/**
 * Created by Vineesh C on 19-06-2016.
 */
public class SNMPWrapper {
    updateTransponderList trListCallBack;
    updateTransponderDetails trDetialsCallback;

    public SNMPWrapper(updateTransponderList cb){
        trListCallBack = cb;
        trDetialsCallback = (updateTransponderDetails)cb;
    }

    public static String tag = "";
   // public static PDU pdu;
   // public static CommunityTarget comtarget;
   // public static VariableBinding req;
  //  public static ResponseEvent response;
    public static CMTSData cmstData;
    public static TransportMapping transport;
  //  public static String port;

    public void getTransponderDetails( final CMTSData cmData) {

            final ArrayList<TransponderData> transIpList = cmData.transDataList;
            final CMTSData cmtsDetail = new CMTSData(cmData.ipAddress, cmData.readCommunity, cmData.writeCommunity, "", cmData._id, transIpList, cmData.siteID);
            final String port = "161";
            // cmtsDetail.ipAddress = ipAddress;
            // cmtsDetail.community = community;
           final String oidValue = "1.3.6.1.2.1.4.35.1.4";
            // private static String oidValue = "1.3.6.1.2.1.10.127.1.3.3.1.21.2";
           final int snmpVersion = SnmpConstants.version2c;

            Snmp snmp;

            String tag = "SNMP CLIENT";




            Thread thread = new Thread() {
                @Override
                public void run() {


                    // ArrayList<TransponderData> transIpList = new ArrayList<TransponderData>();
                    //  try {
                    //      transIpList = getTransponderIPList();
                    //   } catch (Exception e) {
                    //       e.printStackTrace();
                    //   }

                    // Log.i("RESULT >>>>>>>>>> ", "TrasnsllistSize"+ transIpList.size());
                     CommunityTarget comtarget = new CommunityTarget();
                    // comtarget.setCommunity(new OctetString(community));
                    comtarget.setVersion(snmpVersion);
                    // comtarget.setAddress(new UdpAddress(ipAddress + "/" + port));
                    comtarget.setRetries(2);
                    comtarget.setTimeout(1000);

                    // Prepare PDU
                    final VariableBinding  req = new VariableBinding();
                    OID oid = new OID(oidValue);
                    req.setOid(oid);
                    final PDU pdu = new PDU();
                    pdu.add(req);
                    for (int i = 0; i < transIpList.size(); i++) {
                       // transIpList.get(i).community = "RWRWRWRW";
                        if (transIpList.get(i).community != null) {
                            //String transCommunity = transIpList.get(i).community;
                            // String transCommunity = transIpList.get(i).community;//"RWRWRWRW";
                          //  String transCommunity = "RWRWRWRW";
                            String transCommunity=transIpList.get(i).community;

                            Log.i("RESULT >>>>>>>>>> ", "!!!############### Trans Community of  s ::" + transCommunity);
                            try {
                                //ALRM
                                transIpList.get(i).status = GlobalVars.onlineStatus.OFFLINE;
                                String alarmOID = "1.3.6.1.4.1.5591.1.3.1.6.0";

                                String alarmResult = null;
                                ResponseEvent response=null;
                                alarmResult = getSNMPResponse(alarmOID, transIpList.get(i).ipAddress, transCommunity,comtarget,req,response,pdu,port);

                                //  Log.i("RRRRRRRRRRR >>>>>>>>>> ", ">>>jjjjj " +alarmResult);
                                if (alarmResult == null) {
                                    Log.i("RESULT >>>>>>>>>> ", ">>>NULL ");
                                } else {
                                    transIpList.get(i).status = GlobalVars.onlineStatus.ONLINE;
                                    //  Log.i("RESULT ", " IP: " + transIpList.get(i).ipAddress +" ALARM STAT:  "+alarmResult );
                                    String[] alrmTocken = alarmResult.split("=");
                                    String alrStr = alrmTocken[1].replace("]", "").trim();

                                    //    int a = Integer.parseInt(alrStr);


                                    //int alarmStatus= (int)Long.parseLong(Long.parseLong(alrStr, 16));

                                    int alarmStatus = (int) Long.parseLong(alrStr, 16);
                                    Log.i(" >>>>>>>>>> ", ">>>ALARAM  $$$$$ " + alarmStatus);

                                    int c = (alarmStatus & 8);
                                    if (c == 8) {
                                        transIpList.get(i).alarm = GlobalVars.transpoderAlarm.MAJOR;

                                    } else {
                                        c = (alarmStatus & 16);
                                        if (c == 16) {

                                            transIpList.get(i).alarm = GlobalVars.transpoderAlarm.MINOR;
                                        } else {
                                            transIpList.get(i).alarm = GlobalVars.transpoderAlarm.NORMAL;
                                        }

                                    }
                                }

                                String oidSysUpTime = "1.3.6.1.2.1.1.3.0";
                                String upTime = getSNMPResponse(oidSysUpTime, transIpList.get(i).ipAddress, transCommunity,comtarget,req,response,pdu,port);
                                if (upTime == null) {
                                    Log.i("RESULT >>>>>>>>>> ", ">>>NULL ");
                                } else {
                                    transIpList.get(i).status = GlobalVars.onlineStatus.ONLINE;
                                    String[] upTimeTocken = upTime.split("=");

                                    String systemUpTime = upTimeTocken[1].replace("]", "").trim();

                                    transIpList.get(i).sysUpTime = systemUpTime;
                                }


                                String oidTrans = "1.3.6.1.2.1.1.1.0";
                                String result = getSNMPResponse(oidTrans, transIpList.get(i).ipAddress, transCommunity,comtarget,req,response,pdu,port);
                                if (result == null) {
                                    Log.i("RESULT >>>>>>>>>> ", ">>>NULL ");
                                } else {
                                    //Log.i("RESULT >>>>>>>>>> ",""+result);
                                    transIpList.get(i).status = GlobalVars.onlineStatus.ONLINE;
                                    String tocken = result.substring(result.indexOf("<<") + 1, result.indexOf(">>"));
                                    String[] transDisc = tocken.split(";");

                                    String[] HWStr = transDisc[0].split(":");
                                    transIpList.get(i).HWVer = HWStr[1];

                                    String[] FWStr = transDisc[3].split(":");
                                    transIpList.get(i).FWVer = FWStr[1];
                                    Log.i("", "GFWWWWWWWWW : " + FWStr[1]);

                                    String[] modelStr = transDisc[4].split(":");
                                    if (modelStr[1].contains("SMG")) {
                                        transIpList.get(i).transType = GlobalVars.transpoderType.SMG;
                                    } else if (modelStr[1].contains("DSM3")) {
                                        transIpList.get(i).transType = GlobalVars.transpoderType.DSM3;
                                    } else if (modelStr[1].contains("DM")) {
                                        transIpList.get(i).transType = GlobalVars.transpoderType.DM;
                                    } else {
                                        transIpList.get(i).transType = GlobalVars.transpoderType.UNKNOWN;
                                    }

                                }


                            } catch (Exception e) {
                                e.printStackTrace();

                            }
                        }
                        else
                        {
                            transIpList.get(i).transType = GlobalVars.transpoderType.UNKNOWN;
                        }
                        cmtsDetail.transDataList = transIpList;
                        cmtsDetail.ipAddress = cmData.ipAddress;
                        cmtsDetail.community = cmData.community;
                        if (cmtsDetail.transDataList == null) {
                            Log.i("", "CMTS Details -NULL");
                        } else {
                            Log.i("", "CMTS Details NOT NULL : " + cmtsDetail.transDataList.size());
                        }

                    }

                    trDetialsCallback.updateTransDetails(cmData);


                }
            };

            thread.start();



    }

    public void getTransponderList(final CMTSData cmtsData) {

        try {

            Log.i("Transponder","Transponder IP Address is :"+cmtsData.ipAddress);
            Log.i("Transponder","Transponder IP Address is :"+cmtsData.readCommunity);
            final ArrayList<TransponderData> transIpList = new ArrayList<>();
          //  final CMTSData cmtsData = new CMTSData("", "", "", "", 0, transIpList);
            final int port = 161;


            // private static String oidValue = "1.3.6.1.2.1.10.127.1.3.3.1.21.2";
           final int snmpVersion = SnmpConstants.version2c;


            Snmp snmp;


            String tag = "SNMP CLIENT";




            Thread thread = new Thread() {
                @Override
                public void run() {
                    try {
                        String oidValue = "1.3.6.1.2.1.4.35.1.4";
                      CommunityTarget  comtarget = new CommunityTarget();
                        comtarget.setCommunity(new OctetString(cmtsData.readCommunity));
                        comtarget.setVersion(snmpVersion);
                        comtarget.setAddress(new UdpAddress(cmtsData.ipAddress + "/" + port));
                        comtarget.setRetries(2);
                        comtarget.setTimeout(1000);

                        // Prepare PDU
                       VariableBinding req = new VariableBinding();
                        OID oid = new OID(oidValue);
                        req.setOid(oid);
                       PDU pdu = new PDU();
                        pdu.add(req);
                        ResponseEvent response=null;

                        ArrayList<TransponderData> transIpList = new ArrayList<TransponderData>();
                        try {
                            transIpList = getTransponderIPList(pdu,comtarget,response,req);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        for (int i = 0; i < transIpList.size(); i++) {
                            Log.i("Test", "\n " + "IP Address : " + transIpList.get(i).ipAddress +
                                    ",MAC:" + transIpList.get(i).macID);

                        }
                        cmtsData.transDataList = transIpList;
                        trListCallBack.updateTransList(cmtsData);
                    }
                    catch (Exception e)
                    {
                        trListCallBack.updateTransList(cmtsData);
                    }

                }
            };

            thread.start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            trListCallBack.updateTransList(cmtsData);
        }
    }

    String getSNMPResponse(String oidTrans, String TransIP, String transCommunity,CommunityTarget comtarget,VariableBinding req,ResponseEvent response,PDU pdu,String port) throws Exception {
        comtarget.setCommunity(new OctetString(transCommunity));
        String SNMPValue = null;
        TransportMapping transport1 = new DefaultUdpTransportMapping();
        transport1.listen();
        String ip =TransIP.trim();
        comtarget.setAddress(new UdpAddress(ip + "/" + port));

        //String lastOID = new String("1.3.6.1.2.1.4.35.1.5.1.1.4.10.1.3.7 ");
        OID troid = new OID(oidTrans);
        req.setOid(troid);

        Snmp snmp = new Snmp(transport1);
        Log.i(tag, "Sending Request to Agent...");


        response = snmp.get(pdu, comtarget);
        if (response != null) {
            //onReceivingSNMPResponse.sendEmptyMessage(0);

            PDU responsePDU = response.getResponse();  //Here get Null response

            if (responsePDU != null) {
                int errorStatus = responsePDU.getErrorStatus();
                int errorIndex = responsePDU.getErrorIndex();
                String errorStatusText = responsePDU.getErrorStatusText();

                if (errorStatus == PDU.noError) {

                    SNMPValue = responsePDU.getVariableBindings().toString();


                } else {
                    Log.i((String) tag, "Error: Request Failed");
                    Log.i(tag, "Error Status = " + errorStatus);
                    Log.i(tag, "Error Index = " + errorIndex);
                    Log.i(tag, "Error Status Text = " + errorStatusText);
                }

            } else {
                Log.i(tag, "Error: Response PDU is null");  // This get in Logcat
            }


        } else {
            Log.e(tag, " Response >>>Null");
        }


        // Process Agent Response

        snmp.close();


        return SNMPValue;

    }

    private ArrayList<TransponderData> getTransponderIPList(PDU pdu, Target comtarget,ResponseEvent response,VariableBinding req) throws Exception {
        // Create TransportMapping and Listen

        TransportMapping transport = new DefaultUdpTransportMapping();
        transport.listen();
        String lastOID = new String("1.3.6.1.2.1.4.35.1.5.1.1.4.10.1.3.7 ");
        String OIDPart = null;
        String[] transIP = null;
        ArrayList<TransponderData> transponderList = new ArrayList<TransponderData>();
        Snmp snmp = new Snmp(transport);
        boolean stopFlag = true;
        Log.i(tag, "Sending Request to Agent...");

        long startMillis=System.currentTimeMillis();

        do {
            Log.e("SNMP",">>>>>>>>>>>>>>>>>>>>>>>>SNMP: starting the mib walk");
            response = snmp.getNext(pdu, comtarget);
            if (response != null) {
                //onReceivingSNMPResponse.sendEmptyMessage(0);

                PDU responsePDU = response.getResponse();  //Here get Null response

                if (responsePDU != null) {
                    int errorStatus = responsePDU.getErrorStatus();
                    int errorIndex = responsePDU.getErrorIndex();
                    String errorStatusText = responsePDU.getErrorStatusText();

                    if (errorStatus == PDU.noError) {

                        String[] parts = responsePDU.getVariableBindings().toString().split("=");


                        Log.e("SNMP",">>>>>>>>>>>>>>>>>SNMP: variable part data:"+responsePDU.getVariableBindings().toString());
                        OIDPart = parts[0].replace("[", "");
                        String MACPart = parts[1].replace("]", "");

                        if ((!OIDPart.equals("1.3.6.1.2.1.4.35.1.4.1.1.4.10.1.3.7 ")) && (!OIDPart.equals("1.3.6.1.2.1.4.35.1.5.1.1.4.10.1.3.7 "))) {

                            String[] oidByDigit = OIDPart.split("\\.");

                            if ((oidByDigit[11].equals("1")) && (oidByDigit[12].equals("4"))) {
                                TransponderData trans = new TransponderData();
                                trans.ipAddress = oidByDigit[13] + "." + oidByDigit[14] + "." + oidByDigit[15] + "." + oidByDigit[16];
                                trans.macID = MACPart;
                                int stop =Integer.parseInt(oidByDigit[9]);
                                if (stop >4 ){
                                    stopFlag = false;
                                }

                                transponderList.add(trans);

                            } else if ((oidByDigit[11].equals("2")) && (oidByDigit[12].equals("16"))) {

                                //   Log.i(tag," SNMP 6");

                            }
                        }


                    } else {
                        Log.i((String) tag, "Error: Request Failed");
                        Log.i(tag, "Error Status = " + errorStatus);
                        Log.i(tag, "Error Index = " + errorIndex);
                        Log.i(tag, "Error Status Text = " + errorStatusText);
                    }

                } else {
                    Log.i(tag, "Error: Response PDU is null");  // This get in Logcat
                }


            } else {
                Log.e(tag, " Response >>>Null");
            }

            if(OIDPart!=null)
              req.setOid(new OID(OIDPart));
            long currentMillis=System.currentTimeMillis();

            if((currentMillis-startMillis)>3*60*1000)
                stopFlag=true;
        } while (stopFlag);

        Log.e("SNMP",">>>>>>>>>>>>>>>>>>>>>>>>SNMP: completed the mib walk");
        // Process Agent Response

        snmp.close();


        return transponderList;
    }
}
