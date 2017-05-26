package atguigu.com.mediaplayer321.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.ArrayList;

import atguigu.com.mediaplayer321.domain.ContentInfo;

/**
 * Created by sun on 2017/5/26.
 */

public class ScrollContentView extends TextView {
    private Paint paintGreen;
    private Paint paintWhite;
    private int width;
    private int height;
    private ArrayList<ContentInfo> contentInfo;
    private int index = 0;
    private float textHeight = 20;
    private int currentPosition;

    public ScrollContentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }


    private void initView() {
        paintGreen = new Paint();
        paintGreen.setColor(Color.GREEN);
        paintGreen.setTextSize(18);
        paintGreen.setAntiAlias(true);
        paintGreen.setTextAlign(Paint.Align.CENTER);


        paintWhite = new Paint();
        paintWhite.setColor(Color.WHITE);
        paintWhite.setTextSize(18);
        paintWhite.setAntiAlias(true);
        paintWhite.setTextAlign(Paint.Align.CENTER);


       /* contentInfo = new ArrayList<>();

        ContentInfo info = new ContentInfo();

        for (int i = 0; i < 1000; i++) {
            info.setContent("aaaaaaaaaaaa_" + i);
            info.setSleeptime(2000);
            info.setTimepoint(2000 * i);

            contentInfo.add(info);
            info = new ContentInfo();
        }*/

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (contentInfo != null && contentInfo.size() > 0) {

            String currcontent = contentInfo.get(index).getContent();
            canvas.drawText(currcontent, width / 2, height / 2, paintGreen);

            float tempY = height / 2;
            for (int i = index - 1; i >= 0; i--) {

                String precontent = contentInfo.get(i).getContent();

                tempY = tempY - textHeight;

                if (tempY < 0) {
                    break;
                }
                canvas.drawText(precontent, width / 2, tempY, paintWhite);

            }

            tempY = height / 2;
            for (int i = index + 1; i < contentInfo.size(); i++) {
                String nextcontent = contentInfo.get(i).getContent();

                tempY = tempY + textHeight;

                if (tempY > height) {
                    break;
                }
                canvas.drawText(nextcontent, width / 2, tempY, paintWhite);

            }
        } else {
            canvas.drawText("没有找到歌词", width / 2, height / 2, paintGreen);
        }
    }


    public void setNextContent(int currentPosition) {
        this.currentPosition = currentPosition;
        if (contentInfo == null && contentInfo.size() == 0) {
            return;
        }

        for (int i = 1; i < contentInfo.size(); i++) {
            if (currentPosition < contentInfo.get(i).getTimepoint()) {
                int tempIndex = i - 1;
                if (currentPosition >= contentInfo.get(tempIndex).getTimepoint()) {
                    index = tempIndex;
                }

            }

        }
        invalidate();
    }

    public void setLyrics(ArrayList<ContentInfo> lyrics) {
        this.contentInfo = lyrics;

    }

}
