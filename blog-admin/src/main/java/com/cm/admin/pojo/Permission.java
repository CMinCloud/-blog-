package com.cm.admin.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Permission {

    @TableId(type = IdType.AUTO)  //为主键设置自增
    private Long id;

    private String name;

    private String path;

    private String description;
}
