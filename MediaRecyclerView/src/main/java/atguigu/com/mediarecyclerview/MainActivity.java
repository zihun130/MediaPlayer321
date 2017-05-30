package atguigu.com.mediarecyclerview;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;

import atguigu.com.mediarecyclerview.Fragment.BaseFragment;
import atguigu.com.mediarecyclerview.Pager.LocalAudiaPager;
import atguigu.com.mediarecyclerview.Pager.LocalVideoPager;
import atguigu.com.mediarecyclerview.Pager.NetAudiaPager;
import atguigu.com.mediarecyclerview.Pager.NetVideoPager;
import atguigu.com.mediarecyclerview.Pager.RecyclerViewPager;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    private RadioGroup rg_main;
    private ArrayList<BaseFragment> fragments;
    private int position;
    private Fragment tempFragment;

    SensorManager sensorManager;
    JCVideoPlayer.JCAutoFullscreenListener sensorEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rg_main = (RadioGroup)findViewById(R.id.rg_main);
        initFragment();
        rg_main.setOnCheckedChangeListener(this);
        rg_main.check(R.id.rb_local_video);
    }

    private void initFragment() {
        fragments=new ArrayList<>();

        fragments.add(new LocalVideoPager());
        fragments.add(new LocalAudiaPager());
        fragments.add(new NetAudiaPager());
        fragments.add(new NetVideoPager());
        fragments.add(new RecyclerViewPager());

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorEventListener = new JCVideoPlayer.JCAutoFullscreenListener();

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_local_video :
                position=0;
                break;
            case R.id.rb_local_audio :
                position=1;
                break;
            case R.id.rb_net_audio :
                position=2;
                break;
            case R.id.rb_net_video :
                position=3;
                break;
            case R.id.rb_recyclerview:
                position=4;
                break;
        }
        BaseFragment currFragment = fragments.get(position);
        addFragment(currFragment);
    }

    private void addFragment(BaseFragment currFragment) {
        if(tempFragment!=currFragment){

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            if(!currFragment.isAdded()){
                if(tempFragment!=null){
                    ft.hide(tempFragment);
                }
                ft.add(R.id.fl_content,currFragment);
            }else{
                if(tempFragment!=null){
                    ft.hide(tempFragment);
                }
                ft.show(currFragment);
            }

            ft.commit();
            tempFragment=currFragment;
        }
    }


    private boolean isExit=false;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if(position!=0){
                rg_main.check(R.id.rb_local_video);
                return true;
            }else if(!isExit){
                Toast.makeText(MainActivity.this, "再按一次退出应用", Toast.LENGTH_SHORT).show();
                isExit=true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isExit=false;
                    }
                }, 2000);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(sensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(sensorEventListener);
        JCVideoPlayer.releaseAllVideos();
    }


    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

}
