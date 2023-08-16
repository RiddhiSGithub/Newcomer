package com.example.newcomers.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newcomers.activities.RideDetailActivity;
import com.example.newcomers.beans.Trip;
import com.example.newcomers.beans.User;
import com.example.newcomers.databinding.RecordLayoutBinding;
import com.example.newcomers.fragments.RideListFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class TripListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
   private List<Trip> tripList;
   RecordLayoutBinding recordLayoutBinding;
   Context context;

   public TripListAdapter(List<Trip> tripList, Context context) {
      super();
      this.tripList = tripList;
      this.context = context;
   }

   /**

    * @param parent   The ViewGroup into which the new View will be added after it is bound to
    *                 an adapter position.
    * @param viewType The view type of the new View.
    * @return A new ViewHolder that holds a View of the given view type.
    * @see #getItemViewType(int)
    * @see #onBindViewHolder(ViewHolder, int)
    */
   @NonNull
   @Override
   public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      //return null;
      LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
      recordLayoutBinding = RecordLayoutBinding.inflate(layoutInflater,parent,false);
      return new ViewHolder(recordLayoutBinding);
   }


   /**

    * @param holder   The ViewHolder which should be updated to represent the contents of the
    *                 item at the given position in the data set.
    * @param position The position of the item within the adapter's data set.
    */
   @Override
   public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
      ((ViewHolder)holder).bindView(tripList.get(position));
   }

   /**
    * Returns the total number of items in the data set held by the adapter.
    *
    * @return The total number of items in this adapter.
    */
   @Override
   public int getItemCount() {
      return tripList.size();
   }



   protected class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
      Trip trip;
      RecordLayoutBinding recylerRowBinding;
      public ViewHolder(RecordLayoutBinding recordLayoutBinding) {
         super(recordLayoutBinding.getRoot());
         recylerRowBinding = recordLayoutBinding;
      }

      public void bindView(Trip e) {
         this.trip = e;
         recylerRowBinding.txtFrom.setText(String.valueOf(e.from));
         recylerRowBinding.txtDestination.setText(String.valueOf(e.destination));
         recylerRowBinding.txtCarModel.setText(String.valueOf(e.carModel));
         recylerRowBinding.txtSeatRemain.setText(e.getSeatTotal()-e.getSeatTaken()+"/"+e.getSeatTotal()+" Seat Remain");
         // TODO other Attribute

         FirebaseFirestore.getInstance().collection("users")
                         .document(e.getUserID())
                        .get()
                       .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                          @Override
                          public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                              if(task.isSuccessful()) {
                                 DocumentSnapshot user = task.getResult();
                                 User u = user.toObject(User.class);
                                 Log.d("----", "onComplete: "+u.toString());
                                 if(u != null && u.getUsername() != null) {

                                    recylerRowBinding.txtNickname.setText(u.getUsername());
                                    if(u.getProfileImage()!=null && u.getProfileImage().startsWith("http")) {

                                       Picasso.get()
                                               .load(u.getProfileImage())
                                               .resize(50, 50)
                                               .centerCrop()
                                               .transform(new RoundedCornersTransformation(25,0))
                                               .into(recylerRowBinding.ivUserHeader);
                                    }
                                 }
                              }
                          }
                       })
                      .addOnFailureListener(new OnFailureListener() {
                         @Override
                         public void onFailure(@NonNull Exception e) {

                         }
                      });



         recylerRowBinding.row.setOnClickListener(this);
      }

      /**
       * Called when a view has been clicked.
       *
       * @param v The view that was clicked.
       */
      @Override
      public void onClick(View v) {
         Log.i("------",trip.toString());
         Intent intent = new Intent(context, RideDetailActivity.class);
         intent.putExtra("TRIP_DETAIL",trip);
         context.startActivity(intent);
      }
   }
}
