package com.hsspace.hs.manage;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Config类
 * Git to： http://hs.mccspace.com:3000/Qing_ning/HSPasswordManagerServer/
 *
 * @TIME 2020/7/13 15:13
 * @AUTHOR 韩硕~
 */

public enum  Config {

   ENUM;

   private Properties properties = new Properties();

   Config(){
      try {
         properties.load(new FileInputStream("src/main/resources/config.properties"));
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   public String getProperties(String key){
      return (String) properties.get(key);
   }

}
