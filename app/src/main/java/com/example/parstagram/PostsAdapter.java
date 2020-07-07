package com.example.parstagram;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.parstagram.fragments.DetailFragment;
import com.parse.ParseFile;

import java.util.Date;
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private Context context;
    private List<Post> posts;

    public PostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivProfile;
        private TextView tvUsernameTop;
        private ImageView ivImage;
        private TextView tvUsernameBottom;
        private TextView tvDescription;
        private TextView tvTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfile = itemView.findViewById(R.id.ivProfile);
            tvUsernameTop = itemView.findViewById(R.id.tvUsernameTop);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvUsernameBottom = itemView.findViewById(R.id.tvUsernameBottom);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvTime = itemView.findViewById(R.id.tvTime);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("PostsAdapter", "post clicked");
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        // get post at the position
                        Post post = posts.get(position);
                        Fragment fragment;
                        fragment = new DetailFragment();
                        // create bundle of post info to send to detail fragment
                        Bundle args = new Bundle();
                        args.putString("tvDescription", post.getDescription());
                        args.putString("ivImage", post.getImage().getUrl());
                        args.putString("tvUsername", post.getUser().getUsername());
                        // convert time to relative time ago
                        Date date = new Date();
                        String relativeTime = (DateUtils.getRelativeTimeSpanString(post.getCreatedAt().getTime(), date.getTime(), 0)).toString();
                        args.putString("tvTime", relativeTime);
                        if (post.getProfilePicture() == null) {
                            args.putString("ivProfile", null);
                        } else {
                            args.putString("ivProfile", post.getProfilePicture().getUrl());
                        }
                        fragment.setArguments(args);
                        ((MainActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, fragment).commit();
                    }
                }
            });
        }

        public void bind(Post post) {
            // bind post data to the view elements
            tvDescription.setText(post.getDescription());
            tvUsernameTop.setText(post.getUser().getUsername());
            tvUsernameBottom.setText(post.getUser().getUsername());
            ParseFile image = post.getImage();
            if (image != null) {
                Glide.with(context).load(image.getUrl()).into(ivImage);
            }
            Date date = new Date();
            tvTime.setText(DateUtils.getRelativeTimeSpanString(post.getCreatedAt().getTime(), date.getTime(), 0));
            ParseFile profilePicture = post.getProfilePicture();
            if (profilePicture != null) {
                Glide.with(context)
                        .load(profilePicture.getUrl())
                        .fitCenter()
                        .circleCrop()
                        .into(ivProfile);
            } else {
                Glide.with(context)
                        .load(R.drawable.default_profile)
                        .fitCenter()
                        .circleCrop()
                        .into(ivProfile);
            }
        }
    }

    // Clean all elements of the recycler
    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }

}
