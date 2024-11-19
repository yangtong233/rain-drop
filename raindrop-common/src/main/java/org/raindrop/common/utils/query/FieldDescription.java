package org.raindrop.common.utils.query;

import lombok.Data;
import org.raindrop.common.annos.Wrapper;

/**
 * 对象的字段描述类
 */
@Data
public class FieldDescription {
    //修饰该字段的Wrapper注解
    public Wrapper wrapper;
    //该字段类型
    public Class fieldType;
    //该字段名称
    public String fieldName;
    //该字段的值
    public Object fieldValue;
}
