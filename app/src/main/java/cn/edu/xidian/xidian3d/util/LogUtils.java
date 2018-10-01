package cn.edu.xidian.xidian3d.util;

import android.util.Log;

/**
 * @author Yao Keqi
 * @date 2018/8/22
 */
public class LogUtils {

    public static void d(String s) {
        Log.d("",buildMessage(false,s));
    }

    private static String buildMessage(boolean logStack, String msg) {
        StackTraceElement caller = new Throwable().fillInStackTrace().getStackTrace()[2];
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        StringBuilder sb = new StringBuilder();
        sb.append(caller.getClassName())
                .append(".")
                .append(caller.getMethodName())
                .append("(): [")
                .append(caller.getLineNumber())
                .append("]")
                .append(msg).
                append("\n");

        if (logStack) {
            for (StackTraceElement ste : stackTraceElements) {
                sb.append(ste).append("\n");
            }
        }
        return sb.toString();
    }

}
