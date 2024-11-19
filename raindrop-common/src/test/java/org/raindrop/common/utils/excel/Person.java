package org.raindrop.common.utils.excel;

import lombok.Data;
import lombok.experimental.Accessors;
import org.raindrop.common.annos.Excel;

import java.util.Date;

@Data
@Accessors(chain = true)
public class Person {
    @Excel
    private Boolean fkfs;
    @Excel
    private Integer index;
    @Excel
    private String name;
    @Excel
    private String wjmc;
    @Excel
    private Integer djh;
    @Excel
    private String lxdh;
    @Excel
    private String sfz;
    @Excel
    private String fkr;
    @Excel
    private String jzks;
    @Excel
    private int age;
    @Excel
    private Date date;
}
