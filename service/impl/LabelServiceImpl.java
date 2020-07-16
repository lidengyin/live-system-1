package cn.hctech2006.livesystem1.service.impl;

import cn.hctech2006.livesystem1.bean.DeepinLabel;
import cn.hctech2006.livesystem1.bean.Label;
import cn.hctech2006.livesystem1.common.ServerResponse;
import cn.hctech2006.livesystem1.mapper.LabelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class LabelServiceImpl {
    @Autowired
    private LabelMapper labelMapper;

    /**
     * 标签上传
     * @param deepinLabel
     * @return
     */
    public ServerResponse uploadLabel(DeepinLabel deepinLabel) throws NullPointerException{

        try {

            String videoId = deepinLabel.getVideoId();
            System.out.println(deepinLabel.getSpeed());
            Map<String, Integer> map = new HashMap<>();
            if (deepinLabel.getCarUid() != null){
                String []carUid = deepinLabel.getCarUid().split(",");
                for (int i =0; i < carUid.length; i ++){
                    carUid[i] = deleteCharString1(carUid[i],'[');
                    carUid[i] = deleteCharString1(carUid[i],']');
                    System.out.println("carUid["+i+"]"+carUid[i]);
                }
                for (int i = 0; i < carUid.length-1; i+=2){
                    if (carUid[i].equals("None")){
                        continue;
                    }
                    Label label = new Label();
                    label.setVideoId(videoId);
                    label.setCarId(carUid[i]);
                    label.setCarUid(carUid[i+1]);
//                    if (labelMapper.selectByVideoIdAndCarUid(videoId,carUid[i+1]) <= 0)
                    labelMapper.insert(label);
                }
            }
            if(deepinLabel.getSpeed() != null){
                String []speed = deepinLabel.getSpeed().split(",");
                for (int i = 0; i < speed.length; i ++){
                    speed[i] = deleteCharString1(speed[i],'[');
                    speed[i] = deleteCharString1(speed[i],']');

                    System.out.println("speed["+i+"]"+speed[i]);
                }
                for (int i = 0; i <speed.length-1; i+=2 ){

                    Label label = new Label();
                    label.setVideoId(videoId);

                    //BigDecimal b = ;
                    //speed[i+1] = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    //label.setSpeed(Double.parseDouble(speed[i+1]));
                    //label.setSpeed(new BigDecimal(speed[i+1]).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                    System.out.println(speed[i]);
                    label.setCarUid(speed[i]);

                    label.setSpeed(Double.parseDouble(speed[i+1]));
//                    /System.out.println(String.format("%2.f",speed[i+1]));

                    if (labelMapper.selectByVideoIdAndCarUid(videoId,speed[i]) > 0){
                        int result = labelMapper.updateSpeedByVideoIdAndCarUid(videoId,speed[i],Double.parseDouble(speed[i+1]));
                    }

                }
            }

            if (deepinLabel.getOutLine() != null){
                String out = deleteCharString1(deepinLabel.getOutLine(),'[');
                out = deleteCharString1(out,']');
                String []outLine = out.split(",");
                for (int i = 0; i < outLine.length; i ++){
                    int result = labelMapper.updateOutLineByVideoIdAndCarId(videoId, outLine[i]);
                }
            }
            if (deepinLabel.getPedestrian() != null){
                String ped = deleteCharString1(deepinLabel.getPedestrian(),'[');
                ped = deleteCharString1(ped,']');
                String []pedstrain = ped.split(",");
                for (int i = 0; i < pedstrain.length; i ++){
                    int result = labelMapper.updatePedestrianByVideoIdAndCarId(videoId, pedstrain[i]);
                }
            }
            if (deepinLabel.getChangeLine() != null){
                String change = deleteCharString1(deepinLabel.getChangeLine(),'[');
                change = deleteCharString1(change,']');
                String []changeLine = change.split(",");
                for (int i = 0; i < changeLine.length; i ++){
                    int result  = labelMapper.updateChangeLineByVideoIdAndCarId(videoId, changeLine[i]);
                }

            }
                //todo 保存所有车车速，默认不压线，不违规变道，礼让行人
                //todo 根据车牌号修改是否压线
                int i = 0;
                //todo 根据车牌号修改是否礼让行人
                //todo 根据车牌号修改是否违规变道
            return ServerResponse.createBySuccess("上传成功");
        }catch (Exception e){
            e.printStackTrace();
            return ServerResponse.createByError("上传失败");
        }
    }

    /**
     * 根据视频ID获取标注列表
     * @param videoId
     * @return
     */
    public ServerResponse getLabelByVideoId(String videoId){
        List<Label> labels = labelMapper.selectLabelByVideoId(videoId);
        Map<String,Label> map = new HashMap<>();
        for (Label label : labels){
            char f = label.getCarId().charAt(0);

            if (!map.containsKey(label.getCarUid())  && (String.valueOf(f).matches("[\u4e00-\u9fa5]")) ){
                label.setSpeed(Double.parseDouble(String.format("%.2f",label.getSpeed())));
                map.put(label.getCarUid(),label);
            }
            if (map.containsKey(label.getCarUid())){
                Label label1 = map.get(label.getCarUid());
                String carId = label.getCarId();
                String carId1 = label1.getCarId();
                if (!carId1.equals(carId)){
                    if (carId1.length() < carId.length()){
                        label.setCarId(carId1);
                        if (label1.getChangeLine()==1){
                            label.setChangeLine(1);
                        }
                        if (label1.getOutLine() == 1){
                            label.setOutLine(1);
                        }
                        if (label1.getPedestrian() == 1){
                            label.setPedestrian(1);
                        }
                        map.remove(label.getCarUid());
                        label.setSpeed(Double.parseDouble(String.format("%.2f",label.getSpeed())));
                        map.put(label.getCarUid(),label);
                    }
                }
            }
        }
        return ServerResponse.createBySuccess(map);
    }
    private String deleteCharString1(String sourceString, char chElemData) {
        String deleteString = "";
        int iIndex = 0;
        for (int i = 0; i < sourceString.length(); i++) {
            if (sourceString.charAt(i) == chElemData) {
                if (i > 0) {
                    deleteString += sourceString.substring(iIndex, i);
                }
                iIndex = i + 1;
            }
        }
        if (iIndex <= sourceString.length()) {
            deleteString += sourceString.substring(iIndex, sourceString.length());
        }
        return deleteString;
    }
}
