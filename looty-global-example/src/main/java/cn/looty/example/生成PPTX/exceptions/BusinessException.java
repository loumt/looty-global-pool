package cn.looty.example.生成PPTX.exceptions;

/**
 * @Classname BusinessException
 * @Created by loumt
 * @email looty_loumt@hotmail.com
 * @Description TODO
 * @Date 2024/8/15 0:23
 */
public class BusinessException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public BusinessException() {
        super();
    }

    public BusinessException(String message) {
        super(message);
        this.message = message;
    }

    public String message;

    @Override
    public String getMessage() {
        return message;
    }
}
