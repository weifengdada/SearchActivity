package com.example.acer.searchactivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TagFlowLayout mFlowLayout;
    private EditText editText;
    private LayoutInflater mInflater;
    private List<String> strings;

    private Button button;

    //流式布局的子布局
    private TextView tv;
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:

                    mFlowLayout.setAdapter(new TagAdapter<String>(strings) {
                        @Override
                        public View getView(FlowLayout parent, int position, String s) {
                            tv = (TextView) mInflater.inflate(R.layout.tv, mFlowLayout, false);
                            tv.setText(s);
                            return tv;
                        }
                    });
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private TextView tv_cancel;
    private ImageView image;
    private String aa;
    private StringBuilder sb;
    private SharedPreferences.Editor mEdit1;
    private SharedPreferences sp;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.et_search);
        mInflater = LayoutInflater.from(this);
        mFlowLayout = findViewById(R.id.id_flowlayout);
        strings = new ArrayList<>();
        loadArray(strings);
        handler.sendEmptyMessageDelayed(1, 0);
        tv_cancel = findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
            }
        });
        editText.setOnKeyListener(new View.OnKeyListener() {

            private File file1;
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    aa = editText.getText().toString().trim();
                    if (!strings.contains(aa)) {
                        strings.add(aa);
                        saveArray(strings);
                        //通知handler更新UI
                        handler.sendEmptyMessageDelayed(1, 0);
                    }
                }
                return false;
            }
        });
        image = findViewById(R.id.dele);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEdit1.remove("Status_size");
                mEdit1.commit();
                strings.clear();
                handler.sendEmptyMessageDelayed(1, 0);
            }
        });
        mFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                //在这里面可以设置跳转
                Toast.makeText(MainActivity.this, strings.get(position), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }
    public void loadArray(List<String> list) {
        SharedPreferences mSharedPreference1 = this.getSharedPreferences("ingoreList", this.MODE_PRIVATE);
        list.clear();
        int size = mSharedPreference1.getInt("Status_size", 0);
        for(int i=0;i<size;i++) {
            list.add(mSharedPreference1.getString("Status_" + i, null));
        }
    }
    public boolean saveArray(List<String> list) {
        sp = this.getSharedPreferences("ingoreList", this.MODE_PRIVATE);
        mEdit1 = sp.edit();
        mEdit1.putInt("Status_size",list.size());
        for(int i=0;i<list.size();i++) {
            mEdit1.remove("Status_" + i);
            mEdit1.putString("Status_" + i, list.get(i));
        }
        return mEdit1.commit();
    }

}
