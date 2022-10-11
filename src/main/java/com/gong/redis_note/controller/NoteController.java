package com.gong.redis_note.controller;

import com.gong.redis_note.model.Student;
import com.gong.redis_note.service.ShortUrlGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: 公杰
 * @Project: JavaLaity
 */

@Api("短信链接转换器")
//@RestController
@Controller
@RequestMapping("/note")
public class NoteController {
    @Resource
    private HttpServletResponse response;
    @Autowired
    private RedisTemplate redisTemplate;
    private final static String SHORT_URL_KEY = "short:url";

    @ApiOperation("链接转换")
    @GetMapping("/encode")
    @ResponseBody
    public String encode(String url) {
        //一个长连接url转换为四个加密字符串key
        String[] strings = ShortUrlGenerator.shortUrl(url);
        //任意取出一个
        String string = strings[0];
        //用hash，key=加密串，value=原始url
        redisTemplate.opsForHash().put(SHORT_URL_KEY, string, url);
        System.out.println("长链接为" + url + "短链接为" + string);
        return string;
    }


    @GetMapping("/{key}")
    public String url(@PathVariable("key") String key) {
        String url = (String) redisTemplate.opsForHash().get(SHORT_URL_KEY,key);
        return "redirect:" + url;
    }

}
