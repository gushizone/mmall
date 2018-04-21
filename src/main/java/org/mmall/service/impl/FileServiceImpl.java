package org.mmall.service.impl;

import com.google.common.collect.Lists;
import org.mmall.service.IFileService;
import org.mmall.util.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by Apple on 2017/6/14.
 * 文件上传业务类
 */
@Service("iFileService")
public class FileServiceImpl implements IFileService {

    Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    /**
     * 上传文件（客户端->应用服务器->图片服务器）
     * @param file  由SpringMVC提供的文件上传类
     * @param path  路径（客户端）
     * @return  路径（应用服务器上）
     */
    @Override
    public String upload(MultipartFile file, String path) {
        String fileName = file.getOriginalFilename();
        //扩展名   避免abc.abc.jpg命名，使用从后向前截取
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".")+1);//   扩展名：jpg
        String uploadFileName = UUID.randomUUID().toString() +"."+ fileExtensionName;
        logger.info("开始上传文件，上传文件名:{},上传路径:{},新文件名:{}", fileName, path, uploadFileName);// TODO sl4j占位符

        File fileDir = new File(path);
        if(!fileDir.exists()){
            fileDir.setWritable(true);//  获得写权限
            fileDir.mkdirs();//   可递归创建文件夹
        }
        File targetFile = new File(path, uploadFileName);
        try{
//            文件已上传到应用服务器
            file.transferTo(targetFile);
//            将targetFile上传到FTP服务器上
            FTPUtil.uploadFile(Lists.newArrayList(targetFile));
//             上传完之后，删除应用服务器upload下面的文件
            targetFile.delete();
        }catch (IOException e){
            logger.error("上传文件异常", e);
            return null;
        }
        return targetFile.getName();
    }
}
