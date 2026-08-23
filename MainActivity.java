package com.example.txrobotai;

import android.app.*;
import android.os.*;
import android.graphics.Color;
import android.view.*;
import android.widget.*;
import java.util.*;

public class MainActivity extends Activity {
    ArrayList<String> h = new ArrayList<>();
    TextView session, history, result, confidence;
    EditText input;

    @Override public void onCreate(Bundle b) {
        super.onCreate(b); setContentView(R.layout.activity_main);
        session=findViewById(R.id.session); history=findViewById(R.id.history);
        result=findViewById(R.id.result); confidence=findViewById(R.id.confidence);
        input=findViewById(R.id.input);

        findViewById(R.id.add).setOnClickListener(v -> add());
        findViewById(R.id.analyze).setOnClickListener(v -> analyze());
        findViewById(R.id.reset).setOnClickListener(v -> { h.clear(); update(); });

        update();
    }

    void add() {
        String s=input.getText().toString().trim().toUpperCase(Locale.US);
        if(!s.equals("T") && !s.equals("X")) {
            Toast.makeText(this,"Chỉ nhập T hoặc X",Toast.LENGTH_SHORT).show(); return;
        }
        h.add(s); input.setText(""); update();
    }

    void update() {
        session.setText("PHIÊN #" + (h.size()+1));
        StringBuilder sb=new StringBuilder("Lịch sử: ");
        for(String s:h) sb.append(s).append(" ");
        history.setText(sb.toString());
    }

    void analyze() {
        if(h.isEmpty()) { Toast.makeText(this,"Hãy nhập vài kết quả trước",Toast.LENGTH_SHORT).show(); return; }
        int t=0,x=0;
        for(String s:h) { if(s.equals("T")) t++; else x++; }
        // Weighted recent trend: newer entries count more, but this is only a statistic.
        double wt=0, wx=0, w=1;
        for(int i=h.size()-1;i>=0 && i>=Math.max(0,h.size()-8);i--,w++) {
            if(h.get(i).equals("T")) wt+=w; else wx+=w;
        }
        double pt=(wt+1)/(wt+wx+2)*100.0;
        double px=100.0-pt;
        String pred=pt>=px ? "TÀI" : "XỈU";
        result.setText("DỰ BÁO THAM KHẢO: "+pred);
        confidence.setText(String.format(Locale.US,"Tài %.0f%%  •  Xỉu %.0f%%
Thống kê: %d Tài / %d Xỉu",pt,px,t,x));
    }
}
