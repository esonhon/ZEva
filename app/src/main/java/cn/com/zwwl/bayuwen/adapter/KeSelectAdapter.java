package cn.com.zwwl.bayuwen.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.com.zwwl.bayuwen.R;
import cn.com.zwwl.bayuwen.glide.GlideApp;
import cn.com.zwwl.bayuwen.listener.OnItemClickListener;
import cn.com.zwwl.bayuwen.model.KeModel;
import cn.com.zwwl.bayuwen.util.CalendarTools;

/**
 * 选课页面课程列表adapter
 */
public class KeSelectAdapter extends BaseRecylcerViewAdapter<KeModel> {

    public KeSelectAdapter(Context mContext, List<KeModel> list) {
        super(mContext, list);
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int
            viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_course_select, parent,
                false));
    }


    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        KeModel keModel = list.get(position);

        GlideApp.with(mContext).load(keModel.getPic())
                .placeholder(R.drawable.avatar_placeholder)
                .error(R.drawable.avatar_placeholder)
                .into(viewHolder.img);
        viewHolder.tag.setText(keModel.getTagTxt());
        viewHolder.title.setText(keModel.getTitle());
        viewHolder.teacher.setText(keModel.getTname());
        viewHolder.xiaoqu.setText(keModel.getSchool());
        viewHolder.date.setText(CalendarTools.format(Long.valueOf(keModel.getStartPtime()),
                "yyyy-MM-dd") + " 至 " + CalendarTools.format(Long.valueOf(keModel.getEndPtime()),
                "yyyy-MM-dd"));
        viewHolder.time.setText(keModel.getClass_start_at() + " - " + keModel.getClass_end_at());
        viewHolder.left.setText("剩余名额：" + keModel.getNum());
        viewHolder.price.setText("￥" + keModel.getBuyPrice());
        setItemClickView(holder.itemView, position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView img;
        public TextView tag;
        public TextView title;
        public TextView teacher;
        public TextView xiaoqu;
        public TextView date;
        public TextView time;
        public TextView left;
        public TextView price;

        public ViewHolder(View itemView) {
            super(itemView);
            initView();

        }

        private void initView() {
            img = itemView.findViewById(R.id.course_iv);
            tag = itemView.findViewById(R.id.stateTv);
            title = itemView.findViewById(R.id.videoName);
            teacher = itemView.findViewById(R.id.tNameTv);
            xiaoqu = itemView.findViewById(R.id.addressTv);
            date = itemView.findViewById(R.id.dateTv);
            time = itemView.findViewById(R.id.timeTv);
            left = itemView.findViewById(R.id.surplus_quotaTv);
            price = itemView.findViewById(R.id.priceTv);


        }
    }
}
