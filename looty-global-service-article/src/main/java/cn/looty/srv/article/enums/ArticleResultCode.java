package cn.looty.srv.article.enums;

import cn.looty.common.base.BaseResultEnum;

/**
 * @Classname ArticleResultCode
 * @Created by loumt
 * @email looty_loumt@hotmail.com
 * @Description TODO
 * @Date 2024/8/31 22:49
 */
public enum ArticleResultCode implements BaseResultEnum {

    ;

    ArticleResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;
    private String message;

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
