package atguigu.com.media002;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioGroup;

import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    private RadioGroup rg_main;
    private ArrayList<BaseFragment> fragments;
    private int position;
    private Fragment tempfragment;

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
        }

        BaseFragment currFragment = fragments.get(position);
        addFragment(currFragment);
    }

    private void addFragment(BaseFragment currFragment) {
        if(tempfragment!=currFragment){

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            if(!currFragment.isAdded()){
                if(tempfragment!=null){
                    ft.hide(tempfragment);
                }
                ft.add(R.id.fl_content,currFragment);
            }else{
                if(tempfragment!=null){
                    ft.hide(tempfragment);
                }
               ft.show(currFragment);
            }
            ft.commit();
            tempfragment=currFragment;
        }
    }
}
