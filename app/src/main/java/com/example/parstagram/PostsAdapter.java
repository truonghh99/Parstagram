package com.example.parstagram;

import android.content.Context;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.parstagram.fragments.CommentDialogFragment;
import com.example.parstagram.fragments.DetailFragment;
import com.example.parstagram.fragments.OtherUserProfileFragment;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    public static final String TAG = "PostsAdapter";
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
        try {
            holder.bind(post);
        } catch (ParseException e) {
            e.printStackTrace();
        }
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
        private ImageView ivLike;
        private ImageView ivComment;
        private ImageView ivMessage;
        private TextView tvLikes;
        private TextView tvComments;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfile = itemView.findViewById(R.id.ivProfile);
            tvUsernameTop = itemView.findViewById(R.id.tvUsernameTop);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvUsernameBottom = itemView.findViewById(R.id.tvUsernameBottom);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvTime = itemView.findViewById(R.id.tvTime);
            ivLike = itemView.findViewById(R.id.ivLike);
            ivComment = itemView.findViewById(R.id.ivComment);
            ivMessage = itemView.findViewById(R.id.ivMessage);
            tvLikes = itemView.findViewById(R.id.tvLikes);
            tvComments = itemView.findViewById(R.id.tvComments);

            ivProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("PostsAdapter", "profile picture clicked");
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        // get post at the position
                        Post post = posts.get(position);
                        Fragment fragment;
                        fragment = new OtherUserProfileFragment();
                        // create bundle of post info to send to detail fragment
                        Bundle args = new Bundle();
                        args.putString("tvUsername", post.getUser().getUsername());
                        if (post.getUser().getParseFile("profilePicture") == null) {
                            args.putString("ivProfile", null);
                        } else {
                            args.putString("ivProfile", post.getUser().getParseFile("profilePicture").getUrl());
                        }
                        args.putSerializable("post", post);
                        try {
                            args.putBoolean("liked", liked(post));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        fragment.setArguments(args);
                        ((MainActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, fragment).commit();
                    }
                }
            });

            tvUsernameTop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("PostsAdapter", "username clicked");
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        // get post at the position
                        Post post = posts.get(position);
                        Fragment fragment;
                        fragment = new OtherUserProfileFragment();
                        // create bundle of post info to send to detail fragment
                        Bundle args = new Bundle();
                        args.putString("tvUsername", post.getUser().getUsername());
                        if (post.getUser().getParseFile("profilePicture") == null) {
                            args.putString("ivProfile", null);
                        } else {
                            args.putString("ivProfile", post.getUser().getParseFile("profilePicture").getUrl());
                        }
                        args.putSerializable("post", post);
                        try {
                            args.putBoolean("liked", liked(post));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        fragment.setArguments(args);
                        ((MainActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, fragment).commit();
                    }
                }
            });

            tvUsernameBottom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("PostsAdapter", "username clicked");
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        // get post at the position
                        Post post = posts.get(position);
                        Fragment fragment;
                        fragment = new OtherUserProfileFragment();
                        // create bundle of post info to send to detail fragment
                        Bundle args = new Bundle();
                        args.putString("tvUsername", post.getUser().getUsername());
                        if (post.getUser().getParseFile("profilePicture") == null) {
                            args.putString("ivProfile", null);
                        } else {
                            args.putString("ivProfile", post.getUser().getParseFile("profilePicture").getUrl());
                        }
                        args.putSerializable("post", post);
                        try {
                            args.putBoolean("liked", liked(post));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        fragment.setArguments(args);
                        ((MainActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, fragment).commit();
                    }
                }
            });

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
                        args.putString("tvTime", DateUtils.getRelativeTimeSpanString(post.getCreatedAt().getTime(), date.getTime(), 0).toString());
                        if (post.getUser().getParseFile("profilePicture") == null) {
                            args.putString("ivProfile", null);
                        } else {
                            args.putString("ivProfile", post.getUser().getParseFile("profilePicture").getUrl());
                        }
                        args.putSerializable("post", post);
                        try {
                            args.putBoolean("liked", liked(post));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        fragment.setArguments(args);
                        ((MainActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, fragment).commit();
                    }
                }
            });

            ivLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (liked(posts.get(getAdapterPosition()))) {
                            ParseQuery<Like> query = ParseQuery.getQuery(Like.class);
                            query.include(Like.KEY_POST);
                            query.include(Like.KEY_USER);
                            query.whereEqualTo(Like.KEY_POST, posts.get(getAdapterPosition()));
                            query.whereEqualTo(Like.KEY_USER, ParseUser.getCurrentUser());
                            query.getFirstInBackground(new GetCallback<Like>() {
                                @Override
                                public void done(Like object, ParseException e) {
                                    if (e != null) {
                                        Log.e(TAG, "Error retrieving like data", e);
                                        return;
                                    }
                                    try {
                                        object.delete();
                                    } catch (ParseException ex) {
                                        ex.printStackTrace();
                                    }
                                    object.saveInBackground();
                                    ivLike.setImageResource(R.drawable.ufi_heart);
                                    int numQueryLikes = 0;
                                    try {
                                        numQueryLikes = queryLikes(posts.get(getAdapterPosition()));
                                    } catch (ParseException ex) {
                                        ex.printStackTrace();
                                    }
                                    if (numQueryLikes == 1) {
                                        String text = "Liked by 1 person";
                                        tvLikes.setText(text);
                                    } else {
                                        String text = "Liked by " + numQueryLikes + " people";
                                        tvLikes.setText(text);
                                    }
                                }
                            });
                        } else {
                            Like like = new Like();
                            like.setUser(ParseUser.getCurrentUser());
                            like.setPost(posts.get(getAdapterPosition()));
                            like.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e != null) {
                                        Log.e(TAG, "Error liking post", e);
                                        return;
                                    }
                                    ivLike.setImageResource(R.drawable.ufi_heart_active);
                                    DrawableCompat.setTint(
                                            DrawableCompat.wrap(ivLike.getDrawable()),
                                            ContextCompat.getColor(context, R.color.red)
                                    );
                                    int numQueryLikes = 0;
                                    try {
                                        numQueryLikes = queryLikes(posts.get(getAdapterPosition()));
                                    } catch (ParseException ex) {
                                        ex.printStackTrace();
                                    }
                                    if (numQueryLikes == 1) {
                                        String text = "Liked by 1 person";
                                        tvLikes.setText(text);
                                    } else {
                                        String text = "Liked by " + numQueryLikes + " people";
                                        tvLikes.setText(text);
                                    }
                                }
                            });
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });

            ivComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Post post = posts.get(getAdapterPosition());
                    showCommentDialog(post);
                    int numQueryComments = 0;
                    try {
                        numQueryComments = queryComments(posts.get(getAdapterPosition())) + 1;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (numQueryComments == 0) {
                        String text = "There are no comments";
                        tvComments.setText(text);
                    } else if (numQueryComments == 1){
                        String text = "View 1 comment";
                        tvComments.setText(text);
                    } else {
                        String text = "View all " + numQueryComments + " comments";
                        tvComments.setText(text);
                    }
                }
            });
        }

        public void bind(Post post) throws ParseException {
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
            ParseFile profilePicture = post.getUser().getParseFile("profilePicture");
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
            ivLike.setImageResource(R.drawable.ufi_heart);
            int numQueryLikes = queryLikes(posts.get(getAdapterPosition()));
            if (numQueryLikes == 1) {
                String text = "Liked by 1 person";
                tvLikes.setText(text);
            } else {
                String text = "Liked by " + numQueryLikes + " people";
                tvLikes.setText(text);
            }
            if (liked(posts.get(getAdapterPosition()))) {
                ivLike.setImageResource(R.drawable.ufi_heart_active);
                DrawableCompat.setTint(
                        DrawableCompat.wrap(ivLike.getDrawable()),
                        ContextCompat.getColor(context, R.color.red)
                );
            }
            int numQueryComments = queryComments(posts.get(getAdapterPosition()));
            if (numQueryComments == 0) {
                String text = "There are no comments";
                tvComments.setText(text);
            } else if (numQueryComments == 1){
                String text = "View 1 comment";
                tvComments.setText(text);
            } else {
                String text = "View all " + numQueryComments + " comments";
                tvComments.setText(text);
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

    protected int queryLikes(Post post) throws ParseException {
        ParseQuery<Like> query = ParseQuery.getQuery(Like.class);
        query.include(Like.KEY_POST);
        query.whereEqualTo(Like.KEY_POST, post);
        List<Like> likes = query.find();
        return likes.size();
    }

    protected boolean liked(Post post) throws ParseException {
        ParseQuery<Like> query = ParseQuery.getQuery(Like.class);
        query.include(Like.KEY_POST);
        query.include(Like.KEY_USER);
        query.whereEqualTo(Like.KEY_POST, post);
        query.whereEqualTo(Like.KEY_USER, ParseUser.getCurrentUser());
        Log.i(TAG, "current post is " + post);
        Log.i(TAG, "current user is " + ParseUser.getCurrentUser());
        List<Like> likes = query.find();
        if (likes.size() == 0) {
            return false;
        }
        return true;
    }

    protected int queryComments(Post post) throws ParseException {
        ParseQuery<Comment> query = ParseQuery.getQuery(Comment.class);
        query.include(Like.KEY_POST);
        query.whereEqualTo(Like.KEY_POST, post);
        List<Comment> comments = query.find();
        return comments.size();
    }

    private void showCommentDialog(Post post) {
        FragmentManager fm = ((MainActivity)context).getSupportFragmentManager();
        CommentDialogFragment commentDialogFragment = CommentDialogFragment.newInstance(post);
        commentDialogFragment.show(fm, "fragment_comment_dialog");
    }
}
