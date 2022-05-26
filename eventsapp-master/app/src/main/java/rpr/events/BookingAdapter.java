package rpr.events;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


// Adapter pour l'affichage des réservations
public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ViewHolder>  {
    private Context context;
    private List<BookingItem> booking_data;
    private boolean organise;

    public BookingAdapter(Context context, List<BookingItem> booking_data) {
        this.context = context;
        this.booking_data = booking_data;
        this.organise = organise;
    }



    public BookingAdapter(Context context, List<BookingItem> booking_data, boolean organise) {
        this(context,booking_data);
        this.organise = organise;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_card_design,parent,false);
        return new ViewHolder(itemView);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        // Récupérations des données de réservation (dates, nom evenement, etc...)
        holder.name.setText(booking_data.get(position).getName());
        Date date = new Date();
        Date date2 = new Date();
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(booking_data.get(position).getTime());
            date2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(booking_data.get(position).getTime_end());

        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.time.setText(new SimpleDateFormat("dd MMM, yyyy hh:mm aa").format(date));
        holder.time_end.setText(new SimpleDateFormat("dd MMM, yyyy hh:mm aa").format(date2));
        holder.venue.setText(booking_data.get(position).getVenue());

        // Affichage d'un check vert sur la carte de réservation dans le cas où celle-ci est confirmée
        if(booking_data.get(position).getConfirm() != 1){
            holder.confirmBooking.setVisibility(View.INVISIBLE);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EventDisplayUser.class);
                if (organise == true){
                    intent = new Intent(context, EditOrganisedEvent.class);
                }
                // Saisie d'informations nécessaires pour l'événement reservé
                intent.putExtra("event_id", booking_data.get(position).getEvent_id());
                intent.putExtra("name", booking_data.get(position).getName());
                intent.putExtra("time", booking_data.get(position).getTime());
                intent.putExtra("time_end", booking_data.get(position).getTime_end());
                intent.putExtra("venue", booking_data.get(position).getVenue());
                intent.putExtra("details", booking_data.get(position).getDetails());
                intent.putExtra("usertype", booking_data.get(position).getUsertype());
                intent.putExtra("creator_id", booking_data.get(position).getCreator_id());
                intent.putExtra("creator", booking_data.get(position).getCreator());
                intent.putExtra("category_id", booking_data.get(position).getCategory_id());
                intent.putExtra("category", booking_data.get(position).getCategory());
                intent.putExtra("image", booking_data.get(position).getImage());
                intent.putExtra("price", booking_data.get(position).getPrice());
                intent.putExtra("attendance", booking_data.get(position).getAttendance());
                intent.putExtra("nameParticipant", booking_data.get(position).getNameParticipant());
                intent.putExtra("confirm", booking_data.get(position).getConfirm());
                intent.putExtra("confirmDate", booking_data.get(position).getConfirmDate());

                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return booking_data.size();
    }

    // Mise à jour de données de filtre d'une réservation
    public void setFilteredList(ArrayList<BookingItem> filteredList){
        this.booking_data = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{

        public TextView name;
        public TextView time;
        public TextView time_end;
        public TextView venue;
        public TextView price;
        public TextView attendance;
        public TextView nameParticipant;
        public ImageView imgIcon;
        public ImageView confirmBooking;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            time = (TextView) itemView.findViewById(R.id.time);
            time_end = (TextView) itemView.findViewById(R.id.time2);
            venue = (TextView) itemView.findViewById(R.id.venue);
            price = (TextView) itemView.findViewById(R.id.price);
            attendance = (TextView) itemView.findViewById(R.id.attendance);
            imgIcon = (ImageView) itemView.findViewById(R.id.imgIcon);
            confirmBooking = (ImageView) itemView.findViewById(R.id.confirmBooking);


        }
    }
}
