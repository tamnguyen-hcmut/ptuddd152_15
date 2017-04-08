package fapp.dhbk.nhdtam.labproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Asus on 3/17/2017.
 */

public class CurrencyAdapter extends BaseAdapter {

    Context context;
    int myLayout;
    List<Currency> arrList;

    public CurrencyAdapter(Context context, int myLayout, List<Currency> arrList) {
        this.context = context;
        this.myLayout = myLayout;
        this.arrList = arrList;
    }

    @Override
    public int getCount() {
        return arrList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(myLayout, null);

        TextView txtFName = (TextView) convertView.findViewById(R.id.txtFName);
        TextView txtCName = (TextView) convertView.findViewById(R.id.txtCName);
        TextView txtAName = (TextView) convertView.findViewById(R.id.txtAName);
        ImageView imgFlag = (ImageView) convertView.findViewById(R.id.imgFlag);

        txtFName.setText(arrList.get(position).FullName);
        txtCName.setText(arrList.get(position).CountryName);
        txtAName.setText(arrList.get(position).AcronymName);
        String str = arrList.get(position).Image.replaceAll(" \\(.*\\)", "");
        str = str.replaceAll(" \\[.*\\]", "");
        str = str.replaceAll(",", "");
        str = str.replaceAll("Ç", "c");
        str = str.replaceAll("Ô", "o");
        str = str.replaceAll("É", "e");
        str = str.replaceAll("'", "");
        str = str.replaceAll("’", "");
        str = str.replaceAll("-", "_");
        str = str.replaceAll(" ", "_");
        str = str.toLowerCase();
        //txtCName.setText(str);
        Integer index = context.getResources().getIdentifier(str, "drawable", context.getPackageName());
        imgFlag.setImageResource(index);

        return convertView;
    }
}
