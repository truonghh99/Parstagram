package com.example.parstagram.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.parstagram.Comment;
import com.example.parstagram.CommentsAdapter;
import com.example.parstagram.Like;
import com.example.parstagram.MainActivity;
import com.example.parstagram.Post;
import com.example.parstagram.R;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class DetailFragment extends Fragment implements CommentDialogFragment.CommentDialogListener{

    public static final String TAG = "DetailFragment";
    private ImageView ivProfile;
    private TextView tvUsernameTop;
    private ImageView ivImage;
    private TextView tvUsernameBottom;
    private TextView tvDescription;
    private TextView tvTime;
    private RecyclerView rvComments;
    private CommentsAdapter adapter;
    private List<Comment> allComments;
    private TextView tvLikes;
    private ImageView ivLike;
    private ImageView ivComment;
    private ImageView ivMessage;

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvUsernameTop = view.findViewById(R.id.tvUsernameTop);
        tvUsernameBottom = view.findViewById(R.id.tvUsernameBottom);
        ivImage = view.findViewById(R.id.ivImage);
        tvDescription = view.findViewById(R.id.tvDescription);
        tvTime = view.findViewById(R.id.tvTime);
        ivProfile = view.findViewById(R.id.ivProfile);
        rvComments = view.findViewById(R.id.rvComments);
        tvLikes = view.findViewById(R.id.tvLikes);
        ivLike = view.findViewById(R.id.ivLike);
        ivComment = view.findViewById(R.id.ivComment);
        ivMessage = view.findViewById(R.id.ivMessage);

        allComments = new ArrayList<>();

        adapter = new CommentsAdapter(getContext(), allComments);
        rvComments.setAdapter(adapter);
        rvComments.setLayoutManager(new LinearLayoutManager(getContext()));
        queryComments();

        tvUsernameTop.setText(getArguments().getString("tvUsername"));
        tvUsernameBottom.setText(getArguments().getString("tvUsername"));
        tvDescription.setText(getArguments().getString("tvDescription"));
        Glide.with(getContext()).load(getArguments().getString("ivImage")).into(ivImage);
        tvTime.setText(getArguments().getString("tvTime"));
        if (getArguments().getString("ivProfile") == null) {
            Glide.with(getContext())
                    .load(R.drawable.default_profile)
                    .circleCrop()
                    .into(ivProfile);
        } else {
            Glide.with(getContext())
                    .load(getArguments().getString("ivProfile"))
                    .circleCrop()
                    .into(ivProfile);
        }
        ivLike.setImageResource(R.drawable.ufi_heart);
        Post post = (Post) getArguments().getSerializable("post");
        try {
            int numQueryLikes = queryLikes(post);
            if (numQueryLikes == 1) {
                String text = "Liked by 1 person";
                tvLikes.setText(text);
            } else {
                String text = "Liked by " + numQueryLikes + " people";
                tvLikes.setText(text);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            if (liked(post)) {
                ivLike.setImageResource(R.drawable.ufi_heart_active);
                DrawableCompat.setTint(
                        DrawableCompat.wrap(ivLike.getDrawable()),
                        ContextCompat.getColor(getContext(), R.color.red)
                );
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Post post = (Post) getArguments().getSerializable("post");
                    if (liked(post)) {
                        ParseQuery<Like> query = ParseQuery.getQuery(Like.class);
                        query.include(Like.KEY_POST);
                        query.include(Like.KEY_USER);
                        query.whereEqualTo(Like.KEY_POST, post);
                        query.whereEqualTo(Like.KEY_USER, ParseUser.getCurrentUser());
                        query.getFirstInBackground(new GetCallback<Like>() {
                            @Override
                            public void done(Like object, ParseException e) {
                                Post post = (Post) getArguments().getSerializable("post");
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
                                    numQueryLikes = queryLikes(post);
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
                        like.setPost(post);
                        like.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                Post post = (Post) getArguments().getSerializable("post");
                                if (e != null) {
                                    Log.e(TAG, "Error liking post", e);
                                    return;
                                }
                                ivLike.setImageResource(R.drawable.ufi_heart_active);
                                DrawableCompat.setTint(
                                        DrawableCompat.wrap(ivLike.getDrawable()),
                                        ContextCompat.getColor(getContext(), R.color.red)
                                );
                                int numQueryLikes = 0;
                                try {
                                    numQueryLikes = queryLikes(post);
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
                Post post = (Post) getArguments().getSerializable("post");
                showCommentDialog(post);
            }
        });
    }

    private void queryComments() {
        // Specify which class to query
        ParseQuery<Comment> query = ParseQuery.getQuery(Comment.class);
        query.include(Comment.KEY_POST);
        query.include(Comment.KEY_USER);
        query.whereEqualTo(Comment.KEY_POST, (Post) getArguments().getSerializable("post"));
        query.addDescendingOrder(Comment.KEY_CREATED);
        query.findInBackground(new FindCallback<Comment>() {
            @Override
            public void done(List<Comment> comments, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting comments", e);
                    return;
                }
                for (Comment comment : comments) {
                    Log.i(TAG, "Comment: " + comment.getBody() + ", username: " + comment.getUser().getUsername());
                }
                allComments.addAll(comments);
                adapter.notifyDataSetChanged();
            }
        });
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

    private void showCommentDialog(Post post) {
        FragmentManager fm = getFragmentManager();
        CommentDialogFragment commentDialogFragment = CommentDialogFragment.newInstance(post);
        commentDialogFragment.show(fm, "fragment_comment_dialog");
    }

    @Override
    public void onFinishCommentDialog(Comment comment) {
        allComments.add(allComments.size() - 1, comment);
        adapter.notifyItemInserted(allComments.size() - 1);
        rvComments.smoothScrollToPosition(allComments.size() - 1);
    }
}