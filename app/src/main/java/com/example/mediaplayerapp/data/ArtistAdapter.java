package com.example.mediaplayerapp.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mediaplayerapp.R;


import java.util.List;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ARV> {

    private Context context;
    private List<Artist> artisList;

    public ArtistAdapter(Context context, List<Artist> artisList) {
        this.context = context;
        this.artisList = artisList;
    }

    @NonNull
    @Override
    public ARV onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ARV(LayoutInflater.from(parent.getContext()).inflate(R.layout.artist_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ARV holder, int position) {

        Artist artis = artisList.get(position);
        if (artis != null) {
            holder.artNaam.setText(artis.ArtistName);
        }

    }

    @Override
    public int getItemCount() {
        return artisList.size();
    }

    public class ARV extends RecyclerView.ViewHolder{


        private TextView artNaam;

        public ARV(@NonNull View itemView) {
            super(itemView);
            artNaam = itemView.findViewById(R.id.artname);
        }
    }
}
