
package cn.flashzxi.pptbackend.util;

public class RespondFormat<T> {
    private T data;
    private boolean success;
    private String error;

    public RespondFormat(T data){
        this.data = data;
        success = true;
        error = null;
    }

    public T getData() {
        return data;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getError() {
        return error;
    }

    public RespondFormat(boolean success, T data){
        this.success = success;
        this.data = data;
    }

    public RespondFormat(boolean success, T data, String error){
        this.success = success;
        this.data = data;
        this.error = error;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setError(String error) {
        this.error = error;
    }
}
