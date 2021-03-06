package cn.com.zwwl.bayuwen.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import cn.com.zwwl.bayuwen.R;
import cn.com.zwwl.bayuwen.api.CourseApi;
import cn.com.zwwl.bayuwen.api.order.KaiTuanbyCodeApi;
import cn.com.zwwl.bayuwen.listener.FetchEntryListener;
import cn.com.zwwl.bayuwen.model.Entry;
import cn.com.zwwl.bayuwen.model.ErrorMsg;
import cn.com.zwwl.bayuwen.model.KeModel;
import cn.com.zwwl.bayuwen.util.UmengLogUtil;

/**
 * 团购码兑换课程页面
 */
public class TuanCodeUseActivity extends BaseActivity {

    private EditText codeEditText;
    private TextView codeSureTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mContext = this;
        super.onCreate(savedInstanceState);
        UmengLogUtil.logDuihuanClick(mContext);
        setContentView(R.layout.activity_tuan_code_user);
        initView();
    }

    private void initView() {
        findViewById(R.id.code_use_back).setOnClickListener(this);
        codeSureTv = findViewById(R.id.code_use_bt);
        codeSureTv.setOnClickListener(this);
        codeEditText = findViewById(R.id.code_use_code);
        codeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    handler.sendEmptyMessage(0);
                } else {
                    handler.sendEmptyMessage(1);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    codeSureTv.setBackground(getResources().getDrawable(R.drawable.gold_circle));
                    break;
                case 1:
                    codeSureTv.setBackground(getResources().getDrawable(R.drawable.gray_circle));
                    break;
            }
        }
    };

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.code_use_back:
                finish();
                break;
            case R.id.code_use_bt:
                String code = codeEditText.getText().toString();
                if (!TextUtils.isEmpty(code))
                    getKeDeatil(code);
                break;
        }
    }

    private void getKeDeatil(final String code) {
        showLoadingDialog(true);
        new CourseApi(mContext, code, 0, new FetchEntryListener() {
            @Override
            public void setError(ErrorMsg error) {
                showLoadingDialog(false);
                if (error != null)
                    showToast(error.getDesc());
            }

            @Override
            public void setData(Entry entry) {
                showLoadingDialog(false);
                if (entry != null && entry instanceof KeModel) {
                    KeModel keModel = (KeModel) entry;
                    Intent intent = new Intent(mContext, CourseDetailActivity.class);
                    intent.putExtra("CourseDetailActivity_data", keModel);
                    intent.putExtra("CourseDetailActivity_id", code);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void initData() {

    }
}
