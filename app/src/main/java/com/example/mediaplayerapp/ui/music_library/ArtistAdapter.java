package com.example.mediaplayerapp.ui.music_library;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mediaplayerapp.R;
import com.example.mediaplayerapp.data.Album;
import com.example.mediaplayerapp.data.Artist;


import java.util.ArrayList;
import java.util.List;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ARV> implements Filterable {

    private Context context;
    private ArrayList<Artist> artisList;
    private ArrayList<Artist> artisListOld;

    public ArtistAdapter(Context context, ArrayList<Artist> artisList) {
        this.context = context;
        this.artisList = artisList;
        this.artisListOld=artisList;
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

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String strsearch= charSequence.toString();
                if (strsearch.isEmpty())
                {
                    artisList=artisListOld;
                }else {
                    ArrayList<Artist> NewList = new ArrayList<>();
                    for(Artist artist : artisListOld){
                        if(artist.getArtistName().toLowerCase().contains(strsearch.toLowerCase()))
                        {
                            NewList.add(artist);
                        }
                    }
                    artisList=NewList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values=artisList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                artisList= (ArrayList<Artist>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ARV extends RecyclerView.ViewHolder implements View.OnClickListener{


        private TextView artNaam;

        public ARV(@NonNull View itemView) {
            super(itemView);
            artNaam = itemView.findViewById(R.id.artname);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            long artistId = artisList.get(getAbsoluteAdapterPosition()).ArtistId;
            FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            Fragment fragment;
            transaction.setCustomAnimations(R.anim.layout_fad_in, R.anim.layout_fad_out,
                    R.anim.layout_fad_in, R.anim.layout_fad_out);
            fragment = ArtistDetailFragment.newInstance(artistId);
            transaction.add(R.id.song_container,fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}
