package com.example.firebaseapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdapterLihatBarang extends RecyclerView.Adapter<AdapterLihatBarang.ViewHolder> {

    private ArrayList<Barang> daftarBarang;
    private Context context;
    private DatabaseReference databaseReference;

    public AdapterLihatBarang(ArrayList<Barang> barangs, Context ctx){
        daftarBarang = barangs;
        context = ctx;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        ViewHolder(View v){
            super(v);
            tvTitle = (TextView) v.findViewById(R.id.tv_namabarang);

            databaseReference = FirebaseDatabase.getInstance().getReference();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        inisialisasi viewholder

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_barang, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        String kode,nama,key;
        nama = daftarBarang.get(position).getNama();
        kode = daftarBarang.get(position).getKode();
        key = daftarBarang.get(position).getKode();

        holder.tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        holder.tvTitle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                PopupMenu popupMenu = new PopupMenu(view.getContext(),view);
                popupMenu.inflate(R.menu.menubarang);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.mnEdit:
                                Bundle bundle = new Bundle();
                                bundle.putString("kunci1",kode);
                                bundle.putString("kunci2",nama);
                                bundle.putString("kunci3",key);

                                Intent intent = new Intent(view.getContext(),BarangEdit.class);
                                intent.putExtras(bundle);
                                view.getContext().startActivity(intent);
                                break;
                            case R.id.mnHapus:
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext());
                                alertDialog.setTitle("Yakin data " + nama + " akan dihapus?");
                                alertDialog.setMessage("Tekan 'Ya' untuk menghapus")
                                        .setCancelable(false)
                                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                deleteData(key);
                                                Toast.makeText(view.getContext(), "Data " + nama + " berhasil dihapus", Toast.LENGTH_LONG).show();
                                                Intent intent1 = new Intent(view.getContext(), MainActivity.class);
                                                view.getContext().startActivity(intent1);
                                            }
                                        })
                                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.cancel();
                                            }
                                        });
                                AlertDialog dialog = alertDialog.create();
                                dialog.show();
                                break;
                        }
                        return true;
                    }
                });
                popupMenu.show();
                return true;
            }

        });
        holder.tvTitle.setText(nama);


    }

    public void deleteData(String key) {
        if (databaseReference != null) {
            databaseReference.child("Barang").child(key).removeValue();
        }
    }

    @Override
    public int getItemCount() {
        return daftarBarang.size();
    }



}
