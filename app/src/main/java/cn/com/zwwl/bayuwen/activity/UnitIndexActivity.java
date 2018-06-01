package cn.com.zwwl.bayuwen.activity;

import android.content.Intent;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.zwwl.bayuwen.R;
import cn.com.zwwl.bayuwen.adapter.PicAdapter;
import cn.com.zwwl.bayuwen.api.UnitDetailApi;
import cn.com.zwwl.bayuwen.base.BasicActivityWithTitle;
import cn.com.zwwl.bayuwen.listener.ResponseCallBack;
import cn.com.zwwl.bayuwen.model.ErrorMsg;
import cn.com.zwwl.bayuwen.model.UnitDetailModel;

/**
 * 课程单元详情首页
 * Created by zhumangmang at 2018/5/30 11:11
 */
public class UnitIndexActivity extends BasicActivityWithTitle {
    @BindView(R.id.ppt_list)
    RecyclerView pptList;
    @BindView(R.id.homework)
    RecyclerView homework;
    @BindView(R.id.tutor_eval)
    AppCompatTextView tutorEval;
    @BindView(R.id.teacher)
    AppCompatTextView teacher;
    @BindView(R.id.tutor)
    AppCompatTextView tutor;
    @BindView(R.id.adviser)
    AppCompatTextView adviser;
    PicAdapter pptAdapter, jobAdapter;
    UnitDetailModel model;

    @Override
    protected int setContentView() {
        return R.layout.activity_unit_index;
    }

    @Override
    protected void initView() {
        pptList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        pptList.setItemAnimator(new DefaultItemAnimator());
        homework.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        homework.setItemAnimator(new DefaultItemAnimator());
        pptAdapter = new PicAdapter(null);
        pptList.setAdapter(pptAdapter);
        jobAdapter = new PicAdapter(null);
        homework.setAdapter(jobAdapter);

    }

    @Override
    protected void initData() {
        setCustomTitle(getIntent().getStringExtra("title"));
        String kId = getIntent().getStringExtra("kId");
        String cId = getIntent().getStringExtra("cId");
        getData(kId, cId);
    }

    private void getData(String kId, String cId) {
        new UnitDetailApi(this, kId, cId, new ResponseCallBack<UnitDetailModel>() {
            @Override
            public void result(UnitDetailModel unitDetailModel, ErrorMsg errorMsg) {
                if (unitDetailModel != null) {
                    model = unitDetailModel;
                    tutorEval.setText(unitDetailModel.getTaSummary().getContent());
                    pptAdapter.setNewData(unitDetailModel.getAccessory().getData());
                    jobAdapter.setNewData(unitDetailModel.getJob().getData());
                    teacher.setText("授课老师：" + unitDetailModel.getTeachers().getTeacher().getName());
                    tutor.setText("助教老师：" + unitDetailModel.getTeachers().getAssistant().getName());
                    adviser.setText("学业顾问：" + unitDetailModel.getTeachers().getCounselor().getName());
                }
            }
        });
    }

    @Override
    protected void setListener() {

    }

    @OnClick({R.id.submit_work, R.id.ppt})
    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.submit_work:
                intent.setClass(this, UploadPicActivity.class);
                intent.putExtra("cId", "");
                startActivity(intent);
                break;
            case R.id.ppt:
                if (model.getAccessory().getState() != 0) {
                    String[] urls = new String[model.getAccessory().getData().size()];
                    for (int i = 0; i < model.getAccessory().getData().size(); i++)
                        urls[i] = model.getAccessory().getData().get(i).getUrl();
                    intent.setClass(this, LookPPTActivity.class);
                    intent.putExtra("urls", urls);
                    startActivity(intent);
                }
                break;
        }

    }

    @Override
    public boolean setParentScrollable() {
        return true;
    }

    @Override
    public void close() {
        finish();
    }

}
