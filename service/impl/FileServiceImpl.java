package cn.hctech2006.livesystem1.service.impl;

import cn.hctech2006.livesystem1.util.FTPUtil;
import cn.hctech2006.livesystem1.util.ImageAndVideoUtil;
import cn.hctech2006.livesystem1.util.PropertiesUtil;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;
@Service
public class FileServiceImpl {
    /**
     * 文件上传
     * @param file
     * @return
     */
    public String uploadFile(MultipartFile file) throws IOException {
        String name = UUID.randomUUID().toString();
        System.out.println(name);
        System.out.println(file.getOriginalFilename());
        String url = name+file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String Name = ResourceUtils.getURL("").getPath()+url;
        File folder = new File(Name);

        file.transferTo(folder);
        String fileName = ImageAndVideoUtil.converterToMp4(folder);
        System.out.println("fileName"+fileName);
        folder.delete();
        folder = new File(fileName);
        url = name+folder.getName().substring(folder.getName().lastIndexOf("."));
        System.out.println("url"+url);
        FTPUtil.uploadFile(Lists.newArrayList(folder));
        folder.delete();
        return PropertiesUtil.getProperty("ftp.server.http.prefix")+url;
    }
    public String uploadFile(String url) throws IOException {

        File folder = new File(url);
        FTPUtil.uploadFile(Lists.newArrayList(folder));
        folder.delete();
        return PropertiesUtil.getProperty("ftp.server.http.prefix")+url;
    }
}
