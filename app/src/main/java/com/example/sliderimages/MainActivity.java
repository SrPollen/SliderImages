package com.example.sliderimages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    //URL
    private static final String IMG_TITAN= "https://www.playerone.vg/wp-content/uploads/2020/06/Attack-on-Titan-Shingeki-no-Kyojin-130-cap%C3%ADtulo-manga.jpg";
    private static final String IMG_NARUTO = "https://sm.ign.com/t/ign_latam/screenshot/default/naruto4_st6s.1280.jpg";
    private static final String IMG_ONE_PIECE = "https://assets.tonica.la/__export/1592683828506/sites/debate/img/2020/06/20/one-piece-regresa-del-hiatus-tras-pandemia.jpg_673822677.jpg";

    private static final String VIDEO_TITAN= "https://www.youtube.com/watch?v=AgBUP8TJqV8&ab_channel=Kimmeh";
    private static final String VIDEO_NARUTO = "https://www.youtube.com/watch?v=o-hzTmeCqN0&ab_channel=meena%E2%99%A1";
    private static final String VIDEO_ONE_PIECE = "https://www.youtube.com/watch?v=BpHF8zCN6Mg&ab_channel=Nerd%2CUai%21";

    //URL default
    private String imageUrl = IMG_TITAN;
    private String videoUrl = VIDEO_TITAN;

    //Timer
    private TimerTask timerTask;
    private Timer timer;
    private int currentTime = 0;
    int count = 0;

    private ImageView imageview;
    private Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageview = findViewById(R.id.image);
        Button download = findViewById(R.id.download);
        Button permission = findViewById(R.id.permission);
        save = findViewById(R.id.save);
        save.setEnabled(false);

        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW , Uri.parse(videoUrl));
                startActivity(browserIntent);
            }
        });

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadImage(imageUrl);
                chrono();
                save.setEnabled(true);
            }
        });

        permission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aceptarPermisos();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveImage(imageUrl);
            }
        });
    }

    private void loadImage(String imgUrl){
        Picasso.get().load(imgUrl).into(imageview);
    }
    private void chrono(){

        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        currentTime++;
                        if(currentTime % 5 == 0){
                            count++;
                            switch(count){
                                case 1:
                                    imageUrl = IMG_TITAN;
                                    videoUrl = VIDEO_TITAN;
                                    break;
                                case 2:
                                    imageUrl = IMG_NARUTO;
                                    videoUrl = VIDEO_NARUTO;
                                    break;
                                case 3:
                                    imageUrl = IMG_ONE_PIECE;
                                    videoUrl = VIDEO_ONE_PIECE;
                                    count = 0;
                                    break;
                                default:
                                    count = 0;
                            }
                            loadImage(imageUrl);
                        }
                    }
                });
            }
        };

        timer = new Timer();
        timer.schedule(timerTask,1,1000);
    }

    private void aceptarPermisos(){
        if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else if(PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.INTERNET)){
            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.INTERNET}, 2);
        } else {
            Toast.makeText(getApplicationContext(), "Los permisos ya han sido aceptados", Toast.LENGTH_LONG).show();
        }
    }



    private void saveImage(String image){
        Picasso.get().load(image).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                try{
                    File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString());
                    if (!directory.exists()){
                        directory.mkdirs();
                    }
                    //String date = new Date().toString();
                    //Con la fecha no me funciona asi que lo hago con un random
                    int rand = (int)(Math.random() * 999999) + 1;
                    FileOutputStream fileOutputStream = new FileOutputStream(new File(directory, String.valueOf(rand).concat(".jpg")));
                    //new Date().toString().concat(String.valueOf(currentTime)).concat(".jpg")
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    Toast.makeText(getApplicationContext(), "Imagen guardada", Toast.LENGTH_LONG).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
    }

}