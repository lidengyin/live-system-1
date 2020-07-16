package cn.hctech2006.livesystem1.controller;

import cn.hctech2006.livesystem1.bean.DeepinLabel;
import cn.hctech2006.livesystem1.common.ServerResponse;
import cn.hctech2006.livesystem1.service.impl.LabelServiceImpl;
import cn.hctech2006.livesystem1.service.impl.VideoServiceImpl;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Api(tags = "深度学习接口")
@RequestMapping("deepin")
public class DeepInController {
    @Autowired
    private VideoServiceImpl videoService;
    @Autowired
    private LabelServiceImpl labelService;
    /**
     * 深度学习实时标注信息推送，后端活驴保存以及推送给前端
     * @param
     * @return
     */
    @ApiOperation(value = "深度学习实时标注信息推送，后端保存,不再推送给前段")
    @ApiImplicitParams({
            @ApiImplicitParam(type = "query", name = "outLine", value = "压线车辆集合",required = true),
            @ApiImplicitParam(type = "query", name = "changeLine", value = "违规变道车辆集合",required = true),
            @ApiImplicitParam(type = "query", name = "pedstrain", value = "不礼让行人车辆集合",required = true),
            @ApiImplicitParam(type = "query", name = "speed", value = "所有车辆车速集合",required = true),
            @ApiImplicitParam(type = "query", name = "videoId", value = "视频编号",required = true),
            @ApiImplicitParam(type = "query", name = "carUid", value = "车辆唯一编号",required = true)
    })
    @PostMapping(value = "/push_label.do")
    @CrossOrigin(origins = "*", allowCredentials = "true",allowedHeaders = "*",methods = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.HEAD, RequestMethod.OPTIONS, RequestMethod.PUT, RequestMethod.POST, RequestMethod.PATCH})
    public ServerResponse pushLabel(String  speed, String  outLine, String  changeLine, String pedstrain, String videoId,String carUid){
        System.out.println("outLine:"+outLine);
        System.out.println("speed:"+speed);


        DeepinLabel deepinLabel = new DeepinLabel(videoId,outLine,pedstrain,speed,changeLine,carUid);
        return labelService.uploadLabel(deepinLabel);
    }

    /**
     * 根据视频编号上传标注完成视频
     * @param file
     * @param videoId
     * @return
     */
    @ApiOperation(value = "根据视频编号上传标注完成视频以及车辆数量")
    @ApiImplicitParams({
    })
    @PostMapping("/upload_video.do")
    @CrossOrigin(origins = "*", allowCredentials = "true",allowedHeaders = "*",methods = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.HEAD, RequestMethod.OPTIONS, RequestMethod.PUT, RequestMethod.POST, RequestMethod.PATCH})
    public ServerResponse uploadVideo(@RequestBody MultipartFile file,@ApiParam("视频编号") @RequestParam String videoId, @ApiParam("车辆数量") @RequestParam int num) throws Exception {
        return videoService.deepinUploadVideoAndVideoIdAndNum(file, videoId,num);
    }
    @ApiOperation(value = "深度学习发送视频推送完成标志")
    @GetMapping("/push_flag.do")
    @CrossOrigin(origins = "*", allowCredentials = "true",allowedHeaders = "*",methods = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.HEAD, RequestMethod.OPTIONS, RequestMethod.PUT, RequestMethod.POST, RequestMethod.PATCH})
    public ServerResponse pushFlag(){
        return videoService.pushFlag();
    }
}
