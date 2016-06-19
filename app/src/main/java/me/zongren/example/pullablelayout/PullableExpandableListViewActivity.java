package me.zongren.example.pullablelayout;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;

import me.zongren.pullablelayout.Constant.Result;
import me.zongren.pullablelayout.Inteface.OnPullListener;
import me.zongren.pullablelayout.Main.PullableComponent;
import me.zongren.pullablelayout.Main.PullableLayout;

public class PullableExpandableListViewActivity extends ActionBarActivity {

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pullable_expandable_list_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final ArrayList<String> groupData = new ArrayList<>();
        final ArrayList<String> childrenData = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            groupData.add("Group" + i);
        }
        for (int i = 0; i < 5; i++) {
            childrenData.add("Children" + i);
        }
        final BaseExpandableListAdapter adapter = new BaseExpandableListAdapter() {
            @Override
            public int getGroupCount() {
                return groupData.size();
            }

            @Override
            public int getChildrenCount(int i) {
                return childrenData.size();
            }

            @Override
            public Object getGroup(int i) {
                return groupData.get(i);
            }

            @Override
            public Object getChild(int i, int i1) {
                return childrenData.get(i1);
            }

            @Override
            public long getGroupId(int i) {
                return i;
            }

            @Override
            public long getChildId(int i, int i1) {
                return i1;
            }

            @Override
            public boolean hasStableIds() {
                return false;
            }

            @Override
            public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
                TextView textView = (TextView) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_text_view, viewGroup, false);
                textView.setText(groupData.get(i));
                return textView;
            }

            @Override
            public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
                TextView textView = (TextView) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_child_text_view, viewGroup, false);
                textView.setText(childrenData.get(i1));
                return textView;
            }

            @Override
            public boolean isChildSelectable(int i, int i1) {
                return false;
            }
        };
        final ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.activity_pullable_expandableListView);
        expandableListView.setAdapter(adapter);
        final PullableLayout pullableLayout = (PullableLayout) findViewById(R.id.activity_pullableLayout);
        pullableLayout.topComponent.setOnPullListener(new OnPullListener() {
            @Override
            public void onCanceled(PullableComponent pullableComponent) {

            }

            @Override
            public void onLoading(PullableComponent pullableComponent) {

                new android.os.Handler(getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        groupData.clear();
                        childrenData.clear();
                        for (int i = 0; i < 5; i++) {
                            groupData.add("Group" + i);
                        }
                        for (int i = 0; i < 5; i++) {
                            childrenData.add("Children" + i);
                        }
                        adapter.notifyDataSetChanged();
                        pullableLayout.topComponent.finish(Result.SUCCEED);
                    }
                }, 1000);
            }

            @Override
            public void onSizeChanged(PullableComponent pullableComponent, int mSize) {

            }
        });
        pullableLayout.bottomComponent.setOnPullListener(new OnPullListener() {
            @Override
            public void onCanceled(PullableComponent pullableComponent) {

            }

            @Override
            public void onLoading(PullableComponent pullableComponent) {
                new android.os.Handler(getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        int groupCount = groupData.size();
                        for (int i = 0; i < 5; i++) {
                            groupData.add("Group" + (i + groupCount));
                        }
                        adapter.notifyDataSetChanged();
                        pullableLayout.bottomComponent.finish(Result.SUCCEED);
                    }
                }, 1000);
            }

            @Override
            public void onSizeChanged(PullableComponent pullableComponent, int mSize) {

            }
        });
    }
}
