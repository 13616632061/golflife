package com.glorystudent.golflife.activity;

import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.glorystudent.golflibrary.base.BaseActivity;
import com.glorystudent.golflife.R;
import com.glorystudent.golflife.adapter.ChatListAdapter;
import com.glorystudent.golflife.customView.ChatPullToRefreshLayout;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMConversation;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.Bind;

/**
 * TODO 好友聊天页面
 */
public class FriendChatActivity extends BaseActivity {

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
    private AliyunRequestEntity aliyunRequestEntity;
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
}
