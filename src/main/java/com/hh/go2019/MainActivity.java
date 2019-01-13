package com.hh.go2019;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.hh.go2019.audio.MusicPlayerActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.audio_button})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.audio_button:
                startActivity(new Intent(this, MusicPlayerActivity.class));
                break;
            default:
                break;
        }
    }

}
