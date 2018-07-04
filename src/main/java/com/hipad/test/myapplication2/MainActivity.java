package com.hipad.test.myapplication2;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hipad.test.myapplication.IMyAidlInterface;

public class MainActivity extends Activity implements View.OnClickListener{

    private Button set,get;
    private EditText message;
    private IMyAidlInterface iMyAidlInterface;
    private ServiceConnection conn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        conn = new connect();
        set = (Button) this.findViewById(R.id.set);
        get = (Button) this.findViewById(R.id.get);
        message = (EditText) this.findViewById(R.id.message);
        set.setOnClickListener(this);
        get.setOnClickListener(this);
        Intent intent = new Intent();
        ComponentName componentName = new ComponentName("com.hipad.test.myapplication","com.hipad.test.myapplication.MyService");
        intent.setComponent(componentName);
        this.bindService(intent,conn, Context.BIND_AUTO_CREATE);
    }

    private class connect implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if (service != null){
                iMyAidlInterface = IMyAidlInterface.Stub.asInterface(service);
            }else {
                Toast.makeText(MainActivity.this,"服务为null",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.set:
                String str = message.getText().toString();
                int i = Integer.parseInt(str);
                try {
                    iMyAidlInterface.set(i);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.get:
                try {
                    int ii = iMyAidlInterface.get();
                    Toast.makeText(MainActivity.this,"Value="+ii,Toast.LENGTH_SHORT).show();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unbindService(conn);
    }
}
