package atguigu.com.mediaplayer001;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioGroup;

import java.util.ArrayList;

import atguigu.com.mediaplayer001.Pager.LocalAudioPager;
import atguigu.com.mediaplayer001.Pager.LocalVideoPager;
import atguigu.com.mediaplayer001.Pager.NetAudioPager;
import atguigu.com.mediaplayer001.Pager.NetVideoPager;

public class MediaPlayerView extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    private RadioGroup rg_main;
    private ArrayList<BaseFragment> fragments;
    private int position;
    private Fragment tempfragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player_view);
        rg_main = (RadioGroup)findViewById(R.id.rg_main);
        initfragment();
        rg_main.setOnCheckedChangeListener(this);
        rg_main.check(R.id.rb_local_video);
    }

    private void initfragment() {
        fragments = new ArrayList<>();
        fragments.add(new LocalVideoPager());
        fragments.add(new LocalAudioPager());
        fragments.add(new NetAudioPager());
        fragments.add(new NetVideoPager());
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
        };

        BaseFragment currFeamnet = fragments.get(position);
        addFragment(currFeamnet);

    }

    private void addFragment(BaseFragment currFeamnet) {
        if(tempfragment!=currFeamnet){

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            if(!currFeamnet.isAdded()){
                if(tempfragment!=null){
                    ft.hide(tempfragment);
                }
                ft.add(R.id.fl_content,currFeamnet);
            }else {
                if(tempfragment!=null){
                    ft.hide(tempfragment);
                }
                ft.show(currFeamnet);
            }

            ft.commit();
            tempfragment=currFeamnet;

        }
    }
}
