package cn.com.zwwl.bayuwen.activity;

import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.com.zwwl.bayuwen.R;
import cn.com.zwwl.bayuwen.adapter.UnitTableAdapter;
import cn.com.zwwl.bayuwen.base.BasicActivityWithTitle;
import cn.com.zwwl.bayuwen.model.CourseModel;

public class TraceRecordActivity extends BasicActivityWithTitle {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    UnitTableAdapter adapter;

    @Override
    protected int setContentView() {
        return R.layout.activity_trace_record;
    }

    @Override
    protected void initView() {
        setCustomTitle("大语文");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    protected void initData() {
        List<CourseModel> courseModels = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            CourseModel model = new CourseModel();
            model.setPage("XXX");
            courseModels.add(model);
        }
        adapter = new UnitTableAdapter(courseModels);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void setListener() {
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                startActivity(new Intent(mActivity, FCourseIndexActivity.class));
            }
        });
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void close() {
        finish();
    }
}
