package cn.com.zwwl.bayuwen.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.Unbinder;
import cn.com.zwwl.bayuwen.R;
import cn.com.zwwl.bayuwen.activity.TopicDetailActivity;
import cn.com.zwwl.bayuwen.adapter.TopicMessageAdapter;
import cn.com.zwwl.bayuwen.api.TopicMessageApi;
import cn.com.zwwl.bayuwen.api.UrlUtil;
import cn.com.zwwl.bayuwen.base.BasicFragment;
import cn.com.zwwl.bayuwen.listener.ResponseCallBack;
import cn.com.zwwl.bayuwen.model.ErrorMsg;
import cn.com.zwwl.bayuwen.model.TopicMessageModel;
import cn.com.zwwl.bayuwen.util.ToastUtil;


public class TopicFragment extends BasicFragment {

    Unbinder unbinder;
    @BindView(R.id.search_view)
    EditText search_view;
    @BindView(R.id.all_message_list)
    RecyclerView allMessageList;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    @BindView(R.id.all_message_lin)
    LinearLayout allMessageLin;
    @BindView(R.id.id_topfragment)
    FrameLayout idTopfragment;
    Unbinder unbinder1;

    private String url = UrlUtil.getTopicMessage();
    private List<TopicMessageModel> messageModels;
    private TopicMessageAdapter topicMessageAdapter;


    @Override
    protected View setContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_topic, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        HttpData();
    }

    @Override
    protected void initView() {

        messageModels = new ArrayList<>();


        allMessageList.setLayoutManager(new LinearLayoutManager(getActivity()));
        allMessageList.setItemAnimator(new DefaultItemAnimator());

        topicMessageAdapter = new TopicMessageAdapter(null);
        topicMessageAdapter.setEmptyView(R.layout.empty_view, (ViewGroup) allMessageList.getParent());
        allMessageList.setAdapter(topicMessageAdapter);
        refresh.setRefreshContent(allMessageList);
        refresh.autoRefresh();
        refresh.finishLoadMore(false);
    }

    private void HttpData() {
        new TopicMessageApi(activity,url,new ResponseCallBack<List<TopicMessageModel>>() {
            @Override
            public void result(List<TopicMessageModel> messageModel, ErrorMsg errorMsg) {
                refresh.finishLoadMore();
                refresh.finishRefresh();
                if (messageModel != null) {
                    messageModels.clear();
                    messageModels=messageModel;
                    topicMessageAdapter.setNewData(messageModels);
                } else {
                    ToastUtil.showShortToast("暂无数据");
                }
            }


        });
    }



    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {
        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshlayout) {

                refresh.setNoMoreData(false);
                HttpData();
            }
        });
        refresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                HttpData();
            }
        });

        topicMessageAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                Intent intent = new Intent(activity, TopicDetailActivity.class);
                intent.putExtra("topicId", messageModels.get(position).getId());
                startActivity(intent);

            }
        });

        search_view.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    HttpData1(v.getText().toString().trim());
                    return true;
                }
                return false;
            }
        });

    }

    private void HttpData1(String name) {
        new TopicMessageApi(activity,url,name,new ResponseCallBack<List<TopicMessageModel>>() {
            @Override
            public void result(List<TopicMessageModel> messageModel, ErrorMsg errorMsg) {
                refresh.finishLoadMore();
                refresh.finishRefresh();
                if (messageModel != null) {
                    messageModels=messageModel;
                    topicMessageAdapter.setNewData(messageModels);
                } else {
                    ToastUtil.showShortToast("暂无数据");
                }
            }


        });
    }


}

