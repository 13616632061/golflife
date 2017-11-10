package com.glorystudent.golflife.activity;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.glorystudent.golflibrary.base.BaseActivity;
import com.glorystudent.golflibrary.util.SharedUtil;
import com.glorystudent.golflife.Manifest;
import com.glorystudent.golflife.R;
import com.glorystudent.golflife.adapter.ChatListAdapter;
import com.glorystudent.golflife.adapter.FaceGridAdapter;
import com.glorystudent.golflife.api.ConstantsURL;
import com.glorystudent.golflife.api.OkGoRequest;
import com.glorystudent.golflife.api.RequestAPI;
import com.glorystudent.golflife.customView.ChatPullToRefreshLayout;
import com.glorystudent.golflife.database.MyDataBase;
import com.glorystudent.golflife.entity.AliyunRequestEntity;
import com.glorystudent.golflife.entity.ChatEntity;
import com.glorystudent.golflife.entity.CloudVideoEntity;
import com.glorystudent.golflife.entity.ExtEntity;
import com.glorystudent.golflife.entity.FaceEntity;
import com.glorystudent.golflife.entity.FriendsProfileEntity;
import com.glorystudent.golflife.entity.GetObjectSamples;
import com.glorystudent.golflife.entity.SystemExtMessageEntity;
import com.glorystudent.golflife.util.AudioRecoderUtils;
import com.glorystudent.golflife.util.Constants;
import com.glorystudent.golflife.util.EventBusMapUtil;
import com.glorystudent.golflife.util.FileUtil;
import com.glorystudent.golflife.util.GetAmrDuration;
import com.glorystudent.golflife.util.ImageUtil;
import com.glorystudent.golflife.util.ScreenUtils;
import com.glorystudent.golflife.util.TimeUtil;
import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMVoiceMessageBody;
import com.hyphenate.util.ImageUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * TODO 好友聊天页面
 */
public class FriendChatActivity extends BaseActivity implements TextView.OnEditorActionListener, AdapterView.OnItemClickListener, CompoundButton.OnCheckedChangeListener{

    private final static String TAG = "FriendChatActivity";
    @Bind(R.id.tv_username)
    public TextView tv_username;
    @Bind(R.id.ll_more)
    public LinearLayout ll_more;
    @Bind(R.id.ll_input)
    public LinearLayout ll_input;
    @Bind(R.id.et_input)
    public EditText et_input;
    @Bind(R.id.lv_chat)
    public ListView lv_chat;
    @Bind(R.id.tv_record)
    public TextView tv_record;
    @Bind(R.id.cb_voice)
    public CheckBox cb_voice;
    @Bind(R.id.tv_profile)
    public ImageView tv_profile;
    @Bind(R.id.ll_bottom)
    public LinearLayout ll_bottom;
    @Bind(R.id.chat_refresh)
    public ChatPullToRefreshLayout chat_refresh;

    private ChatListAdapter chatListAdapter;
    private Boolean flag = false;
    private LinearLayout rLayout;
    private ConvenientBanner vp_face;
    private List<FaceEntity> faceEntityList;
    private StringBuffer stringBuffer;
    private String phoneNumber;
    // 当前会话对象
    private EMConversation mConversation;
    /**
     * 自定义实现Handler，主要用于刷新UI操作
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x001:
                    //收到消息
                    lv_chat.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                    getMessageed();
                    break;
                case 0x002:
                    lv_chat.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                    //发送消息
                    et_input.setText("");
                    EventBus.getDefault().post(EventBusMapUtil.getIntMap(13, 1));
                    getMessageed();
                    break;
            }
        }
    };
    private EMMessageListener emMessageListener;
    private String customerphoto;
    private List<ChatEntity> datas;
    private Uri imageUri;
    private AudioRecoderUtils audioRecoderUtils;
    private boolean isHide = false;
    private boolean iskeyboardHide = false;
    private String sdCardPath1;
    private List<String> saveTime;
    private boolean isExist;
    private boolean isRefresh;//true 是下拉刷新， false 是上拉加载
    private String headMsgId;
    private int mPosition;
    private SQLiteDatabase sqLiteDatabase;
    private MyDataBase myDataBase;
    private CloudVideoEntity.ListvideosBean sharevideo;
    private boolean isMore = false;
    private int otherHeight;
    private boolean flagEdit = false;
    private int saveSoftHeight = 0;
    private int saveLvHeight = 0;
    private boolean isEnd = true;
    private boolean isKey = false;
    private int bottomPx;
    private int maxPx;
    private int softPx;
    private boolean isCalculate = true;
    private int bottomStatePx;
    private boolean isBiandong = false;

    @Override
    protected int getContentId() {
        return R.layout.activity_friend_chat;
    }

    @Override
    protected void init() {
        myDataBase=MyDataBase.getInstance(this);
        sqLiteDatabase=myDataBase.getWritableDatabase();
//        sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(getDatabasePath("video.db"), null);
        lv_chat.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        /**
         * 聊天消息刷新
         */
        chat_refresh.setOnRefreshListener(new ChatPullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(ChatPullToRefreshLayout pullToRefreshLayout) {
                lv_chat.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_NORMAL);
                isRefresh = true;
                List<EMMessage> msg = null;
                if (datas != null) {
                    msg = mConversation.loadMoreMsgFromDB(headMsgId, 20);
                    if (msg != null && msg.size() > 0) {
                        addMessageToHeader();
                    }
                }
                chat_refresh.setRefreshState(isRefresh, ChatPullToRefreshLayout.SUCCEED);
                if (msg != null && msg.size() > 0) {
                }
            }

            @Override
            public void onLoadMore(ChatPullToRefreshLayout pullToRefreshLayout) {

            }
        });

        sdCardPath1 = ImageUtil.getSDCardPath();
        Intent intent = getIntent();
        String userName="";
        if(intent!=null){
            userName = intent.getStringExtra("username");
            phoneNumber = intent.getStringExtra("phonenumber");
        }
        System.out.println("userName="+userName +" phoneNumber : "+ phoneNumber );
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(phoneNumber);
        //指定会话消息未读数清零
        if (conversation != null) {
            System.out.println("conversation" );
            conversation.markAllMessagesAsRead();
            EventBus.getDefault().post(EventBusMapUtil.getIntMap(11, 1));
        }
        if (phoneNumber.equals("00000")) {
            tv_profile.setVisibility(View.GONE);
            ll_input.setVisibility(View.GONE);
            ll_bottom.setVisibility(View.GONE);
            RelativeLayout.LayoutParams layoutParams3 = (RelativeLayout.LayoutParams) chat_refresh.getLayoutParams();
            layoutParams3.width = RelativeLayout.LayoutParams.MATCH_PARENT;
            layoutParams3.height = RelativeLayout.LayoutParams.MATCH_PARENT;
            chat_refresh.setLayoutParams(layoutParams3);
        }

        customerphoto = intent.getStringExtra("customerphoto");
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            sharevideo = (CloudVideoEntity.ListvideosBean) bundle.getSerializable("sharevideo");
        }
        if (userName != null) {
            tv_username.setText(userName);
        }
        datas = new ArrayList<>();
        String sdCardPath = ImageUtil.getSDCardPath();
        audioRecoderUtils = new AudioRecoderUtils(sdCardPath);
        audioRecoderUtils.setContext(this);
        audioRecoderUtils.setOnAudioStatusUpdateListener(new AudioRecoderUtils.OnAudioStatusUpdateListener() {
            @Override
            public void onUpdate(double db, long time) {

            }

            @Override
            public void onStop(String filePath) {
                Log.d(TAG, "onStop: --->停止发送");
                try {
                    long amrDuration = GetAmrDuration.getAmrDuration(new File(filePath));
                    if (amrDuration > 1) {
                        Log.d(TAG, "onStop: --->" + filePath + " " + audioRecoderUtils.getSaveTime());
                        long t = audioRecoderUtils.getSaveTime() / 1000;
                        if (t >= 1) {
                            sendVoice(filePath, t);
                        } else {
                            Toast.makeText(FriendChatActivity.this, "发送语音不能少于1秒", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

/**
 * TODO 发送语音的按钮的触摸监听
 */
        tv_record.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        audioRecoderUtils.startRecord();
                        tv_record.setText("松开 发送");
                        tv_record.setBackgroundResource(R.drawable.shape_edit_grayinput);
                        break;
                    case MotionEvent.ACTION_UP:
                        tv_record.setText("按住 说话");
                        tv_record.setBackgroundResource(R.drawable.shape_edit_input);
                        long t = audioRecoderUtils.stopRecord();
                        Log.d(TAG, "onTouch: ---->小于一秒" + t);
                        if (t < 1000) {
                            Log.d(TAG, "onTouch: ---->小于一秒" + t);
                        }
                        break;
                }
                return true;
            }
        });

        Log.d(TAG, "init: 手机号码--->" + phoneNumber + " " + customerphoto);
        mConversation = EMClient.getInstance().chatManager().getConversation(phoneNumber, null, true);
        et_input.setOnEditorActionListener(this);
        stringBuffer = new StringBuffer();
        chatListAdapter = new ChatListAdapter(this);
        if (customerphoto != null) {
            chatListAdapter.setCustomerphoto(customerphoto);
        }
        lv_chat.setAdapter(chatListAdapter);
        lv_chat.setOnItemClickListener(this);
        lv_chat.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        cb_voice.setOnCheckedChangeListener(this);
        rLayout = ((LinearLayout) findViewById(R.id.root));
        //输入法到底部的间距(按需求设置)
        final int paddingBottom = 0;


        rLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                rLayout.getWindowVisibleDisplayFrame(r);
                //r.top 是状态栏高度
                int screenHeight = rLayout.getRootView().getHeight();
                int softHeight = screenHeight - r.bottom;
                Log.e(TAG, "screenHeight:" + screenHeight);
                Log.e(TAG, "top:" + r.top);
                Log.e(TAG, "bottom:" + r.bottom);
                Log.e(TAG, "Size: " + softHeight);
                if (softHeight != saveSoftHeight) {
                    saveSoftHeight = softHeight;
//                    calculateHeight();
                    isBiandong = true;
                    Log.i(TAG, "onGlobalLayout: savesoftHeight第一次" + saveSoftHeight);
                    Log.d(TAG, "onGlobalLayout: --这是打开几盘-》" + saveSoftHeight);
                } else {
                    isBiandong = false;
                }
                if (softHeight > 300) {//当输入法高度大于100判定为输入法打开了830
                    Log.d(TAG, "onGlobalLayout: 打开了键盘" + softHeight);
                    iskeyboardHide = true;
                    ll_more.setVisibility(View.GONE);
                    int lvheight = lv_chat.getHeight();
                    Log.i(TAG, "onGlobalLayout: lvheight-->" + lvheight);
                    if (lvheight > saveLvHeight) {
                        saveLvHeight = lvheight;
                    }
                    Log.i(TAG, "onGlobalLayout: savelvheight--->" + saveLvHeight);
                    if (!isKey && isEnd) {
                        isKey = true;
                        Log.i(TAG, "onGlobalLayout: 开始设置高度时的高度：" + saveSoftHeight);
                        startBottomEditAnimator(saveSoftHeight);
                    }
                    if (isBiandong && isKey && isEnd) {
                        flagEdit = false;
                        startBottomEditAnimator(saveSoftHeight);
                        Log.d(TAG, "onGlobalLayout: 重新计算弹出高度");
                    }
                } else {//否则判断为输入法隐藏了
                    Log.d(TAG, "onGlobalLayout: 关闭了键盘");
                    Log.i(TAG, "onGlobalLayout: 关闭时" + saveSoftHeight);
                    iskeyboardHide = false;
                    ll_more.setVisibility(View.VISIBLE);
                    if (isKey && isEnd) {
                        isKey = false;
                        startBottomEditAnimator(saveSoftHeight);
                    }
                }
            }
        });

        if (mConversation != null && mConversation.getLastMessage() != null) {
            initConversation();
            getMessageed();
        } else {
            saveTime = new ArrayList<>();
            datas = new ArrayList<>();
        }

        // 循环遍历当前收到的消息
        // 设置消息为已读
        // 因为消息监听回调这里是非ui线程，所以要用handler去更新ui
        // 如果消息不是当前会话的消息发送通知栏通知
        emMessageListener = new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> list) {
                // 循环遍历当前收到的消息
                Log.d(TAG, "onMessageReceived: --->收到消息");
                for (EMMessage message : list) {
                    Log.d(TAG, "onMessageReceived: --->收到消息" + message.getFrom() + phoneNumber);
                    if (message.getFrom().equals(phoneNumber)) {
                        // 设置消息为已读
                        Log.d(TAG, "onMessageReceived: --这个Id->" + message.getMsgId());
                        mConversation.markMessageAsRead(message.getMsgId());
                        Log.d("yuejin", "onMessageReceived: ---->接收到了消息" + message.getBody().toString());
                        // 因为消息监听回调这里是非ui线程，所以要用handler去更新ui
                        Message msg = mHandler.obtainMessage();
                        msg.what = 0x001;
                        msg.obj = message;
                        mHandler.sendMessage(msg);
                    } else {
                        // 如果消息不是当前会话的消息发送通知栏通知
                    }
                }
                EventBus.getDefault().post(EventBusMapUtil.getIntMap(11, 1));
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> list) {
                Log.d(TAG, "onMessageChanged: ---->收到透传消息");
            }

            @Override
            public void onMessageRead(List<EMMessage> list) {
                Log.d(TAG, "onMessageChanged: ---->收到已读回执");
            }

            @Override
            public void onMessageDelivered(List<EMMessage> list) {
                Log.d(TAG, "onMessageChanged: ---->收到已送达回执");
            }

            @Override
            public void onMessageChanged(EMMessage emMessage, Object o) {
                Log.d(TAG, "onMessageChanged: ---->消息状态变动");
            }
        };
        EMClient.getInstance().chatManager().addMessageListener(emMessageListener);
        if (sharevideo != null) {
            sendCloudVideo();
        }
        getTop();
    }
    private boolean isKeyBoardshow = false;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (isCalculate) {
                isCalculate = false;
                calculateHeight();
            }
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    if (isHide && iskeyboardHide) {
                        startBottomAnimator();
                        isHide = false;
                    }
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }

            if (flag && !isLayoutInput(ev)) {
                startBottomAnimator();
                flag = false;
                isHide = false;
            }
            return super.dispatchTouchEvent(ev);
        }


        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);

    }

    public void getTop() {
        int[] leftTop = {0, 0};
        ll_input.getLocationInWindow(leftTop);
        int left = leftTop[0];
        int top = leftTop[1];

        int[] leftTop2 = {0, 0};
        lv_chat.getLocationInWindow(leftTop2);
        int left2 = leftTop2[0];
        int top2 = leftTop2[1];

        Log.d(TAG, "dispatchTouchEvent: 检测布局上旬--->" + top + " " + top2);
    }

    public boolean isLayoutInput(MotionEvent ev) {
        int[] leftTop = {0, 0};
        ll_input.getLocationInWindow(leftTop);
        int left = leftTop[0];
        int top = leftTop[1];
        int bottom = top + ll_input.getHeight() + ll_more.getHeight();
        int right = left + ll_input.getWidth() + ll_more.getWidth();
        if (ev.getX() > left && ev.getX() < right
                && ev.getY() > top && ev.getY() < bottom) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                isMore = false;
                return false;
            } else {
                return true;
            }
        }
        return false;
    }


    /**
     * TODO 发送语音
     *
     * @param filePath
     */
    private void sendVoice(String filePath, long length) {
        EMMessage message = EMMessage.createVoiceSendMessage(filePath, (int) length, phoneNumber);
        EMClient.getInstance().chatManager().sendMessage(message);
        message.setMessageStatusCallback(new EMCallBack() {
            @Override
            public void onSuccess() {
                // 消息发送成功，打印下日志，正常操作应该去刷新ui
                Log.i("lzan13", "send message on success");
                System.out.println("发送语言成功：");
                mHandler.sendEmptyMessage(0x002);
            }

            @Override
            public void onError(int i, String s) {
                // 消息发送失败，打印下失败的信息，正常操作应该去刷新ui
                System.out.println("发送语言失败：");
                Log.i("lzan13", "send message on error " + i + " - " + s);
            }

            @Override
            public void onProgress(int i, String s) {
                Log.d(TAG, "onProgress: ---->" + i);
                // 消息发送进度，一般只有在发送图片和文件等消息才会有回调，txt不回调
            }
        });
    }

    private void initConversation() {
        /**
         * 初始化会话对象，这里有三个参数么，
         * 第一个表示会话的当前聊天的 useranme 或者 groupid
         * 第二个是绘画类型可以为空
         * 第三个表示如果会话不存在是否创建
         */
        //指定会话消息未读数清零
        mConversation.markAllMessagesAsRead();

    }


    private void createFaceDatas() {
        int[] face = Constants.FACE;
        faceEntityList = new ArrayList<>();
        for (int i = 0; i < face.length / 23; i++) {
            FaceEntity faceEntity = new FaceEntity();
            List<Integer> facestr = new ArrayList<>();
            for (int j = i * 23; j < (i + 1) * 23 + 1; j++) {
                if (j == (i + 1) * 23) {
                    facestr.add(-1);
                } else {
                    facestr.add(face[j]);
                }
            }
            faceEntity.setFaces(facestr);
            faceEntityList.add(faceEntity);
        }
    }

    private void addEmojiToTextView() {
        ll_more.removeAllViews();
        View inflate = LayoutInflater.from(this).inflate(R.layout.item_banner_vp, null);
        vp_face = (ConvenientBanner) inflate.findViewById(R.id.vp_face);
        ll_more.addView(inflate);
        View send = LayoutInflater.from(this).inflate(R.layout.item_chat_send, null);
        TextView tv_face = (TextView) send.findViewById(R.id.tv_face);
        String emojiStringByUnicode = getEmojiStringByUnicode(0x1F60a);
        tv_face.setText(emojiStringByUnicode);
        TextView tv_send = (TextView) send.findViewById(R.id.tv_send);

        tv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = et_input.getText().toString();
                System.out.println("消息内容："+content);
                if (content != null && !content.isEmpty()) {
                    sendHuanxinMessage(content, phoneNumber);
                }
            }
        });
        ll_more.addView(send);
        createFaceDatas();
        addFaceViewPager();
    }


    /**
     * TODO 添加表情
     */
    private void addFaceViewPager() {
        //设置自动轮播
        vp_face.setPages(
                new CBViewHolderCreator<LocalImageHolderView>() {
                    @Override
                    public LocalImageHolderView createHolder() {
                        return new LocalImageHolderView();
                    }
                }, faceEntityList)
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                .setPageIndicator(new int[]{R.drawable.shape_min_greycircle, R.drawable.shape_min_orangecircle})
                //设置指示器的方向
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        boolean isOK = true;
        switch (actionId) {
            case EditorInfo.IME_ACTION_SEND:
                String content = et_input.getText().toString();
                if (content != null && !content.isEmpty()) {
                    sendHuanxinMessage(content, phoneNumber);
                }
                break;
            default:
                isOK = false;
                break;
        }
        return isOK;
    }

    /**
     * TODO 语音发送
     *
     * @param buttonView
     * @param isChecked
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            //发送语音
            et_input.setVisibility(View.GONE);
            tv_record.setVisibility(View.VISIBLE);
        } else {
            //发送文字
            et_input.setVisibility(View.VISIBLE);
            tv_record.setVisibility(View.GONE);
        }
    }

    /**
     * TODO 监听List的点击
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    private OSS oss;
    private static String endpoint;
    private static String accessKeyId;
    private static String accessKeySecret;
    private static String uploadFilePath;

    private static String testBucket;
    private static String uploadObject;
    private static String downloadObject;
    private LocalBroadcastManager localBroadcastManager;
    public void setKeyId(AliyunRequestEntity aliyunRequestEntity) {
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        List<AliyunRequestEntity.ListsettingvalueBean> listsettingvalue = aliyunRequestEntity.getListsettingvalue();
        accessKeyId = listsettingvalue.get(0).getSettingvalue();
        accessKeySecret = listsettingvalue.get(1).getSettingvalue();
        endpoint = "https://" + listsettingvalue.get(2).getSettingvalue() + ".aliyuncs.com";
        testBucket = listsettingvalue.get(3).getSettingvalue();
        setOss();
    }

    public void setOss() {
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(accessKeyId, accessKeySecret);
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        OSSLog.enableLog();
        oss = new OSSClient(this, endpoint, credentialProvider, conf);
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        String chat_type=datas.get(position).getChatType();
        ExtEntity ext = datas.get(position).getExt();
        System.out.println("聊天Type"+ext);
        if("IMAGE".equals(chat_type)&&ext== null){//图片
            String url=datas.get(position).getTxt();
            Intent intent=new Intent(FriendChatActivity.this,ChatImageDetailActivity.class);
            intent.putExtra("url",url);
            startActivity(intent);
        }else {//视频
            String video_path=datas.get(position).getVideoPath();
            System.out.println("聊天video_path "+video_path);
            if(video_path!=null&&FileUtil.fileIsExists(video_path)){
                    Intent intent = new Intent(FriendChatActivity.this, VideoGraffitiActivity.class);
                    intent.putExtra("path",video_path);
                    startActivity(intent);
            }else {
                //下载视频
                RelativeLayout rl_img = (RelativeLayout) view;
                final TextView tv_progress = (TextView) rl_img.findViewById(R.id.tv_progress);
                ImageView iv_play = (ImageView) rl_img.findViewById(R.id.iv_play);
                iv_play.setVisibility(View.GONE);
                tv_progress.setVisibility(View.VISIBLE);
                tv_progress.setText("0%");

                String fileMD5;
                String textMD5 = null;
                String zipMD5 = ext.getZipMD5();
                if (zipMD5 != null && !zipMD5.isEmpty()) {
                    fileMD5 = ext.getVideoMD5();
                    textMD5 = zipMD5;
                } else {
                    fileMD5 = ext.getVideoMD5();
                }
                final String tMD5 = textMD5;
                final String tFolder = ext.getZipFolderPath();

                downloadObject = ext.getVideoFolderPath() + "/" + fileMD5 + ".mp4";
                final Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String fileDirs = Environment.getExternalStorageDirectory().getPath() + "/golf/download/chat/";
                        String filename = System.currentTimeMillis() + ".mp4";
                        Log.d(TAG, "run: --->" + filename);
                        GetObjectSamples getObjectSamples = new GetObjectSamples(oss, testBucket, downloadObject, fileDirs, filename, new ProgressBar(FriendChatActivity.this));
                        getObjectSamples.setOnProgressListener(new GetObjectSamples.OnProgressListener() {
                            @Override
                            public void onProgress(long sum, long current) {
                                final long ss = sum;
                                final long cu = current;
                                //更新百分比进度
                                tv_progress.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (cu != 0) {
                                            float num = (float) cu / ss;
                                            DecimalFormat df = new DecimalFormat("0.00");//格式化小数，.后跟几个零代表几位小数
                                            String s = df.format(num);//返回的是String类型
                                            Float aFloat = Float.valueOf(s);
                                            float v = aFloat * 100;
                                            int progess = (int) v;
                                            if (tMD5 != null) {
                                                if (progess > 99) {
                                                    progess = 99;
                                                }
                                            }
                                            tv_progress.setText(progess + "%");
                                        }
                                    }
                                });
                            }
                        });
                        getObjectSamples.setOnDownSuccessListener(new GetObjectSamples.OnDownSuccessListener() {
                            @Override
                            public void onDownComplete(String path) {
                                Log.d(TAG, "onDownComplete: 视频--->下载完成" + path);
                                if (tMD5 != null) {
                                    downLoadText(tMD5, tFolder, path, tv_progress,position);
                                } else {
                                    System.out.println("path:"+path);
                                    datas.get(position).setVideoPath(path);
                                    chatListAdapter.notifyDataSetChanged();
                                    Intent intent = new Intent(FriendChatActivity.this, VideoGraffitiActivity.class);
                                    intent.putExtra("path", path);
                                    startActivity(intent);
                                }
                            }
                        });
                        getObjectSamples.asyncGetObjectSample();
                    }
                });
                final String progress=tv_progress.getText().toString();
                System.out.println("video_path= "+video_path);
                    AliyunRequestEntity aliyunOSS = RequestAPI.getAliyunOSS();
                    if (aliyunOSS != null) {
                        setKeyId(aliyunOSS);
                        thread.start();
                    } else {
                        Log.d(TAG, "getOssSucceed: ---->为空");
                        OkGoRequest.getOkGoRequest().setOnGetOssListener(new OkGoRequest.OnGetOssListener() {
                            @Override
                            public void getOssSucceed(AliyunRequestEntity aliyunRequestEntity) {
                                setKeyId(aliyunRequestEntity);
                                thread.start();
                            }
                        }).getAliyunOSS(FriendChatActivity.this);
                }
            }
        }
    }
    /**
     * todo 下载TXT文件
     * @param textMD5
     * @param textFolder
     * @param pathMp4
     * @param tv_progress
     */
    private void downLoadText(String textMD5, String textFolder, final String pathMp4, final TextView tv_progress, final int mPosition) {
        if (textMD5 != null) {
            downloadObject = textFolder + "/" + textMD5 + ".zip";
            String fileDirs = Environment.getExternalStorageDirectory().getPath() + "/golf/download/chat/";
            String filename = System.currentTimeMillis() + ".zip";
            GetObjectSamples getObjectSamples = new GetObjectSamples(oss, testBucket, downloadObject, fileDirs, filename, new ProgressBar(FriendChatActivity.this));
            getObjectSamples.setOnProgressListener(new GetObjectSamples.OnProgressListener() {
                @Override
                public void onProgress(long sum, long current) {

                }
            });
            getObjectSamples.setOnDownSuccessListener(new GetObjectSamples.OnDownSuccessListener() {
                @Override
                public void onDownComplete(String path) {
                    try {
                        tv_progress.post(new Runnable() {
                            @Override
                            public void run() {
                                tv_progress.setText("100%");
                            }
                        });
                        Intent intent = new Intent(FriendChatActivity.this, VideoGraffitiActivity.class);
                        intent.putExtra("path", pathMp4);
                        intent.putExtra("type", "2");
                        intent.putExtra("zippath", path);
                        startActivity(intent);
                        System.out.println("path  "+pathMp4);
                        System.out.println("zippath  "+path);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            getObjectSamples.asyncGetObjectSample();
        }
    }
    /**
     * TODO 自动轮播类
     * 自动轮播需要的类
     */
    public class LocalImageHolderView implements Holder<FaceEntity> {
        private GridView gv;
        private FaceGridAdapter faceGridAdapter;

        @Override
        public View createView(Context context) {
            gv = new GridView(context);
            gv.setNumColumns(8);
            gv.setGravity(Gravity.CENTER);
            faceGridAdapter = new FaceGridAdapter(context);
            gv.setAdapter(faceGridAdapter);
            return gv;
        }

        @Override
        public void UpdateUI(Context context, int position, final FaceEntity data) {
            List<Integer> faces = data.getFaces();
            final List<String> datas = new ArrayList<>();
            for (int i = 0; i < faces.size(); i++) {
                if (faces.get(i) != -1) {
                    String emojiStringByUnicode = getEmojiStringByUnicode(faces.get(i));
                    Log.d(TAG, "编码UpdateUI: --->" + emojiStringByUnicode);
                    datas.add(emojiStringByUnicode);
                } else {
                    datas.add("-1");
                }
            }
            faceGridAdapter.setDatas(datas);
            gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //获得输入框光标的位置
                    int selectionEnd = et_input.getSelectionEnd();
                    if (position < 23) {
                        //插入表情到指定的位置
                        et_input.getText().insert(selectionEnd, datas.get(position));
                    } else {
                        int selectionStart = et_input.getSelectionStart();
                        if (selectionStart >= 1) {
                            et_input.getText().delete(selectionStart - 1, selectionStart);
                        }
                    }
                }
            });
        }
    }


    private String getEmojiStringByUnicode(int unicode) {
        return new String(Character.toChars(unicode));
    }

    /**
     * TODO 按钮监听
     *
     * @param view
     */
    @OnClick({R.id.back, R.id.iv_face, R.id.btn_other, R.id.tv_profile})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.back:
                //返回
                finish();
                break;
            case R.id.iv_face:
                //表情
                isMore = true;
                addEmojiToTextView();
                if (!isHide) {
                    isHide = true;
                    startBottomAnimator();
                }
                break;
            case R.id.btn_other:
                //其他功能
                isMore = true;
                addLayoutMore();
                if (!isHide) {
                    isHide = true;
                    startBottomAnimator();
                }
                break;
            case R.id.tv_profile:
                //好友资料
                reQuestFriendDetail();
                break;
        }
    }

    /**
     * 请求好友详细资料并打开页面
     */
    private void reQuestFriendDetail() {
        showLoading();
        String requestJson = RequestAPI. QueryFriend(this, phoneNumber);
        Log.i(TAG, "reQuestFriendDetail: " + requestJson);
        OkGoRequest.getOkGoRequest().setOnOkGoUtilListener(new OkGoRequest.OnOkGoUtilListener() {
            @Override
            public void parseDatas(String json) {
                dismissLoading();
                try {
                    JSONObject jo = new JSONObject(json);
                    String statuscode = jo.getString("statuscode");
                    String statusmessage = jo.getString("statusmessage");
                    if (statuscode.equals("1")) {
                        FriendsProfileEntity friendsProfileEntity = new Gson().fromJson(jo.toString(), FriendsProfileEntity.class);
                        FriendsProfileEntity.UserBean user = friendsProfileEntity.getUser();
                        if (user != null) {
                            Map<String, FriendsProfileEntity.UserBean> map = new HashMap<>();
                            map.put("FriendProfileActivity", user);
                            EventBus.getDefault().postSticky(map);
                            Intent intent = new Intent(FriendChatActivity.this, FriendProfileActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(FriendChatActivity.this, "搜索不到此用户", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(FriendChatActivity.this, "搜索不到此用户", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void requestFailed() {
                dismissLoading();
                Toast.makeText(FriendChatActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
            }
        }).getEntityData(this, ConstantsURL.QueryFriend, requestJson);
    }

    /**
     * TODO 更多
     */
    private void addLayoutMore() {
        ll_more.removeAllViews();
        View more = LayoutInflater.from(this).inflate(R.layout.item_chat_bottom_more, null);
        ImageView iv_photo = (ImageView) more.findViewById(R.id.iv_photo);
        ImageView iv_graph = (ImageView) more.findViewById(R.id.iv_graph);
        ImageView iv_video = (ImageView) more.findViewById(R.id.iv_video);
        //发送照片
        iv_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    intoPhotoAlbum();
            }
        });

        //拍照发送
        iv_graph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCameraPermission();
//                takeCameraSend();
            }
        });

        //发送视频
        iv_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                sendVideo();
                startActivityForResult(new Intent(FriendChatActivity.this, ChooseVideoActivity.class), 0x056);
            }
        });
        ll_more.addView(more);
    }
    private String mPublicPhotoPath;
    private String path;
    private Uri uri;

    /**
     * TODO 拍照
     */
    private void startTake() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //判断是否有相机应用
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            //创建临时图片文件
            File photoFile = null;
            try {
                photoFile = createPublicImageFile();
            //设置Action为拍照
            if (photoFile != null) {
                takePictureIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                //这里加入flag
                takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri photoURI = FileProvider.getUriForFile(this, FriendChatActivity.this.getPackageName() + ".fileprovider", photoFile);
                List<ResolveInfo> resInfoList= getPackageManager().queryIntentActivities(takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo resolveInfo : resInfoList) {
                    String packageName = FriendChatActivity.this.getPackageName();
                    grantUriPermission(packageName, photoURI,
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, 0x864);
            }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("拍照error："+e.toString());
            }
        }
        //将照片添加到相册中
        ImageUtil.galleryAddPic(mPublicPhotoPath, this);
    }
    /**
     * TODO 创建临时图片文件
     *
     * @return
     * @throws IOException
     */
    public  File createPublicImageFile() throws IOException {
        File path = null;
        if (ImageUtil.hasSdcard()) {
            path = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM);
        }
        Date date = new Date();
        String timeStamp = TimeUtil.getTimelocale(date,"yyyyMMdd_HHmmss", Locale.CHINA);
        String imageFileName = "Camera/" + "IMG_" + timeStamp + ".jpg";
        File image = new File(path, imageFileName);
        mPublicPhotoPath = image.getAbsolutePath();
        return image;
    }
    //发送视频
    private void sendVideo() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, 0x056);
    }

    //TODO 从相册里选择图片
    private void intoPhotoAlbum() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, 0x158);
    }

    /**
     * TODO 跳转回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 0x158://相册
                    if (data == null) return;
                    uri = data.getData();
                    sendImage(uri,1);

                    break;
                case 0x056:
                    if (data != null) {
                        try {
                            String path = null;
                            String title = null;
                            String type = null;
                            String zippath = null;
                            int id = data.getIntExtra("id", -1);

                            if (id != -1) {
                                Cursor cursor = sqLiteDatabase.rawQuery("select * from videoModel where id = ?", new String[]{id + ""});
                                if (cursor != null) {
                                    while (cursor.moveToNext()) {
                                        title = cursor.getString(cursor.getColumnIndex("title"));
                                        path = cursor.getString(cursor.getColumnIndex("path"));
                                        type = cursor.getString(cursor.getColumnIndex("type"));
                                        zippath = cursor.getString(cursor.getColumnIndex("zippath"));
                                    }
                                }
                            }
                            Log.d(TAG, "onActivityResult: --->获得id" + id + " " + path + " " + zippath);
                            //获取第一帧
                            MediaMetadataRetriever media = new MediaMetadataRetriever();
                            media.setDataSource(path);
                            Bitmap bitmap = media.getFrameAtTime(1);
                            long timeMillis = System.currentTimeMillis();
                            String sdCardPath = ImageUtil.getSDCardPath();
                            File file = ImageUtil.saveFile(bitmap, sdCardPath, timeMillis + ".jpg");
                            String imgPath = file.getPath();
                            ChatEntity chatEntity = new ChatEntity();
                            chatEntity.setUsername(SharedUtil.getString(Constants.PHONE_NUMBER));
                            String chatTime = TimeUtil.getChatTime(System.currentTimeMillis());
                            boolean isExist = true;
                            for (int i = 0; i < saveTime.size(); i++) {
                                if (chatTime.equals(chatTime)) {
                                    isExist = false;
                                    break;
                                }
                            }

                            if (isExist) {
                                saveTime.add(chatTime);
                                ChatEntity timeEntity = new ChatEntity();
                                timeEntity.setChatTime(chatTime);
                                datas.add(timeEntity);
                            }
                            chatEntity.setChatType("IMAGE");
                            chatEntity.setTxt(imgPath);
                            ExtEntity extEntity = new ExtEntity();
                            extEntity.setVideoMD5(path);
                            String userid = SharedUtil.getString(Constants.USER_ID);
                            String replaceUserId = userid.replace("/", "");
                            extEntity.setVideoFolderPath(replaceUserId + "/videos");
                            if (type != null && type.equals("2")) {
                                extEntity.setZipMD5(zippath);
                                extEntity.setZipFolderPath(replaceUserId + "/videos");
                            }
                            chatEntity.setExt(extEntity);
                            chatEntity.setUpState(1);
                            datas.add(chatEntity);
                            chatListAdapter.setDatas(datas);
                        } catch (Exception e) {
                            e.getStackTrace();
                        }
                    }
                    break;
                case 0x864://拍照
                    if (resultCode != Activity.RESULT_OK) return;
                    uri = Uri.parse(mPublicPhotoPath);
                    sendImage(uri,2);
                    break;
            }
        }

    }
    /**
     * TODO 发送图片
     *
     * @param uri
     * @param type 1相册获取 图片 2拍照
     */
    private void sendImage(Uri uri,int type) {
        if(type==1){
            int sdkVersion = Integer.valueOf(Build.VERSION.SDK);
            if (sdkVersion >= 19) {  // 或者 android.os.Build.VERSION_CODES.KITKAT这个常量的值是19
                path = this.uri.getPath();//5.0直接返回的是图片路径 Uri.getPath is ：  /document/image:46 ，5.0以下是一个和数据库有关的索引值
                // path_above19:/storage/emulated/0/girl.jpg 这里才是获取的图片的真实路径
                path =ImageUtil.getPath_above19(this, this.uri);
            } else {
                path = ImageUtil.getFilePath_below19(this, this.uri);
            }
        }else {
           path = uri.getPath();
        }
        //imagePath为图片本地路径，false为不发送原图（默认超过100k的图片会压缩后发给对方），需要发送原图传true
        System.out.println("图片path:"+path);
        EMMessage imageSendMessage = EMMessage.createImageSendMessage(path, false, phoneNumber);
        //如果是群聊，设置chattype，默认是单聊
        EMClient.getInstance().chatManager().sendMessage(imageSendMessage);
        imageSendMessage.setMessageStatusCallback(new EMCallBack() {
            @Override
            public void onSuccess() {
                // 消息发送成功，打印下日志，正常操作应该去刷新ui
                Log.i("lzan13", "send message on success");
                System.out.println("发送图片成功：");
                mHandler.sendEmptyMessage(0x002);
            }

            @Override
            public void onError(int i, String s) {
                // 消息发送失败，打印下失败的信息，正常操作应该去刷新ui
                System.out.println("发送图片失败：");
                Log.i("lzan13", "send message on error " + i + " - " + s);
            }

            @Override
            public void onProgress(int i, String s) {
                // 消息发送进度，一般只有在发送图片和文件等消息才会有回调，txt不回调
            }
        });
    }
    /**
     * todo 开始底部设置
     */
    private void startBottomAnimator() {
        InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputmanger.hideSoftInputFromWindow(et_input.getWindowToken(), 0);
        int height = ll_more.getHeight();
        int startHeight, endHeight;
        if (!flag) {
            startHeight = -height;
            endHeight = 0;
            flag = true;
        } else {
            startHeight = 0;
            endHeight = -height;
            flag = false;
        }

        ValueAnimator valueAnimator = ValueAnimator.ofInt(startHeight, endHeight);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int margin = (int) animation.getAnimatedValue();
                //设置控件的marginbottom
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ll_more.getLayoutParams();
                layoutParams.bottomMargin = margin;
                ll_more.setLayoutParams(layoutParams);
                if (!flag) {
                    Log.d("anim", "onAnimationEnd: 表情设置为全屏" + (1558 + margin));
                    RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) chat_refresh.getLayoutParams();
                    layoutParams2.width = RelativeLayout.LayoutParams.MATCH_PARENT;
                    layoutParams2.height = bottomPx - margin;
                    chat_refresh.setLayoutParams(layoutParams2);
                    lv_chat.setOverScrollMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                    lv_chat.setSelection(chatListAdapter.getCount() - 1);
                } else {
                    Log.d("anim", "onAnimationEnd: 表情设置为缩小" + (1558 + margin));
                    RelativeLayout.LayoutParams layoutParams3 = (RelativeLayout.LayoutParams) chat_refresh.getLayoutParams();
                    layoutParams3.width = RelativeLayout.LayoutParams.MATCH_PARENT;
                    layoutParams3.height = bottomPx - margin;
                    chat_refresh.setLayoutParams(layoutParams3);
                    lv_chat.setOverScrollMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                    lv_chat.setSelection(chatListAdapter.getCount() - 1);
                }
            }
        });
        valueAnimator.setDuration(250);
        valueAnimator.start();
    }

    /**
     * todo 开始底部编辑
     * @param softHeight
     */
    private void startBottomEditAnimator(int softHeight) {
        int height = softHeight;
        int startHeight, endHeight;
        if (!flagEdit) {
            startHeight = 0;
            endHeight = height;
            flagEdit = true;
        } else {
            startHeight = height;
            endHeight = 0;
            flagEdit = false;
        }
        ValueAnimator valueAnimator = ValueAnimator.ofInt(startHeight, endHeight);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int margin = (int) animation.getAnimatedValue();
                Log.i(TAG, "onAnimationUpdate: " + margin);
                //设置控件的marginbottom
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ll_input.getLayoutParams();
                if (flagEdit) {
                    layoutParams.bottomMargin = margin - bottomStatePx;
                    Log.i(TAG, "onAnimationUpdate: true" + flagEdit + layoutParams.bottomMargin);
                } else {
                    layoutParams.bottomMargin = margin;
                    Log.i(TAG, "onAnimationUpdate: false" + margin);
                }
                ll_input.setLayoutParams(layoutParams);

                if (!flagEdit) {
                    RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) chat_refresh.getLayoutParams();
                    layoutParams2.width = RelativeLayout.LayoutParams.MATCH_PARENT;
                    layoutParams2.height = maxPx - margin + bottomStatePx;
                    chat_refresh.setLayoutParams(layoutParams2);
                    lv_chat.setOverScrollMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                    lv_chat.setSelection(chatListAdapter.getCount() - 1);
                } else {
                    RelativeLayout.LayoutParams layoutParams3 = (RelativeLayout.LayoutParams) chat_refresh.getLayoutParams();
                    layoutParams3.width = RelativeLayout.LayoutParams.MATCH_PARENT;
                    layoutParams3.height = maxPx - margin + bottomStatePx;
                    chat_refresh.setLayoutParams(layoutParams3);
                    lv_chat.setOverScrollMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                    lv_chat.setSelection(chatListAdapter.getCount() - 1);
                }
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isEnd = false;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.d(TAG, "onAnimationEnd: 结束了");

                isEnd = true;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        valueAnimator.setDuration(250);
        valueAnimator.start();
    }

    /**
     * TODO 发送环信消息
     * @param content
     * @param toChatUsername
     */
    private void sendHuanxinMessage(final String content, String toChatUsername) {
        //创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
        EMMessage message = EMMessage.createTxtSendMessage(content, toChatUsername);
        //发送消息
        EMClient.getInstance().chatManager().sendMessage(message);
        message.setMessageStatusCallback(new EMCallBack() {
            @Override
            public void onSuccess() {
                // 消息发送成功，打印下日志，正常操作应该去刷新ui
                System.out.println("发送消息成功："+content);
                mHandler.sendEmptyMessage(0x002);
            }

            @Override
            public void onError(int i, String s) {
                // 消息发送失败，打印下失败的信息，正常操作应该去刷新ui
                System.out.println("发送消息失败："+s);
                Log.i("lzan13", "send message on error " + i + " - " + s);
            }

            @Override
            public void onProgress(int i, String s) {
                // 消息发送进度，一般只有在发送图片和文件等消息才会有回调，txt不回调
            }
        });
    }

    /**
     * todo 获得消息
     */
    private void getMessageed() {
        //获取此会话的所有消息
        //SDK初始化加载的聊天记录为20条，到顶时需要去DB里获取更多
        //获取startMsgId之前的pagesize条消息，此方法获取的messages SDK会自动存入到此会话中，APP中无需再次把获取到的messages添加到会话中
        saveTime = new ArrayList<>();
        String msgId = mConversation.getLastMessage().getMsgId();
        datas = new ArrayList<>();
        List<EMMessage> msg = mConversation.loadMoreMsgFromDB(msgId, 20);
        msg.add(mConversation.getLastMessage());
        for (int i = 0; i < msg.size(); i++) {
            if (i == 0) {
                headMsgId = msg.get(i).getMsgId();
            }
            isExist = true;
            ChatEntity chatEntity = new ChatEntity();
            String type = msg.get(i).getType().toString();
            String text = msg.get(i).getBody().toString();
            String chatTime = TimeUtil.getChatTime(msg.get(i).getMsgTime());
            if (saveTime != null) {
                for (int j = 0; j < saveTime.size(); j++) {
                    if (saveTime.get(j).equals(chatTime)) {
                        isExist = false;
                        break;
                    }
                }
            }
            if (isExist) {
                saveTime.add(chatTime);
                ChatEntity timeEntity = new ChatEntity();
                timeEntity.setChatTime(chatTime);
                datas.add(timeEntity);
            }

            Log.d(TAG, "getMessageed: --->body" + msg.get(i).getBody().toString());
            chatEntity.setChatType(type);
            switch (type) {
                case "TXT":
                    int position = text.indexOf("\"");
                    String subText = text.substring(position + 1, text.length() - 1);
                    chatEntity.setTxt(subText);

                    EMMessage message = msg.get(i);
                    Map<String, Object> ext1 = message.ext();
                    if (ext1 != null) {
                        String textType = (String) ext1.get("type");
                        String json = (String) ext1.get("entityExtMsg");
                        Log.d("hyjsystem", "getMessageed: 这是截取到的消息JSON--->" + textType + " " + json);
                        if (textType != null) {
                            SystemExtMessageEntity systemExtMessageEntity = new Gson().fromJson(json.toString(), SystemExtMessageEntity.class);
                            chatEntity.setTextType(textType);
                            chatEntity.setSystemExtMessageEntity(systemExtMessageEntity);
                        }
                    }
                    break;
                case "IMAGE":
                    EMMessage emMessage = msg.get(i);
                    Map<String, Object> ext = emMessage.ext();
                    if (ext != null && !ext.isEmpty()) {
                        String emVideoExt = (String) ext.get("EMVideoExt");
                        Log.d("ext", "getMessageed: ---->" + msg.get(i).getFrom() + "  ----  " + emVideoExt);
                        try {
                            JSONObject jo = new JSONObject(emVideoExt);
                            ExtEntity extEntity = new Gson().fromJson(jo.toString(), ExtEntity.class);
                            chatEntity.setExt(extEntity);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    EMImageMessageBody body = (EMImageMessageBody) msg.get(i).getBody();
                    String str1 = body.getRemoteUrl();
                    chatEntity.setTxt(str1);
                    break;
                case "VOICE":
                    EMVoiceMessageBody voiceBody = (EMVoiceMessageBody) msg.get(i).getBody();
                    String voiceUrl = voiceBody.getRemoteUrl();
                    int length = voiceBody.getLength();
                    chatEntity.setLength(length);
                    chatEntity.setTxt(voiceUrl);
                    chatEntity.setVoicePath(voiceUrl);
                    break;
            }
            chatEntity.setTime(TimeUtil.getWeek(new Date(msg.get(i).getMsgTime())));
            chatEntity.setUsername(msg.get(i).getFrom());
            datas.add(chatEntity);
        }
        chatListAdapter.setDatas(datas);

        lv_chat.setSelection(datas.size() - 1);

        if (sharevideo != null) {
            sendCloudVideo();
        }
    }

    /**
     * TODO 增加消息头部
     */
    private void addMessageToHeader() {
        //获取此会话的所有消息
        //SDK初始化加载的聊天记录为20条，到顶时需要去DB里获取更多
        //获取startMsgId之前的pagesize条消息，此方法获取的messages SDK会自动存入到此会话中，APP中无需再次把获取到的messages添加到会话中
        List<ChatEntity> headDatas = new ArrayList<>();
        List<EMMessage> msg = mConversation.loadMoreMsgFromDB(headMsgId, 20);
        for (int i = 0; i < msg.size(); i++) {
            if (i == 0) {
                headMsgId = msg.get(i).getMsgId();
            }
            isExist = true;
            ChatEntity chatEntity = new ChatEntity();
            String type = msg.get(i).getType().toString();
            String text = msg.get(i).getBody().toString();
            String chatTime = TimeUtil.getChatTime(msg.get(i).getMsgTime());
            if (saveTime != null) {
                for (int j = 0; j < saveTime.size(); j++) {
                    if (saveTime.get(j).equals(chatTime)) {
                        isExist = false;
                        break;
                    }
                }
            }
            if (isExist) {
                saveTime.add(chatTime);
                ChatEntity timeEntity = new ChatEntity();
                timeEntity.setChatTime(chatTime);
                headDatas.add(timeEntity);
            }
            System.out.println("增加 "+type);
            chatEntity.setChatType(type);
            switch (type) {
                case "TXT":
                    int position = text.indexOf("\"");
                    String subText = text.substring(position + 1, text.length() - 1);
                    chatEntity.setTxt(subText);
                    break;
                case "IMAGE":
                    EMMessage emMessage = msg.get(i);
                    Map<String, Object> ext = emMessage.ext();
                    if (ext != null && !ext.isEmpty()) {
                        String emVideoExt = (String) ext.get("EMVideoExt");
                        try {
                            JSONObject jo = new JSONObject(emVideoExt);
                            ExtEntity extEntity = new Gson().fromJson(jo.toString(), ExtEntity.class);
                            chatEntity.setExt(extEntity);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    EMImageMessageBody body = (EMImageMessageBody) msg.get(i).getBody();
                    String str1 = body.getRemoteUrl();
                    chatEntity.setTxt(str1);
                    break;
                case "VOICE":
                    EMVoiceMessageBody voiceBody = (EMVoiceMessageBody) msg.get(i).getBody();
                    String voiceUrl = voiceBody.getRemoteUrl();
                    int length = voiceBody.getLength();
                    chatEntity.setLength(length);
                    chatEntity.setTxt(voiceUrl);
                    chatEntity.setVoicePath(voiceUrl);
                    break;
            }
            chatEntity.setTime(TimeUtil.getWeek(new Date(msg.get(i).getMsgTime())));
            chatEntity.setUsername(msg.get(i).getFrom());
            headDatas.add(chatEntity);
        }
        headDatas.addAll(datas);
        datas = headDatas;
        chatListAdapter.setDatas(headDatas);
    }

    /**
     * todo 计算高
     */
    private void calculateHeight() {
        maxPx = ll_input.getTop();
        bottomPx = maxPx - ll_more.getHeight();
        bottomStatePx = ScreenUtils.getBottomStatusHeight(this);
        Log.d(TAG, "calculateHeight: 这是一个计算状态栏" + +ScreenUtils.getDpi(this) + " -  " + ScreenUtils.getScreenHeight(this) + " = " + ScreenUtils.getBottomStatusHeight(this) + "  ");
        Log.d(TAG, "calculateHeight: --->" + ll_more.getHeight() + " " + ll_input.getLeft() + "要这个 " + ll_input.getTop() + " " + ll_input.getRight() + " " + ll_input.getBottom());
        Log.d(TAG, "calculateHeight: --->" + chat_refresh.getLeft() + " " + chat_refresh.getTop() + " " + chat_refresh.getRight() + " " + chat_refresh.getBottom());
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void sendVideo(ChatEntity chatEntity) {
        sendNativeVideo(chatEntity);
    }

    /**
     * TODO 发送本地视频
     * @param chatEntity
     */
    private void sendNativeVideo(final ChatEntity chatEntity) {
        //imagePath为图片本地路径，false为不发送原图（默认超过100k的图片会压缩后发给对方），需要发送原图传true
        EMMessage imageSendMessage = EMMessage.createImageSendMessage(chatEntity.getTxt(), false, phoneNumber);
        Map<String, String> ext = new HashMap<>();
        ext.put("EMVideoExt", chatEntity.getVideoPath());
        ExtEntity ext1 = chatEntity.getExt();
        String zipMD5 = ext1.getZipMD5();
        String videoMD5 = ext1.getVideoMD5();
        if (zipMD5 != null) {
            ext1.setVideoMD5(videoMD5 + "_" + zipMD5);
        } else {
            ext1.setVideoMD5(videoMD5);
        }
        String extJson = new Gson().toJson(ext1);
        Log.d(TAG, "sendNativeVideo: --->JSON" + extJson);
        imageSendMessage.setAttribute("EMVideoExt", extJson);
        Log.d(TAG, "sendVideo: --->得到EXT" + imageSendMessage.ext());
        //如果是群聊，设置chattype，默认是单聊
        EMClient.getInstance().chatManager().sendMessage(imageSendMessage);
        imageSendMessage.setMessageStatusCallback(new EMCallBack() {
            @Override
            public void onSuccess() {
                // 消息发送成功，打印下日志，正常操作应该去刷新ui
                Log.i("lzan13", "send message on success");
                Log.d(TAG, "onSuccess: ---->发送视频成功了" + chatEntity.getVideoPath());
                System.out.println("发送本地视频成功：");
//                mHandler.sendEmptyMessage(0x002);
            }

            @Override
            public void onError(int i, String s) {
                // 消息发送失败，打印下失败的信息，正常操作应该去刷新ui
                Log.i("lzan13", "send message on error " + i + " - " + s);
            }

            @Override
            public void onProgress(int i, String s) {
                // 消息发送进度，一般只有在发送图片和文件等消息才会有回调，txt不回调
            }
        });
    }

    /**
     * TODO 发送云视频
     */
    private void sendCloudVideo() {
        //获取第一帧
        Glide.with(this).load(sharevideo.getVideo_picpath()).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                try {
                    String picPath = ImageUtil.saveBitmap(resource, System.currentTimeMillis() + "");
                    sendShareVideo(picPath);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                Toast.makeText(FriendChatActivity.this, "分享失败", Toast.LENGTH_SHORT).show();
                super.onLoadFailed(e, errorDrawable);
            }
        });
    }

    /**
     * TODO 发送分享视频
     * @param picPath
     */
    private void sendShareVideo(String picPath) {
        String imgPath = picPath;
        Log.d(TAG, "sendCloudVideo: --->" + imgPath + " " + sharevideo.getVideo_filemd5());
        ChatEntity chatEntity = new ChatEntity();
        chatEntity.setUsername(SharedUtil.getString(Constants.PHONE_NUMBER));
        String chatTime = TimeUtil.getChatTime(System.currentTimeMillis());
        boolean isExist = true;
        if (saveTime != null) {
            for (int i = 0; i < saveTime.size(); i++) {
                if (chatTime.equals(chatTime)) {
                    isExist = false;
                    break;
                }
            }
        }

        if (isExist) {
            saveTime.add(chatTime);
            ChatEntity timeEntity = new ChatEntity();
            timeEntity.setChatTime(chatTime);
            datas.add(timeEntity);
        }
        chatEntity.setChatType("IMAGE");
        chatEntity.setTxt(imgPath);
        ExtEntity extEntity = new ExtEntity();
        String video_filemd5 = sharevideo.getVideo_filemd5();
        Log.d(TAG, "sendCloudVideo: --->测试" + video_filemd5);
        String userid = SharedUtil.getString(Constants.USER_ID);
        String replaceUserId = userid.replace("/", "");
        extEntity.setVideoFolderPath(replaceUserId + "/videos");
        if (!video_filemd5.contains("_")) {
            extEntity.setVideoMD5(video_filemd5);
        } else {
            String[] split = video_filemd5.split("_");
            extEntity.setVideoMD5(split[0]);
            extEntity.setZipMD5(split[1]);
            extEntity.setZipFolderPath(replaceUserId + "/videos");
        }
        chatEntity.setExt(extEntity);
        chatEntity.setUpState(2);
        datas.add(chatEntity);
        chatListAdapter.setDatas(datas);
        sendNativeVideo(chatEntity);
    }

    /**
     * TODO EvenBus处理
     * @param map
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEvent(Map<String, String> map) {
        if (map.containsKey("FriendChatActivity")) {
            if (map.get("FriendChatActivity").equals("close")) {
                finish();
            } else {
                String showRemark = map.get("FriendChatActivity");
                if (showRemark != null && !showRemark.isEmpty()) {
                    tv_username.setText(showRemark);
                }
            }
        }
    }
    /**
     * TODO 检查相机权限
     */
    private static final int REQUEST_CAMERA_ACCESS_PERMISSION = 1;
    private static final int REQUEST_WRITE_STORAGE_PERMISSION = 2;
    private void checkCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA_ACCESS_PERMISSION);
        } else {
            checkWriteStoragePermission();
        }
    }

    /**
     * TODO 检查写入SDK权限
     */
    private void checkWriteStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE_PERMISSION);
        } else {
//            takeCameraSend();
            startTake();
        }
    }
    /**
     * TODO 获取权限接口
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_ACCESS_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkWriteStoragePermission();
            } else {
                Toast.makeText(this, "CAMERA PERMISSION DENIED", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_WRITE_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                takeCameraSend();
                startTake();
            } else {
                Toast.makeText(this, "WRITE_EXTERNAL_STORAGE PERMISSION DENIED", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().chatManager().removeMessageListener(emMessageListener);
    }
}
