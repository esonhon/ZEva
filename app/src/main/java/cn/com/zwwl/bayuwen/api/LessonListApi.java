package cn.com.zwwl.bayuwen.api;

import android.app.Activity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import cn.com.zwwl.bayuwen.http.BaseApi;
import cn.com.zwwl.bayuwen.listener.ResponseCallBack;
import cn.com.zwwl.bayuwen.model.ErrorMsg;
import cn.com.zwwl.bayuwen.model.LessonModel;
import cn.com.zwwl.bayuwen.util.GsonUtil;

/**
 * 获取子课列表
 * Created by zhumangmang at 2018/6/4 9:35
 */
public class LessonListApi extends BaseApi {
    private ResponseCallBack<List<LessonModel>> callBack;
    private Activity activity;
    private String url;

    public LessonListApi(Activity context, String kid, ResponseCallBack<List<LessonModel>> callBack) {
        super(context);
        this.activity=context;
        this.callBack=callBack;
        url = UrlUtil.getLecturesList() + "?kid=" + kid;
        get();
    }

    @Override
    protected String getUrl() {
        return url;
    }

    @Override
    protected Map<String, String> getPostParams() {
        return null;
    }

    @Override
    protected void handler(JSONObject json, final JSONArray array, final ErrorMsg errorMsg) {
        if (array != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    callBack.result(GsonUtil.parseJsonArray(LessonModel.class, array.toString()), errorMsg);
                }
            });
        }
    }
}
