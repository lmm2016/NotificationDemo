package com.ziyun56.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.ziyun56.notification.utils.ChannelEntity;
import com.ziyun56.notification.utils.ImportanceType;
import com.ziyun56.notification.utils.NotifyManager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private NotifyManager notifyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notifyManager = new NotifyManager(this);

        ChannelEntity chatChannel = new ChannelEntity(Constants.CHANNEL_CHAT, "新聊天消息", ImportanceType.IMPORTANCE_HIGH);
        chatChannel.setDescription("个人或群组发来的聊天消息");
        notifyManager.createNotificationGroupWithChannel(Constants.GROUP_CHAT, "聊天消息", chatChannel);

        ArrayList<ChannelEntity> channelEntityArrayList = new ArrayList<>();
        ChannelEntity downloadCompleteChannel = new ChannelEntity(Constants.CHANNEL_DOWNLOAD_COMPLETE, "下载完成", ImportanceType.IMPORTANCE_LOW);
        downloadCompleteChannel.setDescription("下载完成后通知栏显示");
        channelEntityArrayList.add(downloadCompleteChannel);
        ChannelEntity downloadProgressChannel = new ChannelEntity(Constants.CHANNEL_DOWNLOAD_ERROR, "下载失败", ImportanceType.IMPORTANCE_DEFAULT);
        downloadProgressChannel.setDescription("下载出现问题，下载失败");
        channelEntityArrayList.add(downloadProgressChannel);
        notifyManager.createNotificationGroupWithChannel(Constants.GROUP_DOWNLOAD, "下载消息", channelEntityArrayList);

        ChannelEntity otherChannel = new ChannelEntity(Constants.CHANNEL_OTHER, "未分类", ImportanceType.IMPORTANCE_MIN);
        otherChannel.setShowBadge(false);
        notifyManager.createNotificationChannel(otherChannel);

    }

    public void notifyClick(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager)
                    getSystemService(Context.NOTIFICATION_SERVICE);
            //分组（可选）
            //groupId要唯一
            String groupId = "group_001";
            NotificationChannelGroup group = new NotificationChannelGroup(groupId, "广告");
            //创建group
            if(notificationManager != null) {
                notificationManager.createNotificationChannelGroup(group);
            }
            //channelId要唯一
            String channelId = "channel_001";
            NotificationChannel adChannel = new NotificationChannel(channelId,
                    "推广信息", NotificationManager.IMPORTANCE_DEFAULT);
            //补充channel的含义（可选）
            adChannel.setDescription("推广信息");
            //将渠道添加进组（先创建组才能添加）
            adChannel.setGroup(groupId);
            //创建channel
            if(notificationManager != null) {
                notificationManager.createNotificationChannel(adChannel);
            }
            //创建通知时，标记你的渠道id
            Notification notification = new Notification.Builder(MainActivity.this, channelId)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                    .setContentTitle("一条新通知")
                    .setContentText("这是一条测试消息")
                    .setAutoCancel(true)
                    .build();
            if(notificationManager != null) {
                notificationManager.notify(1, notification);
            }
        }
    }


    public void handleChat(View view) {
        if (!notifyManager.areNotificationsEnabled()) {
            Toast.makeText(this, "总通知被关闭", Toast.LENGTH_SHORT).show();
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!notifyManager.areChannelsEnabled(Constants.CHANNEL_CHAT)) {
                Toast.makeText(getApplicationContext(), "当前渠道通知被关闭", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        NotificationCompat.Builder builder = notifyManager.getDefaultBuilder(Constants.CHANNEL_CHAT);
        builder.setContentTitle("收到了Bill发来的消息");
        builder.setContentText("今天晚上需要加班吗？");
        builder.setNumber(1);
        Notification notification = builder.build();
        notifyManager.notifyNotify(notification);
    }

    public void handleCompete(View view) {
        NotificationCompat.Builder builder = notifyManager.getDefaultBuilder(Constants.CHANNEL_DOWNLOAD_COMPLETE);
        builder.setContentTitle("下载完成");
        builder.setContentText("下载完成，可在我的下载中查看");
        builder.setNumber(2);
        Notification notification = builder.build();
        notifyManager.notifyNotify(notification);
    }

    public void handleError(View view) {
        NotificationCompat.Builder builder = notifyManager.getDefaultBuilder(Constants.CHANNEL_DOWNLOAD_ERROR);
        builder.setContentTitle("下载失败");
        builder.setContentText("由于网络中断导致下载失败");
        builder.setNumber(3);
        Notification notification = builder.build();
        notifyManager.notifyNotify(notification);
    }

    public void handleOther(View view) {
        NotificationCompat.Builder builder = notifyManager.getDefaultBuilder(Constants.CHANNEL_OTHER);
        builder.setContentTitle("其他消息");
        builder.setContentText("系统通知消息");
        builder.setNumber(4);
        Notification notification = builder.build();
        notifyManager.notifyNotify(notification);
    }



}
