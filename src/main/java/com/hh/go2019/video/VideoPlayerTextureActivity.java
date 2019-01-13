package com.hh.go2019.video;

import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hh.go2019.R;
import com.hh.go2019.audio.helper.FileUtils;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VideoPlayerTextureActivity extends AppCompatActivity {

    private static final String TAG = "MusicPlayerActivity";

    @BindView(R.id.player_imageView)
    ImageView mPlayerImageView;
    @BindView(R.id.music_title_textView)
    TextView mMusicNameTextView;
    @BindView(R.id.all_time_textView)
    TextView mTotalTimeTextView;
    @BindView(R.id.current_time_textView)
    TextView mCurrentTimeTextView;
    @BindView(R.id.progress_seekBar)
    SeekBar mProgressSeekBar;
    @BindView(R.id.video_textureView)
    TextureView mTextureView;

    private Surface mSurface;

    private String mMusicFilePath;
    private MediaPlayer mMediaPlayer;
    private boolean mIsPlaying;

    private static final int MESSAGE_UPDATE_TIME = 1;
    private static final int UPDATE_PROGRESS_DELAY = 1000;

    private Handler mMusicHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MESSAGE_UPDATE_TIME) {
                int duration = mMediaPlayer.getDuration();
                int position = mMediaPlayer.getCurrentPosition();
                mCurrentTimeTextView.setText(FileUtils.formatFileTime(position));
                int progress = position * 100 / duration;
                mProgressSeekBar.setProgress(progress);
                mMusicHandler.sendEmptyMessageDelayed(MESSAGE_UPDATE_TIME, UPDATE_PROGRESS_DELAY);
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_texture_player);
        ButterKnife.bind(this);
        initValue();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        release();
    }

    private void initValue() {
        mMediaPlayer = new MediaPlayer();
        Uri uri = getIntent().getData();
        if (uri != null) {
            mMusicFilePath = uri.getPath();
            mMusicNameTextView.setText(FileUtils.getFileName(uri.getPath()));
            start();
        }

        mMediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mediaPlayer) {
                mMusicHandler.sendEmptyMessage(MESSAGE_UPDATE_TIME);
            }
        });

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                start();
            }
        });

        mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                Toast.makeText(VideoPlayerTextureActivity.this,
                        " Something error", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        mProgressSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                // do nothing
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // do nothing
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int duration = mMediaPlayer.getDuration();
                int now = (int) (duration * (seekBar.getProgress() / 100.0));
                mMediaPlayer.seekTo(now);
                mMusicHandler.removeMessages(MESSAGE_UPDATE_TIME);
                play();
            }
        });

        mTextureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
                mSurface = new Surface(surfaceTexture);
                start();
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

            }
        });
    }

    @OnClick({R.id.back_linearLayout, R.id.player_linearLayout})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_linearLayout:
                finish();
                break;
            case R.id.player_linearLayout:
                if (mIsPlaying) {
                    pause();
                } else {
                    play();
                }
                break;
            default:
                break;
        }
    }

    private void play() {
        mIsPlaying = true;
        mPlayerImageView.setImageResource(R.drawable.icon_stop);
        mMediaPlayer.start();
        mMusicHandler.sendEmptyMessage(MESSAGE_UPDATE_TIME);
    }

    private void release() {
        mMusicHandler.removeCallbacksAndMessages(null);
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private void pause() {
        mMediaPlayer.pause();
        mIsPlaying = false;
        mPlayerImageView.setImageResource(R.drawable.icon_play);
        mMusicHandler.removeMessages(MESSAGE_UPDATE_TIME);
    }

    private void start() {
        mIsPlaying = true;
        mPlayerImageView.setImageResource(R.drawable.icon_stop);
        if (mMusicFilePath != null && mSurface != null) {
            try {
                mMediaPlayer.reset();
                mMediaPlayer.setDataSource(mMusicFilePath);
                mMediaPlayer.setSurface(mSurface);
                mMediaPlayer.prepareAsync();
                mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mMediaPlayer.start();
                        int allTime = mMediaPlayer.getDuration();
                        mTotalTimeTextView.setText(FileUtils.formatFileTime(allTime));
                        mMusicHandler.sendEmptyMessage(MESSAGE_UPDATE_TIME);
                    }
                });
            } catch (IOException e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
    }


}