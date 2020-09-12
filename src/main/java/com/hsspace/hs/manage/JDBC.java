package com.hsspace.hs.manage;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

/**
 * JDBC类
 * Git to： http://hs.mccspace.com:3000/Qing_ning/HSPasswordManagerServer/
 *
 * @TIME 2020/7/13 14:46
 * @AUTHOR 韩硕~
 */

public enum JDBC {

    ENUM;

    private DataSource ds;

    public DataSource getDataSource() {
        if(ds == null){
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:mysql://localhost:3306/refusequestion?useSSL=false&characterEncoding=utf-8");
            config.setUsername(Manage.getProperties("sqluser"));
            config.setPassword(Manage.getProperties("sqlpsw"));
            config.addDataSourceProperty("connectionTimeout", "1000"); // 连接超时：1秒
            config.addDataSourceProperty("idleTimeout", "60000"); // 空闲超时：60秒
            config.addDataSourceProperty("maximumPoolSize", "20"); // 最大连接数：10
            return ds = new HikariDataSource(config);
        } else {
            return ds;
        }
    }

}
