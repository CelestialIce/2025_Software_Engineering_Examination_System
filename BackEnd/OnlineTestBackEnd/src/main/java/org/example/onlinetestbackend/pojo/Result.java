package org.example.onlinetestbackend.pojo;

public class Result {
    private Integer success; // 操作结果状态码，1表示成功，0表示失败
    private Object data; // 操作结果数据，可以是任意类型

    // Getter 和 Setter 方法
    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    // 构造方法
    public Result(int success, Object data) {
        this.success = success;
        this.data = data;
    }

    public Result(int success) {
        this.success = success;
    }

    // 重写 toString 方法，方便打印结果
    @Override
    public String toString() {
        return data != null ?
                "{\n" +
                        "\"success\":" + success + ",\n" +
                        "\"data\":" + data.toString() + "\n" +
                        '}' :
                "{\n" +
                        "\"success\":" + success + ",\n" +
                        "\"data\":null\n" +
                        '}';
    }
}