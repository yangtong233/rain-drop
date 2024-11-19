//package org.raindrop.core.mp.interceptor;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
//import com.baomidou.mybatisplus.core.plugins.InterceptorIgnoreHelper;
//import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
//import com.baomidou.mybatisplus.core.toolkit.StringUtils;
//import com.baomidou.mybatisplus.extension.parser.JsqlParserSupport;
//import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
//import lombok.*;
//import net.sf.jsqlparser.expression.Expression;
//import net.sf.jsqlparser.statement.select.PlainSelect;
//import net.sf.jsqlparser.statement.select.Select;
//import net.sf.jsqlparser.statement.select.SetOperationList;
//import org.apache.catalina.User;
//import org.apache.ibatis.executor.Executor;
//import org.apache.ibatis.mapping.BoundSql;
//import org.apache.ibatis.mapping.MappedStatement;
//import org.apache.ibatis.session.ResultHandler;
//import org.apache.ibatis.session.RowBounds;
//
//import java.sql.SQLException;
//import java.util.List;
//import java.util.function.BiFunction;
//import java.util.function.Consumer;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@ToString(callSuper = true)
//@EqualsAndHashCode(callSuper = true)
//public class MyDataPermissionInterceptor extends JsqlParserSupport implements InnerInterceptor {
//    /**
//     * 数据权限处理器
//     */
//    private MyDataPermissionHandler dataPermissionHandler;
//
//    @Override
//    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
//        if (InterceptorIgnoreHelper.willIgnoreDataPermission(ms.getId())) {
//            return;
//        }
//        PluginUtils.MPBoundSql mpBs = PluginUtils.mpBoundSql(boundSql);
//        mpBs.sql(this.parserSingle(mpBs.sql(), ms.getId()));
//    }
//
//    @Override
//    protected void processSelect(Select select, int index, String sql, Object obj) {
//        SelectBody selectBody = select.getSelectBody();
//        if (selectBody instanceof PlainSelect) {
//            this.setWhere((PlainSelect) selectBody, (String) obj);
//        } else if (selectBody instanceof SetOperationList) {
//            SetOperationList setOperationList = (SetOperationList) selectBody;
//            List<SelectBody> selectBodyList = setOperationList.getSelects();
//            selectBodyList.forEach(s -> this.setWhere((PlainSelect) s, (String) obj));
//        }
//    }
//
//    /**
//     * 设置 where 条件
//     *
//     * @param plainSelect  查询对象
//     * @param whereSegment 查询条件片段
//     */
//    private void setWhere(PlainSelect plainSelect, String whereSegment) {
////
////        Expression sqlSegment = this.dataPermissionHandler.getSqlSegment(plainSelect, whereSegment);
////        if (null != sqlSegment) {
////            plainSelect.setWhere(sqlSegment);
////        }
//    }
//
//    public static void main(String[] args) {
////        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
////
////        // 构建查询条件
////        queryWrapper.eq("name", "John")
////                .ge("age", 18)
////                .le("age", 30);
////
////        // 获取生成的 SQL 语句
////        String sql = queryWrapper.getSqlSegment();
////
////        // 获取 WHERE 子句
////        String whereClause = StringUtils.isNotBlank(sql) ? "WHERE " + sql : "";
////
////        // 打印 WHERE 子句
////        System.out.println("WHERE 子句：" + whereClause);
////        System.out.println(queryWrapper.getParamNameValuePairs());
//        Consumer<String> consumer = Person::hello;
//    }
//
//    class Person{
//        static int hello(String name) {
//            return 233;
//        }
//    }
//}
