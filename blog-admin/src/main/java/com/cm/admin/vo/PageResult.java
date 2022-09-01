package com.cm.admin.vo;

import lombok.Data;

import java.util.List;


// 存储在Result的Data属性中
@Data
public class PageResult<T> {

    private List<T> list;

    private Long total;
}
