package com.xieke.test.tyqxcms.web;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


public class AppController {
	/*public void doPostApp(String url,String number){
		

	}
	 *//**
     * HttpGet方法接口（以Parameter的形式接收数据）
     *//*
    @GetMapping(value = "/httpGetByParameter")
    public String httpGetByParameter(HttpServletRequest request, HttpServletResponse response,
                                 @RequestParam("id") String id, @RequestParam("name") String name,
                                 @RequestParam("sex") String sex) {
        System.out.println("请求的User如下:");
        System.out.println("id=" + id);
        System.out.println("name=" + name);
        System.out.println("sex=" + sex);
        handleHeaderAndCookie(request, response);
        return "getByParameterSuccess";
    }

    *//**
     * HttpGet方法接口（以实体对象的形式接收数据）
     *//*
    @GetMapping(value = "/httpGetByEntity")
    public String httpGetByEntity(HttpServletRequest request, HttpServletResponse response, User user) {
        System.out.println("请求的User如下:\n" + user.toString());
        handleHeaderAndCookie(request, response);
        return "getByEntitySuccess";
    }

    *//**
     * HttpPost方法接口（以Form的形式接收数据）
     *//*
    @PostMapping(value = "/httpPostByForm")
    public String httpPostByForm(HttpServletRequest request, HttpServletResponse response, User user) {
        System.out.println("请求的User如下:\n" + user.toString());
        handleHeaderAndCookie(request, response);
        return "postByFormSuccess";
    }*/

    /**
     * HttpPost方法接口（以Json的形式接收数据）
     */
    @PostMapping(value = "/httpPostByJson")
    public String httpPostAppByJson(HttpServletRequest request, HttpServletResponse response, @RequestBody User user) {
        System.out.println("请求的User如下:\n" + user.toString());
        handleHeaderAndCookie(request, response);
        return "postByJsonSuccess";
    }

    /**
     * Header处理和Cookie处理
     */
    private void handleHeaderAndCookie(HttpServletRequest request, HttpServletResponse response) {
        // 打印从客户端接收到的Header
        Enumeration<String> headerNames =  request.getHeaderNames();
        if (headerNames != null) {
            System.out.println("\n请求的Header如下:");
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                String headerValue = request.getHeader(headerName);
                System.out.println("HeaderName:" + headerName + "; HeaderValue:" + headerValue);
            }
        }

        // 打印从客户端接收到的Cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            System.out.println("\n请求的Cookie如下:");
            for (Cookie cookie : cookies) {
                String cookieName = cookie.getName();
                String cookieValue = cookie.getValue();
                String cookieDomain = cookie.getDomain();
                String cookiePath = cookie.getPath();
                System.out.println("CookieName:" + cookieName + "; CookieValue:" + cookieValue + "; CookieDomain:" + cookieDomain + "; CookiePath:" + cookiePath);
            }
        }

        // 返回Header给客户端
        response.addHeader("responseHeaderName_1", "responseHeaderValue_1");
        response.addHeader("responseHeaderName_2", "responseHeaderValue_2");

        // 返回Cookie给客户端
        response.addCookie(new Cookie("responseCookieName_1", "responseCookieValue_1"));
        response.addCookie(new Cookie("responseCookieName_2", "responseCookieValue_2"));
    }

}