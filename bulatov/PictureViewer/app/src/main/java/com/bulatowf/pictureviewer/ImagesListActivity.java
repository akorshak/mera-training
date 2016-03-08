package com.bulatowf.pictureviewer;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class ImagesListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    public static final String LOG_TAG = "ImagesListActivity";

    public static final String IMAGES_PATHS_KEY = "path_list";
    public static final String IMAGE_CURR_POSITION_KEY = "curr_pos";

    private ArrayList<String> mImagesPathList = new ArrayList<>();

    @TargetApi(23)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                requestPermissions(new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE }, 1);
            else
                init();
        } else {
            init();
        }

    }

    public void init() {
        ListView listView = (ListView) findViewById(R.id.images_grid_view);

        String externalStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath();

        //  default camera folder
        //  String imagesFolderPath = externalStoragePath + "/DCIM/Camera/";

        //FIXME: hardcoded path
        String imagesFolderPath = externalStoragePath + "/Pictures/MaterialWalls2/";

        initImagesPathsList(imagesFolderPath);

        ListAdapter imageAdapter = new ImageAdapter(this, mImagesPathList);

        listView.setAdapter(imageAdapter);
        listView.setOnItemClickListener(this);
    }

    public void initImagesPathsList(String folderPath) {

        File folder = new File(folderPath);
        File[] files = folder.listFiles();

        for (File image : files) {
            mImagesPathList.add(image.getAbsolutePath());
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, ImagesPagerActivity.class);

        // TODO: need better approach (to not pass string list each time)
        intent.putStringArrayListExtra(IMAGES_PATHS_KEY, mImagesPathList);
        intent.putExtra(IMAGE_CURR_POSITION_KEY, position);

        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            init();
        }  else {
            Toast.makeText(ImagesListActivity.this, "Access denied", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    public static class ImageAdapter extends BaseAdapter {

        private Context mContext;
        private ArrayList<String> mImagesPathsList;
        private ImageLoader mImageLoader;

        public ImageAdapter(Context context, ArrayList<String> imagesPaths) {
            super();
            this.mContext = context;
            this.mImageLoader = new ImageLoader(context);
            this.mImagesPathsList = imagesPaths;
        }

        @Override
        public int getCount() {
            return mImagesPathsList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(mContext);
                imageView.setPadding(8, 8, 8, 8);
            } else {
                imageView = (ImageView) convertView;
            }

            mImageLoader.loadImage(mImagesPathsList.get(position), imageView);
            return imageView;
        }


    }




}
