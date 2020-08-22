package com.example.alifdeltaforce.realtimebooking.Adepter;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alifdeltaforce.realtimebooking.Model.Booking;
import com.example.alifdeltaforce.realtimebooking.Model.Session;
import com.example.alifdeltaforce.realtimebooking.PaymentActivity;
import com.example.alifdeltaforce.realtimebooking.R;

import java.util.List;


/**
 * Created by Alif Delta Force on 7/23/2018.
 */

public class OderAdepter extends RecyclerView.Adapter<OderAdepter.ViewHolder> {

    private List<Booking.BookingBean> data_booking;
    private Context context;

    public OderAdepter(List<Booking.BookingBean> data_booking, Context context) {
        this.data_booking = data_booking;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txt_tv_Tripname,txt_tv_seat,txt_tv_numberOfcar,txt_tv_status;
        private ItemClickListener itemClickListener;

        public ViewHolder(View itemView) {
            super(itemView);

            txt_tv_Tripname = (TextView) itemView.findViewById(R.id.name);
            txt_tv_seat = (TextView) itemView.findViewById(R.id.tv_seat);
            txt_tv_numberOfcar = (TextView) itemView.findViewById(R.id.numberOfcar);
            txt_tv_status = (TextView) itemView.findViewById(R.id.numberOfstatus);

            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener){
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v,getAdapterPosition());
        }
    }



    @NonNull
    @Override
    public OderAdepter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_oder,parent,false);

        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final OderAdepter.ViewHolder holder, int position) {
        holder.txt_tv_Tripname.setText(""+data_booking.get(position).getBooking_id());
        holder.txt_tv_seat.setText("Amount of Seat : "+data_booking.get(position).getAmount_seat());
        holder.txt_tv_numberOfcar.setText("Number of Car : "+data_booking.get(position).getBus_id());

        if (data_booking.get(position).getStatus().equals("0")){
            holder.txt_tv_status.setBackgroundColor(Color.parseColor("#ffa31a"));
            holder.txt_tv_status.setText("Status : Wait");
        }else if (data_booking.get(position).getStatus().equals("1")){
            holder.txt_tv_status.setBackgroundColor(Color.parseColor("#00cc44"));
            holder.txt_tv_status.setText("Status : Pass");
        }else if (data_booking.get(position).getStatus().equals("3")){
            holder.txt_tv_status.setBackgroundColor(Color.parseColor("#ff4000"));
            holder.txt_tv_status.setText("Status : Fail!");
        }

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (data_booking.get(position).getStatus().equals("0")){
                    Snackbar.make(view,"Not Generate Payout Please Wait",Snackbar.LENGTH_SHORT).show();
                }else if (data_booking.get(position).getStatus().equals("1")){
                    Intent i = new Intent(context, PaymentActivity.class);
                    i.putExtra(Session.TAG_FORGENREPORT,data_booking.get(position).getBooking_id());
                    context.startActivity(i);
                }else if (data_booking.get(position).getStatus().equals("3")){
                    Snackbar.make(view,"Not Generate Payout Please not response for driver",Snackbar.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        if (data_booking != null){
            return data_booking.size();
        }
       return 0;
    }

}
