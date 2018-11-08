package com.shsxt.xmjf.web.controller;

import com.google.code.kaptcha.Producer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 *  验证码
 * @author zhangxuan
 * @date 2018/11/8
 * @time 15:48
 */

@Controller
public class ImageController {

    @Resource
    private Producer producer;

    @RequestMapping("image")
    public void createImage(HttpServletRequest request, HttpServletResponse response) {
        response.setDateHeader("Expires", 0);
        // Set standard HTTP/1.1 no-cache headers.
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        // Set standard HTTP/1.0 no-cache header.
        response.setHeader("Pragma", "no-cache");
        // return a jpeg
        response.setContentType("image/jpeg");

        // 图片验证码值
        String code = producer.createText();
        System.out.println();
        BufferedImage image = producer.createImage(code);
        ServletOutputStream sos = null;


        try {
            sos = response.getOutputStream();
            ImageIO.write(image,"jpg",sos);

            //将验证码存入session
            request.getSession().setAttribute("image",code);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != sos){
                try {
                    sos.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    sos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
