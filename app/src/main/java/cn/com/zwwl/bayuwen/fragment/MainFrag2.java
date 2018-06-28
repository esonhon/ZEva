package cn.com.zwwl.bayuwen.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import cn.com.zwwl.bayuwen.MyApplication;
import cn.com.zwwl.bayuwen.R;
import cn.com.zwwl.bayuwen.activity.AllTeacherActivity;
import cn.com.zwwl.bayuwen.activity.CityActivity;
import cn.com.zwwl.bayuwen.activity.MainActivity;
import cn.com.zwwl.bayuwen.activity.MessageActivity;
import cn.com.zwwl.bayuwen.activity.CourseCenterActivity;
import cn.com.zwwl.bayuwen.activity.SearchCourseActivity;
import cn.com.zwwl.bayuwen.adapter.DianzanAdapter;
import cn.com.zwwl.bayuwen.adapter.KeTagGridAdapter;
import cn.com.zwwl.bayuwen.api.KeTagListApi;
import cn.com.zwwl.bayuwen.api.KeTagListApi.TagCourseModel;
import cn.com.zwwl.bayuwen.api.PraiseListApi;
import cn.com.zwwl.bayuwen.api.PraiseListApi.PraiseModel;
import cn.com.zwwl.bayuwen.api.fm.RecommentApi;
import cn.com.zwwl.bayuwen.db.TempDataHelper;
import cn.com.zwwl.bayuwen.glide.ImageLoader;
import cn.com.zwwl.bayuwen.listener.FetchEntryListListener;
import cn.com.zwwl.bayuwen.listener.FetchEntryListener;
import cn.com.zwwl.bayuwen.model.Entry;
import cn.com.zwwl.bayuwen.model.ErrorMsg;
import cn.com.zwwl.bayuwen.model.fm.RecommentModel;
import cn.com.zwwl.bayuwen.util.Tools;
import cn.com.zwwl.bayuwen.widget.LoopViewPager;
import cn.com.zwwl.bayuwen.widget.NoScrollListView;
import cn.com.zwwl.bayuwen.widget.observable.ObservableScrollView;
import cn.com.zwwl.bayuwen.widget.observable.ObservableScrollViewCallbacks;
import cn.com.zwwl.bayuwen.widget.observable.ScrollState;
import cn.com.zwwl.bayuwen.widget.observable.ScrollUtils;

/**
 * 选课
 */
public class MainFrag2 extends Fragment
        implements View.OnClickListener, ObservableScrollViewCallbacks {
    private Activity mActivity;
    private LoopViewPager bannerView;
    private View root;
    private RelativeLayout mToolbarView;
    private ObservableScrollView mScrollView;
    private GridView mGridView;

    private NoScrollListView tListView, gListView, zListView;
    private DianzanAdapter tAdapter, gAdapter, zAdapter;

    private int mParallaxImageHeight;
    private KeTagGridAdapter gridAdapter;
    private List<TagCourseModel> tagList = new ArrayList<>();
    private PraiseModel praiseModel;
    private List<RecommentModel> bannerData = new ArrayList<>();

    public boolean isCityChanged = false;// 城市状态是否变化

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    gridAdapter = new KeTagGridAdapter(getActivity(), tagList);
                    mGridView.setAdapter(gridAdapter);
                    gridAdapter.notifyDataSetChanged();
                    break;
                case 1:
                    setAdapterData();
                    break;
                case 2:
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout
                            .LayoutParams.MATCH_PARENT, MyApplication.width * 9 / 16);
                    bannerView.setSize(params);
                    List<View> views = new ArrayList<>();
                    for (RecommentModel recommentModel : bannerData) {
                        ImageView r = new ImageView(mActivity);
                        r.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        ImageLoader.display(mActivity, r, recommentModel.getPic(), R.mipmap
                                        .app_icon,
                                R.mipmap.app_icon);
                        views.add(r);
                    }
                    bannerView.setViewList(views);
                    bannerView.startLoop(true);
                    break;
            }
        }
    };

    /**
     * 默认fragment创建的时候是可见的，但是不会调用该方法！切换可见状态的时候会调用，但是调用onResume，onPause的时候却不会调用
     *
     * @param hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (isCityChanged) {// 切换城市之后 要重新获取课程tag list,点赞排行不必重新获取
                getEleCourseList();
            }
        } else {
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        bannerView.startLoop(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        bannerView.startLoop(true);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_main2, container, false);
        initView();
        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getEleCourseList();
        getPraiseList();
        getBannerList();
    }

    private void getBannerList() {
        new RecommentApi(mActivity, new RecommentApi.FetchRecommentListListener() {
            @Override
            public void setData(List<RecommentModel> list) {
                if (Tools.listNotNull(list)) {
                    bannerData.clear();
                    for (RecommentModel r : list) {
                        if (r.getParent().equals("19")) {
                            bannerData.add(r);
                        }
                    }
                    handler.sendEmptyMessage(2);
                }
            }

            @Override
            public void setError(ErrorMsg error) {

            }
        });
    }

    /**
     * 带传参的构造方法
     *
     * @param ss
     * @return
     */
    public static MainFrag2 newInstance(String ss) {
//        Bundle args = new Bundle();
        MainFrag2 fragment = new MainFrag2();
//        fragment.setArguments(args);
        return fragment;
    }

    private void initView() {
        mParallaxImageHeight = MyApplication.width * 9 / 16;
        bannerView = root.findViewById(R.id.frag2_head);
        mToolbarView = root.findViewById(R.id.main2_toolbar);
        mToolbarView.setBackgroundColor(ScrollUtils.getColorWithAlpha(0, getResources().getColor
                (R.color.transparent)));

        mGridView = root.findViewById(R.id.gridView);

        mScrollView = root.findViewById(R.id.scroll);
        mScrollView.setScrollViewCallbacks(this);
        mScrollView.setZoomView(bannerView);

        tListView = root.findViewById(R.id.jiaoshi_layout);
        gListView = root.findViewById(R.id.guwen_layout);
        zListView = root.findViewById(R.id.zhujiao_layout);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("SearchCourseActivity_id", tagList.get(position).getId() + "");
                intent.setClass(mActivity, CourseCenterActivity.class);
                startActivity(intent);
            }
        });
        root.findViewById(R.id.look_all_teacher).setOnClickListener(this);
        mToolbarView.findViewById(R.id.menu_more).setOnClickListener(this);
        mToolbarView.findViewById(R.id.menu_news).setOnClickListener(this);
        mToolbarView.findViewById(R.id.menu_school).setOnClickListener(this);
        mToolbarView.findViewById(R.id.menu_search).setOnClickListener(this);

    }

    private void setAdapterData() {
        tAdapter = new DianzanAdapter(mActivity);
        tAdapter.setData(praiseModel.getTeacherModels());
        tListView.setAdapter(tAdapter);
        gAdapter = new DianzanAdapter(mActivity);
        gAdapter.setData(praiseModel.getGuwenModels());
        gListView.setAdapter(gAdapter);
        zAdapter = new DianzanAdapter(mActivity);
        zAdapter.setData(praiseModel.getTeacherModels());
        zListView.setAdapter(zAdapter);
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        int baseColor = getResources().getColor(R.color.body_gray);
        float alpha = Math.min(1, (float) scrollY / mParallaxImageHeight * 1.4f);
        mToolbarView.setBackgroundColor(ScrollUtils.getColorWithAlpha(alpha, baseColor));
    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu_news:
                startActivity(new Intent(mActivity, MessageActivity.class));
                break;
            case R.id.menu_more:
                ((MainActivity) mActivity).openDrawer();
                break;
            case R.id.menu_school:
                Intent intent =new Intent(getActivity(), CityActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_search:
                startActivity(new Intent(mActivity, SearchCourseActivity.class));
                break;
            case R.id.look_all_teacher:
                startActivity(new Intent(mActivity, AllTeacherActivity.class));
                break;
        }
    }

    /**
     * 获取课程标签数据
     */
    private void getEleCourseList() {
        new KeTagListApi(getActivity(), new FetchEntryListListener() {

            @Override
            public void setData(List list) {
                if (list != null && list.size() > 0) {
                    tagList.clear();
                    tagList.addAll(list);
                    handler.sendEmptyMessage(0);
                    isCityChanged = false;
                }
            }

            @Override
            public void setError(ErrorMsg error) {
            }
        });
    }

    /**
     * 获取点赞排行数据
     */
    private void getPraiseList() {
        new PraiseListApi(getActivity(), new FetchEntryListener() {

            @Override
            public void setData(Entry entry) {
                if (entry != null && entry instanceof PraiseModel) {
                    praiseModel = (PraiseModel) entry;
                    handler.sendEmptyMessage(1);
                }
            }

            @Override
            public void setError(ErrorMsg error) {
            }
        });
    }
}
