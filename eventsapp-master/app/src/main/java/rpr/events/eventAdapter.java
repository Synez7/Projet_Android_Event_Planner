package rpr.events;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


// Adapter réalisé pour l'affichage des events
public class eventAdapter extends RecyclerView.Adapter<eventAdapter.ViewHolder>  {
    private Context context;
    private List<eventItem> event_data;
    private boolean organise;

    public eventAdapter(Context context, List<eventItem> event_data) {
        this.context = context;
        this.event_data = event_data;
        this.organise = organise;
    }



    public eventAdapter(Context context, List<eventItem> event_data, boolean organise) {
        this(context,event_data);
        this.organise = organise;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_card_design,parent,false);
        itemView.findViewById(R.id.confirmBooking).setVisibility(View.INVISIBLE);
        return new ViewHolder(itemView);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.name.setText(event_data.get(position).getName());
        Date date = new Date();
        Date date2 = new Date();
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(event_data.get(position).getTime());
            date2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(event_data.get(position).getTime_end());

        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.time.setText(new SimpleDateFormat("dd MMM, yyyy hh:mm aa").format(date));
        holder.time_end.setText(new SimpleDateFormat("dd MMM, yyyy hh:mm aa").format(date2));
        holder.venue.setText(event_data.get(position).getVenue());



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EventDisplayUser.class);
                if (organise == true){
                    intent = new Intent(context, EditOrganisedEvent.class);
                }
                intent.putExtra("event_id", event_data.get(position).getEvent_id());
                intent.putExtra("name", event_data.get(position).getName());
                intent.putExtra("time", event_data.get(position).getTime());
                intent.putExtra("time_end", event_data.get(position).getTime_end());
                intent.putExtra("venue", event_data.get(position).getVenue());
                intent.putExtra("details", event_data.get(position).getDetails());
                intent.putExtra("usertype", event_data.get(position).getUsertype());
                intent.putExtra("creator_id", event_data.get(position).getCreator_id());
                intent.putExtra("creator", event_data.get(position).getCreator());
                intent.putExtra("category_id", event_data.get(position).getCategory_id());
                intent.putExtra("category", event_data.get(position).getCategory());
                intent.putExtra("image", event_data.get(position).getImage());
                intent.putExtra("price", event_data.get(position).getPrice());
                intent.putExtra("attendance", event_data.get(position).getAttendance());


                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return event_data.size();
    }

    public void setFilteredList(ArrayList<eventItem> filteredList){
        this.event_data = filteredList;
        notifyDataSetChanged();
    }

    public  class ViewHolder extends  RecyclerView.ViewHolder{

        public TextView name;
        public TextView time;
        public TextView time_end;
        public TextView venue;
        public TextView price;
        public TextView attendance;
        public ImageView imgIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            time = (TextView) itemView.findViewById(R.id.time);
            time_end = (TextView) itemView.findViewById(R.id.time2);
            venue = (TextView) itemView.findViewById(R.id.venue);
            price = (TextView) itemView.findViewById(R.id.price);
            attendance = (TextView) itemView.findViewById(R.id.attendance);



            imgIcon = (ImageView) itemView.findViewById(R.id.imgIcon);

        }
    }
}
