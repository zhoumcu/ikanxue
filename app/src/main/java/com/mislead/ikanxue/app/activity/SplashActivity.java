package com.mislead.ikanxue.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.mislead.ikanxue.app.R;
import com.mislead.ikanxue.app.model.DailyEnglishObject;
import com.mislead.ikanxue.app.util.AndroidHelper;
import com.mislead.ikanxue.app.util.DailyEnglishUtil;
import com.mislead.ikanxue.app.view.AutoSizeTextView;
import com.mislead.ikanxue.app.volley.VolleyHelper;

/**
 * SplashActivity
 * AUTHOR:Zhaoyy  2015/6/27
 * DESC:
 **/
public class SplashActivity extends Activity {

  private static String TAG = "SplashActivity";
  private AutoSizeTextView tvDailyEn;
  private AutoSizeTextView tvDailyZh;
  private ImageView ivDaily;

  private boolean hasPost = false;
  private boolean autoFinish = true;

  private Runnable runnable = new Runnable() {
    @Override public void run() {
      if (hasPost) return;
      hasPost = true;
      startActivity(new Intent(SplashActivity.this, MainActivity.class));
      SplashActivity.this.finish();
    }
  };

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);

    tvDailyEn = (AutoSizeTextView) findViewById(R.id.tv_daily_en);
    tvDailyZh = (AutoSizeTextView) findViewById(R.id.tv_daily_zh);
    ivDaily = (ImageView) findViewById(R.id.iv_daily);

    autoFinish = getIntent().getBooleanExtra("auto", true);
    setDailyEnglish();
    if (autoFinish) {
      findViewById(R.id.rl_root).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          tvDailyEn.postDelayed(runnable, 1000);
        }
      });
      tvDailyEn.postDelayed(runnable, 4 * 1000);
    }
  }

  @Override public void onBackPressed() {
    if (autoFinish) {
      tvDailyEn.post(runnable);
    } else {
      finish();
    }
  }

  private void setDailyEnglish() {

    DailyEnglishObject dailyEnglishObject = new DailyEnglishUtil().GetDailyEnglish();

    if (dailyEnglishObject == null) {
      String content = getResources().getString(R.string.default_daily_en);
      tvDailyEn.setText(content);
      String note = getResources().getString(R.string.default_daily_zh);
      tvDailyZh.setText(note);
      ivDaily.setImageResource(R.mipmap.daily);
    } else {
      tvDailyZh.setText(dailyEnglishObject.getNote());
      tvDailyEn.setText(dailyEnglishObject.getContent());

      String key = VolleyHelper.getCacheKey(dailyEnglishObject.getPicture());
      Bitmap bitmap = AndroidHelper.getSplashImageCache().getBitmap(key);
      if (bitmap != null) {
        ivDaily.setImageBitmap(bitmap);
      } else {
        ivDaily.setImageResource(R.mipmap.daily);
      }
    }
  }
}
