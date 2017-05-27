package com.kd.grabandcut.menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.ads.AdRequest;
import com.kd.grabandcut.menu.level.BitmapProvider;
import com.kd.grabandcut.menu.level.GameHistory;
import com.kd.grabandcut.menu.level.TestGame;
import java.util.ArrayList;

public class LevelScreen extends Activity {
  public static int level;
  private GridView gridview;
  private ImageAdapter imageAdapter;
  public static Integer[] mThumbIds = {
      R.drawable.level_unlocked, R.drawable.level_unlocked, R.drawable.level_unlocked, R.drawable.level_unlocked,
      R.drawable.level_unlocked, R.drawable.level_unlocked, R.drawable.level_unlocked, R.drawable.level_unlocked,
      R.drawable.level_unlocked, R.drawable.level_unlocked, R.drawable.level_unlocked, R.drawable.level_unlocked,
      R.drawable.level_unlocked, R.drawable.level_unlocked, R.drawable.level_unlocked, R.drawable.level_unlocked,
      R.drawable.level_unlocked, R.drawable.level_unlocked
  };

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.levelscreen);

    MainScreen.setLevelThumbs();
    AdRequest ad = new AdRequest();
    gridview = (GridView) findViewById(R.id.levelGridview);
    imageAdapter = new ImageAdapter(this);
    gridview.setAdapter(imageAdapter);

    gridview.setOnItemClickListener(new OnItemClickListener() {
      public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

        ArrayList<GameHistory> levelStates = MainScreen.dbh.fetchAllRows();
        for (GameHistory g : levelStates) {
          if (g.levelId - 1 == position) {
            if (g.status != 1) {

              // mThumbIds[position] = R.drawable.level_locked;
              // write the code that would be shown when clicked
              // at any level locked

              Toast.makeText(LevelScreen.this, "Level Locked", Toast.LENGTH_SHORT).show();
              return;
            }
          }
        }
        level = position;

        Intent myIntent = new Intent(v.getContext(), TestGame.class);
        startActivityForResult(myIntent, level);

        // finish();
      }
    });
  }

  private MediaPlayer mediaPlayer;

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    MainScreen.setLevelThumbs();

    // imageAdapter.notifyDataSetChanged();
    gridview.invalidateViews();

    MainScreen.musicHandlerState = MainScreen.MUSIC_STATE_MENU;
    MainScreen.musicChanged = true;
    //		MainScreen.musicHandler.resume();
    //		MainScreen.synchronizeMusic.notifyAll();
    MainScreen.h.sendEmptyMessage(0);
    //		startMediaPlayer();
  }

  public void startMediaPlayer() {
    if (mediaPlayer != null) {
      if (mediaPlayer.isPlaying()) {
        mediaPlayer.stop();
        mediaPlayer.release();
      }
    }
    mediaPlayer = MediaPlayer.create(this, R.raw.menu);
    mediaPlayer.setLooping(true);
    try {
      mediaPlayer.start();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    public ImageAdapter(Context c) {
      mContext = c;
    }

    public int getCount() {
      return mThumbIds.length;
    }

    public Object getItem(int position) {
      return null;
    }

    public long getItemId(int position) {
      return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
      // Log.d("LevelScreen", "Came to getView: " + position);
      ImageView imageView;
      if (convertView == null) {

        // if it's not recycled, initialize some
        // attributes
        imageView = new ImageView(mContext);
        imageView.setLayoutParams(new GridView.LayoutParams(getResources().getDimensionPixelOffset(R.dimen.dp_80),
            getResources().getDimensionPixelOffset(R.dimen.dp_80)));
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setPadding(8, 8, 8, 8);
      } else {
        imageView = (ImageView) convertView;
      }
      // Log.d("LevelScreen", "Setting image resource: " + position);

      //			imageView.setImageDrawable(getResources().getDrawable(mThumbIds[position]));
      Bitmap b = BitmapProvider.getBitmap(mThumbIds[position]);
      if (b != null) {
        imageView.setImageBitmap(b);
      } else {
        return convertView;
      }

      return imageView;
    }
  }

  @Override public void onDestroy() {
    super.onDestroy();
  }
}