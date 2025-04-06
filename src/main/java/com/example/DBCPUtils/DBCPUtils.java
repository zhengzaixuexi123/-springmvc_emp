package com.example.DBCPUtils;

import org.apache.commons.dbcp2.BasicDataSource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;

/*
* 1.初始化连接池
* 2.获取资源文件
* 3.设置数据库配置
* 4.提供连接和释放资源的方法
* */
public class DBCPUtils {
    private static final BasicDataSource dataSource;
    static {
        //初始化连接池
        Properties properties = new Properties();
        //创建输入流来获取资源文件
        InputStream inputStream = DBCPUtils.class.getClassLoader()
                .getResourceAsStream("jdbc.properties");
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String driverClassName = properties.getProperty("driver");
        String url = properties.getProperty("url");
        String username = properties.getProperty("username");
        String password = properties.getProperty("password");
        int initialSize = Integer.parseInt(properties.getProperty("initialSize"));
        int maxActive = Integer.parseInt(properties.getProperty("maxActive"));
        dataSource = new BasicDataSource();
        //设置数据库驱动类名
        dataSource.setDriverClassName(driverClassName);
        //设置连接数据库的URL
        dataSource.setUrl(url);
        //设置访问数据库的用户名

        dataSource.setUsername(username);
        //设置访问数据库的密码
        dataSource.setPassword(password);
        //设置连接池初始化时创建的初始连接数
        dataSource.setInitialSize(initialSize);
        //设置最大空闲数
        dataSource.setMaxIdle(maxActive);

    }

    public static Connection getConnection() {
        Connection conn ;
        try {
            conn = dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return conn;
    }
    public static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
