package com.renny.simplebrowser.view.page;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.renny.simplebrowser.R;
import com.renny.simplebrowser.business.base.BaseFragment;
import com.renny.simplebrowser.business.base.CommonAdapter;
import com.renny.simplebrowser.business.db.dao.BookMarkDao;
import com.renny.simplebrowser.business.db.entity.BookMark;
import com.renny.simplebrowser.business.helper.Validator;
import com.renny.simplebrowser.business.permission.PermissionHelper;
import com.renny.simplebrowser.business.permission.PermissionListener;
import com.renny.simplebrowser.business.permission.Permissions;
import com.renny.simplebrowser.business.toast.ToastHelper;
import com.renny.simplebrowser.view.adapter.ExtendHeadAdapter;
import com.renny.simplebrowser.view.adapter.ExtendMarkAdapter;
import com.renny.simplebrowser.view.adapter.OverFlyingLayoutManager;
import com.renny.simplebrowser.view.listener.GoPageListener;
import com.renny.simplebrowser.view.widget.pullextend.ExtendListFooter;
import com.renny.simplebrowser.view.widget.pullextend.ExtendListHeader;
import com.renny.simplebrowser.view.widget.pullextend.PullExtendLayout;
import com.renny.zxing.Activity.CaptureActivity;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


/**
 * Created by Renny on 2018/1/2.
 */
public class HomePageFragment extends BaseFragment implements View.OnClickListener {
    private GoPageListener mGoPageListener;
    private static final int REQUEST_SCAN = 0;
    ExtendListHeader mPullNewHeader;
    ExtendListFooter mPullNewFooter;
    PullExtendLayout mPullExtendLayout;
    RecyclerView listHeader, listFooter;
    List<String> mDatas = new ArrayList<>();
    BookMarkDao mMarkDao = new BookMarkDao();
    ExtendMarkAdapter mExtendMarkAdapter;
    List<BookMark> markList;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home_page;
    }

    @Override
    public void bindView(View rootView, Bundle savedInstanceState) {
        super.bindView(rootView, savedInstanceState);
        mPullNewHeader = rootView.findViewById(R.id.extend_header);
        mPullNewFooter = rootView.findViewById(R.id.extend_footer);
        mPullExtendLayout = rootView.findViewById(R.id.pull_extend);
        rootView.findViewById(R.id.scan).setOnClickListener(this);
        rootView.findViewById(R.id.url_edit).setOnClickListener(this);
        listHeader = mPullNewHeader.getRecyclerView();
        listFooter = mPullNewFooter.getRecyclerView();
        listHeader.setLayoutManager(new OverFlyingLayoutManager(OrientationHelper.HORIZONTAL));
        listFooter.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        listFooter.setItemAnimator(new DefaultItemAnimator());
    }

    public void afterViewBind(View rootView, Bundle savedInstanceState) {
        markList = mMarkDao.queryForAll();
        mExtendMarkAdapter = new ExtendMarkAdapter(markList);
        reloadMarkListData();
        mExtendMarkAdapter.setItemClickListener(new CommonAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(int position, View view) {
                if (mGoPageListener != null) {
                    mGoPageListener.onGoPage(markList.get(position).getUrl());
                }
            }
        });
        mExtendMarkAdapter.setLongClickListener(new ExtendMarkAdapter.ItemLongClickListener() {
            @Override
            public void onItemLongClicked(int position, View view) {
                mMarkDao.delete(markList.get(position).getUrl());
                mExtendMarkAdapter.removeData(position);
                if (mExtendMarkAdapter.getItemCount() > 0) {
                    mPullExtendLayout.setPullLoadEnabled(true);
                } else {
                    mPullExtendLayout.closeExtendHeadAndFooter();
                    mPullExtendLayout.setPullLoadEnabled(false);
                }
            }
        });
        listFooter.setItemAnimator(new DefaultItemAnimator());
        listFooter.setAdapter(mExtendMarkAdapter);
        mDatas.add("历史记录");
        mDatas.add("无痕浏览");
        mDatas.add("新建窗口");
        mDatas.add("无图模式");
        mDatas.add("夜间模式");
        mDatas.add("网页截图");
        mDatas.add("禁用JS");
        mDatas.add("下载内容");
        mDatas.add("查找");
        mDatas.add("拦截广告");
        mDatas.add("全屏浏览");
        mDatas.add("翻译");
        mDatas.add("切换UA");
        listHeader.setAdapter(new ExtendHeadAdapter(mDatas).setItemClickListener(new CommonAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(int position, View view) {
                ToastHelper.makeToast(mDatas.get(position) + " 功能待实现");
            }
        }));
    }

    public void reloadMarkListData() {
        markList.clear();
        markList.addAll(mMarkDao.queryForAll());
        mExtendMarkAdapter.notifyDataSetChanged();
        if (mExtendMarkAdapter.getItemCount() > 0) {
            mPullExtendLayout.setPullLoadEnabled(true);
        } else {
            mPullExtendLayout.closeExtendHeadAndFooter();
            mPullExtendLayout.setPullLoadEnabled(false);
        }
    }

    private void startBrowser(String text) {
        if (mGoPageListener != null) {
            if (TextUtils.isEmpty(text)) {
                Toast.makeText(getActivity(), "请输入网址", Toast.LENGTH_SHORT).show();
            } else {
                if (!Validator.checkUrl(text)) {
                    mGoPageListener.onGoPage("http://www.baidu.com/s?wd=" + text);
                } else {
                    mGoPageListener.onGoPage(text);
                }
            }
        }
    }

    public void setGoPageListener(GoPageListener goPageListener) {
        mGoPageListener = goPageListener;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SCAN && resultCode == RESULT_OK) {
            startBrowser(data.getStringExtra("barCode"));
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.scan:
                if (mGoPageListener != null) {
                    PermissionHelper.requestPermissions(getActivity(), Permissions.PERMISSIONS_CAMERA, new PermissionListener() {
                        @Override
                        public void onPassed() {
                            //跳转到扫码页
                            startActivityForResult(new Intent(getActivity(), CaptureActivity.class), REQUEST_SCAN);
                        }
                    });
                }
                break;
            case R.id.url_edit:
                if (getActivity() != null) {
                    ((WebViewActivity) getActivity()).goSearchPage();
                }
        }
    }



}
