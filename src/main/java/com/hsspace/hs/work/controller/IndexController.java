package com.hsspace.hs.work.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hsspace.hs.manage.Manage;
import com.hsspace.hs.util.RandomStatic;
import com.hsspace.hs.util.Util;
import com.hsspace.hs.work.bean.ComplateBean;
import com.hsspace.hs.work.bean.LoginBean;
import com.hsspace.hs.work.framework.GetMapping;
import com.hsspace.hs.work.framework.PostMapping;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;

public class IndexController {

    @GetMapping("/getQuestion")
    public void getQuestion(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter pw = resp.getWriter();
        int qs_num = 1;
        try {
            switch (req.getParameter("level")) {
                case "1":
                    qs_num = 5;
                    break;
                case "2":
                    qs_num = 12;
                    break;
                case "3":
                    qs_num = 30;
            }
        } catch (NullPointerException e) {
            pw.write("{\"data\":[]}");
            pw.flush();
            return;
        }
        JSONObject qst = new JSONObject();
        JSONArray data = new JSONArray();
        qst.put("data", data);
        int finalQs_num = qs_num;
        Manage.SqlSearch((ResultSet rs) -> {
            while (rs.next()) {
                JSONObject que = new JSONObject();
                data.put(que);
                que.put("question", rs.getString("question"));
                JSONObject option = new JSONObject();
                String op;
                que.put("id", rs.getInt("id"));
                for (int i = 1; i < 7; i++) {
                    if (!(op = rs.getString("a" + i)).equals("")) {
                        option.put("" + ((char) ('A' + i - 1)), op);
                    }
                }
                que.put("option", option);
                if (rs.getString("type").equals("radio")) {
                    que.put("type", 1);
                    que.put("true", rs.getString("answer"));
                } else {
                    que.put("type", 2);
                    JSONArray as = new JSONArray();
                    for (String i : rs.getString("answer").split(","))
                        as.put(i);
                    que.put("true", as);
                }
                que.put("scores", 100 / finalQs_num);
                que.put("checked", false);
            }
        }, "SELECT * FROM question ORDER BY RAND() LIMIT ?", qs_num);
        pw.write(qst.toString());
        pw.flush();
    }

    @PostMapping("/complate")
    public void complate(ComplateBean bean, HttpServletRequest req) {
        Manage.SqlBatchPut("UPDATE question SET yes=yes+1 WHERE id = ?", bean.yes.toArray());
        Manage.SqlBatchPut("UPDATE question SET no=no+1 WHERE id = ?", bean.no.toArray());
        if (bean.login != null) {
            int score = 0;
            int yesnum = bean.yes.size();
            int nonum = bean.no.size();
            int nowgamenum = yesnum + nonum;
            switch (nowgamenum) {
                case 5:
                    if (yesnum == 5)
                        score = 2;
                    else
                        score = 1;
                    break;
                case 12:
                    if (yesnum > 10)
                        score = 4;
                    else
                        score = 2;
                    break;
                case 30:
                    if (yesnum > 26)
                        score = 8;
                    else
                        score = 5;
                    break;
            }
            Map<String, Object> map = Manage.login.get(bean.login);
            Manage.SqlPut("INSERT INTO user (user, avatarURL,win, gamenum, score, nickName, city, province, gender, country) VALUES (?,?, ?, ?, ?,?,?,?,?,?) ON DUPLICATE KEY UPDATE avatarURL=?, win=win+?, gamenum=gamenum+?, score=score+?, nickName=?;"
                    , map.get("openId"), map.get("avatarUrl"), yesnum, nowgamenum, score, map.get("nickName"), map.get("city"), map.get("province"), map.get("gender"), map.get("country"), map.get("avatarUrl"), yesnum, nowgamenum, score,map.get("nickName"));
            Manage.SqlPut("INSERT INTO playlog (user_id, time, yes, no, question_num, add_score) VALUES (?,?,?,?,?,?);"
                    , map.get("openId"), new Date(), bean.yes.toString(), bean.no.toString(), nowgamenum, score);
        }
    }

    @PostMapping("/login")
    public void login(LoginBean bean, HttpServletResponse resp) throws IOException {
        PrintWriter pw = resp.getWriter();
        Map a = Util.decodeUserInfo(bean.iv, bean.encryptedData, bean.code);
        if ((Integer) a.get("status") == 1) {
            JSONObject data = new JSONObject();
            data.put("status", 1);
            int login = RandomStatic.loginStatic.next();
            data.put("login", login);
            Manage.login.put(login, (Map) a.get("userInfo"));
            pw.write(data.toString());
        } else {
            pw.write("{\"status\":0}");
        }
        pw.flush();
    }

    @GetMapping("/getzz")
    public void getzz(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        JSONObject jsonObject = new JSONObject();
        Manage.SqlSearch((ResultSet rs) -> {
            if (rs.next()) {
                jsonObject.put("jf", rs.getInt("score"));
                jsonObject.put("cc", rs.getInt("gamenum"));
                NumberFormat nt = NumberFormat.getPercentInstance();
                nt.setMinimumFractionDigits(2);
                jsonObject.put("sl", nt.format(1.0 * rs.getInt("win") / rs.getInt("gamenum")));
            }
        }, "SELECT * FROM user WHERE user=?;", Manage.login.get(Integer.parseInt(req.getParameter("login"))).get("openId"));
        PrintWriter pw = resp.getWriter();
        pw.write(jsonObject.toString());
        pw.flush();
    }

    @GetMapping("/getWideBanner")
    public void getBigBanner(HttpServletResponse resp) throws IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data", new JSONArray());
        Manage.SqlSearch((ResultSet rs) -> {
            while (rs.next()) {
                jsonObject.getJSONArray("data").put(Manage.getProperties("address") + "/" + rs.getString("pkc_name"));
            }
        }, "SELECT * FROM banner WHERE type=?;", "widebanner");
        PrintWriter pw = resp.getWriter();
        pw.write(jsonObject.toString());
        pw.flush();
    }

    @GetMapping("/getHBanner")
    public void getHBanner(HttpServletResponse resp) throws IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data", new JSONArray());
        Manage.SqlSearch((ResultSet rs) -> {
            while (rs.next()) {
                jsonObject.getJSONArray("data").put(Manage.getProperties("address") + "/" + rs.getString("pkc_name"));
            }
        }, "SELECT * FROM banner WHERE type=?;", "hbanner");
        PrintWriter pw = resp.getWriter();
        pw.write(jsonObject.toString());
        pw.flush();
    }

    @GetMapping("/gettop")
    public void gettop(HttpServletResponse resp) throws IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data", new JSONArray());
        Manage.SqlSearch((ResultSet rs) -> {
            while (rs.next()) {
                JSONObject d = new JSONObject();
                d.put("tx", rs.getString("avatarURL"));
                d.put("js", rs.getInt("gamenum")+"次问答");
                d.put("nc", rs.getString("nickName"));
                d.put("jf", "积分："+rs.getInt("score"));
                NumberFormat nt = NumberFormat.getPercentInstance();
                nt.setMinimumFractionDigits(2);
                d.put("sl", nt.format(1.0 * rs.getInt("win") / rs.getInt("gamenum")));
                jsonObject.getJSONArray("data").put(d);
            }
        }, "SELECT * FROM user ORDER BY score DESC LIMIT ?;", Integer.parseInt(Manage.getProperties("top_num")));
        PrintWriter pw = resp.getWriter();
        pw.write(jsonObject.toString());
        pw.flush();
    }
}
