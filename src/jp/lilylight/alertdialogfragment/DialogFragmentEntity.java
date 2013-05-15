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
import android.view.KeyEvent;

class DialogFragmentEntity implements DialogInterface {

    final int mId;
    final DialogFragmentInterface mInterface;

    DialogFragmentEntity(int id, DialogFragmentInterface dfi) {
        mId = id;
        mInterface = dfi;
    }

    @Override
    public void cancel() {
        mInterface.cancel(mId);
    }

    @Override
    public void dismiss() {
        mInterface.dismiss(mId);
    }

    static class OnCancelListener implements DialogInterface.OnCancelListener {

        final int mId;
        final DialogFragmentInterface.OnCancelListener mListener;

        OnCancelListener(int id, DialogFragmentInterface.OnCancelListener listener) {
            mId = id;
            mListener = listener;
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            mListener.onCancel(mId, dialog);
        }

    }

    static class OnDismissListener implements DialogInterface.OnDismissListener {

        final int mId;
        final DialogFragmentInterface.OnDismissListener mListener;

        OnDismissListener(int id, DialogFragmentInterface.OnDismissListener listener) {
            mId = id;
            mListener = listener;
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            mListener.onDismiss(mId, dialog);
        }

    }

    static class OnShowListener implements DialogInterface.OnShowListener {

        final int mId;
        final DialogFragmentInterface.OnShowListener mListener;

        OnShowListener(int id, DialogFragmentInterface.OnShowListener listener) {
            mId = id;
            mListener = listener;
        }

        @Override
        public void onShow(DialogInterface dialog) {
            mListener.onShow(mId, dialog);
        }

    }

    static class OnClickListener implements DialogInterface.OnClickListener {

        final int mId;
        final DialogFragmentInterface.OnClickListener mListener;

        OnClickListener(int id, DialogFragmentInterface.OnClickListener listener) {
            mId = id;
            mListener = listener;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            mListener.onClick(mId, dialog, which);
        }

    }

    static class OnMultiChoiceClickListener implements DialogInterface.OnMultiChoiceClickListener {

        final int mId;
        final DialogFragmentInterface.OnMultiChoiceClickListener mListener;

        OnMultiChoiceClickListener(int id, DialogFragmentInterface.OnMultiChoiceClickListener listener) {
            mId = id;
            mListener = listener;
        }

        @Override
        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
            mListener.onClick(mId, dialog, which, isChecked);
        }

    }

    static class OnKeyListener implements DialogInterface.OnKeyListener {

        final int mId;
        final DialogFragmentInterface.OnKeyListener mListener;

        OnKeyListener(int id, DialogFragmentInterface.OnKeyListener listener) {
            mId = id;
            mListener = listener;
        }

        @Override
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            return mListener.onKey(mId, dialog, keyCode, event);
        }

    }

}
