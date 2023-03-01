package com.maple.communication.nms.Data;

import java.util.ArrayList;

/**
 * Created by 1013373 on 11-05-2016.
 */
public class siteData {

    public String siteName;
    public boolean isSelected;
    public long _id;
    public ArrayList<CMTSData> cmtsDatas;

    public void  siteData(){}

    public siteData(String siteName, boolean isSelected, long _id, ArrayList<CMTSData> cmtsDatas) {
        this.siteName = siteName;
        this.isSelected = isSelected;
        this._id=_id;
        this.cmtsDatas=cmtsDatas;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
