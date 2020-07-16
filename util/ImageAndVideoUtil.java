package cn.hctech2006.livesystem1.util;


import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacv.*;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.UUID;

public class ImageAndVideoUtil {

    public static  void findImgName(String imgUrl, MultipartFile multipartFile)throws IOException {

        //获得文件所在目录
        File folder = new File(imgUrl);
        //添加目录
        if(!folder.isDirectory()){
            folder.mkdirs();
        }
        try{
            //转移加载文件
            multipartFile.transferTo(folder);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String showImage(String imageUrl, HttpServletResponse response
    )throws IOException{
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        File file = new File(imageUrl);
        BufferedImage bi = ImageIO.read(new FileInputStream(file));
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(bi,"jpg",out);
        try{
            out.flush();
        }finally {
            out.close();
        }
        return null;
    }
    /**
     * 捕捉图片第一帧
     * @throws Exception
     */
    public static void fetchFrame(String videoUrl,String imgUrl)throws Exception{
//

        FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(videoUrl);
        Java2DFrameConverter converter = new Java2DFrameConverter();

        frameGrabber.start();
        //解码长度
        int length = frameGrabber.getLengthInFrames();
        //时间
        int i = 0;
        Frame frame = null;
        while(i < length){
            //过滤前五帧，避免出现全黑的图片
            frame = frameGrabber.grabFrame();
            if(i > 10 && (frame.image != null)){
                break;
            }
            i ++;
        }
        // Frame frame = frameGrabber.grabFrame();
        BufferedImage bufferedImage = converter.convert(frame);
        //照片保存文件夹
        File targetFile = new File(imgUrl);
        //文件夹不存在就新建
        if(!targetFile.isDirectory()){
            targetFile.mkdirs();
        }
        //写入文件夹,以jpg图片格式
        ImageIO.write(bufferedImage, "jpg", targetFile);
        //停止转化为帧
        frameGrabber.stop();
    }
    /**
     * 获取视频所有帧
     * @throws Exception
     */
    public static void fetchALLFrame(String videoUrl,String imgUrl)throws Exception{
//

        FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(videoUrl);
        Java2DFrameConverter converter = new Java2DFrameConverter();
        frameGrabber.start();
        //解码长度
        int length = frameGrabber.getLengthInFrames();
        //时间
        int i = 0;
        Frame frame = null;
        while(i < length){
            //过滤前五帧，避免出现全黑的图片
            frame = frameGrabber.grabFrame();
            if((frame.image != null)){
                // Frame frame = frameGrabber.grabFrame();
                BufferedImage bufferedImage = converter.convert(frame);
                //照片保存文件夹
                File targetFile = new File(imgUrl+i+".jpg");
                //文件夹不存在就新建
                if(!targetFile.isDirectory()){
                    targetFile.mkdirs();
                }
                //写入文件夹,以jpg图片格式
                ImageIO.write(bufferedImage, "jpg", targetFile);
            }
            i ++;
        }

        //停止转化为帧
        frameGrabber.stop();
    }

    public static String converterToMp4(File file) throws FileNotFoundException {
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(file);
        FFmpegFrameRecorder recorder = null;
        String fileName = null;
        try {
            grabber.start();
            fileName = file.getAbsolutePath().replace(file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf(".")),".mp4");

            recorder = new FFmpegFrameRecorder(fileName,grabber.getImageWidth(),grabber.getImageHeight(),grabber.getAudioChannels());
            //recorder.setVideoCodecName(grabber.getVideoCodecName());
            recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
            recorder.setFormat("mp4");
            recorder.setFrameRate(grabber.getFrameRate());
            recorder.setVideoBitrate(grabber.getVideoBitrate());
            recorder.setAudioBitrate(grabber.getAudioBitrate());
            recorder.setAudioChannels(grabber.getAudioChannels());
            recorder.setAudioOptions(grabber.getAudioOptions());
            recorder.setAudioQuality(0);
            recorder.setSampleRate(grabber.getSampleRate());
            recorder.setAudioCodec(grabber.getAudioCodec());

            recorder.start();
            Frame frame = null;
//            while (true){
//                frame = grabber.grabFrame();
//                if (frame == null){
//                    System.out.println("!!! Failed cvQueryFrame");
//                    break;
//                }
//                recorder.setTimestamp(grabber.getTimestamp());
//                recorder.record(frame);
//
//            }

// 抓取屏幕画面
            for (int i = 0; (frame = grabber.grabFrame()) != null; i++) {
                //可以通过这种方式判断音频还是视频，性能比较低，可以直接通过Frame中的image和samples的大小判断
                //EnumSet<Type> videoOrAudio=frame.getTypes();
                recorder.setTimestamp(grabber.getTimestamp());
                recorder.record(frame);
            }

            recorder.stop();
            recorder.release();
            grabber.stop();
            grabber.release();
            recorder.close();
            grabber.close();


        } catch (FrameGrabber.Exception | FrameRecorder.Exception e) {
            e.printStackTrace();
        }
        return fileName;



    }

    public static String showVideo(String videoUrl, HttpServletResponse response)throws IOException{
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("video/mp4");
        FileInputStream fis = null;
        OutputStream os = null ;
        File file = new File(videoUrl);
        fis = new FileInputStream(file);
        int size = fis.available(); // 得到文件大小
        byte data[] = new byte[size];
        fis.read(data); // 读数据
        fis.close();
        fis = null;
        response.setContentType("video/mp4"); // 设置返回的文件类型
        os = response.getOutputStream();
        os.write(data);
        try{
            os.flush();
        }finally {
            os.close();
        }
        return null;
    }

    public static void main(String[]args) throws Exception {
        String url = ResourceUtils.getURL("").getPath()+"image/"+"视频三";
        System.out.println(url);
        fetchALLFrame("https://public-ldy.oss-cn-beijing.aliyuncs.com/viedo-03.avi", url);
    }
}

