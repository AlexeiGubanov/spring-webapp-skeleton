package org.swas.service;

import org.apache.commons.lang3.StringUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;

/**
 * Common return type.
 *
 * @author Alexei.Gubanov@gmail.com
 *         Date: 08.12.11
 */
public class Result {

    public static final int OK = 0;
    public static final int ERROR_GENERAL = -1;


    private int code;
    private String msg;
    private Object value;
    private Object[] values;
    private Throwable ex;

    public boolean isOk() {
        return this.code == Result.OK;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * @return main value
     */
    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }


    public Object[] getValues() {
        return values;
    }

    /**
     * <p/>
     * Return some value from additional values array{@link #getValues()}, index starts with 0.
     */
    public Object getValue(int index) {
        if (this.values != null) {
            if (this.values.length > index)
                return this.values[index];
        }
        return null;
    }

    public void setValues(Object[] values) {
        this.values = values;
    }

    public Throwable getEx() {
        return ex;
    }

    public void setEx(Exception ex) {
        this.ex = ex;
    }

    public Result(int code) {
        this.code = code;
    }

    public Result(String msg, Object value) {
        this.code = OK;
        this.msg = msg;
        this.value = value;
    }

    public Result(Object value) {
        this.code = OK;
        this.value = value;
    }

    public Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Result(int code, String msg, Object value) {
        this.code = code;
        this.msg = msg;
        this.value = value;
    }

    public Result(int code, Object value) {
        this.code = code;
        this.value = value;
    }

    public Result(int code, String msg, Throwable ex) {
        this.code = code;
        this.msg = msg;
        this.ex = ex;
    }

    public Result(int code, Throwable ex) {
        this.code = code;
        this.ex = ex;
    }

    public Result(Throwable ex) {
        this.code = ERROR_GENERAL;
        this.ex = ex;
        this.msg = ex.getMessage();
    }

    @Override
    public String toString() {
        if (ex != null) {
            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            ex.printStackTrace(printWriter);
            return writer.toString();
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Result[code=")
                    .append(code);
            if (!StringUtils.isBlank(msg))
                sb.append(",msg=").append(msg);
            if (null != value)
                sb.append(",value=").append(value);
            if (null != values)
                sb.append(",value2=").append(Arrays.toString(values));
            sb.append("]");
            return sb.toString();

        }
    }
}
