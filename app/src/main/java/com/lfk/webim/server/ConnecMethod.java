package com.lfk.webim.server;

import android.util.Log;

import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Registration;

/**
 * Created by Administrator on 2015/4/7.
 */
public class ConnecMethod {

    public static boolean login(String account, String password) {
        try {
            if (connect.getConnection() == null)
                return false;
            /** 登录 */
            SASLAuthentication.supportSASLMechanism("PLAIN", 0);
            connect.getConnection().login(account, password);
            // 设置登录状态：在线
             Presence presence = new Presence(Presence.Type.available);
             connect.getConnection().sendPacket(presence);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public static String regist(String account, String password) {
        if (connect.getConnection() == null)
            return "0";
        Registration reg = new Registration();
        reg.setType(IQ.Type.SET);
        reg.setTo(connect.getConnection().getServiceName());
        reg.setUsername(account);// 注意这里createAccount注册时，参数是username，不是jid，是“@”前面的部分。
        reg.setPassword(password);
        reg.addAttribute("android", "geolo_createUser_android");// 这边addAttribute不能为空，否则出错。所以做个标志是android手机创建的吧！！！！！
        PacketFilter filter = new AndFilter(new PacketIDFilter(
                reg.getPacketID()), new PacketTypeFilter(IQ.class));
        PacketCollector collector = connect.getConnection()
                .createPacketCollector(filter);
        connect.getConnection().sendPacket(reg);
        IQ result = (IQ) collector.nextResult(SmackConfiguration
                .getPacketReplyTimeout());
        // Stop queuing results
        collector.cancel();// 停止请求results（是否成功的结果）
        if (result == null) {
            Log.e("RegistActivity", "No response from server.");
            return "no find on internet";
        } else if (result.getType() == IQ.Type.RESULT) {
            return "regist success";
        } else { // if (result.getType() == IQ.Type.ERROR)
            if (result.getError().toString().equalsIgnoreCase("conflict(409)")) {
                Log.e("RegistActivity", "IQ.Type.ERROR: "
                        + result.getError().toString());
                return "this account has existed";
            } else {
                Log.e("RegistActivity", "IQ.Type.ERROR: "
                        + result.getError().toString());
                return "regist failed";
            }
        }
    }
}
