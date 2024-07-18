package com.primax.dinnerscan_wx.config;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.primax.dinnerscan_wx.config.CommonUtil.httpsRequest;


/**
 * Created by admin on 2017/10/20.
 */
public class WeixinHelper {

    private static final Logger logger = LoggerFactory.getLogger(WeixinHelper.class);
    /**
     * 获取网页授权凭证
     *
     * @param appId     公众账号的唯一标识
     * @param appSecret 公众账号的密钥
     * @return WeixinAouth2Token
     */
    public static Wxoauth2token getOauth2AccessToken(String appId, String appSecret,String code) {
        Wxoauth2token wat = null;
        // 拼接请求地址
        String requestUrl = WxConstants.GET_ACCESS_TOKEN;
        requestUrl = requestUrl.replace("ID", appId);
        requestUrl = requestUrl.replace("SECRET", appSecret);
        requestUrl = requestUrl.replace("CODE", code);
        // 获取网页授权凭证
        JSONObject jsonObject = httpsRequest(requestUrl, "GET", null);
        logger.info("{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}");
        logger.info("获取网页凭证json数据的值是：" + jsonObject.toString());
        logger.info("获取网页凭证json数据的值是：" + jsonObject.toString());
        logger.info("获取网页凭证json数据的值是：" + jsonObject.toString());
        logger.info("获取网页凭证json数据的值是：" + jsonObject.toString());
        logger.info("获取网页凭证json数据的值是：" + jsonObject.toString());
        logger.info("获取网页凭证json数据的值是：" + jsonObject.toString());
        if (null != jsonObject) {
            try {
                wat = new Wxoauth2token();
                wat.setAccessToken(jsonObject.getString("access_token"));
                wat.setExpiresIn(7200);
                wat.setOpenId(jsonObject.getString("openid"));
            } catch (Exception e) {
                wat = null;
            }
        }
        return wat;
    }


    /**
     * 通过网页授权获取成员信息
     *
     * @param access_token 网页授权接口调用凭证
     * @param code         登录code
     * @return memberInfo
     */
    public static MemberInfo getMemberInfo(String access_token, String code) {
        MemberInfo memberInfo = null;
        //拼接请求地址
        String requestUrl = WxConstants.GET_USER_INFO;
        requestUrl = requestUrl.replace("ACCESS_TOKEN", access_token);
        requestUrl = requestUrl.replace("CODE", code);
        // 通过网页授权获取用户信息
        JSONObject jsonObject = httpsRequest(requestUrl, "GET", null);
        logger.info("11111111111111111111查询用户唯一标识：++++++++++++++++++++++++++++++++++++++++++++++++++++");
        logger.info(requestUrl);
        logger.info(jsonObject.toString());
        logger.info(jsonObject.toString());

        if (null != jsonObject) {
            try {
                memberInfo = new MemberInfo();
                if (Integer.parseInt(jsonObject.getString("errcode")) == 0 && jsonObject.getString("errmsg").equals("ok")) {
                    //返回码成功
                    memberInfo.setErrCode(0);
                    memberInfo.setErrMsg(jsonObject.getString("errmsg"));
                    memberInfo.setDeviceId(jsonObject.getString("DeviceId"));
                    if (jsonObject.getString("OpenId") != null && !jsonObject.getString("OpenId").equals("")) {
                        //说明返回的是第二种结果，不是内部成员
                        memberInfo.setOpenId(jsonObject.getString("OpenId"));
                        return memberInfo;
                    } else if (jsonObject.getString("UserId") != null && !jsonObject.getString("UserId").equals("")) {
                        System.out.println("jsonObject.getString(UserId)" + jsonObject.getString("UserId"));
                        System.out.println("jsonObject.getString(UserId)" + jsonObject.getString("UserId"));
                        System.out.println("jsonObject.getString(UserId)" + jsonObject.getString("UserId"));
                        System.out.println("jsonObject.getString(UserId)" + jsonObject.getString("UserId"));
                        //说明是内部成员，可以通过userid获取成员信息
                        memberInfo.setUserId(jsonObject.getString("UserId"));
                        return memberInfo;
                    } else {
                        //返回出错
                        return null;
                    }
                } else {
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }
}














