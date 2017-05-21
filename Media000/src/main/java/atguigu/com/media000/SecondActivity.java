package atguigu.com.media000;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioGroup;

import java.util.ArrayList;

import atguigu.com.media000.mediapager.LocalAudioPager;
import atguigu.com.media000.mediapager.LocalVideoPager;
import atguigu.com.media000.mediapager.NetAudioPager;
import atguigu.com.media000.mediapager.NetVideoPager;

public class SecondActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    private RadioGroup rg_main;
    private ArrayList<BaseFragment> fragment;
    private int position;
    private Fragment tempFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        rg_main = (RadioGroup)findViewById(R.id.rg_main);

        initFragment();
        rg_main.setOnCheckedChangeListener(this);
        rg_main.check(R.id.rb_local_video);
    }

    private void initFragment() {
        fragment = new ArrayList<>();
        fragment.add(new LocalVideoPager());
        fragment.add(new LocalAudioPager());
        fragment.add(new NetAudioPager());
        fragment.add(new NetVideoPager());


    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case  R.id.rb_local_video:
                position=0;
                break;
            case  R.id.rb_local_audio:
                position=1;
                break;
            case  R.id.rb_net_audio:
                position=2;
                break;
            case  R.id.rb_net_video:
                position=3;
                break;

        }
        BaseFragment currfragment= fragment.get(position);
        addFragment(currfragment);
    }

    private void addFragment(BaseFragment currfragment) {
        if(tempFragment!=currfragment){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if(!currfragment.isAdded()){
                if(tempFragment!=null){
                    ft.hide(tempFragment);
                }
                ft.add(R.id.fl_content,currfragment);
            }else{
                if(tempFragment!=null){
                    ft.hide(tempFragment);
                }
                ft.show(currfragment);
            }

            ft.commit();
            tempFragment=currfragment;
        }
    }
}
