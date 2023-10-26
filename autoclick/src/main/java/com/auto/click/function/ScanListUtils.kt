package com.auto.click.function

import android.text.TextUtils
import android.util.ArraySet
import android.util.Log
import android.util.Log.e
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import java.lang.Exception
import java.util.HashSet

/**
 * @Author Roger
 * @Date 2023/9/11 10:17
 * @Description
 */
object ScanListUtils {

    //记录列表名称和列表下的item数据
    val map: HashMap<String, ArraySet<String>> = HashMap();

//    /**
//     * 打开列表，点击列表的名称
//     */
//    fun openList(event: AccessibilityEvent): ArraySet<String>? {
//        val categoryName = ArraySet<String>();
//        //获取点击按钮里面的全部文字，做为分类名称
//        getItemTexts(event.source, categoryName)
//        if (!map.containsKey(categoryName.toString())) {
//            map[categoryName.toString()] = ArraySet<String>();
//        }
//        return map[categoryName.toString()];
//    }

    /**
     * 扫描获取焦点的list
     */
    fun scanFocusListText(event: AccessibilityEvent?, listSet: ArraySet<String>?) {
        if (event == null || event.source == null) {
            Log.e("liao", "scanFocusListText event is null")
            return
        }
        if (listSet == null) {
            Log.e("liao", "scanFocusListText list is null")
            return
        }
        var node: AccessibilityNodeInfo? = getParentListView(event.source)
        if (node == null) {
            Log.e("liao", "getParentListView is null")
            return
        }
        var itemText = ""
        //遍例底下的item
        for (index in 0 until node.childCount) {
            itemText = getItemTexts(node.getChild(index), listSet);
            if (listSet.add(itemText)) {
                //新的
                Log.i("liao", itemText)
            }
        }
//        Log.d("liao", "result : ${listSet.size}");
    }

    /**
     * 获取节点的父布局list
     */
    private fun getParentListView(node: AccessibilityNodeInfo?): AccessibilityNodeInfo? {
        var tempNode: AccessibilityNodeInfo? = node;
        for (i in 0 until 5) {
            //只找5层，list的item应该不会嵌套这么多层吧
            // todo 如果是item中的子view获取焦点，则会找不到，后面具体在分析，修改找寻方案
            if (isListView(tempNode)) {
                return tempNode
            }
            if (tempNode == null) {
                return null;
            }
            tempNode = tempNode.parent
        }
        return null;
    }

    private fun isListView(node: AccessibilityNodeInfo?): Boolean {
        if (node == null || node.className == null) {
            return false
        }
        Log.d("liao", "isListView = " + node.className);
        if (node.className.endsWith("RecyclerView")
            || node.className.endsWith("ListView")
            || node.className.endsWith("GridView")
        ) {
            return true;
        }
        return false;
    }

    private fun getItemTexts(node: AccessibilityNodeInfo?, list: ArraySet<String>): String {
        if (node == null) {
            return ""
        }
        if (isViewGroup(node)) {
            var temp = ""
            for (index in 0 until node.childCount) {
                if (index == 0) {
                    temp = getItemTexts(node.getChild(index), list);
                } else {
                    temp = temp + " -- " + getItemTexts(node.getChild(index), list);
                }
            }
            return temp;
        } else {
            if (!TextUtils.isEmpty(node.text)) {
                return node.text.toString()
            }
        }
        return ""
    }

    /**
     * 是否是viewGroup
     */
    private fun isViewGroup(node: AccessibilityNodeInfo?): Boolean {
        if (node == null || TextUtils.isEmpty(node.className)) {
            return false
        }
        if (node.className.endsWith("Layout")
            || node.className.endsWith("CardView")
        ) {
            return true;
        }
        return false
    }

}