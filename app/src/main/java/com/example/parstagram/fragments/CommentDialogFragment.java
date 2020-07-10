package com.example.parstagram.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.parstagram.Comment;
import com.example.parstagram.Post;
import com.example.parstagram.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class CommentDialogFragment extends DialogFragment implements TextView.OnEditorActionListener {

    public static final String TAG = "CommentDialogFragment";

    EditText etComment;
    Button btnComment;

    Comment comment;

    public CommentDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public interface CommentDialogListener {
        void onFinishCommentDialog(Comment comment);
    }

    public static CommentDialogFragment newInstance(Post post) {
        CommentDialogFragment frag = new CommentDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable("post", post);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().setGravity(Gravity.BOTTOM | Gravity.LEFT | Gravity.RIGHT);
        return inflater.inflate(R.layout.fragment_comment_dialog, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        etComment = (EditText) view.findViewById(R.id.etComment);
        btnComment = (Button) view.findViewById(R.id.btnComment);

        // Show soft keyboard automatically and request focus to field
        etComment.requestFocus();

        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String commentBody = etComment.getText().toString();
                Comment comment = new Comment();
                comment.setUser(ParseUser.getCurrentUser());
                comment.setPost((Post) getArguments().getSerializable("post"));
                comment.setBody(commentBody);
                comment.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            Log.e(TAG, "Error while saving comment", e);
                        }
                        Log.i(TAG, "Comment save was successful!");
                    }
                });
                dismiss();
            }
        });

        btnComment.setOnEditorActionListener(this);
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (EditorInfo.IME_ACTION_DONE == i) {
            // Return input text back to activity through the implemented listener
            CommentDialogListener listener = (CommentDialogListener) getActivity();
            listener.onFinishCommentDialog(comment);
            // Close the dialog and return back to the parent activity
            dismiss();
            return true;
        }
        return false;
    }
}
