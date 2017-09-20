package com.attracttest.attractgroup.sqlitetask;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by nexus on 20.09.2017.
 */

public class CustomClassAdapter extends ArrayAdapter<CustomClass> {

    private List<CustomClass> items;
    private LayoutInflater mInflater;


    public CustomClassAdapter(Context context, List<CustomClass> items) {
        super(context, 0, items);
        this.items = items;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        // A ViewHolder keeps references to children views to avoid unneccessary calls
        // to findViewById() on each row.
        ViewHolder holder;


        // When convertView is not null, we can reuse it directly, there is no need
        // to reinflate it. We only inflate a new View when the convertView supplied
        // by ListView is null.
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item, null);

            // Creates a ViewHolder and store references to the two children views
            // we want to bind data to.
            holder = new ViewHolder();
            holder.text1 =  convertView.findViewById(R.id.first_row);
            holder.text2 =  convertView.findViewById(R.id.second_row);
            holder.text3 =  convertView.findViewById(R.id.third_row);
            holder.text4 =  convertView.findViewById(R.id.fourth_row);
            holder.text5 =  convertView.findViewById(R.id.fifth_row);

            convertView.setTag(holder);
        } else {

            // Get the ViewHolder back to get fast access to the TextView
            holder = (ViewHolder) convertView.getTag();
        }

        // Bind the data efficiently with the holder.
        holder.text1.setText(items.get(i).getName());
        holder.text2.setText(items.get(i).getSurname());
        holder.text3.setText(items.get(i).getDate());
        holder.text4.setText(items.get(i).getDesc());
        holder.text5.setText(items.get(i).getMisc());

        return convertView;
    }

    static class ViewHolder{
        TextView text1;
        TextView text2;
        TextView text3;
        TextView text4;
        TextView text5;
    }


}
