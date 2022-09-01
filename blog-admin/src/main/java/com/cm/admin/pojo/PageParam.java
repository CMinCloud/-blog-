package com.cm.admin.pojo;
import lombok.Data;

//用来接受客户端传来的分页参数
@Data
public class PageParam {

    private Integer currentPage;

    private Integer pageSize;

//    查询条件，前端传来的可能为空
    private String queryString;

//    total属性存储在PageResult中作为返回值
}


