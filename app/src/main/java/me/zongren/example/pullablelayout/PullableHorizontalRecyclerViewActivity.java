package me.zongren.example.pullablelayout;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import me.zongren.pullablelayout.Constant.Result;
import me.zongren.pullablelayout.Inteface.OnPullListener;
import me.zongren.pullablelayout.Main.PullableComponent;
import me.zongren.pullablelayout.Main.PullableLayout;

public class PullableHorizontalRecyclerViewActivity extends ActionBarActivity {

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
        setContentView(R.layout.activity_pullable_horizontal_recycler_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final ArrayList<String> data = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            data.add("Item" + i);
        }
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.activity_pullable_recyclerView);
        final RecyclerView.Adapter<ViewHolder> adapter = new RecyclerView.Adapter<ViewHolder>() {
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text_view, parent, false);
                return new ViewHolder(view);
            }

            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {
                holder.textView.setText(data.get(position));
            }

            @Override
            public int getItemCount() {
                return data.size();
            }
        };
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        final PullableLayout pullableLayout = (PullableLayout) findViewById(R.id.activity_pullableLayout);
        pullableLayout.leftComponent.setOnPullListener(new OnPullListener() {
            @Override
            public void onCanceled(PullableComponent pullableComponent) {

            }

            @Override
            public void onLoading(PullableComponent pullableComponent) {

                new android.os.Handler(getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        data.clear();
                        for (int i = 0; i < 15; i++) {
                            data.add("Item" + i);
                        }
                        adapter.notifyDataSetChanged();
                        pullableLayout.leftComponent.finish(Result.SUCCEED);
                    }
                }, 1000);
            }

            @Override
            public void onSizeChanged(PullableComponent pullableComponent, int mSize) {

            }
        });
        pullableLayout.rightComponent.setOnPullListener(new OnPullListener() {
            @Override
            public void onCanceled(PullableComponent pullableComponent) {

            }

            @Override
            public void onLoading(PullableComponent pullableComponent) {
                new android.os.Handler(getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 15; i++) {
                            data.add("Item" + i);
                        }
                        adapter.notifyDataSetChanged();
                        pullableLayout.rightComponent.finish(Result.SUCCEED);
                    }
                }, 1000);
            }

            @Override
            public void onSizeChanged(PullableComponent pullableComponent, int mSize) {

            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public ViewHolder(View view) {
            super(view);
            textView = (TextView) view.findViewById(R.id.item_textView);
        }
    }
}
