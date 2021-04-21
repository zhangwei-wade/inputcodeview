package com.dzw.inputpwdcodetext.dialog;

import android.app.Dialog;

/**
 * @author zhangwei on 2021/4/21.
 */
public interface OnInputCodeListener {
    /**
     * 完成输入
     *
     * @param dialog
     * @param verificationCode
     */
    void inputCodeComplete(Dialog dialog, String verificationCode);

    /**
     * 未完成输入
     *
     * @param dialog
     * @param verificationCode
     */
    void inputCodeInput(Dialog dialog, String verificationCode);
}
