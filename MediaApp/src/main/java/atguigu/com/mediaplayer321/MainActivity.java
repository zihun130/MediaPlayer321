package atguigu.com.mediaplayer321;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioGroup;

import java.util.ArrayList;

import atguigu.com.mediaplayer321.Pager.LocalAudioPager;
import atguigu.com.mediaplayer321.Pager.LocalVideoPager;
import atguigu.com.mediaplayer321.Pager.NetAudioPager;
import atguigu.com.mediaplayer321.Pager.NetVideoPager;
import atguigu.com.mediaplayer321.fragment.BaseFragment;


public class MainActivity extends AppCompatActivity {
    private RadioGroup rg_main;
    private ArrayList<BaseFragment> fragments;
    private int position;
    private Fragment tempFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rg_main = (RadioGroup)findViewById(R.id.rg_main);
        initFragmnt();
        rg_main.setOnCheckedChangeListener(new MyOnCheckedChange());
        rg_main.check(R.id.rb_local_video);
    }

    private void initFragmnt() {
        fragments = new ArrayList<>();
        fragments.add(new LocalVideoPager());
        fragments.add(new LocalAudioPager());
        fragments.add(new NetAudioPager());
        fragments.add(new NetVideoPager());

    }

    private class MyOnCheckedChange implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rb_local_video :
                    position=0;
                    break;

                case R.id.rb_local_audio :
                    position=1;

                    break;

                case R.id.rb_net_audio:
                    position=2;

                    break;

                case R.id.rb_net_video :
                    position=3;

                    break;
            }
            BaseFragment currentFragment=fragments.get(position);
            addFragment(currentFragment);
        }
    }

    public void addFragment(BaseFragment currentFragment){
        if(tempFragment!=currentFragment){

           FragmentTransaction ft=getSupportFragmentManager().beginTransaction();

            if(!currentFragment.isAdded()){
                if(tempFragment!=null){
                    ft.hide(tempFragment);
                }
                ft.add(R.id.fl_content,currentFragment);
            }else{
                if(tempFragment!=null){
                    ft.hide(tempFragment);
                }
                ft.show(currentFragment);
            }
            ft.commit();
            tempFragment=currentFragment;

        }

    }

    /**
     * 解决安卓6.0以上版本不能读取外部存储权限的问题
     * @param activity
     * @return
     */
    public static boolean isGrantExternalRW(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity.checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            activity.requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1);

            return false;
        }

        return true;
    }
}
