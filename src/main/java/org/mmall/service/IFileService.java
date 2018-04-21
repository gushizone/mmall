package org.mmall.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by Apple on 2017/6/14.
 * 文件上传业务接口
 */
public interface IFileService {

    String upload(MultipartFile file, String path);
}
