package atguigu.com.mediaplayer321.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * Created by sun on 2017/5/21.
 */

public class VideoView extends android.widget.VideoView {

    public VideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec,heightMeasureSpec);
    }

    public void setVideoSize(int width ,int height){
        ViewGroup.LayoutParams l=getLayoutParams();
        l.width=width;
        l.height=height;
        setLayoutParams(l);
    }
}
