package atguigu.com.mediaplayer321.Media;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import atguigu.com.mediaplayer321.Adapter.SearchNewsAdapter;
import atguigu.com.mediaplayer321.R;
import atguigu.com.mediaplayer321.domain.SearchBean;
import atguigu.com.mediaplayer321.utils.JsonParser;
import io.vov.vitamio.utils.Log;

public class SpeechVoiceActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etSousuo;
    private ImageView ivVoice;
    private TextView tvGo;
    private ListView lv;
    public static final String NET_SEARCH_URL = "http://hot.news.cntv.cn/index.php?controller=list&action=searchList&sort=date&n=20&wd=";
    private String url;
    private List<SearchBean.ItemsBean> datas;
    private SearchNewsAdapter adapter;
    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2017-05-27 11:27:01 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        etSousuo = (EditText) findViewById(R.id.et_sousuo);
        ivVoice = (ImageView) findViewById(R.id.iv_voice);
        tvGo = (TextView) findViewById(R.id.tv_go);
        lv = (ListView) findViewById(R.id.lv);

        //设置点击事件
        ivVoice.setOnClickListener(this);
        tvGo.setOnClickListener(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_voice);
        findViews();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_voice:
                //语言输入
                showVoiceDialog();
                break;
            case R.id.tv_go:
                toSearch();
                break;
        }
    }

    private void toSearch() {
        String trim = etSousuo.getText().toString().trim();
        if (!TextUtils.isEmpty(trim)) {
            url =NET_SEARCH_URL+trim;
            getDataFromNet(url);
        } else {
            Toast.makeText(SpeechVoiceActivity.this, "请输入您要搜索的内容", Toast.LENGTH_SHORT).show();
        }


    }

    private void getDataFromNet(String url) {
        final RequestParams request = new RequestParams(url);
        x.http().get(request, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("TAG", "请求成功-result==" + result);
                gettingData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
    private void gettingData(String json) {
        SearchBean searchBean = new Gson().fromJson(json, SearchBean.class);
        datas = searchBean.getItems();
        if (datas != null && datas.size() > 0) {
            adapter = new SearchNewsAdapter(this, datas);
            lv.setAdapter(adapter);
        }
    }

    private void showVoiceDialog() {
        //1.创建RecognizerDialog对象
        RecognizerDialog mDialog = new RecognizerDialog(this, new MyInitListener());
        //2.设置accent、 language等参数

        mDialog.setParameter(SpeechConstant.ACCENT, "mandarin");
        mDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        //默认设置普通话
        //若要将UI控件用于语义理解，必须添加以下参数设置，设置之后onResult回调返回将是语义理解
        //结果
        // mDialog.setParameter("asr_sch", "1");
        // mDialog.setParameter("nlp_version", "2.0");
        //3.设置回调接口
        mDialog.setListener(new MyRecognizerDialogListener());
        //4.显示dialog，接收语音输入
        mDialog.show();
    }

    class MyRecognizerDialogListener implements RecognizerDialogListener {

        @Override
        public void onResult(RecognizerResult recognizerResult, boolean b) {
            String resultString = recognizerResult.getResultString();
            Log.e("TAG","联网成功");
            printResult(recognizerResult);

        }

        @Override
        public void onError(SpeechError speechError) {

        }
    }

    class MyInitListener implements InitListener {

        @Override
        public void onInit(int i) {
            if (i == ErrorCode.SUCCESS) {
                Toast.makeText(SpeechVoiceActivity.this, "初始化成功", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }

        etSousuo.setText(resultBuffer.toString());
        etSousuo.setSelection(etSousuo.length());
    }
}
