package com.cm.pojo;

import lombok.Data;

@Data
public class Category {
    private Long id;                //类别id
    private String avatar;          //类别头像
    private String categoryName;    //类别名
    private String description;    //类别描述
}
