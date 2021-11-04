package com.jhlkdz.temperatremeasure.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.jhlkdz.temperatremeasure.R;
import com.jhlkdz.temperatremeasure.model.BarnInfo;
import com.jhlkdz.temperatremeasure.model.CheckInfo;
import com.jhlkdz.temperatremeasure.service.BarnInfoService;
import com.jhlkdz.temperatremeasure.service.BaseInfoService;
import com.jhlkdz.temperatremeasure.service.CheckInfoService;
import com.jhlkdz.temperatremeasure.service.SimulateInfoService;
import com.jhlkdz.temperatremeasure.service.impl.BarnInfoServiceImpl;
import com.jhlkdz.temperatremeasure.service.impl.BaseInfoServiceImpl;
import com.jhlkdz.temperatremeasure.service.impl.CheckInfoServiceImpl;
import com.jhlkdz.temperatremeasure.service.impl.SimulateInfoServiceImpl;
import com.jhlkdz.temperatremeasure.ui.adapter.Spinner2Adapter;
import com.jhlkdz.temperatremeasure.util.ConverseUtil;
import com.jhlkdz.temperatremeasure.util.LevelData;
import com.jhlkdz.temperatremeasure.widget.FirstView;
import com.jhlkdz.temperatremeasure.widget.SecondView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ShowPlaneActivity extends AppCompatActivity {

    private CheckInfoService checkInfoService;
    private BarnInfoService barnInfoService;
    private BaseInfoService baseInfoService;

    SimulateInfoService simulateInfoService = new SimulateInfoServiceImpl();

    private int currentBarnIndex;
    List<CheckInfo> checkInfos = new ArrayList<>();
    HashMap<Integer,List<CheckInfo>> datas;
    private boolean isSimulate;
    String[] barns;
    String[] barnNames;
    List<String> times = new ArrayList<>();//spinner2
    private float[][][] data;

    RelativeLayout relativeLayout;
    RelativeLayout relativeLayout2;
    RelativeLayout relativeLayout3;
    LinearLayout showLinear;
    LinearLayout showLinear2;

    LinearLayout mainLinear1;
    LinearLayout mainLinear2;
    LinearLayout mainLinear3;
    RadioGroup radioGroup;
    RadioButton radioButton1;
    RadioButton radioButton2;
    Button button3;
    Button button4;
    Spinner spinner1;
    Spinner spinner2;
    LinearLayout linearLayout1;
    LinearLayout linearLayout2;
    TextView text1;
    TextView text2;
    TextView text3;
    TextView text4;
    TextView text5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_plane);

        checkInfoService = new CheckInfoServiceImpl(this);
        barnInfoService = new BarnInfoServiceImpl(this);
        baseInfoService = new BaseInfoServiceImpl(this);

        relativeLayout = findViewById(R.id.relative);
        relativeLayout2 = findViewById(R.id.relative2);
        relativeLayout3 = findViewById(R.id.relative3);
        WindowManager wManager = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        Display display = wManager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        relativeLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,(int)(point.y*0.5)));
        relativeLayout2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,(int)(point.y*0.5)));
        showLinear = findViewById(R.id.showLinear);
        showLinear2 = findViewById(R.id.showLinear2);
        mainLinear1 = findViewById(R.id.mainLinear1);
        mainLinear2 = findViewById(R.id.mainLinear2);
        mainLinear3 = findViewById(R.id.mainLinear3);
        radioGroup = findViewById(R.id.radioGroup);
        radioButton1 = findViewById(R.id.radio1);
        radioButton2 = findViewById(R.id.radio2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        spinner1 = findViewById(R.id.spinner1);
        spinner2 = findViewById(R.id.spinner2);

        linearLayout1 = findViewById(R.id.table1);
        linearLayout2 = findViewById(R.id.table2);
        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);
        text3 = findViewById(R.id.text3);
        text4 = findViewById(R.id.text4);
        text5 = findViewById(R.id.text5);

        //标题列
        String[] titles1 ={"层数","最高","最低","平均"};
        LinearLayout titleLayout1 = new LinearLayout(this);
        titleLayout1.setOrientation(LinearLayout.VERTICAL);
        titleLayout1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.MATCH_PARENT));
        for(int i=0;i<4;i++){
            TextView text=new TextView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,0, 1);
            text.setLayoutParams(layoutParams);
            text.setText(titles1[i]);
            text.setTextColor(Color.parseColor("#000000"));
            text.setGravity(Gravity.CENTER);
            //使它居中
            titleLayout1.addView(text);//添加到水平线性布局
        }
        linearLayout1.addView(titleLayout1);

        //初试数据
        Intent intent = getIntent();
        isSimulate = intent.getIntExtra("isSimulate",-1)==1;
        if (isSimulate){
            datas = (HashMap<Integer, List<CheckInfo>>) intent.getSerializableExtra("map");
        }

        ArrayList<Integer> barnList = intent.getIntegerArrayListExtra("barnList");
        ArrayList<String> barnNameList = intent.getStringArrayListExtra("barnNameList");
        barns = new String[barnList.size()];
        barnNames = new String[barnList.size()];
        for(int i=0;i<barnList.size();i++){
            barns[i] = String.valueOf(barnList.get(i));
            barnNames[i] = barnNameList.get(barnList.get(i)-1);
        }


        SpinnerAdapter spinnerAdapter1 = new com.jhlkdz.temperatremeasure.ui.adapter.SpinnerAdapter(this,barnNames);
        spinner1.setAdapter(spinnerAdapter1);

        currentBarnIndex = 0;
        getData(Integer.parseInt(barns[0]));
        int length = checkInfos.size();

        if(length==0){
            setNull("该仓无检测数据，请先检测或查看其他仓");
        }
        else {
            for(int i=0;i<length;i++){
                times.add(checkInfos.get(i).getTime());
            }
            Spinner2Adapter spinner2Adapter = new Spinner2Adapter(this,times);
            spinner2.setAdapter(spinner2Adapter);
        }

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currentBarnIndex = i;
                getData(Integer.parseInt(barns[i]));
                if(checkInfos.size()==0){
                    setNull("该仓无检测数据，请先检测或查看其他仓");
                }
                else {
                    update(0);
                }
                times = new ArrayList<>();
                for(int j=0;j<checkInfos.size();j++){
                    times.add(checkInfos.get(j).getTime());
                }

                Spinner2Adapter spinner2Adapter2 = new Spinner2Adapter(ShowPlaneActivity.this, times);
                spinner2.setAdapter(spinner2Adapter2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentBarnIndex==0){

                }
                else {
                    currentBarnIndex--;
                    // getData(Integer.parseInt(barns[currentBarnIndex]));
                    spinner1.setSelection(currentBarnIndex,true);

                }
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentBarnIndex==barns.length-1){

                }
                else {
                    currentBarnIndex++;
                    // getData(Integer.parseInt(barns[currentBarnIndex]));
                    spinner1.setSelection(currentBarnIndex,true);

                }
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = findViewById(i);
                if("分层".equals(radioButton.getText())){
                    relativeLayout.setVisibility(View.GONE);
                    relativeLayout2.setVisibility(View.VISIBLE);
                }
                else if("分行".equals(radioButton.getText())){
                    relativeLayout2.setVisibility(View.GONE);
                    relativeLayout.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    public void getData(int barn){
        if(isSimulate){
            if (datas.get(barn)!=null){
                checkInfos = datas.get(barn);
            }
        }
        else {
            try {
                checkInfos = checkInfoService.getCheckInfo(barn);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Collections.reverse(checkInfos);
    }

    public void setNull(String text){
        //mainLinear1.setVisibility(View.INVISIBLE);
        radioGroup.setVisibility(View.INVISIBLE);
        mainLinear3.setVisibility(View.INVISIBLE);
        relativeLayout.setVisibility(View.GONE);
        relativeLayout2.setVisibility(View.GONE);
        relativeLayout3.setVisibility(View.VISIBLE);

        WindowManager wManager = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        Display display = wManager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        TextView textView = new TextView(this);
        textView.setGravity(Gravity.CENTER);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)(point.y*0.5)));
        textView.setTextColor(Color.parseColor("#FF0000"));
        textView.setText(text);
        relativeLayout3.setVisibility(View.VISIBLE);
        relativeLayout3.addView(textView);
    }

    public void update(int index){

        relativeLayout.setVisibility(View.GONE);
        relativeLayout2.setVisibility(View.GONE);
        relativeLayout3.setVisibility(View.GONE);
        //mainLinear1.setVisibility(View.VISIBLE);
        radioGroup.setVisibility(View.VISIBLE);
        mainLinear3.setVisibility(View.VISIBLE);

        radioButton2.setChecked(true);

        //得到参数
        CheckInfo checkInfo = checkInfos.get(index);
        int row,column,level;
        if(isSimulate){
            row = simulateInfoService.getRow(checkInfo.getId());
            column = simulateInfoService.getColumn(checkInfo.getId());
            level = simulateInfoService.getLevel(checkInfo.getId());
        }
        else {
            BarnInfo barnInfo = barnInfoService.getBarnInfo(checkInfo.getCid()).get(0);
            row = barnInfo.getRow();
            column = barnInfo.getColumn();
            level= barnInfo.getLevel();
            if (checkInfo.getTemperature().size()!=row*column*level){
                setNull("数据异常，请查看其它时间数据或者重新检测");
                return;
            }
        }

        if (showLinear.getChildCount()!=0){
            showLinear.removeAllViews();
            showLinear2.removeAllViews();
        }
        data = ConverseUtil.ListToArray(checkInfos.get(index).getTemperature(),row,column,level);

        //统一textview
        LinearLayout.LayoutParams textLayout = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,1);
        textLayout.setMargins(10,10,10,10);

        HashMap<Integer, LevelData> levelDatas = ConverseUtil.getLevelData(checkInfos.get(index).getTemperature(),row,column,level);
        HashMap<Integer, LevelData> rowDatas = ConverseUtil.getRowData(checkInfos.get(index).getTemperature(),row,column,level);

        //relativelayout
        for(int i=0;i<row;i++){
            TextView textView = new TextView(this);
            textView.setPadding(0,15,0,0);
            textView.setText("第"+(i+1)+"行");
            textView.setTypeface(Typeface.DEFAULT_BOLD);
            showLinear.addView(textView);
            LinearLayout linearTitle = new LinearLayout(this);
            for(int j=column;j>=0;j--){
                TextView temp = new TextView(this);
                temp.setLayoutParams(textLayout);
                if (j!=column)
                    temp.setText(j+1+"列");
                temp.setMinWidth(110);
                temp.setGravity(Gravity.CENTER);
                linearTitle.addView(temp);
            }
            showLinear.addView(linearTitle);
            float rowMax = rowDatas.get(i+1).getMax();
            for(int j=0;j<level;j++){
                LinearLayout linearLayout = new LinearLayout(this);
                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                TextView temp = new TextView(this);
                temp.setLayoutParams(textLayout);
                temp.setText((j+1)+"层");
                temp.setMinWidth(110);
                temp.setGravity(Gravity.CENTER);
                linearLayout.addView(temp);
                for(int k=column-1;k>=0;k--){
                    TextView temp1 = new TextView(this);
                    temp1.setLayoutParams(textLayout);
                    float f = data[level-1-j][column-1-k][row-1-i];
                    temp1.setTextColor(getResources().getColor(R.color.black));
                    if (f==rowMax)
                        temp1.setTextColor(getResources().getColor(R.color.red));
                    temp1.setText(floatToString(f));
                    temp1.setMinWidth(110);
                    temp1.setGravity(Gravity.CENTER);
                    linearLayout.addView(temp1);
                }
                showLinear.addView(linearLayout);
            }
        }

        //relativelayout2
        for(int i=0;i<level;i++){
            TextView textView = new TextView(this);
            textView.setText("第"+(i+1)+"层");
            textView.setPadding(0,15,0,0);
            textView.setTypeface(Typeface.DEFAULT_BOLD);
            showLinear2.addView(textView);
            LinearLayout linearTitle = new LinearLayout(this);
            for(int j=column;j>=0;j--){
                TextView temp = new TextView(this);
                temp.setLayoutParams(textLayout);
                if (j!=column)
                    temp.setText(j+1+"列");
                temp.setMinWidth(110);
                temp.setGravity(Gravity.CENTER);
                linearTitle.addView(temp);
            }
            showLinear2.addView(linearTitle);
            float levelMax = levelDatas.get(level-i).getMax();
            for(int j=row-1;j>=0;j--){
                LinearLayout linearLayout = new LinearLayout(this);
                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                TextView temp = new TextView(this);
                temp.setLayoutParams(textLayout);
                temp.setText((j+1)+"行");
                temp.setMinWidth(110);
                temp.setGravity(Gravity.CENTER);
                linearLayout.addView(temp);
                for(int k=column-1;k>=0;k--){
                    TextView temp1 = new TextView(this);
                    temp1.setLayoutParams(textLayout);
                    float f = data[level-1-i][column-1-k][row-1-j];
                    temp1.setTextColor(getResources().getColor(R.color.black));
                    if (f==levelMax)
                        temp1.setTextColor(getResources().getColor(R.color.red));
                    temp1.setText(floatToString(f));
                    temp1.setMinWidth(110);
                    temp1.setGravity(Gravity.CENTER);
                    linearLayout.addView(temp1);
                }
                showLinear2.addView(linearLayout);
            }
        }

        relativeLayout2.setVisibility(View.VISIBLE);

        //表格
        //后续列
        if(linearLayout2.getChildCount()!=0){
            linearLayout2.removeAllViews();
        }
        for(int i=0;i<level;i++){
            LinearLayout dataColumn = new LinearLayout(this);
            dataColumn.setOrientation(LinearLayout.VERTICAL);
            dataColumn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.MATCH_PARENT));
            LevelData levelData = levelDatas.get(level-i);
            String[] temp = {""+(i+1)+"层",""+levelData.getMax(),""+levelData.getMin(),""+levelData.getAverage()};
            for(int j=0;j<4;j++){
                TextView text=new TextView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 0,1);
                layoutParams.setMargins(8,0,0,0);
                layoutParams.gravity = Gravity.CENTER;
                text.setLayoutParams(layoutParams);
                text.setMinWidth(110);
                text.setText(temp[j]);
                text.setGravity(Gravity.CENTER);
                dataColumn.addView(text);//添加到水平线性布局
            }
            linearLayout2.addView(dataColumn);
        }

        //右下角
        text1.setText(floatToString(ConverseUtil.getTotalAverage(checkInfos.get(index).getTemperature())));
        text2.setText(floatToString(ConverseUtil.getNormalFloat(checkInfos.get(index).getOut_temperature(),1)));
        text3.setText(floatToString(ConverseUtil.getNormalFloat(checkInfos.get(index).getOut_humidity(),1)));
        text4.setText(floatToString(ConverseUtil.getNormalFloat(checkInfos.get(index).getIn_temperature(),1)));
        text5.setText(floatToString(ConverseUtil.getNormalFloat(checkInfos.get(index).getIn_humidity(),1)));

    }

    private String floatToString(float f){
        if (f==888.0f)
            return "**";
        else
            return String.valueOf(f);
    }

}
