package elena.notesapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class NotesAdapter extends ArrayAdapter<String> {

    Context mContext;
    int mLayoutResourceId;
    private ArrayList<String> mData ;

    public NotesAdapter(Context context, int resource, ArrayList<String> data) {
        super(context, resource, data);
        this.mContext = context;
        this.mLayoutResourceId = resource;
        this.mData = data;
    }

    @Override
    public String getItem(int position) {
        return super.getItem(position);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        row = inflater.inflate(mLayoutResourceId, parent, false);
        
        TextView text=(TextView)row.findViewById(R.id.nameTextView);
        ImageView img=(ImageView)row.findViewById(R.id.imageView);
        img.setImageResource(R.drawable.note);

        String mystring=mData.get(position);
        text.setText(mystring);
        return row;
    }
}