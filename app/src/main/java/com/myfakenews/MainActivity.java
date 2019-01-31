package com.myfakenews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    EditText editText, editText2;
    Button button;
    ConstraintLayout clASD;
    private Retrofit retrofit;
    private static DefaultApi defaultApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editText);
        editText2 = findViewById(R.id.editText2);
        button = findViewById(R.id.button);
        clASD = findViewById(R.id.clASD);
        // status bar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorWhite2));
        }else{
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            View view = new View(this);
            view.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            view.getLayoutParams().height =  Utils.GetStatusBarHeight(MainActivity.this);
            ((ViewGroup) getWindow().getDecorView()).addView(view);
            view.setBackgroundColor(getResources().getColor(R.color.colorWhite2));
        }
        retrofit = new Retrofit.Builder()
                .baseUrl("https://samples.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        defaultApi = retrofit.create(DefaultApi.class);
        Utils.ShowKeyboard(editText);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utils.IsEmailValid(editText.getText().toString())){
                    checkPass(editText2.getText().toString());
               }else{
                   Toast.makeText(MainActivity.this,"Неверный формат почты!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void checkPass(String pass){
        if(pass.length()== 0){
            Toast.makeText(this,"Введите пароль",Toast.LENGTH_SHORT).show();
        }else if(pass.length() < 6){
            Toast.makeText(this,"Пароль должен состоять из 6 символов",Toast.LENGTH_SHORT).show();
        }else{
            if(Utils.CheckPass(pass)){
                Utils.CloseKeyboard(this);
                checkWeather();
            }else {
                Toast.makeText(this,"Пароль должен содержать 1 цифру, 1 заглавную и 1 строчную букву",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void checkWeather(){
        defaultApi.getData().enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {
                if(response.body() != null) {
                    Weather weather = response.body();
                    Snackbar.make(clASD, "Температура в Лондоне " + weather.getTemp() + " к.", Snackbar.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<Weather> call, Throwable t) {
                Snackbar.make(clASD, "Ошибка " + t.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });

    }

    public interface DefaultApi {
        @GET("weather?q=london&appid=08b93d738fe416c4fafa845345a85af4")
        Call<Weather> getData();
    }
}
