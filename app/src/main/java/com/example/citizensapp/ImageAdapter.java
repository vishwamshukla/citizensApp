package com.example.citizensapp;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

 public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
        private Context mContext;
        private List<Upload> mUploads;
        private HomeActivity mListener;

        public ImageAdapter(Context context,List<Upload> uploads){
            mContext = context;
            mUploads = uploads;
        }

        @NonNull
        @Override
        public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.image_item, parent, false);
            return new ImageViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
            Upload uploadCurrent = mUploads.get(position);
            holder.textViewPotholeType.setText(uploadCurrent.getmPotholeType());
            holder.textViewComment.setText(uploadCurrent.getmLandmark());
            Picasso.get()
                    .load(uploadCurrent.getImageUrl())
                    .fit()
                    .centerInside()
                    .into(holder.imageView);
        }

        @Override
        public int getItemCount() {
            return mUploads.size();
        }
        public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
                View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
            public TextView textViewPotholeType;
            public TextView textViewComment;
            public ImageView imageView;

            public ImageViewHolder(@NonNull View itemView) {
                super(itemView);

                textViewPotholeType = itemView.findViewById(R.id.text_view_pothole_type);
                textViewComment = itemView.findViewById(R.id.text_view_comment);
                imageView = itemView.findViewById(R.id.image_view_upload);

                itemView.setOnClickListener(this);
                itemView.setOnCreateContextMenuListener(this);
            }

            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (mListener != null){
                    int position = getAdapterPosition();
                    if (position!= RecyclerView.NO_POSITION){
                        mListener.onDeleteClick(position);
                        return true;
                    }
                }

                return false;
            }

            @Override
            public void onClick(View view) {
                if (mListener != null){
                    int position = getAdapterPosition();
                    if (position!= RecyclerView.NO_POSITION){
                        mListener.onItemClick(position);
                    }
                }

            }

            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                MenuItem delete = contextMenu.add(Menu.NONE,1,1,"Delete");

                delete.setOnMenuItemClickListener(this);
            }
        }

        public interface OnItemClickListener{
            void onItemClick(int position);
            void onDeleteClick(int position);
        }
        public void setOnItemClickListener(HomeActivity listener) {
            mListener = listener;
        }

    }

