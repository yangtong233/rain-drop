package org.raindrop.common.utils.excel;

import lombok.Data;
import lombok.experimental.Accessors;
import org.raindrop.common.annos.Marker;

import java.util.Date;

@Data
@Accessors(chain = true)
@Marker("学生信息")
public class Student {
    @Marker(name = "下标")
    private Integer index;

    @Marker(name = "反馈方式")
    private Boolean fkfs;

    @Marker(name = "姓名")
    private String name;

    @Marker(name = "年龄")
    private Integer age;

    @Marker(name = "出生日期", width = 20)
    private Date birthday;

    @Marker(name = "备注", width = 30)
    private String remark;
}
