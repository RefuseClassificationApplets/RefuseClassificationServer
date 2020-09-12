import com.hsspace.hs.manage.Manage;

import java.sql.ResultSet;

/**
 * test2类
 * Git to： http://hs.mccspace.com:3000/Qing_ning/HSPasswordManagerServer/
 *
 * @TIME 2020/7/14 20:28
 * @AUTHOR 韩硕~
 */

public class test2 {

    public static void main(String[] args) {
        Manage.SqlSearch((ResultSet resultSet) -> {
            resultSet.next();
            System.out.println(resultSet.getString("id"));
        }, "SELECT * FROM `user`");
    }
}
