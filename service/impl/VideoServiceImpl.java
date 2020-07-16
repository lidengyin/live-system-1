package cn.hctech2006.livesystem1.service.impl;

import cn.hctech2006.livesystem1.bean.FrontVideo;
import cn.hctech2006.livesystem1.bean.Label;
import cn.hctech2006.livesystem1.bean.Video;
import cn.hctech2006.livesystem1.common.ServerResponse;
import cn.hctech2006.livesystem1.mapper.VideoMapper;
import cn.hctech2006.livesystem1.server.RealTimeDeepinWebSocketServer;
import cn.hctech2006.livesystem1.server.RealTimeWebSocketServer;
import cn.hctech2006.livesystem1.util.ImageAndVideoUtil;
import cn.hctech2006.livesystem1.util.PropertiesUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class VideoServiceImpl {
    @Autowired
    private VideoMapper videoMapper;
    @Autowired
    private LabelServiceImpl labelService;
    @Autowired
    private FileServiceImpl fileService;

    /**
     * 返回前段视频和截图链接
     * @param file
     * @return
     * @throws Exception
     */
    public ServerResponse returnVideoUrlAndImgUrl(MultipartFile file) throws Exception {
       try{
           String videoUrl = fileService.uploadFile(file);
           System.out.println(videoUrl);
           String imgUrl = screenshot(videoUrl);
           Map<String, String> map = new HashMap<>();
           map.put("videoUrl",videoUrl);
           map.put("imgUrl",imgUrl);
           return ServerResponse.createBySuccess(map);
       }catch (Exception e){
           e.printStackTrace();
           return ServerResponse.createByError("生成失败");
       }
    }

    /**
     * 深度学习上传视频
     * @param file
     * @param videoId
     * @return
     * @throws Exception
     */
    public ServerResponse deepinUploadVideoAndVideoIdAndNum(MultipartFile file, String videoId,int  num) throws Exception {
        try{
            String videoUrl = fileService.uploadFile(file);

            String imgUrl = screenshot(videoUrl);
            Video video = new Video();
            video.setVideoId(videoId);
            video.setImgUrl(imgUrl);
            video.setVideoUrl(videoUrl);
            video.setNum(num);
            int result  = videoMapper.updateVideoUrlAndImgUrlAndNumByVideoId(video);
            return ServerResponse.createBySuccess("上传成功");
        }catch (Exception e){
            e.printStackTrace();
            return ServerResponse.createByError("上传失败");
        }
    }

    /**
     * 视频上传推送
     * @param video
     * @return
     */
    public ServerResponse uploadVideo(Video video){
       try{
           video.setVideoId(UUID.randomUUID().toString());
           videoMapper.insert(video);
           Map<String,String> map = new HashMap<>();
           map.put("flag","true");
           map.put("videoUrl",video.getVideoUrl());
           map.put("label",video.getLabel());
           map.put("videoId",video.getVideoId());
           //WebSocket推送
           RealTimeDeepinWebSocketServer.sendAll(map);
           return ServerResponse.createBySuccess("上传推送成功，视频ID用作获取标注列表",video.getVideoId());
       }catch (Exception e){
           e.printStackTrace();
           return ServerResponse.createByError("上传推送失败");
       }
    }

    /**
     *　获取视频列表
     * @param pageNum
     * @param pageSize
     * @param begin
     * @param end
     * @return
     */
    public ServerResponse getVideoList(int pageNum, int pageSize, Date begin, Date end){
        PageHelper.startPage(pageNum, pageSize);
        List<Video> videos = videoMapper.selectVideoByDate(begin, end);
        PageInfo result = new PageInfo(videos);
        return ServerResponse.createBySuccess(result);

    }

    /**
     * 获取某一个视频
     * @param videoId
     * @return
     */
    public ServerResponse getVideoOne(String videoId){
        try{
            Video video = videoMapper.selectByVideoId(videoId);
            ServerResponse response = labelService.getLabelByVideoId(videoId);
            //List<Label> labels = (List<Label>) response.getData();
            Map<String,Label> map = (Map<String, Label>) response.getData();
            FrontVideo frontVideo = new FrontVideo();
            frontVideo.setImgUrl(video.getImgUrl());
            frontVideo.setVideoUrl(video.getVideoUrl());
            frontVideo.setLabel(video.getLabel());
            frontVideo.setName(video.getName());
            frontVideo.setVideoId(videoId);
            frontVideo.setLabels(map);

            return ServerResponse.createBySuccess(frontVideo);
        }catch (Exception e){
            e.printStackTrace();
            return ServerResponse.createByError("获取失败");
        }
    }

    public ServerResponse pushFlag(){
        try{
            RealTimeWebSocketServer.send("上传完毕,调用标注结果接口");
            return ServerResponse.createBySuccess("上传成功");
        }catch (Exception e){
            e.printStackTrace();
            return ServerResponse.createByError("上传失败");
        }
    }

    /**
     * 截图类
     * @param videoUrl
     * @return
     * @throws Exception
     */
    private String  screenshot(String videoUrl) throws Exception {
        String url = UUID.randomUUID().toString()+".jpg";
        String imgUrl = ResourceUtils.getURL("").getPath()+ url;
        ImageAndVideoUtil.fetchFrame(videoUrl, imgUrl);
        fileService.uploadFile(imgUrl);
        imgUrl = PropertiesUtil.getProperty("ftp.server.http.prefix")+url;
        return imgUrl;
    }
}
