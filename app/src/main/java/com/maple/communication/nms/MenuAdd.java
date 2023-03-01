package com.maple.communication.nms;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;

import com.maple.communication.nms.Adapter.SiteAdapter;
import com.maple.communication.nms.Data.siteViewData;
import com.maple.communication.nms.DataManager.RAMManager;


import java.util.ArrayList;

/**
 * Created by 1013373 on 11-05-2016.
 */
public class MenuAdd {

    public void showRemoveMenu(final Activity activity, final Menu menu, final ArrayList<siteViewData> siteListItems, final SiteAdapter adapter, final SiteActivity siteactivity, final RAMManager ramManager)
    {
        MenuItem btnAdd = menu.findItem(R.id.add);
        // set your desired icon here based on a flag if you like
        btnAdd.setIcon(activity.getResources().getDrawable(R.drawable.delete_icon));

btnAdd.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
    @Override
    public boolean onMenuItemClick(MenuItem item) {


        for(int i=0;i<siteListItems.size();i++)
        {
            if(siteListItems.get(i).isSelected)
            {
                long _id=siteListItems.get(i)._id;

                ramManager.DeleteSite(siteListItems.get(i).siteName);
                siteListItems.remove(i);
            }
           // siteListItems.get(i).isSelected=false;
        }

        adapter.notifyDataSetChanged();
        showAddMenu(activity,menu,siteactivity);
        //siteactivity.refreshSiteList();
        return true;
    }
});
    }

    public void showAddMenu(Activity activity, Menu menu, final SiteActivity siteActivity)
    {
        MenuItem btnAdd = menu.findItem(R.id.add);
        // set your desired icon here based on a flag if you like
        btnAdd.setIcon(activity.getResources().getDrawable(R.drawable.addbtn));
        btnAdd.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                siteActivity.showSiteAddScreen();
                return true;
            }
        });

    }

}
