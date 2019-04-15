package org.mmall.util;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by Apple on 2017/6/15.
 * FTP服务器工具类
 * TODO FTPClient
 */
public class FTPUtil {

    private static final Logger logger = LoggerFactory.getLogger(FTPUtil.class);

    private static String ftpIp = PropertiesUtil.getProperty("ftp.server.ip");
    private static String ftpUser = PropertiesUtil.getProperty("ftp.user");
    private static String ftpPass = PropertiesUtil.getProperty("ftp.pass");

    private String ip;
    private int port;
    private String user;
    private String pwd;
    private FTPClient ftpClient;

    public FTPUtil(String ip, int port, String user, String pwd){
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.pwd = pwd;
    }

    /**
     * 上传文件
     * @param fileList      支持批量上传
     * @return  boolean     成功/失败
     * @throws IOException  异常在业务层统一处理
     */
    public static boolean uploadFile(List<File> fileList) throws IOException {
        FTPUtil ftpUtil = new FTPUtil(ftpIp, 21, ftpUser, ftpPass);
        logger.info("开始连接ftp服务器");
        boolean result = ftpUtil.uploadFile("img", fileList);//ftp服务器目录下的img下
        logger.info("开始连接ftp服务器，结束上传，上传结果:{}");
        return result;
    }

    /**
     * 上传的具体逻辑
     * @param remotePath    远程路径（支持多级文件夹）
     * @param fileList      支持批量上传
     * @return  boolean     成功/失败
     * @throws IOException
     */
    private boolean uploadFile(String remotePath, List<File> fileList) throws IOException {
        boolean uploaded = true;//标识位：是否已上传
        FileInputStream fis = null;
        //连接FTP服务器
        if(connectServer(this.ip, this.port, this.user, this.pwd)){
            try {
                ftpClient.changeWorkingDirectory(remotePath);// 更改工作目录（传null不会切换工作目录）
                ftpClient.setBufferSize(1024);//    设置缓冲区
                ftpClient.setControlEncoding("UTF-8");//    统一字符集
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);//  设置为二进制文件类型，防止乱码
                ftpClient.enterLocalPassiveMode();//    TODO 打开本地被动模式（和ftp统一）
                for(File fileItem : fileList){
                    fis = new FileInputStream(fileItem);
                    ftpClient.storeFile(fileItem.getName(), fis);// 存储文件
                }
            }catch (IOException e){
                logger.error("上传文件异常", e);
                uploaded = false;
                e.printStackTrace();
            }finally {
                if (fis != null) {
                    fis.close();
                }
                ftpClient.disconnect();
            }
        }
        return  uploaded;
    }

    /**
     * 连接服务器
     * @param ip
     * @param port
     * @param user
     * @param pwd
     * @return
     */
    private boolean connectServer(String ip, int port, String user, String pwd){
        boolean isSuccess = false;//标识位：连接是否成功
        ftpClient = new FTPClient();
        try {
            ftpClient.connect(ip);
            isSuccess = ftpClient.login(user, pwd);
        } catch (IOException e) {
            logger.error("连接FTP服务器异常", e);
            e.printStackTrace();
        }
        return isSuccess;
    }

//    ---------- getter and setter ----------------
    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
