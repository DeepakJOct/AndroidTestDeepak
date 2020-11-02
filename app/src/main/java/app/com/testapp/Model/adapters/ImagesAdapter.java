package app.com.testapp.Model.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import app.com.testapp.Model.models.WebImage;
import app.com.testapp.R;
import app.com.testapp.Room.models.MemberInfo;


public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.MyViewHolder> {

    private Context context;
    private List<WebImage> webImageList;

    public ImagesAdapter(Context context,
                         List<WebImage> webImageList) {
        this.context = context;
        this.webImageList = webImageList;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false));
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if(webImageList != null) {
            Log.d("CommentsListAdapter-->", webImageList.toArray() + "");
            final WebImage webImage = webImageList.get(position);
            Log.d("CommentOne-->", webImage.toString() + "::" + webImage.getTitle() + " ::" + webImage.getThumbnailUrl());
            holder.tvTitle.setText(webImage.getTitle() + "");
            Picasso.get().load(webImage.getUrl()).into(holder.ivImage);
            /*try {
                URL url = new URL(webImage.getThumbnailUrl());
                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... voids) {
                        try {
                            Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    holder.ivImage.setImageBitmap(image);
                                }
                            });
                        } catch (IOException e) {

                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
//                        holder.ivImage.setImageBitmap(image);
                    }
                }.execute();
                *//*Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                holder.ivImage.setImageBitmap(image);*//*
            } catch(IOException e) {
                System.out.println(e);
            }*/

            /*Glide.with(context)
                    .load(webImage.getUrl())
                    .override(150, 150)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(holder.ivImage);*/


        } else {
            Toast.makeText(context, "List is null", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public int getItemCount() {
        return webImageList.size();
    }

    public void updateList(List<WebImage> list) {
        this.webImageList = list;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        ImageView ivImage;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_title);
            ivImage = itemView.findViewById(R.id.iv_webImage);
        }
    }
}
