package com.lsc.utils;

import lombok.Data;
import java.io.Serializable;

@Data
public class ResponseResult implements Serializable {
    private Integer code;
    private String msg;
    private Object data;

    public static ResponseResult ok(String msg, Object data){
        ResponseResult responseResult=new ResponseResult();
        responseResult.setCode(200);
        responseResult.setMsg(msg);
        responseResult.setData(data);
        return responseResult;
    }

    public static ResponseResult ok(String msg){
        ResponseResult responseResult=new ResponseResult();
        responseResult.setCode(200);
        responseResult.setMsg(msg);
        responseResult.setData("");
        return responseResult;
    }

    public static ResponseResult error(String msg) {
        ResponseResult responseResult=new ResponseResult();
        responseResult.setCode(500);
        responseResult.setMsg(msg);
        responseResult.setData("");
        return responseResult;
    }

}