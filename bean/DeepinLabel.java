package cn.hctech2006.livesystem1.bean;

public class DeepinLabel {

    private String videoId;

    private String outLine;

    private String pedestrian;

    private String speed;

    private String changeLine;

    private String carUid;
    public DeepinLabel(String videoId, String outLine, String pedestrian, String speed, String changeLine, String carUid) {
        this.videoId = videoId;
        this.outLine = outLine;
        this.pedestrian = pedestrian;
        this.speed = speed;
        this.changeLine = changeLine;
        this.carUid = carUid;
    }

    public String getCarUid() {
        return carUid;
    }

    public void setCarUid(String carUid) {
        this.carUid = carUid;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getOutLine() {
        return outLine;
    }

    public void setOutLine(String outLine) {
        this.outLine = outLine;
    }

    public String getPedestrian() {
        return pedestrian;
    }

    public void setPedestrian(String pedestrian) {
        this.pedestrian = pedestrian;
    }

//    public String getSpeed() {
//        return speed;
//    }
//
//    public void setSpeed(String speed) {
//        this.speed = speed;
//    }


    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getChangeLine() {
        return changeLine;
    }

    public void setChangeLine(String changeLine) {
        this.changeLine = changeLine;
    }
}
