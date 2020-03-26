package com.example.admin.context;

/**
 * @Author: wsg
 * @Date: 2019-09-25
 * @Description
 */
public class ReturnData {

    //响应业务状态码
    private Integer status;

    //响应信息
    private String msg;

    //响应数据
    private Object data;
    public ReturnData(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public ReturnData(Integer status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public static ReturnData build(Integer status, String msg,Object data) {
        return new ReturnData(status, msg, data);
    }

    public static ReturnData build(Integer status, String msg) {
        return new ReturnData(status, msg, null);
    }


    public static ReturnData ok(Object data) {
        return new ReturnData(data);
    }

    public static ReturnData ok() {
        return new ReturnData(null);
    }


    public ReturnData() {
    }


    public ReturnData(Object data) {
        this.status = ResponseCode.OPERATION_SUCCESS.getStatus();
        this.msg = ResponseCode.OPERATION_SUCCESS.getMsg();
        this.data = data;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ReturnData{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
