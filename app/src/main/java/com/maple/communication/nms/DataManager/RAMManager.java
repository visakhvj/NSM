package com.maple.communication.nms.DataManager;

import android.content.Context;
import android.util.Log;

import com.maple.communication.nms.Data.CMTSData;
import com.maple.communication.nms.Data.SiteListItem;
import com.maple.communication.nms.Data.TransponderData;
import com.maple.communication.nms.Data.cmtsViewData;
import com.maple.communication.nms.Data.siteData;
import com.maple.communication.nms.Data.siteViewData;
import com.maple.communication.nms.Data.transponderViewData;
import com.maple.communication.nms.SnmpManager.GlobalVars;
import com.maple.communication.nms.SnmpManager.SNMPWrapper;
import com.maple.communication.nms.SnmpManager.updateTransponderDetails;
import com.maple.communication.nms.SnmpManager.updateTransponderList;



import java.util.ArrayList;

/**
 * Created by Vineesh C on 17-07-2016.
 */
public class RAMManager implements updateTransponderList,updateTransponderDetails {
    Context context;
    RAMDataStorage RamData;
    DataHandler dataHndlr;
    ArrayList<SiteListItem> siteList;
    String lastIP ="";
    String stopIP ="";
    private static RAMManager ramManager;


    public static RAMManager getInstance(Context context) {
        if(ramManager==null)
            ramManager=new RAMManager(context);
        return ramManager;
    }

    SNMPWrapper snmp = new SNMPWrapper(this);
    public RAMManager(Context cntx){
        context=cntx;
        RamData = RAMDataStorage.getInstance();
        dataHndlr = new DataHandler(cntx);

        ArrayList<SiteListItem> siteList = getDBDataToRAM();
  Log.e("","<<<<<<<<<<<Completed1 >>>>>>>>>>>>>>>>>>>>>");


    }


    public void  AddSite(String siteName){
        dataHndlr.AddSite(siteName);
    }
    public void  DeleteSite(String siteName){
        dataHndlr.deleteSiteTable(siteName);
    }

    public void  DeleteCMTS(long cmtsID){
        dataHndlr.deleteCMTS(cmtsID);
    }
    public void deleteCMTSfromTableForSite(long siteID){
        dataHndlr.deleteCMTSfromTableForSite(siteID);
    }

    public String getTransponderCommunity(long cmtsID,String ipAddress)
    {
       return dataHndlr.getTransponderCommunity(cmtsID,ipAddress);
    }
    public void updateRAMData(){
        //Get Site from DB
     //   ArrayList<siteData> siteArrayList =  RamData.getSiteListFromRAMData();


        //Get CMTS from DB for each sites

        //Get transponder for each cmts.

    }


    public ArrayList<SiteListItem> getSiteListFromRAMData(){
        ArrayList<SiteListItem> siteArrayList =  RamData.getSiteListFromRAMData();

        //dataHndlr.getSiteTable();
       return siteArrayList;
    }

    public ArrayList<SiteListItem>  getDBDataToRAM(){
        isCompletedLoading=false;
       siteList =  dataHndlr.getSiteTable();
        Log.e("TAG","GET DB TO RAM");

        if (siteList.size()== 0){
            isCompletedLoading=true;
            return null;
        }


        boolean isNoCMTS=true;
        for (int i = 0; i < siteList.size(); i++) {
           Log.e("TAG","SITE NAme " + siteList.get(i).siteName );
            Log.e("TAG", "SITE ID " + siteList.get(i)._id);
            ArrayList<CMTSData> cmtsData =   siteList.get(i).cmtsDatas;
            if(cmtsData.size()>0)
                isNoCMTS=false;
            for (int j = 0; j < cmtsData.size(); j++) {
                Log.e("TAG", "CMTS IP " + cmtsData.get(j).ipAddress);
                Log.e("TAG", "CMTS READ " + cmtsData.get(j).readCommunity);
                Log.e("TAG", "CMTS WRITE " + cmtsData.get(j).writeCommunity);
                lastIP =cmtsData.get(j).ipAddress;

                snmp.getTransponderList(cmtsData.get(j));
            }

        }
        stopIP =lastIP;
        try {
            Thread.sleep(1000);
        }catch(InterruptedException e){

        }
        Log.e("TAG...",">>>>>>>>>>>>>>> FINISHEDDDDDDDDDDDDDDDDDDDDDDDDDDDDD >>>>>>>>>>>>>>" );
        if(isNoCMTS)
        {
            isCompletedLoading=true;
            // siteViewData siteView = RamData.getSiteList();
            RamData.setSiteList(siteList);
        }
    return siteList;
    }
    public void insertCmtsTable(String name,String ip,String readCommunity,String writeCommunity,long siteID,String macID,String status){

        dataHndlr.insertCmtsTable(name, ip, readCommunity, writeCommunity, siteID, macID, status);

    }

    public void insertTransponderTable(String ip,String readCommunity,String writeCommunity,long cmtsID,String macID,String status){

        dataHndlr.insertTransponderTable(ip, readCommunity, writeCommunity, cmtsID, macID, status);

    }

    @Override
    public void updateTransList(CMTSData cmtsData ) {
        if(cmtsData!=null) {
            Log.e("TAG...", ">>>>>>>>>>>>>>> WOW>>>>>>>>>>>>>>" + cmtsData);
            ArrayList<TransponderData> transIpList = cmtsData.transDataList;

            boolean isCommunityAvailable=false;
            for (int i = 0; i < transIpList.size(); i++) {
                Log.i("RESULT", "\n " + "IP Address : " + transIpList.get(i).ipAddress +
                        ",MAC:" + transIpList.get(i).macID);

                transIpList.get(i).community=getTransponderCommunity(cmtsData._id,transIpList.get(i).ipAddress);
                if(!transIpList.get(i).community.equals(""))
                    isCommunityAvailable=true;

            }
            for (int i = 0; i < siteList.size(); i++) {
                Log.e("", "<<<<<<CHECK>>>> Site ID = " + siteList.get(i)._id + " CMTS ID : " + cmtsData.siteID);

                if (siteList.get(i)._id == cmtsData.siteID) {
                    Log.e("", "<<<<<JUST ADD TO CMTS>>>>>>>>");

                    for (int s = 0; s < (siteList.get(i).cmtsDatas.size()); s++) {
                        if (siteList.get(i).cmtsDatas.get(s)._id == cmtsData._id) {
                            siteList.get(i).cmtsDatas.set(s, cmtsData);
                        }
                    }
                    // Log.i("TAGGGGGG", "\n " + "SITEID : " + siteList.get(i)._id);
                    // Log.i("TAGGGGGG", "\n " + "CMTS ID : " + cmtsData.siteID);


                }


            }

            if(isCommunityAvailable)
              snmp.getTransponderDetails(cmtsData);
            else {
                isCompletedLoading = true;
                // siteViewData siteView = RamData.getSiteList();
                RamData.setSiteList(siteList);
            }

        }


    }

    public boolean isCompletedLoading=false;
    @Override
    public void updateTransDetails(CMTSData cmtsData) {


        for (int p = 0; p < siteList.size(); p++) {

            // Log.i("AAAAAAAAA", "\n " + "SITEID : " + siteList.get(p)._id);
            //  Log.i("AAAAAAAAA", "\n " + "CMTS ID : " + cmtsData.siteID);
            if (siteList.get(p)._id == cmtsData.siteID) {

                for (int s =0;s<(siteList.get(p).cmtsDatas.size());s++) {
                    if (siteList.get(p).cmtsDatas.get(s)._id ==cmtsData._id){
                        siteList.get(p).cmtsDatas.set(s,cmtsData);
                        siteList.get(p).cmtsDatas.get(s).isUpdated=true;
                    }
                }



                for (int k = 0; k < cmtsData.transDataList.size(); k++) {
                    Log.i("YYYYYYY", "\n " + "TRARARARAR IP: " + cmtsData.transDataList.get(k).ipAddress);
                    Log.i("YYYYYYY", "\n " + "TRARARARAR SYSTEMUPTIME: " + cmtsData.transDataList.get(k).sysUpTime);
                     Log.i("YYYYYYY", "\n " + "TRARARARAR ALARM: " + cmtsData.transDataList.get(k).alarm);
                    // Log.i("YYYYYYY", "\n " + "TRARARARAR : " + cmtsData.transDataList.get(k).HWVer);

                }
            }
        }


        boolean isUpdatedAll=true;
        for (int p = 0; p < siteList.size(); p++) {



                for (int s =0;s<(siteList.get(p).cmtsDatas.size());s++) {
                if(siteList.get(p).cmtsDatas.get(s)!=null)
                {
                    if(!siteList.get(p).cmtsDatas.get(s).isUpdated)
                        isUpdatedAll=false;
                }
            }
        }
        if (isUpdatedAll) {
            isCompletedLoading=true;
            // siteViewData siteView = RamData.getSiteList();
                           RamData.setSiteList(siteList);

               /* ArrayList<cmtsViewData> sData = RamData.getRamCMTSList("Wee");
                Log.e("TAGGGGGG", "\n " + " TOTAL CMTS Count = "+ sData.size());
                for (int p = 0; p < sData.size(); p++) {
                    Log.i("YYYYYYY", "\n " + "ANSWER NAME : " + sData.get(p).ipAddress);
                    //  Log.i("YYYYYYY", "\n " + "ANSWER DSM3 : " + sData.get(p).transType);
                    // Log.i("YYYYYYY", "\n " + "ANSWER DM : " + sData.get(p).HwRev);
                    //  Log.i("YYYYYYY", "\n " + "ANSWER UNKNOWN : " + sData.get(p).FWRev);
                    Log.i("YYYYYYY", "\n " + "ANSWER MAJOR COUNT : " + sData.get(p).majorAlarmCount);
                    Log.i("YYYYYYY", "\n " + "ANSWER MINOR COUNT : " + sData.get(p).minorrAlarmCount);
                    Log.i("YYYYYYY", "\n " + "ANSWER SYSUPTIME : " + sData.get(p).minorrAlarmCount);


            }*/
        }

    }

}
