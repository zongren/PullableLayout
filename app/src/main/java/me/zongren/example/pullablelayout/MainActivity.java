package me.zongren.example.pullablelayout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ArrayList<String> data = new ArrayList<>();
        data.add("PullableTextView");
        data.add("PullableImageView");
        data.add("PullableWebView");
        data.add("PullableScrollView");
        data.add("PullableListView");
        data.add("PullableHorizontalScrollView");
        data.add("PullableHorizontalRecyclerViewActivity");
        data.add("PullableVerticalRecyclerViewActivity");
        data.add("PullableGridViewActivity");
        data.add("PullableExpandableListViewActivity");
        ListView listView = (ListView) findViewById(R.id.activity_pullable_listView);
        listView.setAdapter(new ArrayAdapter<>(this, R.layout.item_text_view, data));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                switch (i) {
                    case 0:
                        intent.setClass(adapterView.getContext(), PullableTextViewActivity.class);
                        break;
                    case 1:
                        intent.setClass(adapterView.getContext(), PullableImageViewActivity.class);
                        break;
                    case 2:
                        intent.setClass(adapterView.getContext(), PullableWebViewActivity.class);
                        break;
                    case 3:
                        intent.setClass(adapterView.getContext(), PullableScrollViewActivity.class);
                        break;
                    case 4:
                        intent.setClass(adapterView.getContext(), PullableListViewActivity.class);
                        break;
                    case 5:
                        intent.setClass(adapterView.getContext(), PullableHorizontalScrollViewActivity.class);
                        break;
                    case 6:
                        intent.setClass(adapterView.getContext(), PullableHorizontalRecyclerViewActivity.class);
                        break;
                    case 7:
                        intent.setClass(adapterView.getContext(), PullableVerticalRecyclerViewActivity.class);
                        break;
                    case 8:
                        intent.setClass(adapterView.getContext(), PullableGridViewActivity.class);
                        break;
                    case 9:
                        intent.setClass(adapterView.getContext(), PullableExpandableListViewActivity.class);
                        break;
                }
                startActivity(intent);
            }
        });
    }
}
