package atguigu.com.mediarecyclerview;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioGroup;

import java.util.ArrayList;

import atguigu.com.mediarecyclerview.Fragment.BaseFragment;
import atguigu.com.mediarecyclerview.Pager.LocalAudiaPager;
import atguigu.com.mediarecyclerview.Pager.LocalVideoPager;
import atguigu.com.mediarecyclerview.Pager.NetAudiaPager;
import atguigu.com.mediarecyclerview.Pager.NetVideoPager;
import atguigu.com.mediarecyclerview.Pager.RecyclerViewPager;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    private RadioGroup rg_main;
    private ArrayList<BaseFragment> fragments;
    private int position;
    private Fragment tempFragment;

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
}
