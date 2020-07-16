package cn.hctech2006.livesystem1.bean;

import java.io.Serializable;

public class Label implements Serializable {

    private Integer id;

    private String carId;

    private String videoId;

    private Integer outLine;

    private Integer pedestrian;

    private double speed;

    private Integer changeLine;

    private String carUid;

    private static final long serialVersionUID = 1L;

    public String getCarUid() {
        return carUid;
    }

    public void setCarUid(String carUid) {
        this.carUid = carUid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public Integer getOutLine() {
        return outLine;
    }

    public void setOutLine(Integer outLine) {
        this.outLine = outLine;
    }

    public Integer getPedestrian() {
        return pedestrian;
    }

    public void setPedestrian(Integer pedestrian) {
        this.pedestrian = pedestrian;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public Integer getChangeLine() {
        return changeLine;
    }

    public void setChangeLine(Integer changeLine) {
        this.changeLine = changeLine;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", carId=").append(carId);
        sb.append(", videoId=").append(videoId);
        sb.append(", outLine=").append(outLine);
        sb.append(", pedestrian=").append(pedestrian);
        sb.append(", speed=").append(speed);
        sb.append(", changeLine=").append(changeLine);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}