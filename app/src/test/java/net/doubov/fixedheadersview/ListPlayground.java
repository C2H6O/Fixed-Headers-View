package net.doubov.fixedheadersview;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ListPlayground {

    List<List<String>> data = new ArrayList<>();

    @Test
    public void testAddAllOnDoubleList() {
        List<String> row1 = new ArrayList<String>();
        row1.add("row1 item1");
        row1.add("row1 item2");

        List<String> row2 = new ArrayList<String>();
        row2.add("row2 item1");
        row2.add("row2 item2");

        data.add(row1);
        data.add(row2);

        List<List<String>> test = new ArrayList<>();
        test.addAll(data);

        assertEquals("row2 item2", test.get(1).get(1));

    }


}
