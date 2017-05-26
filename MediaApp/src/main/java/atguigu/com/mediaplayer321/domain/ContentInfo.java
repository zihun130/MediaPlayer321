package atguigu.com.mediaplayer321.domain;

/**
 * Created by sun on 2017/5/26.
 */

public class ContentInfo {

    private String content;
    private long   timepoint;
    private long   sleeptime;

    public ContentInfo(String content, long timepoint, long sleeptime) {
        this.content = content;
        this.timepoint = timepoint;
        this.sleeptime = sleeptime;
    }

    public ContentInfo() {

    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTimepoint() {
        return timepoint;
    }

    public void setTimepoint(long timepoint) {
        this.timepoint = timepoint;
    }

    public long getSleeptime() {
        return sleeptime;
    }

    public void setSleeptime(long sleeptime) {
        this.sleeptime = sleeptime;
    }

    @Override
    public String toString() {
        return "ContentInfo{" +
                "content='" + content + '\'' +
                ", timepoint=" + timepoint +
                ", sleeptime=" + sleeptime +
                '}';
    }
}
