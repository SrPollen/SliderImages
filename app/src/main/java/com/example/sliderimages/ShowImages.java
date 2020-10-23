package com.example.sliderimages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ShowImages extends AppCompatActivity {

    private ImageView show1;
    private ImageView show2;
    private ImageView show3;
    private ArrayList<String> arrayImages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_images);
        Button showbtn = findViewById(R.id.show);
        show1 = findViewById(R.id.showimage1);
        show2 = findViewById(R.id.showimage2);
        show3 = findViewById(R.id.showimage3);

        Intent getData = getIntent();
        arrayImages = getData.getStringArrayListExtra("arrayImages");

        showbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImages();
            }
        });
    }

    private void showImages(){
        for(int i = 0; i<arrayImages.size();i++){
           loadImage(arrayImages.get(i), i);
        }
    }

    private void loadImage(String imgUrl, int indexArray){
        switch (indexArray){
            case 0:
                Picasso.get().load(imgUrl).into(show1);
                break;
            case 1:
                Picasso.get().load(imgUrl).into(show2);
                break;
            case 2:
                Picasso.get().load(imgUrl).into(show3);
                break;
        }
    }
}