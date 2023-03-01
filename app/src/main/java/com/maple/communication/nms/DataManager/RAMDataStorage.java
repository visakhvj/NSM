package com.maple.communication.nms.DataManager;

import android.util.Log;

import com.maple.communication.nms.Data.CMTSData;
import com.maple.communication.nms.Data.SiteListItem;
import com.maple.communication.nms.Data.TransponderData;
import com.maple.communication.nms.Data.cmtsViewData;
import com.maple.communication.nms.Data.siteData;
import com.maple.communication.nms.Data.siteViewData;
import com.maple.communication.nms.Data.transponderViewData;
import com.maple.communication.nms.SnmpManager.GlobalVars;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Vineesh C on 17-07-2016.
 */
public class RAMDataStorage {
    private static HashMap<String, SiteListItem> RAMDataMap = new HashMap<String, SiteListItem>();
    private static RAMDataStorage RAMStorage = new RAMDataStorage();
    ArrayList<SiteListItem> ramSiteList;

    public static RAMDataStorage getInstance() {
        return RAMStorage;
    }

    public HashMap<String, SiteListItem> getRAMData() {
        return RAMDataMap;

    }

    public void setSiteList(ArrayList<SiteListItem> siteList) {
        ramSiteList = siteList;
    }

    public ArrayList<SiteListItem> getRAMSiteList() {
        return ramSiteList;
    }

    public ArrayList<SiteListItem> getSiteListFromRAMData() {
       /* ArrayList<siteData> siteList = new ArrayList<siteData>();
        Iterator it = RAMDataMap.entrySet().iterator();
        while (it.hasNext()) {
            //siteData site =new siteData();
            HashMap.Entry pair = (HashMap.Entry)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            Log.e("DATATATA ", "KEY : " +pair.getKey()+" VALUE : " + pair.getValue());


            it.remove(); // avoids a ConcurrentModificationException

        }*/
        return ramSiteList;
    }

    public ArrayList<siteViewData> getSiteList() {
        ArrayList<siteViewData> siteView = new ArrayList<siteViewData>();

        ArrayList<SiteListItem> siteListA = getSiteListFromRAMData();
        if(siteListA==null)
            return null;
        Log.e("TAG ", "##### DEBUGGGGGGG size : " + siteListA.size());
        for (int p = 0; p < siteListA.size(); p++) {
            siteViewData site = new siteViewData();

            site.siteName = siteListA.get(p).siteName;
            site.cmtsCount = siteListA.get(p).cmtsDatas.size();
            site.DMCount = 0;
            site.DSM3Count = 0;
            site.SMGCount = 0;
            site.UNKNOWNCount = 0;
            site.majorAlarmCount = 0;
            site.minorrAlarmCount = 0;
            site.isSelected=false;
            site._id=siteListA.get(p)._id;
            for (int k = 0; k < siteListA.get(p).cmtsDatas.size(); k++) {
                Log.e("TAG ", "##### DEBUGGGGGGG size 2 : " + siteListA.get(p).cmtsDatas.size());
                ArrayList<TransponderData> trlist = siteListA.get(p).cmtsDatas.get(k).transDataList;
                Log.e("TAG ", "##### DEBUGGGGGGG Trans-size 2 : " + trlist.size());

                for (int a = 0; a < trlist.size(); a++) {
                    Log.i("YYYYYYY", "\n " + "VINEES : " + trlist.get(a).transType);
                    if (trlist.get(a).transType != null) {
                        Log.i("YYYYYYY", "\n " + "NO NULL : " + trlist.get(a).transType);
                        if (trlist.get(a).transType.equals(GlobalVars.transpoderType.DM)) {
                            site.DMCount++;
                        } else if (trlist.get(a).transType.equals(GlobalVars.transpoderType.DSM3)) {
                            site.DSM3Count++;

                        } else if (trlist.get(a).transType.equals(GlobalVars.transpoderType.SMG)) {
                            site.SMGCount++;

                        } else {

                            site.UNKNOWNCount++;

                        }
                    }


                    if (trlist.get(a).alarm != null) {
                        Log.i("YYYYYYY", "\n " + "VINEES : " + trlist.get(a).alarm);
                        if (trlist.get(a).alarm.equals(GlobalVars.transpoderAlarm.MAJOR)) {
                            site.majorAlarmCount++;
                        } else if (trlist.get(a).alarm.equals(GlobalVars.transpoderAlarm.MINOR)) {
                            site.minorrAlarmCount++;

                        }
                    }

                }


            }
            siteView.add(site);
        }

        return siteView;
    }


    public ArrayList<cmtsViewData> getRamCMTSList(String siteName) {
        ArrayList<cmtsViewData> cmtsView = new ArrayList<cmtsViewData>();


        ArrayList<SiteListItem> siteListA = getSiteListFromRAMData();
        Log.e("TAG ", "##### DEBUGGGGGGG size : " + siteListA.size()+ " Name :" + siteName);
        for (int p = 0; p < siteListA.size(); p++) {
            if (siteListA.get(p).siteName.equals(siteName)) {

                ArrayList<CMTSData> cmtsD = siteListA.get(p).cmtsDatas;
                Log.e("TAG ", "##### YYYYYYYYY cmts size : " + cmtsD.size()+ " Name :" + siteName);
                for (int k = 0; k < cmtsD.size(); k++) {
                    Log.e("TAG ", "##### YYYYYYYYY cmts IP : " + cmtsD.get(k).ipAddress);
                    cmtsViewData cmtsV = new cmtsViewData();
                    cmtsV.ipAddress = cmtsD.get(k).ipAddress;
                    cmtsV.MAC = cmtsD.get(k).macID;
                    cmtsV.cmtsName = cmtsD.get(k).name;
                    cmtsV.isSelected=false;
                    cmtsV._Id=cmtsD.get(k)._id;

                   // Log.i("CMTS","CMTS Data:"+cmtsV.ipAddress+)
                    ArrayList<TransponderData> trans = cmtsD.get(k).transDataList;


                    for (int a = 0; a < trans.size(); a++) {
                        Log.i("YYYYYYY", "\n " + "VINEES : " + trans.get(a).transType);
                        if (trans.get(a).transType != null) {
                            Log.i("YYYYYYY", "\n " + "NO NULL : " + trans.get(a).transType);
                            if (trans.get(a).transType.equals(GlobalVars.transpoderType.DM)) {
                                cmtsV.DMCount++;
                            } else if (trans.get(a).transType.equals(GlobalVars.transpoderType.DSM3)) {
                                cmtsV.DSM3Count++;

                            } else if (trans.get(a).transType.equals(GlobalVars.transpoderType.SMG)) {
                                cmtsV.SMGCount++;

                            } else {

                                cmtsV.UNKNOWNCount++;

                            }
                        }


                        if (trans.get(a).alarm != null) {
                            Log.i("YYYYYYY", "\n " + "VINEES : " + trans.get(a).alarm);
                            if (trans.get(a).alarm.equals(GlobalVars.transpoderAlarm.MAJOR)) {
                                cmtsV.majorAlarmCount++;
                            } else if (trans.get(a).alarm.equals(GlobalVars.transpoderAlarm.MINOR)) {
                                cmtsV.minorrAlarmCount++;

                            }
                        }


                    }

                    cmtsView.add(cmtsV);
                }
              /*  for (int k = 0; k < siteListA.get(p).cmtsDatas.size(); k++) {
                    Log.e("TAG ", "##### DEBUGGGGGGG size 2 : " + siteListA.get(p).cmtsDatas.size());
                    ArrayList<TransponderData> trlist = siteListA.get(p).cmtsDatas.get(k).transDataList;
                    Log.e("TAG ", "##### DEBUGGGGGGG Trans-size 2 : " + trlist.size());

                }*/
            }
        }
        return cmtsView;
    }

    public ArrayList<transponderViewData> getRamTransList(String siteName, String cmtsIP) {
        ArrayList<transponderViewData> transView = new ArrayList<transponderViewData>();

        ArrayList<SiteListItem> siteListA = getSiteListFromRAMData();
        Log.e("TAG ", "##### DEBUGGGGGGG size : " + siteListA.size());
        for (int p = 0; p < siteListA.size(); p++) {
            if (siteListA.get(p).siteName.equals(siteName)) {

                ArrayList<CMTSData> cmtsD = siteListA.get(p).cmtsDatas;

                for (int k = 0; k < cmtsD.size(); k++) {
                    if (cmtsD.get(k).ipAddress.equals(cmtsIP)) {


                        ArrayList<TransponderData> trans = cmtsD.get(k).transDataList;


                        for (int a = 0; a < trans.size(); a++) {
                            transponderViewData trasViewData = new transponderViewData();
                            trasViewData.HwRev = trans.get(a).HWVer;
                            trasViewData.FWRev = trans.get(a).FWVer;
                            trasViewData.ipAddress = trans.get(a).ipAddress;
                            trasViewData.MAC = trans.get(a).macID;
                            trasViewData.sysupTime = trans.get(a).sysUpTime;
                            trasViewData.transType = trans.get(a).transType;
                            trasViewData.alarm = trans.get(a).alarm;
                            trasViewData.status = trans.get(a).status;
                            trasViewData.isSelected = false;
                            trasViewData.cmtsID=cmtsD.get(k)._id;
                            transView.add(trasViewData);

                        }


                    }
                }

            }
        }
        return transView;
    }

}