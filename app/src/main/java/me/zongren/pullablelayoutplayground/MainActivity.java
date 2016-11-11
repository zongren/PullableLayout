package me.zongren.pullablelayoutplayground;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListViewCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    static final List<String> mList = new ArrayList() {{
        add("PullableRecyclerViewActivity");
        add("PullableListViewActivity");
        add("PullableExpandableListViewActivity");
        add("PullableGridViewActivity");
        add("PullableScrollViewActivity");
        add("PullableHorizontalScrollViewActivity");
        add("PullableTextViewActivity");
        add("PullableWebViewActivity");
        add("PullableImageViewActivity");
    }};

    ListViewCompat mListViewCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListViewCompat = (ListViewCompat) findViewById(R.id.activity_main_listViewCompat);
        mListViewCompat.setAdapter(new ArrayAdapter(this, R.layout.activity_main_item, mList));
        mListViewCompat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String className = mList.get(position);
                String fullClassName = getPackageName() + "." + className;
                try {
                    Intent intent = new Intent(MainActivity.this, Class.forName(fullClassName));
                    startActivity(intent);
                } catch (ClassNotFoundException e) {
                    Toast.makeText(MainActivity.this,
                            "ClassNotFoundException occurred,class name is " + className,
                            Toast.LENGTH_SHORT).show();
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(MainActivity.this,
                            "ActivityNotFoundException occurred,activity name is " + className,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
