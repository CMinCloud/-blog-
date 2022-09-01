package com.cm.vo.param;

import lombok.Data;

@Data
public class PageParam {

    private int page = 1;  //当前页数，默认为1

    private int pageSize = 10;   //每页显示的数量，默认为10

    //    选择特定类别或者标签的文章参数
    private Long categoryId;

    private Long tagId;

//      用作文件归档日期,采用String格式
    private String year;

    private String month;

//    对month 的字符串进行补全
    public String getMonth(){
        if(month != null && month.length() == 1){
            return "0" +this.month;
        }
        return this.month;
    }
}
