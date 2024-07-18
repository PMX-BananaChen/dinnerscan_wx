package com.primax.dinnerscan_wx.config;

public class WxConstants {

    //企业的CorpId
    public static String APPID_PMX="ww24261f29f0d4837c";
    public static String APPID_TYM="wx91b34d8e7feff276";

    public static String GET_ACCESS_TOKEN = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=ID&corpsecret=SECRET";//网页授权凭证access_token
    public static String GET_USER_INFO = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token=ACCESS_TOKEN&code=CODE";//获取用户信息
    public static String GET_JSAPI_TICKET = "https://qyapi.weixin.qq.com/cgi-bin/get_jsapi_ticket?access_token=ACCESS_TOKEN";

    //应用id
    public static String AGENTID_PMX = "1000036";
    //应用密钥
    public static String CROPSECRET_PMX= "JvNq4-oqFoWiaqh2Ap1eKfN_wCzSfsiZpvtEXsUGrVY";

    //应用id
    public static String AGENTID_TYM = "1000027";
    //应用密钥
    public static String CROPSECRET_TYM= "SDUiuZgKvkR-scLCCsQ19b5txo16cclzScdn3c8NFPg";

}
