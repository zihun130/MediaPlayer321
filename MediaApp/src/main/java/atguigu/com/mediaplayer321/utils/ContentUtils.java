package atguigu.com.mediaplayer321.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import atguigu.com.mediaplayer321.domain.ContentInfo;

/**
 * Created by sun on 2017/5/26.
 */

public class ContentUtils {
    private ArrayList<ContentInfo> info;

    public boolean isLyric() {
        return isLyric;
    }

    public void setLyric(boolean lyric) {
        isLyric = lyric;
    }

    private boolean isLyric=false;


    public void readFile(File file){
        if(file==null || !file.exists()){
            //文件不存在
            isLyric=false;
        }else {
           info = new ArrayList<>();
            isLyric=true;

            FileInputStream fis=null;

            try {

                fis=new FileInputStream(file);
                InputStreamReader isr=new InputStreamReader(fis,"GBK");
                BufferedReader    buff=new BufferedReader(isr);
                String line;
                while ((line=buff.readLine())!=null){
                    analyzeLyric(line);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            Collections.sort(info, new Comparator<ContentInfo>() {
                @Override
                public int compare(ContentInfo o1, ContentInfo o2) {
                    if(o1.getTimepoint() > o2.getTimepoint()){
                        return -1;
                    }else if(o1.getTimepoint() < o2.getTimepoint()){
                        return 1;
                    }else {
                        return 0;
                    }

                }
            });
            //计算高亮时间
            for(int i = 0; i <info.size() ; i++) {
              ContentInfo oneLyirc=info.get(i);
                if(i+1 < info.size()){
                    ContentInfo twoLyirc=info.get(i+1);
                    //每行的高亮时间后一个时间戳减去当前时间戳
                    oneLyirc.setSleeptime(twoLyirc.getTimepoint()-oneLyirc.getTimepoint());
                }
            }
        }
    }



    private void analyzeLyric(String line) {
        int post1=line.indexOf("[");//0 子串在字符串中的第一个坐标
        int post2=line.indexOf("]");//9
        if(post1==0 && post2!=-1){//至少有一句歌词
            //装Longs的时间戳数组
            long[] tempLongs=new long[getContentTag(line)];
            String trimStr=line.substring(post1+1,post2);//截取02:04.12;
            //解析第0句
            tempLongs[0]=stringToLong(trimStr);
            if(tempLongs[0]==-1){
                return;
            }

            int i=1;

            String content=line;
            while (post1==0 && post2!=-1){
                content=content.substring(post2+1);//[03:37.32][00:59.73]我在这里欢笑-->[00:59.73]我在这里欢笑

                post1=content.indexOf("[");
                post2=content.indexOf("]");

                if(post1==0 && post2!=-1){
                    trimStr= line.substring(post1 + 1, post2);

                    //解析第 1 句
                    tempLongs[i]=stringToLong(trimStr);

                    if(tempLongs[i]==-1){
                        return;
                    }
                    i++;
                }

            }
            //匹配时间戳与歌词
            for(int j = 0; j <tempLongs.length; j++) {
              if(tempLongs[j]!=0){
                  ContentInfo Lyirc=new ContentInfo();
                  Lyirc.setTimepoint(tempLongs[i]);
                  Lyirc.setContent(content);

                  info.add(Lyirc);
              }
            }


        }
    }




    private long stringToLong(String trimStr) {
        long result=-1;

        try {
            String[] s1 = trimStr.split(":");//把字符串以 : 分割
            String[] s2 = s1[1].split("."); //将第一个数组的第二个子串再以 . 分割

            long min=Long.valueOf(s1[0]);
            long second=Long.valueOf(s2[0]);
            long mil=Long.valueOf(s2[1]);
            //把时间戳转换为毫秒  用于计算高亮时间
            result=min*60*1000 + second*1000 + mil*10;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return result;
    }

    private int getContentTag(String line) {
        int result=1;
        String[] s1=line.split("\\[");
        String[] s2=line.split("\\]");

        if(s1.length==0 && s2.length==0){
            result=1;
        }else if(s1.length > s2.length){
            result=s1.length;
        }else {
            result=s2.length;
        }

        return result;
    }
}
