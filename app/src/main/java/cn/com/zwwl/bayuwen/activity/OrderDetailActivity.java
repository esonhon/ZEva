package cn.com.zwwl.bayuwen.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import cn.com.zwwl.bayuwen.R;
import cn.com.zwwl.bayuwen.view.YouHuiJuanPopWindow;

/**
 * 订单详情页面
 */
public class OrderDetailActivity extends BaseActivity {
    private TextView bt1, bt2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mContext = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        initView();
    }

    private void initView() {

        findViewById(R.id.order_d_back).setOnClickListener(this);
        findViewById(R.id.order_d_you).setOnClickListener(this);
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    break;
                case 1:
                    break;
            }
        }
    };


    @Override
    protected void initData() {
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.order_d_back:
                finish();
                break;
            case R.id.order_d_you:// 优惠券
                new YouHuiJuanPopWindow(mContext);
                break;
        }
    }


}
