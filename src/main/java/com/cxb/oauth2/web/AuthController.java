package com.cxb.oauth2.web;

import com.alibaba.fastjson.JSON;
import com.cxb.oauth2.helper.HttpHelper;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;


@Controller
public class AuthController {

    @Autowired
    private HttpHelper httpHelper;

    String client_id = "70b0503b92ada105ca14";
    String client_secret = "3ce52d25b52c325d5b0013a2092fcde1285865ac";

    @RequestMapping("/callback")
    @ResponseBody
    public String callback(@RequestParam("code") String code) {
        //1.code参数为github回调callback_uri时，github传递过来的
        System.out.println("请求callback...,code:" + code);
        Map<String,Object> map = new HashMap<>();
        map.put("client_id",client_id);
        map.put("client_secret",client_secret);
        map.put("code",code);
        map.put("redirect_url","http://localhost:8090");
        map.put("state", "test");
        //获取access token
        String url = "https://github.com/login/oauth/access_token";
        String json = JSON.toJSONString(map);
        //2.根据传入的参数（包含code），post请求https://github.com/login/oauth/access_token，获取返回值
        String result = httpHelper.Post(url, json);//access_token=your_client_id&scope=user&token_type=bearer
        System.out.println("callback result:" + result);

        String[] strs = result.split("&");
        String access_token = strs[0].split("=")[1];//解析access_token

        //3.根据access token,请求https://api.github.com/user获取用户信息
        String url_user = "https://api.github.com/user?access_token=" + access_token;
        String userInfo = httpHelper.Get(url_user);
        System.out.println("userInfo:" + userInfo);//返回的是一个json字符串
        return userInfo;
    }
}
