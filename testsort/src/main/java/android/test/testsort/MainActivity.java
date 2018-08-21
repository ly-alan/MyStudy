package android.test.testsort;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ascSort();
        descSort();
        switchSort();
        testSort();
        testSort00();
        testSort01();
        detailSort();
        detailSort01();
        detailSort02();
        //对于排序来讲，你可以认为当返回1时，指定的数和参数会进行交换，否则不会交换数据
    }

    /**
     * 正序
     * 1 : 2 : 3 : 4 : 5 : 6 : 7 :
     */
    private void ascSort() {
        Log.v("liao", "ascSort");
        List<String> list = createTestList();
        Collections.sort(list);
        showListLog(list);
    }

    /**
     * 倒序
     * 7 : 6 : 5 : 4 : 3 : 2 : 1 :
     */
    private void descSort() {
        Log.v("liao", "descSort");
        List<String> list = createTestList();
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o2.compareTo(o1);
            }
        });
        showListLog(list);
    }

    /**
     * 倒序输出
     * 6 : 4 : 2 : 7 : 5 : 3 : 1 :
     */
    private void switchSort() {
        Log.v("liao", "reverse");
        List<String> list = createTestList();
        Collections.reverse(list);
        showListLog(list);
    }

    /**
     * 1 : 3 : 5 : 7 : 2 : 4 : 6 :
     */
    private void testSort() {
        Log.v("liao", "return 1");
        List<String> list = createTestList();
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return 1;
            }
        });
        showListLog(list);
    }

    /**
     * 1 : 3 : 5 : 7 : 2 : 4 : 6 :
     */
    private void testSort00() {
        Log.v("liao", "return 0");
        List<String> list = createTestList();
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return 0;
            }
        });
        showListLog(list);
    }

    /**
     * 倒序输出
     * 6 : 4 : 2 : 7 : 5 : 3 : 1 :
     */
    private void testSort01() {
        Log.v("liao", "return -1");
        List<String> list = createTestList();
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return -1;
            }
        });
        showListLog(list);
    }

    /**
     * 详情
     * 2 : 1 : 3 : 7 : 6 : 5 : 4 :
     */
    private void detailSort() {
        Log.v("liao", "detail");
        final List<String> list = createTestList();
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                String m = "3";
                //以m为分界线,m在正中间，比m小的放前面，比m大的放后面，倒序排列
                if (o1.compareTo(m) > 0 && o2.compareTo(m) > 0) {
                    return o2.compareTo(o1);
                } else if (o1.compareTo(m) < 0 && o2.compareTo(m) < 0) {
                    return o2.compareTo(o1);
                } else {
                    return o1.compareTo(o2);
                }
            }
        });
        showListLog(list);
    }

    /**
     * 4 : 3 : 2 : 1 : 7 : 6 : 5 :
     */
    private void detailSort01() {
        Log.v("liao", "detail 01");
        final List<String> list = createTestList();
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                String m = "5";
                //以m为分界线，m和比m大的放后面
                if (o1.compareTo(m) >= 0 && o2.compareTo(m) >= 0) {
                    return o2.compareTo(o1);
                } else if (o1.compareTo(m) < 0 && o2.compareTo(m) < 0) {
                    return o2.compareTo(o1);
                } else {
                    return o1.compareTo(o2);
                }
            }
        });
        showListLog(list);
    }


    /**
     * 5 : 4 : 3 : 2 : 1 : 7 : 6 :
     */
    private void detailSort02() {
        Log.v("liao", "detail 02");
        final List<String> list = createTestList();
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                String m = "5";
                //以m为分界线，m和比m小的放前面
                if (o1.compareTo(m) > 0 && o2.compareTo(m) > 0) {
                    return o2.compareTo(o1);
                } else if (o1.compareTo(m) <= 0 && o2.compareTo(m) <= 0) {
                    return o2.compareTo(o1);
                } else {
                    return o1.compareTo(o2);
                }
            }
        });
        showListLog(list);
    }


    private List<String> createTestList() {
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("3");
        list.add("5");
        list.add("7");
        list.add("2");
        list.add("4");
        list.add("6");
        return list;
    }

    private void showListLog(List<String> list) {
        StringBuffer sb = new StringBuffer();
        for (String string : list) {
            sb.append(string + " : ");
        }
        Log.i("liao", sb.toString());
    }

}
