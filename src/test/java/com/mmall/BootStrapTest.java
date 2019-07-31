package com.mmall;

import org.apache.commons.net.ftp.FTPClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mmall.service.IUserService;
import org.mmall.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.URL;

/**
 * 测试当前环境是否部署成功
 *
 * <p>
 * # 启动脚本
 * # nginx -s reload
 * $ sudo /usr/local/nginx/sbin/nginx
 *
 * # tomcat
 * $ sudo /developer/apache-tomcat-7.0.73/bin/startup.sh
 *
 * # vsftpd
 * $ sudo service vsftpd restart
 * </p>
 *
 * @author gushizone@gmail.com
 * @createTime 2019-06-12 22:45
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:applicationContext.xml", "file:src/main/webapp/WEB-INF/dispatcher-servlet.xml" })
public class BootStrapTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(BootStrapTest.class);

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void initMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    /**
     * 测试当前环境是否部署成功
     */
    @Test
    public void mainTest(){
        // 测试 FTP连接登录
        Assert.assertTrue("FTP连接登录失败！", testFtpLogin());
        // 测试 Nginx-文件服务器访问
        Assert.assertTrue("Nginx-文件服务器访问失败！", testNginxDeploy());

    }



    /**
     * 测试 Nginx-文件服务器访问测试
     * todo 需要提供测试文件 emoji.png
     * @return
     */
    private boolean testNginxDeploy() {
        boolean flag = Boolean.FALSE;
        try {
            URL url = new URL(PropertiesUtil.getProperty("ftp.server.http.prefix") + "emoji.png");
            DataInputStream dataInputStream = new DataInputStream(url.openStream());
            flag = Boolean.TRUE;
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return flag;
    }

    /**
     * 测试 ftp 连接登录
     * @return
     */
    private boolean testFtpLogin() {
        boolean flag = Boolean.FALSE;

        FTPClient ftp = new FTPClient();
        ftp.setConnectTimeout(1000);
        try {
            ftp.connect(PropertiesUtil.getProperty("ftp.server.ip"));
            flag = ftp.login(PropertiesUtil.getProperty("ftp.user"),
                    PropertiesUtil.getProperty("ftp.pass"));
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return flag;
    }

}
