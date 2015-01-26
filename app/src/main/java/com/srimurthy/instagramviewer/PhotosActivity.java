package com.srimurthy.instagramviewer;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


public class PhotosActivity extends ActionBarActivity {

    private static final String CLIENT_ID = "bb47b2875b7f47a0881a71e7799365a0";
    private List<InstagramPhoto> photos;
    private InstagramPhotosAdapter aPhotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        fetchPopularPhotos();
    }

    private void fetchPopularPhotos() {
        photos = new ArrayList<>();
        aPhotos = new InstagramPhotosAdapter(this, photos);
        ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);
        lvPhotos.setAdapter(aPhotos);
        String popularURL = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(popularURL, new JsonHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                JSONArray photosJSON = null;

                try {
                    photosJSON = response.getJSONArray("data");
                    for (int i = 0; i < photosJSON.length(); i++) {
                        JSONObject photoJSON = photosJSON.getJSONObject(i);
                        InstagramPhoto eachPhoto = new InstagramPhoto();

                        try {
                            JSONObject userJSONObject = (JSONObject) photoJSON.opt("user");
                            if (userJSONObject != null) {
                                String username = userJSONObject.getString("username");
                                if (username != null) {
                                    eachPhoto.setUserName(username);
                                }
                                if (userJSONObject.isNull("profile_picture") == false) {
                                    String url = userJSONObject.getString("profile_picture");
                                    if (url != null && url.isEmpty() == false) {
                                        try {
                                            String encodedUrl = URLEncoder.encode(url, "UTF-8");
                                            eachPhoto.setUserProfilePictureURL(encodedUrl);
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try {
                            String creationDateTimeStr = photoJSON.get("created_time").toString();
                            if (creationDateTimeStr != null) {
                                String createdTimestamp = DateUtils.getRelativeTimeSpanString(new Long(creationDateTimeStr.toString()) * 1000).toString();
                                if (createdTimestamp != null) {
                                    eachPhoto.setCreatedTimeString(createdTimestamp);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try {
                            JSONObject captionJSONObject = photoJSON.getJSONObject("caption");
                            if (captionJSONObject != null) {
                                String caption = captionJSONObject.getString("text");
                                if (caption != null) {
                                    eachPhoto.setCaption(caption);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try {
                            JSONObject imagesJSONObject = photoJSON.getJSONObject("images");
                            if (imagesJSONObject != null) {
                                JSONObject imageJSONObject = imagesJSONObject.getJSONObject("standard_resolution");
                                if (imageJSONObject != null) {
                                    String url = imageJSONObject.getString("url");
                                    if (url != null) {
                                        eachPhoto.setImageURL(url);
                                    }
                                    Integer height = imageJSONObject.getInt("height");
                                    if (height != null) {
                                        eachPhoto.setImageHeight(height);
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {

                            JSONObject likesJSONObject = photoJSON.getJSONObject("likes");
                            if (likesJSONObject != null) {
                                Integer count = likesJSONObject.getInt("count");
                                if (count != null) {
                                    eachPhoto.setLikesCount(count);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try {

                            JSONObject commentsJSONObject = photoJSON.getJSONObject("comments");
                            if (commentsJSONObject != null) {
                                JSONArray commentsJSONArray = commentsJSONObject.getJSONArray("data");
                                if (commentsJSONArray.length() > 0) {
                                    eachPhoto.setFirstComment(((JSONObject)commentsJSONArray.get(0)).getString("text"));
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        photos.add(eachPhoto);
                    }
                    aPhotos.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
