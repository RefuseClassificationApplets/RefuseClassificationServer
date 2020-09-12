package com.hsspace.hs.util;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * SqlRunIm类
 *
 * @TIME 2020/8/17
 * @AUTHOR 韩硕~
 */

public interface SqlRunIm {
    void run(ResultSet resultSet) throws SQLException;
}
