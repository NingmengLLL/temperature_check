package com.jhlkdz.temperatremeasure.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jhlkdz.temperatremeasure.R;
import com.jhlkdz.temperatremeasure.socket.Client;
import com.jhlkdz.temperatremeasure.socket.api.Response;
import com.jhlkdz.temperatremeasure.ui.activity.api.LoginInfo;
import com.jhlkdz.temperatremeasure.widget.LoadingView;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class LoginActivity extends Activity {

    private EditText edt_ip;
    private EditText edt_address;
    private EditText edt_account;
    private EditText edt_password;
    private CheckBox pwdCheckBox;
    private CheckBox settingsCheckBox;
    private Button btn_login;
    private Button btn_simulate;

    private LinearLayout ipLinear;
    private LinearLayout addressLinear;

    private LoadingView loading;
    private LoadingView loading1;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        edt_ip = findViewById(R.id.ip_input);
        edt_address = findViewById(R.id.address_input);
        edt_account = findViewById(R.id.account_input);
        edt_password = findViewById(R.id.password_input);
        pwdCheckBox = findViewById(R.id.pwd_checkbox);
        settingsCheckBox  =findViewById(R.id.settings_checkbox);
        btn_login = findViewById(R.id.btn_login);
        btn_simulate = findViewById(R.id.simulate);

        ipLinear = findViewById(R.id.ipLinear);
        addressLinear = findViewById(R.id.addressLinear);

        sp = getSharedPreferences("login", Context.MODE_PRIVATE);

        setData();

        if(sp.getInt("showSetting",-1)==0){
            settingsCheckBox.setChecked(false);
            noShowSeetings();
        }

        settingsCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    showSettings();
                }
                else {
                   noShowSeetings();
                }
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                load();

                LoginInfo loginInfo = new LoginInfo(edt_ip.getText().toString(),edt_address.getText().toString(),
                        edt_account.getText().toString(),edt_password.getText().toString());
                String temp = loginInfo.check();
                if(!"OK".equals(temp)){
                    Toast.makeText(LoginActivity.this,temp,Toast.LENGTH_LONG).show();
                }
                else {
                    checkLogin(loginInfo);
                }

            }
        });

        btn_simulate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this,"模拟登录成功",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, SimulateMainActivity.class);
                startActivity(intent);
            }
        });

        if(isLogin()){
            login();
        }
    }

    private void showSettings(){
        ipLinear.setVisibility(View.VISIBLE);
        addressLinear.setVisibility(View.VISIBLE);
    }

    private void noShowSeetings(){
        ipLinear.setVisibility(View.INVISIBLE);
        addressLinear.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void setData(){
        if(sp.getString("ip",null)==null)
            return;
        edt_ip.setText(sp.getString("ip",null));
        edt_address.setText(String.valueOf(sp.getInt("address",0)));
        edt_account.setText(String.valueOf(sp.getInt("account",0)));
        pwdCheckBox.setChecked(sp.getBoolean("savePwd",false));
        if(sp.getBoolean("savePwd",false)){
            edt_password.setText(String.valueOf(sp.getInt("password",0)));
        }

    }

    public boolean isLogin(){
        if(sp.getBoolean("hasLogin",false)){
            return true;
        }else {
            return false;
        }
    }

    private void checkLogin(final LoginInfo loginInfo){

        Callable<String> call = new Callable<String>() {
            public String call() throws Exception {
                Client client = new Client(loginInfo.getIp(),loginInfo.getPort());
                Response response = client.executeHands(loginInfo.getAddress(),loginInfo.getAccount(),loginInfo.getPassword());
                client.destroy();
                System.out.println(response);
                if(response.getOperationType()==0){
                    Message msg = new Message();
                    msg.what = 2;
                    handler.sendMessage(msg);
                }
                return "";
            }
        };

        ExecutorService executor = Executors.newCachedThreadPool();

        try {
            Future<String> future = executor.submit(call);
            future.get(2000, TimeUnit.MILLISECONDS); //任务处理超时时间设为 1 秒
        } catch (TimeoutException ex) {
            Toast.makeText(LoginActivity.this,"登录超时，请检查填写是否正确",Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 关闭线程池
        executor.shutdown();

    }

    public void login(){
        sp.edit().putBoolean("hasLogin",true).apply();
        sp.edit().putInt("showSetting",0).apply();
        Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void saveData(){
        String ip = edt_ip.getText().toString();
        sp.edit()
                .putString("ip",edt_ip.getText().toString())
                .putString("ip1",ip.substring(0,ip.indexOf(':')))
                .putInt("ip2",Integer.parseInt(ip.substring(ip.indexOf(':')+1)))
                .putInt("address", Integer.valueOf(edt_address.getText().toString()))
                .putInt("account", Integer.valueOf(edt_account.getText().toString()))
                .putInt("password", Integer.valueOf(edt_password.getText().toString()))
                .putBoolean("savePwd",pwdCheckBox.isChecked())
                .apply();
    }

    private void load(){
        loading = new LoadingView(LoginActivity.this,R.style.CustomDialog);

        Runnable work = new Runnable() {
            @Override
            public void run() {
                loading.show();
                loading.setMag("登录中");
            }
        };
        handler.post(work);

    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    break;
                case 2:
                    login();
                    saveData();
                    break;
            }
        }
    };


}
