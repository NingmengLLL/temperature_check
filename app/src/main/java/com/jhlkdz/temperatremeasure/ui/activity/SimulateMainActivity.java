package com.jhlkdz.temperatremeasure.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.jhlkdz.temperatremeasure.R;
import com.jhlkdz.temperatremeasure.model.CheckInfo;
import com.jhlkdz.temperatremeasure.service.SimulateInfoService;
import com.jhlkdz.temperatremeasure.service.impl.SimulateInfoServiceImpl;
import com.jhlkdz.temperatremeasure.ui.adapter.ListViewAdapter;
import com.jhlkdz.temperatremeasure.ui.adapter.MultiItemRowListAdapter;
import com.jhlkdz.temperatremeasure.widget.LoadingView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SimulateMainActivity extends AppCompatActivity {

    private ArrayList<String> list;
    private HashMap<Integer, List<CheckInfo>> datas = new HashMap<>();

    SharedPreferences spLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        list = new ArrayList<>();
        int numOfBarns = (int)(Math.random()*7)+3;
        for(int i=0;i<numOfBarns;i++){
            list.add((i+1) + " 仓");
            datas.put(i+1,new ArrayList<CheckInfo>());
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


        LinearLayout imageView = findViewById(R.id.check);
        final SimulateInfoService simulateInfoService = new SimulateInfoServiceImpl();
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
                    Toast.makeText(SimulateMainActivity.this, "请选择仓号", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(SimulateMainActivity.this, list.toString(), Toast.LENGTH_SHORT).show();
                    final ProgressDialog pd = new ProgressDialog(SimulateMainActivity.this);
                    pd.setCanceledOnTouchOutside(false);
                    pd.setMax(100);// 进度条显示的最大值依赖于max
                    pd.setTitle("检测中...请勿退出");
                    pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    pd.show();
                    new Thread() {
                        @Override
                        public void run() {
                            pd.setProgress(15);
                            sleep(500);
                            pd.setProgress(30);
                            sleep(1000);
                            for(int i=0;i<list.size();i++){
                                int index = list.get(i);
                                datas.get(index).add(simulateInfoService.getCheckInfoById(index));
                            }
                            pd.setProgress(80);
                            sleep(500);
                            pd.setProgress(100);
                            sleep(500);
                            pd.cancel();
                        }

                        private void sleep(int time){
                            try {
                                Thread.sleep(time);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                    Toast.makeText(SimulateMainActivity.this,"检测完成",Toast.LENGTH_LONG).show();
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
                    Toast.makeText(SimulateMainActivity.this, "请选择仓号", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(SimulateMainActivity.this, list.toString(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SimulateMainActivity.this,ShowActivity.class);
                    intent.putIntegerArrayListExtra("barnList",list);
                    intent.putExtra("isSimulate",1);
                    intent.putExtra("map",datas);
                    startActivity(intent);
                }

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
                    Toast.makeText(SimulateMainActivity.this, "请选择仓号", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(SimulateMainActivity.this, list.toString(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SimulateMainActivity.this,ShowPlaneActivity.class);
                    intent.putIntegerArrayListExtra("barnList",list);
                    intent.putExtra("isSimulate",1);
                    intent.putExtra("map",datas);
                    startActivity(intent);
                }
            }
        });

        LinearLayout imageViewSync = findViewById(R.id.sync);
        imageViewSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SimulateMainActivity.this, "模拟模式暂不支持同步", Toast.LENGTH_SHORT).show();
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
            Intent intent = new Intent(SimulateMainActivity.this,LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
