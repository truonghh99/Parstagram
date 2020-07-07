package com.example.parstagram.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.parstagram.R;

public class DetailFragment extends Fragment {

    private ImageView ivProfile;
    private TextView tvUsernameTop;
    private ImageView ivImage;
    private TextView tvUsernameBottom;
    private TextView tvDescription;
    private TextView tvTime;


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
    }


}