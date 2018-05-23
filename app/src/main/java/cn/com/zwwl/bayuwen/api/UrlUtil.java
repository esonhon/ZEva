package cn.com.zwwl.bayuwen.api;

import android.text.TextUtils;

import cn.com.zwwl.bayuwen.MyApplication;

public class UrlUtil {

    /**
     * Host信息
     **/
    public static String HOST;

    /**
     * 初始化host
     */
    public static void setHost() {
        if (MyApplication.DEBUG == 0) {// 线上环境
            HOST = "https://api.zhugexuetang.com/v2";

        } else if (MyApplication.DEBUG == 1) {// 测试环境
            HOST = "http://api.dev.zhugexuetang.com/v2";
        }
    }

    // 账号密码登录
    public static String LoginUrl() {
        return HOST + "/user/login";
    }

    // 快捷免密登录
    public static String smsloginUrl() {
        return HOST + "/user/appsignup";
    }

    // 注册
    public static String registerUrl() {
        return HOST + "/user/appsignup";
    }

    // 获取用户信息
    public static String userInfoUrl() {
        return HOST + "/user/info";
    }

    // 喜欢
    public static String likeUrl() {
        return HOST + "/fm/like";
    }

    // 获取课程的专辑列表
    public static String getAlbumListUrl(String kid, int page) {
        return HOST + "/fm/list?type=" + kid + "&page=" + page;
    }

    // 搜索接口
    public static String getSearchUrl(String title) {
        return HOST + "/fm/list?title=" + title;
    }

    // 增加播放量
    public static String addPlayUrl() {
        return HOST + "/fm/play";
    }

    // 修改密码
    public static String changePwd() {
        return HOST + "/user/apprest";
    }

    // 获取fm信息
    public static String getAlbumUrl(String kid) {
        return HOST + "/fm/detail?kid=" + kid;
    }

    //首页
    public static String getMainurl() {
        return HOST + "/tuijian/list";
    }

    //播放历史
    public static String getHistoryurl() {
        return HOST + "/history";
    }

    //获取收藏列表/ 添加收藏/ 删除收藏
    public static String getCollecturl() {
        return HOST + "/collection";
    }

    //获取评论
    public static String getPingListurl(String kid) {
        if (TextUtils.isEmpty(kid))
            return HOST + "/comment";
        return HOST + "/comment?kid=" + kid;
    }

    // 上传文件
    public static String uploadUrl() {
        return HOST + "/upload";
    }

    // 修改用户信息
    public static String changeInfoUrl(String uid) {
        return HOST + "/user/" + uid;
    }

    // 管理收货地址
    public static String addressUrl() {
        return HOST + "/address";
    }

    // 选课列表
    public static String getEleCourseListUrl() {
        return HOST + "/course/type";
    }

    // 赞列表
    public static String getTopListUrl() {
        return HOST + "/vote/toplist";
    }

}
