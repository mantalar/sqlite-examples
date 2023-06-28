package com.example.sqlite_examples.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sqlite_examples.AddAndViewActivity;
import com.example.sqlite_examples.R;
import com.example.sqlite_examples.helper.Helper;
import com.example.sqlite_examples.model.Friend;
import com.example.sqlite_examples.service.FriendServiceSQLiteViaContentValues;

import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {
    private final Context mContext;
    private final List<Friend> mlist;

    public FriendAdapter(Context mContext, List<Friend> mlist) {
        this.mContext = mContext;
        this.mlist = mlist;
    }

    @NonNull
    @Override
    public FriendAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View contentView = LayoutInflater.from(mContext)
                .inflate(R.layout.item_list, parent, false);
        return new ViewHolder(contentView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendAdapter.ViewHolder holder, int position) {
        holder.tvName.setText(mlist.get(position).getName());
        holder.tvPhone.setText(mlist.get(position).getPhone());
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvName;
        TextView tvPhone;
        FriendAdapter adapter;

        public ViewHolder(@NonNull View itemView, FriendAdapter adapter) {
            super(itemView);
            this.adapter = adapter;
            tvName = itemView.findViewById(R.id.tv_name);
            tvPhone = itemView.findViewById(R.id.tv_phone);

            itemView.setOnClickListener(this::itemOnClicked);
            itemView.setOnLongClickListener(this::itemOnLongClicked);
        }

        @SuppressLint("NotifyDataSetChanged")
        private boolean itemOnLongClicked(View view) {
            new AlertDialog.Builder(adapter.mContext)
                    .setTitle("Action:")
                    .setItems(new CharSequence[]{"Edit", "Delete"}, (dialog, which) -> {
                        switch (which) {
                            case 0 : //edit
                                Intent intent = new Intent(adapter.mContext, AddAndViewActivity.class);
                                intent.putExtra(Helper.ACTIVITY_MODE, Helper.EDIT_MODE);
                                intent.putExtra(Helper.TAG_FRIEND, adapter.mlist.get(getLayoutPosition()));
                                adapter.mContext.startActivity(intent);
                                break;
                            case 1 :
                                new AlertDialog.Builder(adapter.mContext)
                                        .setTitle("Confirmation")
                                        .setMessage(String.format("hapus data: %s", adapter.mlist.get(getLayoutPosition()).getName()))
                                        .setNegativeButton("No", null)
                                        .setPositiveButton("Yes", (dialog1, which1) -> {
                                            try(FriendServiceSQLiteViaContentValues db = new FriendServiceSQLiteViaContentValues(adapter.mContext)){
                                                db.delete(adapter.mlist.get(getLayoutPosition()).getId());
                                                adapter.mlist.remove(getLayoutPosition());
                                                adapter.notifyDataSetChanged();
                                            }
                                        })
                                        .show();
                                break;
                        }
                    })
                    .show();

            return true;
        }

        private void itemOnClicked(View view) {
            Intent intent = new Intent(adapter.mContext, AddAndViewActivity.class);
            intent.putExtra(Helper.ACTIVITY_MODE, Helper.VIEW_MODE);
            intent.putExtra(Helper.TAG_FRIEND, adapter.mlist.get(getLayoutPosition()));
            adapter.mContext.startActivity(intent);
        }
    }
}
