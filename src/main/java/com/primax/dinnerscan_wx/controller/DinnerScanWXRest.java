package com.primax.dinnerscan_wx.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.primax.dinnerscan_wx.config.*;
import com.primax.dinnerscan_wx.model.Result;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.primax.dinnerscan_wx.config.CommonUtil.httpsRequest;

@RequestMapping("/pmx/dinner/OAuth2")
@RestController
public class DinnerScanWXRest {
    private static final Logger logger = LoggerFactory.getLogger(DinnerScanWXRest.class);

    /**
     * 通过登录code,access_token获取用户的信息
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/authorize1")
    public Result authorize1(HttpServletRequest request, HttpSession session) throws  Exception{
        Result r = new Result(Result.RESULT_FAILURE, Result.RESULT_FAILURE_MSG);
        try {

            //获取登录code
            String code = request.getParameter("code");

            //获取access_token
            String access_token = getAccessToken("pmx");

            logger.info("access_token========>"+access_token);
            //获取用户信息
            MemberInfo userInfo=WeixinHelper.getMemberInfo(access_token,code);

            r.setCode(Result.RESULT_SUCCESS);
            r.setMsg(Result.RESULT_SUCCESS_MSG);
            if (userInfo.getUserId() == null && userInfo.getOpenId() != null){
                r.setData(userInfo.getOpenId());
            }else if(userInfo.getUserId() != null && userInfo.getOpenId() == null){
                r.setData(userInfo.getUserId());
            }

        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return r;
    }

    /**
     * 通过登录code,access_token获取用户的信息
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/authorize2")
    public Result authorize2(HttpServletRequest request, HttpSession session) throws  Exception{
        Result r = new Result(Result.RESULT_FAILURE, Result.RESULT_FAILURE_MSG);
        try {

            //获取登录code
            String code = request.getParameter("code");


            //获取access_token
            String access_token = getAccessToken("tym");


            //获取用户信息
            MemberInfo userInfo=WeixinHelper.getMemberInfo(access_token,code);

            logger.info("access_token========>"+access_token);

            //企业微信绑定userId

            if (userInfo.getUserId() == null && userInfo.getOpenId() != null){
                r.setData(userInfo.getOpenId());
            }else if(userInfo.getUserId() != null && userInfo.getOpenId() == null){
                r.setData(userInfo.getUserId());
            }

            r.setCode(Result.RESULT_SUCCESS);
            r.setMsg(Result.RESULT_SUCCESS_MSG);

        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return r;
    }


    /**
     * 东聚通过登录code,access_token获取用户的信息
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/getTicketSignByPmx")
    public Result getTicketSignByPmx(HttpServletRequest request, HttpSession session) throws  Exception{
        Result r = new Result(Result.RESULT_FAILURE, Result.RESULT_FAILURE_MSG);
        try {

            //获取当前url

            String url = request.getParameter("url");

            //获取access_token
            String access_token = getAccessToken("pmx");

            //获取企业的jsapi_ticket
            String jsapiTicket = getTicket(access_token, "pmx");

            Map<String, String> signMap = SignUtil.sign(jsapiTicket, url);
            logger.info("我是返回的签名包========="+ JSON.toJSONString(signMap));
            r.setCode(Result.RESULT_SUCCESS);
            r.setMsg(Result.RESULT_SUCCESS_MSG);
            r.setData(signMap);


        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return r;
    }


    /**
     * 东聚通过登录code,access_token获取用户的信息
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/getTicketSignByTym")
    public Result getTicketSignByTym(HttpServletRequest request, HttpSession session) throws  Exception{
        Result r = new Result(Result.RESULT_FAILURE, Result.RESULT_FAILURE_MSG);
        try {

            //获取当前url

            String url = request.getParameter("url");

            //获取access_token
            String access_token = getAccessToken("tym");

            //获取企业的jsapi_ticket
            String jsapiTicket = getTicket(access_token, "tym");

            Map<String, String> signMap = SignUtil.sign(jsapiTicket, url);

            r.setCode(Result.RESULT_SUCCESS);
            r.setMsg(Result.RESULT_SUCCESS_MSG);
            r.setData(signMap);


        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return r;
    }


    /**
     * 获取access_token
     *
     * @updateDate 2015年8月3日23:22:39 增加对缓存的处理
     * @return
     * @throws Exception
     */
    public static String getAccessToken(String company) throws Exception {
        String access_Token;
        String cropsecret;
        String appId;
        if (company == "pmx"){
            access_Token = "accessTokenPMX";
            cropsecret = WxConstants.CROPSECRET_PMX;
            appId = WxConstants.APPID_PMX;
        }else {
            access_Token = "accessTokenTYM";
            cropsecret = WxConstants.CROPSECRET_TYM;
            appId = WxConstants.APPID_TYM;
        }
        logger.info("company======================company====="+company);
        String accessToken = (String) EhcacheUtil.get("dinnerScan", access_Token);
        logger.info("accessToken=========11111==============="+accessToken);
        if (StringUtils.isBlank(accessToken)
                || StringUtils.isEmpty(accessToken)) {

            String requestUrl = WxConstants.GET_ACCESS_TOKEN;
            requestUrl = requestUrl.replace("ID", appId);
            requestUrl = requestUrl.replace("SECRET", cropsecret);
            logger.info("accessToken+requestUrl================="+requestUrl);

            JSONObject jsonObject = httpsRequest(requestUrl,"GET", null);

            accessToken = jsonObject.getString("access_token");
            EhcacheUtil.put("dinnerScan", access_Token, accessToken);
        }
        return accessToken;
    }


    /**
     * 获得jsapi_ticket（有效期7200秒)
     *
     * @updateDate 2015年8月4日00:00:46 z 增加对缓存的
     * @param accessToken
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     * @throws IOException
     * @throws NoSuchProviderException
     */
    public static String getTicket(String accessToken,String company) throws Exception {

        String jsapi_ticket;
        if (company == "pmx"){
            jsapi_ticket = "jsapiTicketPMX";
        }else {
            jsapi_ticket = "jsapiTicketTYM";
        }
        String jsapiTicket = (String) EhcacheUtil.get("dinnerScan", jsapi_ticket);
        logger.info("jsapiTicket=========11111==============="+jsapiTicket);
        if (StringUtils.isBlank(jsapiTicket) || StringUtils.isEmpty(jsapiTicket)) {
            String requestUrl = WxConstants.GET_JSAPI_TICKET;
            requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken);
            logger.info("jsapiTicket========requestUrl========="+requestUrl);
            JSONObject jsonObject = httpsRequest(requestUrl,"GET",null);

            jsapiTicket = jsonObject.getString("ticket");
            EhcacheUtil.put("dinnerScan", jsapi_ticket, jsapiTicket);
        }
        return jsapiTicket;
    }
}
