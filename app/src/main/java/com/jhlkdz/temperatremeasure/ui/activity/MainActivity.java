package com.jhlkdz.temperatremeasure.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.jhlkdz.temperatremeasure.R;
import com.jhlkdz.temperatremeasure.service.CheckInfoService;
import com.jhlkdz.temperatremeasure.service.impl.CheckInfoServiceImpl;
import com.jhlkdz.temperatremeasure.socket.Client;
import com.jhlkdz.temperatremeasure.socket.api.Response;
import com.jhlkdz.temperatremeasure.ui.MyProgressBar;
import com.jhlkdz.temperatremeasure.ui.MyProgressDialog;
import com.jhlkdz.temperatremeasure.ui.Task.CheckTask;
import com.jhlkdz.temperatremeasure.ui.Task.InitTask;
import com.jhlkdz.temperatremeasure.ui.Task.SyncTask;
import com.jhlkdz.temperatremeasure.ui.activity.api.LoginInfo;
import com.jhlkdz.temperatremeasure.ui.adapter.ListViewAdapter;
import com.jhlkdz.temperatremeasure.ui.adapter.MultiItemRowListAdapter;
import com.jhlkdz.temperatremeasure.util.ConverseUtil;
import com.jhlkdz.temperatremeasure.widget.LoadingView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MainActivity extends AppCompatActivity {

    MyProgressDialog myProgressDialog;
    MyProgressBar myProgressBar;

    private ArrayList<String> list;
    private LoadingView loading;
    private CheckInfoService checkInfoService;


    SharedPreferences sp;
    SharedPreferences spLogin;

    ArrayList<Integer> syncListShow=  new ArrayList<>();

    private long exitTime = 0;


    //设置字体为默认大小，不随系统字体大小改而改变
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.fontScale != 1)//非默认值
            getResources();
        super.onConfigurationChanged(newConfig);
    }


    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        if (res.getConfiguration().fontScale != 1) {//非默认值
            Configuration newConfig = new Configuration();
            newConfig.setToDefaults();//设置默认
            res.updateConfiguration(newConfig, res.getDisplayMetrics());
        }
        return res;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myProgressDialog = new MyProgressDialog(MainActivity.this);
        myProgressBar = new MyProgressBar(MainActivity.this);

        list = new ArrayList<>();
        loading = new LoadingView(this,R.style.CustomDialog);
        sp = getSharedPreferences("choose", Context.MODE_PRIVATE);
        spLogin = getSharedPreferences("login",Context.MODE_PRIVATE);

        if(sp.getStringSet("barnList",null)==null){
         //   loading.show();
            Callable<String> call = new Callable<String>() {
                public String call() throws Exception {

                    System.out.println(spLogin.getString("ip1",null));
                    System.out.println(spLogin.getInt("ip2",0));
                    Client client = new Client(spLogin.getString("ip1",null),spLogin.getInt("ip2",0));
                    client.executeHands(spLogin.getInt("address",1),spLogin.getInt("account",0), ConverseUtil.pwdIntToArray(spLogin.getInt("password",0)));
                    Response response = client.executeSystemParam(spLogin.getInt("address",0),0,0);
                    System.out.println(response);
                    if(response.getOperationType()==0){
                        Message msg = new Message();
                        msg.what=0;
                        //handler.sendMessage(msg);

                        List<Byte> params = response.getParams();
                        int numOfBarns = params.get(0);
                        for (int i=0;i<=(numOfBarns-1)/16;i++){
                            Response nameRes = client.executeSystemParam(spLogin.getInt("address",0),1,i/16);
                            List<Byte> nameList = nameRes.getParams();
                            for (int j=0;j<nameList.size();j+=4){
                                String one = ConverseUtil.byteToSingleChar(nameList.get(j));
                                String two = ConverseUtil.byteToSingleChar(nameList.get(j+1));
                                String three = ConverseUtil.byteToSingleChar(nameList.get(j+2));
                                String four = ConverseUtil.byteToSingleChar(nameList.get(j+3));
                                String name = one+two+three+four;
                                System.out.println("newName"+name);
                            }
                        }
                        for(int i=0;i<numOfBarns;i++){
                            list.add((i+1) + " 仓");
                        }
                    }
                    return "";
                }
            };

            ExecutorService executor = Executors.newCachedThreadPool();

            try {
                Future<String> future = executor.submit(call);
                future.get(2000, TimeUnit.MILLISECONDS); //任务处理超时时间设为 1 秒
            } catch (TimeoutException ex) {
             //   loading.dismiss();
                Toast.makeText(this,"请求超时",Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 关闭线程池
            executor.shutdown();

            sp.edit().putStringSet("barnList",new HashSet<>(list)).apply();
        }
        else {
            ArrayList<String> temp = new ArrayList<>(sp.getStringSet("barnList",null));
            //Collections.sort(temp);
            Collections.sort(temp, new Comparator<String>() {
                @Override
                public int compare(String s, String t1) {
                    return Integer.valueOf(s.substring(0,s.length()-2))-Integer.valueOf(t1.substring(0,t1.length()-2));
                }
            });
            System.out.println("排序!!!!!!!!!!!!:"+temp);
            list = temp;
        }

        int spacing = (int)getResources().getDimension(R.dimen.spacing);
        int itemsPerRow = 2;

        final ListView listView = findViewById(R.id.listview);
        final ListViewAdapter listViewAdapter = new ListViewAdapter(list,this);
        listViewAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ListViewAdapter.ViewHolder holder = (ListViewAdapter.ViewHolder) view.getTag();
                // 改变CheckBox的状态
                holder.cb.toggle();
                // 将CheckBox的选中状况记录下来
                listViewAdapter.getIsSelected().put(i, holder.cb.isChecked());
            }
        });
        listViewAdapter.setOnLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final String[] items = new String[]{"初始化"};
                android.support.v7.app.AlertDialog.Builder builder1=new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
                final int barnIndex = Integer.valueOf(list.get(i).substring(0,1));
                builder1.setTitle("请选择对"+barnIndex+"仓要进行的操作");
                builder1.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ProgressDialog pd = new ProgressDialog(MainActivity.this);
                        pd.setMax(100);// 进度条显示的最大值依赖于max
                        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        pd.setCanceledOnTouchOutside(false);
                        pd.setTitle("初始化中...");
                        pd.show();

                        InitTask initTask = new InitTask(MainActivity.this,spLogin,barnIndex,pd);
                        initTask.start();
                    }
                });
                builder1.show();
                return false;
            }
        });

        MultiItemRowListAdapter wrapperAdapter = new MultiItemRowListAdapter(this, listViewAdapter, itemsPerRow, spacing);
        listView.setAdapter(wrapperAdapter);

//        Button btSelectAll = findViewById(R.id.selectAll);
//        Button btSelectNo = findViewById(R.id.selectNo);
//        Button btSelectOther = findViewById(R.id.selectOther);
//        btSelectAll.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // 遍历list的长度，将MyAdapter中的map值全部设为true
//                for (int i = 0; i < list.size(); i++) {
//                    listViewAdapter.getIsSelected().put(i, true);
//                }
//                // 刷新listview和TextView的显示
//                listViewAdapter.notifyDataSetChanged();
//            }
//        });
//        btSelectOther.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // 遍历list的长度，将已选的设为未选，未选的设为已选
//                for (int i = 0; i < list.size(); i++) {
//                    if (listViewAdapter.getIsSelected().get(i)) {
//                        listViewAdapter.getIsSelected().put(i, false);
//                    } else {
//                        listViewAdapter.getIsSelected().put(i, true);
//                    }
//                }
//                // 刷新listview和TextView的显示
//                listViewAdapter.notifyDataSetChanged();
//            }
//        });
//        // 取消按钮的回调接口
//        btSelectNo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // 遍历list的长度，将已选的按钮设为未选
//                for (int i = 0; i < list.size(); i++) {
//                    if (listViewAdapter.getIsSelected().get(i)) {
//                        listViewAdapter.getIsSelected().put(i, false);
//                    }
//                }
//                // 刷新listview和TextView的显示
//                listViewAdapter.notifyDataSetChanged();
//            }
//        });

        checkInfoService = new CheckInfoServiceImpl(this);

        LinearLayout imageView = findViewById(R.id.check);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<Integer, Boolean> map = listViewAdapter.getIsSelected();
                final List<Integer> list = new ArrayList<>();
                for (int i : map.keySet()) {
                    if (map.get(i))
                        list.add(i+1);
                }
                if(list.size()==0){
                    Toast.makeText(MainActivity.this, "请选择仓号", Toast.LENGTH_SHORT).show();
                }
                else {
                    ProgressDialog pd = new ProgressDialog(MainActivity.this);
                    pd.setMax(100);// 进度条显示的最大值依赖于max
                    pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    pd.setCanceledOnTouchOutside(false);
                    pd.setTitle("检测中...请勿退出");
                    pd.show();

                    // 在此启动一个线程
                    CheckTask t = new CheckTask(MainActivity.this,spLogin,list,pd);
                    t.start();

                }
            }
        });

        LinearLayout imageViewShow = findViewById(R.id.show);
        imageViewShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<Integer, Boolean> map = listViewAdapter.getIsSelected();
                final ArrayList<Integer> list = new ArrayList<>();
                for (int i : map.keySet()) {
                    if (map.get(i))
                        list.add(i+1);
                }
                if(list.size()==0){
                    Toast.makeText(MainActivity.this, "请选择仓号", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(MainActivity.this, list.toString(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this,ShowActivity.class);
                    intent.putIntegerArrayListExtra("barnList",list);
                    startActivity(intent);
                }

            }
        });

        LinearLayout imageViewSync = findViewById(R.id.sync);
        imageViewSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ProgressDialog pd = new ProgressDialog(MainActivity.this);
                pd.setMax(100);// 进度条显示的最大值依赖于max
                pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pd.setCanceledOnTouchOutside(false);
                pd.setTitle("同步中...请勿退出");
                pd.show();

//                Callable<ArrayList<Integer>> callable = new Callable<ArrayList<Integer>>() {
//                    @Override
//                    public ArrayList<Integer> call() throws Exception {
//                        ArrayList<Integer> syncList = new ArrayList<>();
//                        try {
//                            LoginInfo loginInfo = new LoginInfo(spLogin.getString("ip",null),
//                                    String.valueOf(spLogin.getInt("address",0)),
//                                    String.valueOf(spLogin.getInt("account",0)),
//                                    String.valueOf(spLogin.getInt("password",0)));
//                            syncList = checkInfoService.syncCheckInfo(loginInfo,pd);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                      //  loading.dismiss();
//                        return syncList;
//                    }
//                };
//
//                ExecutorService executor = Executors.newCachedThreadPool();
//                try {
//                    Future<ArrayList<Integer>> future = executor.submit(callable);
//                    syncListShow = future.get(10000, TimeUnit.MILLISECONDS); //任务处理超时时间设为 1 秒
//
//                } catch (TimeoutException ex) {
//                    dialog("请求超时");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                // 关闭线程池
//                executor.shutdown();

                SyncTask t = new SyncTask(MainActivity.this,spLogin,listViewAdapter,pd);
                t.start();
//                try {
//                    t.join(4000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                syncListShow = t.syncList;
//
//                for (int i = 0; i < list.size(); i++) {
//                    if (listViewAdapter.getIsSelected().get(i)) {
//                        listViewAdapter.getIsSelected().put(i, false);
//                    }
//                }
//                if (syncListShow.size()==0){
//                    dialog("没有需要同步的数据");
//                }
//                else {
//                    // 遍历list的长度，将已选的按钮设为未选
//                    System.out.println(syncListShow+"!!!!!!!!!");
//                    String text = "";
//                    for(int i:syncListShow){
//                        text = text+i+" ";
//                    }
//                    text+="仓数据";
//                    dialog("已同步"+text);
//                    for (int i = 0; i < list.size(); i++) {
//                        if(syncListShow.contains(i+1)){
//                            listViewAdapter.getIsSelected().put(i, true);
//                        }
//                    }
//                }
//                // 刷新listview和TextView的显示
//                listViewAdapter.notifyDataSetChanged();
            }
        });

        LinearLayout imageViewSettings = findViewById(R.id.settings);
        imageViewSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<Integer, Boolean> map = listViewAdapter.getIsSelected();
                final ArrayList<Integer> list = new ArrayList<>();
                for (int i : map.keySet()) {
                    if (map.get(i))
                        list.add(i+1);
                }
                if(list.size()==0){
                    Toast.makeText(MainActivity.this, "请选择仓号", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(MainActivity.this, list.toString(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this,ShowPlaneActivity.class);
                    intent.putIntegerArrayListExtra("barnList",list);
                    startActivity(intent);
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            spLogin.edit().putBoolean("hasLogin",false).apply();
            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;
        }
        if (id==R.id.action_settings){
            Intent intent =new Intent(MainActivity.this,SettingsActivity.class);
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }

    private void sleep(int time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void dialog(final String text){

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(text);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();

    }


}
