package com.kcbs.webforum.exception;

public enum  WebforumExceptionEnum {
    NOT_FOND(404,"资源不存在"),
    NEED_USER_NAME(10001,"用户名不能为空"),
    NEED_PASSWORD(10002,"密码不能为空"),
    PASSWORD_TOO_SHORT(10003,"密码长度不能小于8位"),
    NAME_EXISTED(10004, "用户名已存在"),
    INSERT_FAILED(10005, "注册失败，请重试"),
    REQUEST_PARAM_ERROR(10012, "参数错误"),
    WRONG_USERNAME(10006,"账号不存在"),
    WRONG_PASSWORD(10007, "密码错误"),
    NEED_CONTENT(10008, "内容不能为空"),
    NEED_LOGIN(10009,"需要登录"),
    NEED_ADMIN(10010,"需要管理员权限"),
    TITLE_EXISTED(10011,"请勿重复发布"),
    POST_FAILED(10012,"发布失败,请重新发布"),
    WRONG_CATEGORY(10017,"发布失败,该分类不存在"),
    DELETE_FAILED(10013,"删除失败"),
    ROLLBACK_FAILED(10014,"恢复失败"),
    MKDIR_FAILED(10015,"文件夹创建失败"),
    UPLOAD_FAILED(10016,"图片上传失败"),
    WRONG_CONTENT(10018,"至少评论5个字"),
    COMMENT_FAILED(10019,"评论失败"),
    LOGIN_FAILED(10020,"登录失败"),
    LOGIN_EXPIRED(10020,"登录过期"),
    EMAIL_EXISTED(10021,"该邮箱已被注册"),
    WRONG_CODE(10022,"验证码错误"),
    CATEGORY_EXISTED(10023,"该分类已存在"),
    SUBSCRIBE_FAILED(10024,"关注失败"),
    DELSUBSCRIBE_FAILED(10024,"取消关注失败"),
    ADDCATEGORY_FAILED(10025,"路径生成失败"),
    UPDATE_FAILED(10026,"密码修改失败"),
    ISBAN(10027,"该账号已被封禁"),
    BANERROR(10028,"封禁失败"),
    UNBANERROR(10028,"解禁失败"),
    ADD_ESSENCES_ERROR(10029,"加精失败"),
    CANCEL_ESSENCES_ERROR(10030,"取消加精失败"),
    UPDATEINFO_FAILED(10031,"密码修改失败"),
    POST_EXISTED(10032,"帖子不存在"),
    OLDIMAGE_DELETE_FAILED(10034,"旧图片删除失败"),
    UPATE_CATEGORYNAME_FAILED(10033,"修改分类名称失败"),
    UPATE_CATEGORYIMAGE_FAILED(10035,"修改分类图片失败"),
    RECOMMEND_FAILED(10036,"分类推荐失败"),
    UNRECOMMEND_FAILED(10037,"分类取消推荐失败"),
    DELETECATEGORY_FAILED(10038,"分类删除失败"),
    JEDIS_FAILED(20002,"JEDIS连接失败"),
    SYSTEM_ERROR(20000, "系统异常，请从控制台或日志中查看具体错误信息"),
    METHOD_ERROR(20001, "请求方法异常，请使用对应方法提交请求");
    /**
     * 异常码
     */
    Integer code;
    /**
     * 错误信息
     */
    String message;

    WebforumExceptionEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
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
}
