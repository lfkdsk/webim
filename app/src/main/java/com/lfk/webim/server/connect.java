package com.lfk.webim.server;

import com.lfk.webim.appli.user;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;

public class connect {
    private static ConnectionConfiguration connConfig;
    public static XMPPConnection con;

    public static XMPPConnection getConnection() {
        if (con == null || !con.isConnected()) {
            openConnection();
        }
        return con;
    }
    public static boolean openConnection() {
        try {
            connConfig = new ConnectionConfiguration(user.My_Ip, 5222);
            // 设置登录状态为离线
            connConfig.setSendPresence(false);
            // 断网重连
            connConfig.setReconnectionAllowed(true);
            con = new XMPPConnection(connConfig);
            con.connect();
            return true;
        } catch (Exception e) {

        }
        return false;
    }
    public static void closeConnection() {
        con.disconnect();
    }
}
