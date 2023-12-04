package com.roger.main.view;

import android.content.Context;
import android.os.Build;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

/**
 * 长按能够编辑的控件。可能可编辑状态下布局不一样，需要自己调整。
 */
public class PressedEditView extends AppCompatEditText {

    int inputType;

    IActionDoneListener doneListener;


    public PressedEditView(Context context) {
        super(context);
        init();
    }

    public PressedEditView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PressedEditView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inputType = getInputType();
        setImeOptions(EditorInfo.IME_ACTION_DONE);//设置确认键
        setEditTextEnable(false);
        setCustomActionModeCallback();//禁止长按复制
        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                setEditTextEnable(true);
                return false;
            }
        });
        setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE ||
                        i == EditorInfo.IME_ACTION_GO ||
                        i == EditorInfo.IME_ACTION_SEND) {
                    KeyBoardUtils.hideKeyboard(textView);
                    clickActionDone(textView);
                    return true;
                }
                return false;
            }
        });
        setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    //焦点丢失保存数据
                    clickActionDone((TextView) v);
                }
            }
        });
    }

    public void clickActionDone(TextView textView) {
        setEditTextEnable(false);
        //执行对应的操作
        if (doneListener != null) {
            doneListener.done(textView.getText().toString().trim());
        }
    }

    public IActionDoneListener getDoneListener() {
        return doneListener;
    }

    public void setDoneListener(IActionDoneListener doneListener) {
        this.doneListener = doneListener;
    }

    /**
     * 是否可编辑
     *
     * @param editable
     */
    private void setEditTextEnable(boolean editable) {
        setFocusable(editable);
        setFocusableInTouchMode(editable);
        setInputType(editable ? (inputType > 0 ? inputType : InputType.TYPE_CLASS_TEXT) : InputType.TYPE_NULL);
    }

    /**
     * 设置文本选择Action的回调对象
     */
    private void setCustomActionModeCallback() {
        super.setCustomSelectionActionModeCallback(new NoCopyCutShareAction());
    }

    /**
     * 禁止edittext的长按复制
     */
    private static class NoCopyCutShareAction implements ActionMode.Callback {

        /**
         * Called when action mode is first created. The menu supplied will be used to
         * generate action buttons for the action mode.
         *
         * @param mode ActionMode being created
         * @param menu Menu used to populate action buttons
         * @return true if the action mode should be created, false if entering this
         * mode should be aborted.
         */
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // 在菜单创建阶段删除复制、剪切、分享菜单项
            menu.removeItem(android.R.id.copy);
            menu.removeItem(android.R.id.cut);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                menu.removeItem(android.R.id.shareText);
            }
            return true;
        }

        /**
         * Called to refresh an action mode's action menu whenever it is invalidated.
         *
         * @param mode ActionMode being prepared
         * @param menu Menu used to populate action buttons
         * @return true if the menu or action mode was updated, false otherwise.
         */
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return true;
        }

        /**
         * Called to report a user click on an action button.
         *
         * @param mode The current ActionMode
         * @param item The item that was clicked
         * @return true if this callback handled the event, false if the standard MenuItem
         * invocation should continue.
         */
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            return false;
        }

        /**
         * Called when an action mode is about to be exited and destroyed.
         *
         * @param mode The current ActionMode being destroyed
         */
        @Override
        public void onDestroyActionMode(ActionMode mode) {

        }
    }

    /**
     * 文本选择菜单项选中执行回调
     *
     * @return false对当前菜单项不执行
     */
    @Override
    public boolean onTextContextMenuItem(int id) {
        if (id == android.R.id.copy || id == android.R.id.cut) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && id == android.R.id.shareText) {
            return false;
        }
        return super.onTextContextMenuItem(id);
    }

}
