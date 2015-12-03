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

    String[][] contentData = new String[][] {
            {"row1 item1", "row1 item2", "row1 item3", "row1 item4", "row1 item5", "row1 item6", "row1 item7", "row1 item8", "row1 item9", "row1 item10"},
            {"row2 item1", "row2 item2", "row2 item3", "row2 item4", "row2 item5", "row2 item6", "row2 item7", "row2 item8", "row2 item9", "row2 item10"},
            {"row3 item1", "row3 item2", "row3 item3", "row3 item4", "row3 item5", "row3 item6", "row3 item7", "row3 item8", "row3 item9", "row3 item10"},
            {"row4 item1", "row4 item2", "row4 item3", "row4 item4", "row4 item5", "row4 item6", "row4 item7", "row4 item8", "row4 item9", "row4 item10"},
            {"row5 item1", "row5 item2", "row5 item3", "row5 item4", "row5 item5", "row5 item6", "row5 item7", "row5 item8", "row5 item9", "row5 item10"},
            {"row6 item1", "row6 item2", "row6 item3", "row6 item4", "row6 item5", "row6 item6", "row6 item7", "row6 item8", "row6 item9", "row6 item10"}
    };

    String[] topData = new String[] {"top1", "top2", "top3", "top4", "top5", "top6", "top7", "top8", "top9", "top10"};
    String[] sideData = new String[] {"side1", "side2", "side3", "side4", "side5", "side6"};

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

        vm.setData(topData, sideData, contentData);
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
