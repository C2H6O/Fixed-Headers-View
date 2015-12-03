package net.doubov.fixedheadersview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import net.doubov.fixedheadersview.adapters.ContentAdapter;
import net.doubov.fixedheadersview.adapters.SideAdapter;
import net.doubov.fixedheadersview.adapters.TopAdapter;

public class ScrollingActivity extends AppCompatActivity {

    FixedHeadersViewManager<String, String, String> vm;

    String[][] contentData;

    String[] topData;
    String[] sideData;

    public static final int COLS = 20;
    public static final int ROWS = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        RelativeLayout container = (RelativeLayout) findViewById(R.id.container);

        vm = new FixedHeadersViewManager.Builder<String, String, String>()
                .container(container)
                .context(this)
                .contentAdapter(new ContentAdapter())
                .topAdapter(new TopAdapter())
                .sideAdapter(new SideAdapter())
                .build();

        generateFakeData();

        vm.setData(topData, sideData, contentData);
    }

    private void generateFakeData() {
        contentData = new String[ROWS][COLS];
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                contentData[row][col] = "row" + row + " item" + col;
            }
        }

        topData = new String[COLS];
        for (int col = 0; col < COLS; col++) {
            topData[col] = "top" + col;
        }

        sideData = new String[ROWS];
        for (int row = 0; row < ROWS; row++) {
            sideData[row] = "side" + row;
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
