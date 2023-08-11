package com.example.newcomers.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.newcomers.R;
import com.example.newcomers.beans.Trip;
import com.example.newcomers.databinding.RecordLayoutBinding;

import java.util.List;

public class RideListAdapter extends ArrayAdapter<Trip> {
    private RecordLayoutBinding recordLayoutBinding;

    //List<Trip> list;
    public RideListAdapter(Context context, List<Trip> list) {
        super(context, R.layout.record_layout, R.id.listView ,list);
        //this.list = list;
    }

    @Override
    public int getCount() {
        Log.i("-----","count:"+super.getCount());
        return super.getCount();
    }

    @Nullable
    @Override
    public Trip getItem(int position) {
        Log.i("----+","position:"+position+" item:"+super.getItem(position).toString());
        return super.getItem(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Log.i("-----",position+"]["+convertView.toString());
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        recordLayoutBinding = RecordLayoutBinding.inflate(layoutInflater,parent,false);
        bindingView(super.getItem(position));
        return recordLayoutBinding.getRoot();
        
        //return super.getView(position, convertView, parent);
    }

    private void bindingView(Trip e) {
        recordLayoutBinding.txtFrom.setText(String.valueOf(e.from));
        recordLayoutBinding.txtDestination.setText(String.valueOf(e.destination));
        recordLayoutBinding.txtCarModel.setText(String.valueOf(e.carModel));
        recordLayoutBinding.txtSeatRemain.setText(e.seatRemain+" Seat Remain");
        // TODO other Attribute
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }


}
