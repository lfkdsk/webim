package com.lfk.webim.server;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;

/**
 * Created by Administrator on 2015/4/7.
 */
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
            connConfig = new ConnectionConfiguration("172.6.33.68", 5222);
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
