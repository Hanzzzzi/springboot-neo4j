package org.omaha.neo4j.result;

import java.io.Serializable;

/**
 * @Author: mor
 * @Date: 2020/12/24 17:04
 */
public class ResultVO<T> implements Serializable {
    private static final long serialVersionUID = -3170332662487867689L;

    private boolean ok;
    private Integer code;
    private String message;
    private T result;

    public ResultVO() {
    }

    public ResultVO(ResultCode resultCode, T result){
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        this.result = result;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
