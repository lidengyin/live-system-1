package cn.hctech2006.livesystem1.controller;

import cn.hctech2006.livesystem1.bean.Video;
import cn.hctech2006.livesystem1.common.ServerResponse;
import cn.hctech2006.livesystem1.service.impl.FileServiceImpl;
import cn.hctech2006.livesystem1.service.impl.LabelServiceImpl;
import cn.hctech2006.livesystem1.service.impl.VideoServiceImpl;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@RestController
@RequestMapping("front")
@Api(tags = "前段接口")
public class FrontController {
    @Autowired private VideoServiceImpl videoService;
    @Autowired
    private FileServiceImpl fileService;
    @Autowired
    private LabelServiceImpl labelService;


    /**
     * 上传视频返回视频链接和截图连接
     * @param file
     * @return
     */
    @ApiOperation(value = "上传视频返回视频链接和截图连接")
    @PostMapping("/return_video_image.do")
    @CrossOrigin(origins = "*", allowCredentials = "true",allowedHeaders = "*",methods = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.HEAD, RequestMethod.OPTIONS, RequestMethod.PUT, RequestMethod.POST, RequestMethod.PATCH})
    public ServerResponse returnVideoUrlAndImgUrl(MultipartFile file) throws Exception {
        return videoService.returnVideoUrlAndImgUrl(file);
    }

    /**
     * 推送视频，包括视频链接，截图链接，视频名，标注信息
     * @param video
     * @return
     */
    @ApiOperation(value = "推送视频，包括视频链接,视频名，标注信息,之后通过websocket将必要信息推送给深度学习同时返回videoId")
    @ApiImplicitParams({
            @ApiImplicitParam(type = "query", name = "videoUrl",value = "视频链接",required = true),
            @ApiImplicitParam(type = "query", name = "name",value = "视频名",required = true),
            @ApiImplicitParam(type = "query", name = "label",value = "视频标注",required = true)

    })
    @PostMapping("/upload_video_name_label.do")
    @CrossOrigin(origins = "*", allowCredentials = "true",allowedHeaders = "*",methods = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.HEAD, RequestMethod.OPTIONS, RequestMethod.PUT, RequestMethod.POST, RequestMethod.PATCH})
    public ServerResponse uploadVideoUrlNameAndLabel(Video video){
        return videoService.uploadVideo(video);
    }

    /**
     * 分页分时间获取视频列表
     * @param pageNum
     * @param pageSize
     * @param
     * @return
     */
    @ApiOperation(value = "分页分时间获取视频列表")
    @ApiImplicitParams({
            @ApiImplicitParam(type = "query", name = "pageNum", value = "页码", required = true, defaultValue = "1"),
            @ApiImplicitParam(type = "query", name = "pageSize", value = "每页行数", required = true, defaultValue = "10"),
            @ApiImplicitParam(type = "query", name = "begin", value = "开始时间(yyyy-MM-dd HH:mm:ss)"),
            @ApiImplicitParam(type = "query", name = "end", value = "结束时间(yyyy-MM-dd HH:mm:ss)")

    })
    @GetMapping("get_video_list.do")
    @CrossOrigin(origins = "*", allowCredentials = "true",allowedHeaders = "*",methods = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.HEAD, RequestMethod.OPTIONS, RequestMethod.PUT, RequestMethod.POST, RequestMethod.PATCH})
    public ServerResponse getVideoList(int pageNum, int pageSize,
                                       @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8") Date begin,     @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                           @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")Date end){
        System.out.println(begin);
        System.out.println(end);
        return videoService.getVideoList(pageNum, pageSize, begin, end);
    }

    /**
     * 根据视频编号获取视频以及标注
     * @param videoId
     * @return
     */
    @ApiOperation(value = "根据视频编号获取录播视频以及标注")
    @ApiImplicitParams({
            @ApiImplicitParam(type = "query", name = "videoId", value = "视频编号",required = true)
    })
    @GetMapping("get_video_one.do")
    @CrossOrigin(origins = "*", allowCredentials = "true",allowedHeaders = "*",methods = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.HEAD, RequestMethod.OPTIONS, RequestMethod.PUT, RequestMethod.POST, RequestMethod.PATCH})
    public ServerResponse getVideoOne(String videoId){
        return videoService.getVideoOne(videoId);
    }

    /**
     * 根据视频编号获取实时标注整理后集合
     * @param videoId
     * @return
     */
    @ApiOperation(value = "根据视频编号获取实时标注整理后集合")
    @ApiImplicitParams({
            @ApiImplicitParam(type = "query", name = "videoId", value = "视频编号",required = true)
    })
    @GetMapping("get_label.do")
    @CrossOrigin(origins = "*", allowCredentials = "true",allowedHeaders = "*",methods = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.HEAD, RequestMethod.OPTIONS, RequestMethod.PUT, RequestMethod.POST, RequestMethod.PATCH})
    public ServerResponse getLabelByVideoId(String videoId){
        return labelService.getLabelByVideoId(videoId);
    }
}
