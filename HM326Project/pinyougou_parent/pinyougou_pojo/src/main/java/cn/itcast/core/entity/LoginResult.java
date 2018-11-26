package cn.itcast.core.entity;

public class LoginResult {

    private boolean success;
    private String loginname;
    private Object data;

    public LoginResult(boolean success, String loginname, Object data) {
        this.success = success;
        this.loginname = loginname;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
