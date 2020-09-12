package com.hsspace.hs.manage;

import com.hsspace.hs.util.SqlRunIm;
import org.json.JSONObject;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Manage类
 * Git to： http://hs.mccspace.com:3000/Qing_ning/HSPasswordManagerServer/
 *
 * @TIME 2020/7/13 14:46
 * @AUTHOR 韩硕~
 */

public class Manage {

    public static Map<Integer,Map<String, Object>> login = new HashMap<>();

    public static Connection getConnection() {
        try {
            return JDBC.ENUM.getDataSource().getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public static void SqlBatchPut(String sql, Object[]... data) {
        try (Connection conn = Manage.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                // 对同一个PreparedStatement反复设置参数并调用addBatch():
                for (int i = 0 ; i<data[0].length;i++){
                    for(int j = 1; j < data.length + 1; j++){
                        ps.setObject(j, data[j-1][i]);
                    }
                    ps.addBatch(); //添加到JDBC batch
                }
                // 执行batch:
                int[] ns = ps.executeBatch();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void SqlPut(String sql, Object... data) {
        try (Connection conn = Manage.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                for (int i = 1; i < data.length + 1; i++) {
                    ps.setObject(i, data[i - 1]);
                }
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void SqlSearch(SqlRunIm sqlRunIm, String sql, Object... data) {
        try (Connection conn = Manage.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                for (int i = 1; i < data.length + 1; i++) {
                    ps.setObject(i, data[i - 1]);
                }
                try (ResultSet rs = ps.executeQuery()) {
                    sqlRunIm.run(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int fastTipNum(String sql, String... data) {
        try (Connection conn = Manage.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                for (int i = 1; i < data.length + 1; i++) {
                    ps.setObject(i, data[i - 1]);
                }
                try (ResultSet rs = ps.executeQuery()) {
                    rs.next();
                    return rs.getInt("num");
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }

    public static void sendMail(String address, String title, String content) {
        try {
            MimeMessage message = new MimeMessage(Email.getSession());
            message.setFrom(new InternetAddress("1750359613@qq.com"));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(address));
            message.setSubject(title, "UTF-8");
            message.setText(content, "UTF-8");
            Transport.send(message);
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public static String getProperties(String key) {
        return Config.ENUM.getProperties(key);
    }

    public static String addZeroForNum(String str, int strLength) {
        int strLen = str.length();
        StringBuffer sb = null;
        while (strLen < strLength) {
            sb = new StringBuffer();
            sb.append("0").append(str);// 左补0
            // sb.append(str).append("0");//右补0
            str = sb.toString();
            strLen = str.length();
        }
        return str;
    }

}
