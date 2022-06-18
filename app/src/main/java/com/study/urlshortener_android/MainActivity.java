package com.study.urlshortener_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btn;
    private EditText longUrlField, shortUrlField;
    private ListView listView;
    private List<Link> links;
    private Link link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // <Установка значений>

        btn = findViewById(R.id.button);
        longUrlField = findViewById(R.id.longUrlField);
        shortUrlField = findViewById(R.id.shortUrlField);
        listView = findViewById(R.id.listView);
        links = new ArrayList<>();

        // <Действия при нажатии>
        btn.setOnClickListener(v -> {

            // <Проверки и установка строк в кнопку>
            if (longUrlField.getText().toString().isEmpty() || !longUrlField.getText().toString().contains("."))
                btn.setText(R.string.btn_wrong_long_url);
            else if (shortUrlField.getText().toString().isEmpty())
                btn.setText(R.string.btn_wrong_short_url);
            else if (isExists(shortUrlField.getText().toString()))
                btn.setText(R.string.btn_already_exists);
            else {

                // <Действия, если всё ок>
                btn.setText(R.string.btn_init);
                String LongUrl = checkInputLong(longUrlField.getText().toString());
                String ShortUrl = shortUrlField.getText().toString();
                link = new Link(LongUrl, ShortUrl);
                links.add(link);
                LinksAdapter adapter = new LinksAdapter(MainActivity.this, R.layout.link_list, links);
                listView.setAdapter(adapter);
            }
        });
    }

    // <Подготовка ссылки>
    public String checkInputLong(String l) {
        l = l.replaceAll(" ", "");
        StringBuilder oneForLong = new StringBuilder();
        if (!l.contains("http://"))
            l = oneForLong.append("http://").append(l).toString();
        return l;
    }

    // <Проверка наличия сокращения>
    public boolean isExists (String s) {
        boolean x = false;
        for (Link l : links) {
            if (l.getUrlShort().equals(s)) {
                x = true;
                break;
            }
        }
        return x;
    }

    // <Убирание клавиатуры при потере фокуса>
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }
}