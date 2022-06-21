package com.study.urlshortener_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.study.urlshortener_android.Models.Link;
import com.study.urlshortener_android.Models.User;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Button btn;
    private ImageView btn_delete, btn_logout;
    private EditText longUrlField, shortUrlField;
    private ListView listView;
    private ArrayList<Link> allLinks;
    private static final String ALLOWED_CHARACTERS ="0123456789qwertyuiopasdfghjklzxcvbnm";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://urlshortener-android-17eb1-default-rtdb.firebaseio.com/");
        final DatabaseReference table = database.getReference("Link");

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        btn = findViewById(R.id.button);
        btn_delete = findViewById(R.id.btn_delete);
        longUrlField = findViewById(R.id.longUrlField);
        shortUrlField = findViewById(R.id.shortUrlField);
        listView = findViewById(R.id.listView);
        btn_logout = findViewById(R.id.logout_btn);

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });


        table.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    allLinks = new ArrayList<>();
                    ArrayList<String> allKeys = new ArrayList<>();
                    for (DataSnapshot obj : snapshot.getChildren()) {
                        Link link = obj.getValue(Link.class);
                        if (link.getUid().equals(currentUser.getUid())) {
                            allLinks.add(link);
                            allKeys.add(obj.getKey());
                        }
                    }
                    LinksAdapter adapter = new LinksAdapter(MainActivity.this, R.layout.link_list, allLinks, allKeys);
                    listView.setAdapter(adapter);
                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "No internet connection", Toast.LENGTH_LONG).show();
            }
        });


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String random = getRandomString(28);
                String LongUrl = checkInputLong(longUrlField.getText().toString());
                String ShortUrl = shortUrlField.getText().toString();
                Link link = new Link(LongUrl, ShortUrl, currentUser.getUid());

                if (longUrlField.getText().toString().isEmpty() || !longUrlField.getText().toString().contains("."))
                    Toast.makeText(MainActivity.this, R.string.btn_wrong_long_url, Toast.LENGTH_LONG).show();
                else if (shortUrlField.getText().toString().isEmpty())
                    Toast.makeText(MainActivity.this, R.string.btn_wrong_short_url, Toast.LENGTH_LONG).show();
                else if (isExists(ShortUrl))
                    Toast.makeText(MainActivity.this, R.string.btn_already_exists, Toast.LENGTH_SHORT).show();
                else if (ShortUrl.length() > 10)
                    Toast.makeText(MainActivity.this, "Short URL is too long", Toast.LENGTH_LONG).show();
                else {
                    table.child(random).setValue(link);
                    Toast.makeText(MainActivity.this, "Added!", Toast.LENGTH_LONG).show();
                    shortUrlField.setText("");
                    longUrlField.setText("");
                }
            }
        });
    }

    private void logoutUser() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    public String checkInputLong(String l) {
        l = l.replaceAll(" ", "");
        StringBuilder oneForLong = new StringBuilder();
        if (!l.contains("http://"))
            l = oneForLong.append("http://").append(l).toString();
        return l;
    }

    public boolean isExists (String s) {
        boolean x = false;
        for (Link l : allLinks) {
            if (l.getUrlShort().equals(s)) {
                x = true;
                break;
            }
        }
        return x;
    }

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

    private static String getRandomString(final int sizeOfRandomString)
    {
        final Random random = new Random();
        final StringBuilder sb = new StringBuilder(sizeOfRandomString);
        for(int i = 0; i < sizeOfRandomString; ++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }

}