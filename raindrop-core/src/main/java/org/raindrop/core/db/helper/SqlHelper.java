package org.raindrop.core.db.helper;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 使用JDBC编程，有六个步骤
 * 1.注册驱动(将数据库驱动文件加载到JVM内存)
 * 2.获取数据库连接(在mysql进程和JVM进程间打开一个连接通道)
 * 3.获取数据库操作对象(该对象可以将sql语句发送给数据库，数据库执行sql)
 * 4.执行sql(发送并执行具体的sql)
 * 5.处理查询结果集
 * 6.释放资源(关闭mysql进程和JVM进程间的连接通道)
 */
public class SqlHelper {

    private ResultHandler handler;

    static {
        try {
            /**
             * 注册驱动，本质就是执行com.mysql.cj.jdbc.Driver类里静态代码块的代码
             * 也可以: DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
             */
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public SqlHelper(ResultHandler handler) {
        this.handler = handler;
    }

    public List<Map<String, QueryResultField>> doSelect(DataSource dataSource, SqlObj sqlObj) throws SQLException {
        //获取连接
        Connection connection = dataSource.getConnection();
        //预编译sql
        PreparedStatement ps = connection.prepareStatement(sqlObj.getSql());
        //为sql占位符赋值
        sqlObj.parseSql(ps);

        ResultSet rs = ps.executeQuery();
        List<Map<String, QueryResultField>> results = new ArrayList<>();
        while (rs.next()) {
            results.add(handler.mapping(rs));
        }

        rs.close();
        ps.close();
        connection.close();

        return results;
    }

    public int doUpdate(DataSource dataSource, SqlObj sqlObj) throws SQLException {
        //获取连接
        Connection connection = dataSource.getConnection();
        //预编译sql
        PreparedStatement ps = connection.prepareStatement(sqlObj.getSql(), Statement.RETURN_GENERATED_KEYS);
        //为sql占位符赋值
        sqlObj.parseSql(ps);

        int i = ps.executeUpdate();

        ps.close();
        connection.close();

        return i;
    }

}
