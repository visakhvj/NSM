package com.maple.communication.nms.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.maple.communication.nms.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Vineesh C on 26-01-2016.
 */
public class ExAdapter extends BaseExpandableListAdapter {
    List<Map<String, String>> groupData = new ArrayList<Map<String, String>>();
    List<List<Map<String, String>>> childData = new ArrayList<List<Map<String, String>>>();
    private static final String G_TEXT = "g_text";

    private static final String C_TEXT1 = "c_text1";
    private static final String C_TEXT2 = "c_text1";
    Context context;
    public ExAdapter(Context ctx) {
        super();
        context=ctx;
        for (int i = 0; i < 5; i++) {
            Map<String, String> curGroupMap = new HashMap<String, String>();
            groupData.add(curGroupMap);
            curGroupMap.put(G_TEXT, "CMTS " + i);

            List<Map<String, String>> children = new ArrayList<Map<String, String>>();
            for (int j = 0; j < 5; j++) {
                Map<String, String> curChildMap = new HashMap<String, String>();
                children.add(curChildMap);
                curChildMap.put(C_TEXT1, "Transponder " + j);
                curChildMap.put(C_TEXT2, "Transponder " + j);
            }
            childData.add(children);
        }

    }
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.member_listview, null);
        }

        TextView title = (TextView) view.findViewById(R.id.content_001);
        title.setText(getGroup(groupPosition).toString());
      //  Log.e("",""+ groupPosition);
        if (groupPosition==0){
            if (isExpanded) {
                view.findViewById(R.id.plus).setBackgroundResource(R.drawable.linestart);
            }else {
                view.findViewById(R.id.plus).setBackgroundResource(R.drawable.plusstart);
            }
        }
        else if (groupPosition==4){
            if (isExpanded){
            view.findViewById(R.id.plus).setBackgroundResource(R.drawable.lineter);}
            else{
                view.findViewById(R.id.plus).setBackgroundResource(R.drawable.plusend);
            }
        }
        else  {
            if (isExpanded){
            view.findViewById(R.id.plus).setBackgroundResource(R.drawable.line);}
            else {
                view.findViewById(R.id.plus).setBackgroundResource(R.drawable.plusline);
            }
        }

        //     ImageView image=(ImageView) view.findViewById(R.id.tubiao);
        //      if(isExpanded)
        //         image.setBackgroundResource(R.drawable.ic_launcher);
        //     else image.setBackgroundResource(R.drawable.background);

        return view;
    }


    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public Object getGroup(int groupPosition) {
        return groupData.get(groupPosition).get(G_TEXT).toString();
    }

    public int getGroupCount() {
        return groupData.size();

    }
    //**************************************
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.member_childitem, null);
        }
        final TextView title = (TextView) view.findViewById(R.id.child_text);
        ImageView line = (ImageView) view.findViewById(R.id.line_image);
        ImageView parentLine = (ImageView) view.findViewById(R.id.parentline);
        //Log.e(">>>>>>>>>>>>>>>", "PPPPPPOOOOSSSSSS"+groupPosition);
        if (groupPosition != 4){
            parentLine.setBackgroundResource(R.drawable.linecmts);
        }else {
            parentLine.setBackgroundResource(R.drawable.transline);
        }

        if(childPosition ==4){
            line.setBackgroundResource(R.drawable.lineter);}
        else{line.setBackgroundResource(R.drawable.line);}
        title.setText(childData.get(groupPosition).get(childPosition).get(C_TEXT1).toString());
        //   final TextView title2 = (TextView) view.findViewById(R.id.child_text2);
        //       title2.setText(childData.get(groupPosition).get(childPosition).get(C_TEXT2).toString());

        return view;
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public Object getChild(int groupPosition, int childPosition) {
        return childData.get(groupPosition).get(childPosition).get(C_TEXT1).toString();
    }

    public int getChildrenCount(int groupPosition) {
        return childData.get(groupPosition).size();
    }
    //**************************************
    public boolean hasStableIds() {
        return true;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
