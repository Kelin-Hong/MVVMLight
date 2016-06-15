package com.kelin.mvvmlight.zhihu.retrofit;

import java.io.IOException;

/**
 * Created by liupei on 15/11/10.
 */
public class ApiException extends IOException {
    public final int code;
    public final String msg;

    public ApiException(int code) {
        this.code = code;
        this.msg = null;
    }

    public ApiException(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
