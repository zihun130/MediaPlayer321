package atguigu.com.mediaplayer001.domain;

import java.io.Serializable;

/**
 * Created by sun on 2017/5/25.
 */

public class MediaItem implements Serializable{
    private String name;
    private long   duration;
    private String title;
    private String data;

    public MediaItem(String name, long duration, String title, String data) {
        this.name = name;
        this.duration = duration;
        this.title = title;
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "MediaItem{" +
                "name='" + name + '\'' +
                ", duration=" + duration +
                ", title='" + title + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
