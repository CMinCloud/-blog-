package com.cm.admin.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor   //设置全参构造函数，以便于设置
public class Result {
    private boolean success;   //代表成功与否

    private Integer code;       //相应码

    private String msg;

    private Object data;        //作为返回数据

    public static Result Success(Object data){
//        请求成功，返回对应的data值
        return new Result(true,200,"success",data);
    }

    public static Result Fail(Integer code,String message){
//        请求失败，数据为空
        return new Result(false,code,message,null);
    }
}
