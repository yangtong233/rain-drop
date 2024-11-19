package org.raindrop.core.db.framework;

import org.raindrop.common.KeyValuePair;
import org.raindrop.common.utils.bean.Beans;
import org.raindrop.common.utils.string.Strs;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * created by yangtong on 2024/5/14 17:21
 *
 * @Description: SQL创建器
 */
public class SQLCreator {

    /**
     * 通过方法及其方法创建一个SQL
     * getNameFromPersonById ---> select name from person where id = ? limit 1
     * listAgeFromPersonByIdAndName ---> select age from person where id = ? and name =?
     * listSexFromPersonJoinCardOnIdByPName ---> select sex from person join card on person.id = card.id where person.name = ?
     *
     * @param method 方法对象
     * @param args   方法参数值
     * @return 创建的SQL
     */
    public static String parseSqlByMethod(Method method, Object[] args) {
        Map<String, Object> paramMap = new HashMap<>();
        for (int i = 0; i < method.getParameters().length; i++) {
            Parameter parameter = method.getParameters()[i];
            //参数名称
            String parameterName = parameter.getName();
            //参数值
            Object parameterValue = args[i];

            Beans.deconstruct(new KeyValuePair<>(parameterName, parameterValue), paramMap);
        }

        doParse(method.getName(), paramMap);
        return null;
    }


    public static void doParse(String methodName, Map<String, Object> paramMap) {
        String[] sqlCause = Strs.underline(methodName).split("_");
        StringBuilder sql = new StringBuilder();
        for (String s : sqlCause) {
            if (Strs.contain(s, "get", "list", "select", "page")) {
                sql.append("select ");
            } else if (Strs.contain(s, "by")) {
                sql.append("where ");

            }
            sql.append(s).append(" ");
        }
    }



    public static void main(String[] args) {
        List<String> sqlCause = Arrays.asList(Strs.underline("getNameFromPersonByIdAndAge").split("_"));
        System.out.println(sqlCause);
    }

}
