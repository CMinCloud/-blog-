package com.cm.pojo.dos;

import lombok.Data;
//用来存储非数据库对象
//  归档（记录年月期间 文章数量）
@Data
public class Archives {
    private Integer year;

    private Integer month;

    private Long count; //文章数量
}
