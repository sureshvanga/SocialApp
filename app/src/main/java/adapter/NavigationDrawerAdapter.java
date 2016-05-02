package adapter;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.socialapp.R;

import java.util.Collections;
import java.util.List;

import model.LoginResponseValues;
import model.NavDrawerItem;


public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.MyViewHolder> {
    List<NavDrawerItem> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;
    private TypedArray img;
    Typeface typeface;
    TextView title;

    public NavigationDrawerAdapter(Context context, List<NavDrawerItem> data) {
        this.context = context;
        typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Ubuntu-Light.ttf");
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.nav_drawer_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);


        title = (TextView) view.findViewById(R.id.title);
        title.setTypeface(typeface);
        if (LoginResponseValues.getInstance().getIsGmail() == 0) {
            img = view.getResources().obtainTypedArray(R.array.fb_imgs);
        } else {
            img = view.getResources().obtainTypedArray(R.array.gmail_imgs);
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NavDrawerItem current = data.get(position);
        holder.title.setText(current.getTitle());
        holder.image.setBackgroundResource(img.getResourceId(position, -1));

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView image;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            image = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }
}
