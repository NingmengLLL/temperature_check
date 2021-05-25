package com.jhlkdz.temperatremeasure.ui.Task;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.TimeoutException;

public class TimeoutThread extends Thread{
    /**
     * 计时器超时时间
     */
    private long timeout;
    /**
     * 计时是否被取消
     */
    private boolean isCanceled = false;
    /**
     * 当计时器超时时抛出的异常
     */

    private TimeoutException timeoutException;

    private Context context;
    private ProgressDialog pd;

    public int getCurrentBarn() {
        return currentBarn;
    }

    public void setCurrentBarn(int currentBarn) {
        this.currentBarn = currentBarn;
    }

    private int currentBarn;

    public TimeoutThread(Context context, ProgressDialog p, long timeout,TimeoutException timeoutErr) {
        super();
        this.context = context;
        this.pd = p;
        this.timeout = timeout;
        this.timeoutException = timeoutErr;
        //设置本线程为守护线程
        this.setDaemon(true);
    }
    /**
     * 取消计时
     */
    public synchronized void cancel(){
        isCanceled = true;
    }
    /**
     * 启动超时计时器
     */
    public void run(){
        try {
            System.out.println("start");
            Thread.sleep(timeout);

            if(!isCanceled){
                System.out.println(timeoutException);
                pd.cancel();
                dialog("连接超时");
            }
        } catch (InterruptedException e) {
            System.err.println(e);
        }
    }

    private void message(final String text){
        new Handler(context.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                // 在这里执行你要想的操作 比如直接在这里更新ui或者调用回调在 在回调中更新ui
                Toast.makeText(context,text,Toast.LENGTH_LONG).show();
            }
        });
    }

    private void dialog(final String text){
        new Handler(context.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(text);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
            }
        });
    }
}