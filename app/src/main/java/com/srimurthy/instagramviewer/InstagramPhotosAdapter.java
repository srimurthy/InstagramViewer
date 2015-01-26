package com.srimurthy.instagramviewer;

import android.content.Context;
import android.graphics.Movie;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by srmurthy on 1/23/15.
 */
public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto> {

    public InstagramPhotosAdapter(Context context, List<InstagramPhoto> photos) {
        super(context, R.layout.item_photo, photos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        InstagramPhoto photo = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
        }

        TextView tvUsername = (TextView) convertView.findViewById(R.id.tvUsername);
        tvUsername.setText(photo.getUserName());

        TextView tvCreationTimestamp = (TextView) convertView.findViewById(R.id.tvCreationTimestamp);
        tvCreationTimestamp.setText(photo.getCreatedTimeString());

        ImageView ivUser = (ImageView) convertView.findViewById(R.id.ivUser);
        ivUser.getLayoutParams().height = 50;
        ivUser.setImageResource(0);
        Picasso.with(getContext()).load(photo.getUserProfilePictureURL()).into(ivUser);

        ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
        ivPhoto.getLayoutParams().height = photo.getImageHeight();
        ivPhoto.setImageResource(0);
        Picasso.with(getContext()).load(photo.getImageURL()).fit().into(ivPhoto);

        //TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
        //tvCaption.setText(photo.getCaption());

        TextView tvLikesCount = (TextView) convertView.findViewById(R.id.tvLikesCount);
        tvLikesCount.setText(photo.getLikesCount() + " Likes");

        TextView tvFirstComment = (TextView) convertView.findViewById(R.id.tvFirstComment);
        tvFirstComment.setText(photo.getFirstComment());

        return convertView;
    }
}
