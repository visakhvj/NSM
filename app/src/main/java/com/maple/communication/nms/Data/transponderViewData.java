package com.maple.communication.nms.Data;

import com.maple.communication.nms.SnmpManager.GlobalVars;

/**
 * Created by 1012648 on 8/22/2016.
 */
public class transponderViewData {
    public String ipAddress;
    public String MAC;
    public String HwRev;
    public String FWRev;
    public String sysupTime;
    public  GlobalVars.transpoderAlarm alarm;
    public GlobalVars.transpoderType transType;
    public GlobalVars.onlineStatus status;

    public int majorAlarmCount;
    public int minorrAlarmCount;

    public boolean isSelected=false;
    public long cmtsID;
}


