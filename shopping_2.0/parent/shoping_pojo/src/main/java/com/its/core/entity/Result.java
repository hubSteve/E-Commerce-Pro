package com.its.core.entity;

import java.io.Serializable;

/**
 * 封装操作（新增、修改、删除）结果信息
 */
public class Result implements Serializable {

    private boolean flag;   // 是否操作成功
    private String message; // 提示信息

    public Result(boolean flag, String message) {
        this.flag = flag;
        this.message = message;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
