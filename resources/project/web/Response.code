package $packageName.web;

/**
 * Api 响应对象，如果响应成功，可通过 {@link #getData()} 获取服务器返回的数据对象。如果响应失败,
 * 可基于改对象的 {@code code} 和 {@code message} 字段信息来对出错信息进行分类处理。
 *
 * @author chenwei & hiyuno
 */
public class Response {

    /**
     * 响应代码，大于等于 0 为正常响应，小于 0 为异常响应（存在错误）, 默认为 0
     */
    private int code = 0;

    /**
     * 响应信息，在出现异常时，该字段保存错误信息相关的提示文案，例如 "connection timeout", "invalid user" 等。
     */
    private String message = "";

    /**
     * 当响应成功时，该字段保存服务端返回的数据对象
     */
    private Object data = null;

    /**
     * 空构造函数。
     */
    public Response() {

    }

    /**
     * 使用指定的响应代码及响应消息构造当前响应对象。
     *
     * @param code 响应代码
     * @param message 响应消息
     */
    public Response(int code, String message) {
        super();
        this.code = code;
        this.message = message;
    }

    /**
     * 获取响应代码，大于等于 0 为正常响应，小于 0 为异常响应（存在错误）, 默认为 0。
     *
     * @return 响应代码
     */
    public int getCode() {
        return code;
    }

    /**
     * 设置响应代码，大于等于 0 为正常响应，小于 0 为异常响应（存在错误）, 默认为 0。
     *
     * @param code 响应代码
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * 获取响应信息，在出现异常时，该字段保存错误信息相关的提示文案，例如 "connection timeout", "invalid user" 等。
     *
     * @return 响应信息
     */
    public String getMessage() {
        return message;
    }

    /**
     * 设置响应信息，在出现异常时，该字段保存错误信息相关的提示文案，例如 "connection timeout", "invalid user" 等。
     *
     * @param message 响应信息
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 获取当响应成功时，服务端返回的数据对象，可能返回 {@code null}。
     *
     * @return data 数据对象
     */
    public <T> T getData() {
        return (T) data;
    }

    /**
     * 设置当响应成功时，服务端返回的数据对象。
     *
     * @param data 数据对象
     */
    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Response{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
