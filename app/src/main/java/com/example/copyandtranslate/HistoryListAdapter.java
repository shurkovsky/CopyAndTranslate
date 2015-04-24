package com.example.copyandtranslate;

/**
 * Created by Alexander on 4/6/2015.
 */

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/********* Adapter class extends with BaseAdapter and implements with OnClickListener ************/
public class HistoryListAdapter extends BaseAdapter implements View.OnClickListener {

    /*********** Declare Used Variables *********/
    private Activity mActivity;
    private ArrayList mData;
    private static LayoutInflater mInflater =null;
    public Resources mResources;
    HistoryListModel mTempValues =null;
    int i=0;

    /*************  CustomAdapter Constructor *****************/
    public HistoryListAdapter(Activity a, ArrayList d,Resources resLocal) {

        /********** Take passed values **********/
        mActivity = a;
        mData = d;
        mResources = resLocal;

        /***********  Layout inflator to call external xml layout () ***********/
        mInflater = ( LayoutInflater )mActivity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    /******** What is the size of Passed Arraylist Size ************/
    public int getCount() {

        if (mData == null)
            return 0;

        return mData.size();
    }

    public Object getItem(int position) {
        return mData.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    /********* Create a holder Class to contain inflated xml file elements *********/
    public static class ViewHolder{

        public TextView text;
        public TextView text1;
        public TextView textWide;
    }

    /****** Depends upon mData size called for each row , Create each ListView row *****/
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder;

        if(convertView==null){

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = mInflater.inflate(R.layout.history_item_layout, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.text = (TextView) vi.findViewById(R.id.sourceText);
            holder.text1=(TextView)vi.findViewById(R.id.translatedText);

            /************  Set holder with LayoutInflater ************/
            vi.setTag( holder );
        }
        else
            holder=(ViewHolder)vi.getTag();

        /***** Get each Model object from Arraylist ********/
        mTempValues = null;
        mTempValues = ( HistoryListModel ) mData.get( position );

        /************  Set Model values in Holder elements ***********/

        holder.text.setText( mTempValues.getSourceText() );
        holder.text1.setText(mTempValues.getTranslatedText());

        /******** Set Item Click Listener for LayoutInflater for each row *******/

        vi.setOnClickListener(new OnItemClickListener( position ));

        return vi;
    }

    @Override
    public void onClick(View v) {
        Log.v("HistoryListAdapter", "=====Row button clicked=====");
    }

    /********* Called when Item click in ListView ************/
    private class OnItemClickListener  implements View.OnClickListener {
        private int mPosition;

        OnItemClickListener(int position){
            mPosition = position;
        }

        @Override
        public void onClick(View arg0) {


            //CustomListViewAndroidExample sct = (CustomListViewAndroidExample)activity;

            /****  Call  onItemClick Method inside CustomListViewAndroidExample Class ( See Below )****/

            //sct.onItemClick(mPosition);
        }
    }
}