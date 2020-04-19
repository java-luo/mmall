package com.mmall.common;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;
/**
 * 统一响应对象
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL) //如果为空 转为Json后就不会有此字段
public class ServerReponse<T> implements Serializable {
    private int code;
    private String msg;
    private T data;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    private ServerReponse(int code) {
        this.code = code;
    }

    private ServerReponse(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    @JsonIgnore //添加注解格式化Json不会出现再结果内
    public Boolean isSccess(){
        return this.code == ServerReponseCode.SUCCESS.getCode();
    }

    public ServerReponse(int code,T data) {
        this.code = code;
        this.data = data;
    }

    private ServerReponse(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }



    public static <T> ServerReponse<T> createBySuccess(){
        return new ServerReponse(ServerReponseCode.SUCCESS.getCode());
    }

    public static <T> ServerReponse<T> createBySuccess(T t){
        return new ServerReponse<T>(ServerReponseCode.SUCCESS.getCode(),t);
    }

    public static <T> ServerReponse<T> createByMessageSuccess(String msg){
        return new ServerReponse<T>(ServerReponseCode.SUCCESS.getCode(),msg);
    }

    public static <T> ServerReponse<T> createBySuccess(String msg, T data){
        return new ServerReponse<T>(ServerReponseCode.SUCCESS.getCode(),msg,data);
    }

    public static <T> ServerReponse<T> createByError(){
        return new ServerReponse<T>(ServerReponseCode.ERROR.getCode(), ServerReponseCode.ERROR.getDesc());
    }

    public static <T> ServerReponse<T> createByError(ServerReponseCode reponseCode){
        return new ServerReponse<T>(reponseCode.getCode(), reponseCode.getDesc());
    }

    public static <T> ServerReponse<T> createByMessageError(String msg){
        return new ServerReponse<T>(ServerReponseCode.ERROR.getCode(),msg);
    }

    public static <T> ServerReponse<T> createByError(int errorCode, String msg){
        return new ServerReponse<T>(errorCode,msg);
    }






}
