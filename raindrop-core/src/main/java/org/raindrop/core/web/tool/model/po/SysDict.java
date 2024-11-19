package org.raindrop.core.web.tool.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.raindrop.common.web.PersistenceModel;

/**
 * 系统字典，格式
 *   - 字典(dict)
 *      - 字典项(dict_item)
 * 比如:
 *   - 性别(sex)
 *      - 女(0)
 *      - 男(1)
 * 对应数据库的数据就是3条:
 *   - {id:1, pid:0, type:1, dict_name:"性别", dict_code:"sex"}
 *   - {id:2, pid:1, type:0, item_text:"女", item_value:"0"}
 *   - {id:3, pid:1, type:0, item_text:"男", item_value:"1"}
 * type=0的两条字典项数据通过pid找到对应的字典数据
 */
@Data
@TableName("sys_dict")
@Schema(name = "字典", description = "字典")
public class SysDict extends PersistenceModel {

    /**
     * 字典项对应的字典id，如果是字典，则pid=0
     */
    @TableField(value = "pid")
    private String pid;
    /**
     * 字典type = 1， 字典项type = 0，与pid多多少少有点冗余，主要为了一目了然
     */
    private Integer type;
    /**
     * 字典(项)名称，比如性别、男、女
     */
    @TableField(value = "dict_name")
    private String dictName;
    /**
     * 字典code
     *  - 对于字典，一般是英文名称，比如 性别 -> sex，作为字典时应该唯一
     *  - 对于字典项，一般是映射的数字，比如 男 -> 1、女 -> 0
     */
    @TableField(value = "dict_code")
    private String dictCode;

    /**
     * 字典项值，比如"0"
     */
    @TableField(value = "item_value")
    private String itemValue;

    /**
     * 字典项文字，比如"女"
     */
    @TableField(value = "item_text")
    private String itemText;

    /**
     * 排序
     */
    @TableField(value = "sort")
    private Integer sort;
    /**
     * 描述
     */
    @TableField(value = "dict_desc")
    private String dictDesc;

}
