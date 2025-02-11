package org.raindrop.common.utils.tree;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.raindrop.common.web.PersistenceModel;

import java.util.List;

/**
 * 要求，如果一个元素没有父元素，则pid = 0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "抽象树结构")
public abstract class BaseTree<T> extends PersistenceModel {

    @Schema(description = "父数据主键id")
    private String pid;
    @Schema(description = "该节点的关键字，用于树的搜索")
    @TableField(exist = false)
    private String key;
    @Schema(description = "该数据的子数据")
    private List<T> children;
}
