package tn.LostAndFound.mini_projet_android_laf.utils;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ProgressBar;

import tn.LostAndFound.mini_projet_android_laf.R;

public class MediaPlayerActivity extends Activity implements
        MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnVideoSizeChangedListener, SurfaceHolder.Callback {
    private MediaPlayer mMediaPlayer;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private String mUrl;
    private String mName;

    private ProgressBar mProgressBar;
    private View mContainer;

    private int mVideoWidth;
    private int mVideoHeight;

    private boolean mIsVideoReadyToBePlayed = false;
    private boolean mIsVideoSizeKnown = false;
    private boolean mIsContainerSizeKnown = false;

    private boolean mIsPaused = false;
    private int mCurrentPosition = -1;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_media_player);

        mSurfaceView = (SurfaceView) findViewById(R.id.surface);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        Bundle extras = getIntent().getExtras();
        mUrl = extras.getString("url");
        mName = extras.getString("name");

        mProgressBar.setVisibility(View.VISIBLE);
        initToolbar();
    }

    private void initToolbar() {
        mContainer = findViewById(R.id.layout_media_player);
        setContainerLayoutListener(false);
    }

    private void setContainerLayoutListener(final boolean screenRotated) {
        mContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= 16) {
                    mContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    mContainer.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }

                mIsContainerSizeKnown = true;
                if (screenRotated) {
                    setVideoSize();
                } else {
                    tryToStartVideoPlayback();
                }
            }
        });
    }

    private void playVideo() {
        mProgressBar.setVisibility(View.VISIBLE);
        doCleanUp();
        try {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setDataSource(mUrl);
            mMediaPlayer.setDisplay(mSurfaceHolder);
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnBufferingUpdateListener(this);
            mMediaPlayer.setOnCompletionListener(this);
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setOnVideoSizeChangedListener(this);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onBufferingUpdate(MediaPlayer mp, int percent) {
    }

    public void onCompletion(MediaPlayer mp) {
        finish();
    }

    public void onPrepared(MediaPlayer mediaplayer) {
        mIsVideoReadyToBePlayed = true;
        tryToStartVideoPlayback();
    }

    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        if (width == 0 || height == 0) {
            return;
        }

        mVideoWidth = width;
        mVideoHeight = height;
        mIsVideoSizeKnown = true;
        tryToStartVideoPlayback();
    }

    public void surfaceChanged(SurfaceHolder surfaceholder, int i, int j, int k) {
    }

    public void surfaceDestroyed(SurfaceHolder surfaceholder) {
    }

    public void surfaceCreated(SurfaceHolder holder) {
        playVideo();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            mCurrentPosition = mMediaPlayer.getCurrentPosition();
            mIsPaused = true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMediaPlayer();
        doCleanUp();
    }

    private void releaseMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private void doCleanUp() {
        mVideoWidth = 0;
        mVideoHeight = 0;

        mIsVideoReadyToBePlayed = false;
        mIsVideoSizeKnown = false;
    }

    private void tryToStartVideoPlayback() {
        if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown && mIsContainerSizeKnown) {
            startVideoPlayback();
        }
    }

    private void startVideoPlayback() {
        mProgressBar.setVisibility(View.INVISIBLE);
        if (!mMediaPlayer.isPlaying()) {
            mSurfaceHolder.setFixedSize(mVideoWidth, mVideoHeight);
            setVideoSize();

            if (mIsPaused) {
                mMediaPlayer.seekTo(mCurrentPosition);
                mIsPaused = false;
            }
            mMediaPlayer.start();
        }
    }

    private void setVideoSize() {
        try {
            int videoWidth = mMediaPlayer.getVideoWidth();
            int videoHeight = mMediaPlayer.getVideoHeight();
            float videoProportion = (float) videoWidth / (float) videoHeight;

            int videoWidthInContainer = mContainer.getWidth();
            int videoHeightInContainer = mContainer.getHeight();
            float videoInContainerProportion = (float) videoWidthInContainer / (float) videoHeightInContainer;

            android.view.ViewGroup.LayoutParams lp = mSurfaceView.getLayoutParams();
            if (videoProportion > videoInContainerProportion) {
                lp.width = videoWidthInContainer;
                lp.height = (int) ((float) videoWidthInContainer / videoProportion);
            } else {
                lp.width = (int) (videoProportion * (float) videoHeightInContainer);
                lp.height = videoHeightInContainer;
            }
            mSurfaceView.setLayoutParams(lp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setContainerLayoutListener(true);
    }
}