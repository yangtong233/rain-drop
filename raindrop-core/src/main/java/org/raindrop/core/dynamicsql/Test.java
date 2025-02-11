package org.raindrop.core.dynamicsql;

import org.apache.ibatis.builder.xml.XMLMapperEntityResolver;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;
import org.apache.ibatis.session.Configuration;

import java.util.HashMap;
import java.util.List;

/**
 * created by yangtong on 2025/1/26 17:06:47
 */
public class Test {
    public static void main(String[] args) {
        String xmlSql = "select\n" +
                "            r.*\n" +
                "        from\n" +
                "            sys_user_role ur\n" +
                "        join\n" +
                "            sys_role r\n" +
                "        on\n" +
                "            ur.role_id = r.id\n" +
                "        where ur.user_id = #{userId} and ${sql} " +
                "<if test=\"name != '' and name != null\"> and name = #{name}</if>";
        HashMap<String, Object> dto = new HashMap<>();
        dto.put("userId", "114514");
        dto.put("sql", "name = '张三'");
        dto.put("name", 21L);
        System.out.println(praseMybatisTags(xmlSql, dto));
    }


    static String praseMybatisTags(String xmlSql, HashMap<String, Object> dto) {
        String sql = "<select>" + xmlSql + "</select>";
        // 实例化解析XML对象
        XPathParser parser = new XPathParser(sql, false, null, new XMLMapperEntityResolver());
        XNode context = parser.evalNode("/select");
        Configuration configuration = new Configuration();
        configuration.setDatabaseId("");
        WkXMLScriptBuilder xmlScriptBuilder = new WkXMLScriptBuilder(configuration, context);
        WkDynamicSqlSource sqlSource = xmlScriptBuilder.parseWkScriptNode();
        System.out.println(sqlSource.getBoundSql(dto).getSql());
        List<ParameterMapping> parameterMappings = sqlSource.getBoundSql(dto).getParameterMappings();
        return sqlSource.getWkParseSql(dto);
    }

}
