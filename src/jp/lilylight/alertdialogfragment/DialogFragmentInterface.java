/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * Copyright (C) 2013 Takuya Naraoka
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jp.lilylight.alertdialogfragment;

import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListAdapter;

public interface DialogFragmentInterface {

    /**
     * The identifier for the positive button.
     */
    public static final int BUTTON_POSITIVE = -1;

    /**
     * The identifier for the negative button. 
     */
    public static final int BUTTON_NEGATIVE = -2;

    /**
     * The identifier for the neutral button. 
     */
    public static final int BUTTON_NEUTRAL = -3;
    
    public void cancel(int id);

    public void dismiss(int id);

    /**
     * Interface used to allow the creator of a dialog to run some code when the
     * dialog is canceled.
     * <p>
     * This will only be called when the dialog is canceled, if the creator
     * needs to know when it is dismissed in general, use
     * {@link DialogInterface.OnDismissListener}.
     */
    interface OnCancelListener {
        /**
         * This method will be invoked when the dialog is canceled.
         * 
         * @param dialog The dialog that was canceled will be passed into the
         *            method.
         */
        public void onCancel(int id, DialogInterface dialog);
    }

    /**
     * Interface used to allow the creator of a dialog to run some code when the
     * dialog is dismissed.
     */
    interface OnDismissListener {
        /**
         * This method will be invoked when the dialog is dismissed.
         * 
         * @param dialog The dialog that was dismissed will be passed into the
         *            method.
         */
        public void onDismiss(int id, DialogInterface dialog);
    }

    /**
     * Interface used to allow the creator of a dialog to run some code when the
     * dialog is shown.
     */
    interface OnShowListener {
        /**
         * This method will be invoked when the dialog is shown.
         *
         * @param dialog The dialog that was shown will be passed into the
         *            method.
         */
        public void onShow(int id, DialogInterface dialog);
    }

    /**
     * Interface used to allow the creator of a dialog to run some code when an
     * item on the dialog is clicked..
     */
    interface OnClickListener {
        /**
         * This method will be invoked when a button in the dialog is clicked.
         * 
         * @param dialog The dialog that received the click.
         * @param which The button that was clicked (e.g.
         *            {@link DialogInterface#BUTTON1}) or the position
         *            of the item clicked.
         */
        /* TODO: Change to use BUTTON_POSITIVE after API council */
        public void onClick(int id, DialogInterface dialog, int which);
    }
    
    /**
     * Interface used to allow the creator of a dialog to run some code when an
     * item in a multi-choice dialog is clicked.
     */
    interface OnMultiChoiceClickListener {
        /**
         * This method will be invoked when an item in the dialog is clicked.
         * 
         * @param dialog The dialog where the selection was made.
         * @param which The position of the item in the list that was clicked.
         * @param isChecked True if the click checked the item, else false.
         */
        public void onClick(int id, DialogInterface dialog, int which, boolean isChecked);
    }
    
    /**
     * Interface definition for a callback to be invoked when a key event is
     * dispatched to this dialog. The callback will be invoked before the key
     * event is given to the dialog.
     */
    interface OnKeyListener {
        /**
         * Called when a key is dispatched to a dialog. This allows listeners to
         * get a chance to respond before the dialog.
         * 
         * @param dialog The dialog the key has been dispatched to.
         * @param keyCode The code for the physical key that was pressed
         * @param event The KeyEvent object containing full information about
         *            the event.
         * @return True if the listener has consumed the event, false otherwise.
         */
        public boolean onKey(int id, DialogInterface dialog, int keyCode, KeyEvent event);
    }

    interface ViewDelegate {
        public View getView(int id);
    }

    interface DrawableDelegate {
        public Drawable getDrawable(int id);
    }

    interface ListAdapterDelegate {
        public ListAdapter getAdapter(int id);
    }

    interface CursorDelegate {
        public Cursor getCursor(int id);
    }
}
