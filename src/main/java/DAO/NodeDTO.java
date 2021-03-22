package DAO;

import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

public class NodeDTO {
    private long id;
    private double lat;
    private double lon;
    private String user_name;
    private long uid;
    private int version;
    private long changeset;
    private Date timestomp;
    private List<TagDTO> tags;


    public NodeDTO(long id, double lat, double lon, String user_name, long uid, int version, long changeset, Date timestomp) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.user_name = user_name;
        this.uid = uid;
        this.version = version;
        this.changeset = changeset;
        this.timestomp = timestomp;
        tags = new ArrayList<>();
    }

    public NodeDTO(long id, double lat, double lon, String user_name, long uid, int version, long changeset, Date timestomp, List<TagDTO> tags) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.user_name = user_name;
        this.uid = uid;
        this.version = version;
        this.changeset = changeset;
        this.timestomp = timestomp;
        this.tags = tags;
    }

    public long getId() {
        return id;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getUser_name() {
        return user_name;
    }

    public long getUid() {
        return uid;
    }


    public int getVersion() {
        return version;
    }

    public long getChangeset() {
        return changeset;
    }

    public Date getTimestomp() {
        return timestomp;
    }

    public List<TagDTO> getTags() {
        return tags;
    }

    public void setTags(List<TagDTO> tags) {
        this.tags = tags;
    }
}
