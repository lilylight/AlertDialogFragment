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

import jp.lilylight.alertdialogfragment.DialogFragmentInterface.CursorDelegate;
import jp.lilylight.alertdialogfragment.DialogFragmentInterface.DrawableDelegate;
import jp.lilylight.alertdialogfragment.DialogFragmentInterface.ListAdapterDelegate;
import jp.lilylight.alertdialogfragment.DialogFragmentInterface.OnCancelListener;
import jp.lilylight.alertdialogfragment.DialogFragmentInterface.OnClickListener;
import jp.lilylight.alertdialogfragment.DialogFragmentInterface.OnKeyListener;
import jp.lilylight.alertdialogfragment.DialogFragmentInterface.OnMultiChoiceClickListener;
import jp.lilylight.alertdialogfragment.DialogFragmentInterface.ViewDelegate;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListAdapter;

public class AlertDialogFragment extends DialogFragment {

    public static final String TAG = "AlertDialogFragment";

    enum Set {
        TITLE {
            @Override
            void setValue(int id, Object parent, AlertDialog.Builder builder, Bundle args) {
                builder.setTitle(args.getCharSequence(getArgKey(0)));
            }

            @Override
            Bundle getArgments(Object... args) {
                Bundle b = new Bundle();
                b.putCharSequence(getArgKey(0), (CharSequence) args[0]);
                return b;
            }
        },
        CUSTOM_TITLE {
            @Override
            void setValue(int id, Object parent, AlertDialog.Builder builder, Bundle args) {
                builder.setCustomTitle(findView(id, parent, args, getArgKey(0)));
            }

            @Override
            Bundle getArgments(Object... args) {
                Bundle b = new Bundle();
                b.putBoolean(getArgKey(0), isFragment(args[0]));
                return b;
            }
        },
        MESSAGE {
            @Override
            void setValue(int id, Object parent, AlertDialog.Builder builder, Bundle args) {
                builder.setMessage(args.getCharSequence(getArgKey(0)));
            }

            @Override
            Bundle getArgments(Object... args) {
                Bundle b = new Bundle();
                b.putCharSequence(getArgKey(0), (CharSequence) args[0]);
                return b;
            }
        },
        ICON {
            @Override
            void setValue(int id, Object parent, AlertDialog.Builder builder, Bundle args) {
                if (args.get(getArgKey(0)) instanceof Integer) {
                    builder.setIcon(args.getInt(getArgKey(0)));
                }
                else if (args.get(getArgKey(0)) instanceof Boolean) {
                    builder.setIcon(findDrawable(id, parent, args, getArgKey(0)));
                }
            }

            @Override
            Bundle getArgments(Object... args) {
                Bundle b = new Bundle();
                if (args[0] instanceof Integer) {
                    b.putInt(getArgKey(0), (Integer) args[0]);
                }
                else if (args[0] instanceof DrawableDelegate) {
                    b.putBoolean(getArgKey(0), isFragment(args[0]));
                }
                return b;
            }
        },
        POSITIVE_BUTTON {
            @Override
            void setValue(int id, Object parent, AlertDialog.Builder builder, Bundle args) {
                builder.setPositiveButton(
                        args.getCharSequence(getArgKey(0)),
                        findOnClickListener(id, parent, args, getArgKey(1)));
            }

            @Override
            Bundle getArgments(Object... args) {
                Bundle b = new Bundle();
                b.putCharSequence(getArgKey(0), (CharSequence) args[0]);
                b.putBoolean(getArgKey(1), isFragment(args[1]));
                return b;
            }
        },
        NEGATIVE_BUTTON {
            @Override
            void setValue(int id, Object parent, AlertDialog.Builder builder, Bundle args) {
                builder.setNegativeButton(
                        args.getCharSequence(getArgKey(0)),
                        findOnClickListener(id, parent, args, getArgKey(1)));
            }

            @Override
            Bundle getArgments(Object... args) {
                Bundle b = new Bundle();
                b.putCharSequence(getArgKey(0), (CharSequence) args[0]);
                b.putBoolean(getArgKey(1), isFragment(args[1]));
                return b;
            }
        },
        NEUTRAL_BUTTON {
            @Override
            void setValue(int id, Object parent, AlertDialog.Builder builder, Bundle args) {
                builder.setNeutralButton(
                        args.getCharSequence(getArgKey(0)),
                        findOnClickListener(id, parent, args, getArgKey(1)));
            }

            @Override
            Bundle getArgments(Object... args) {
                Bundle b = new Bundle();
                b.putCharSequence(getArgKey(0), (CharSequence) args[0]);
                b.putBoolean(getArgKey(1), isFragment(args[1]));
                return b;
            }
        },
        CANCELABLE {
            @Override
            void setValue(int id, Object parent, AlertDialog.Builder builder, Bundle args) {
                ((AlertDialogFragment) parent).setCancelable(args.getBoolean(getArgKey(0)));
            }

            @Override
            Bundle getArgments(Object... args) {
                Bundle b = new Bundle();
                b.putBoolean(getArgKey(0), (Boolean) args[0]);
                return b;
            }
        },
        ON_CANCEL_LISTENER {
            @Override
            void setValue(int id, Object parent, AlertDialog.Builder builder, Bundle args) {
                // onCancel()で処理を行う
            }

            @Override
            Bundle getArgments(Object... args) {
                Bundle b = new Bundle();
                b.putBoolean(getArgKey(0), isFragment(args[0]));
                return b;
            }
        },
        ON_KEY_LISTENER {
            @Override
            void setValue(int id, Object parent, AlertDialog.Builder builder, Bundle args) {
                builder.setOnKeyListener(
                        findOnKeyListener(id, parent, args, getArgKey(0)));
            }

            @Override
            Bundle getArgments(Object... args) {
                Bundle b = new Bundle();
                b.putBoolean(getArgKey(0), isFragment(args[0]));
                return b;
            }
        },
        ITEMS {
            @Override
            void setValue(int id, Object parent, AlertDialog.Builder builder, Bundle args) {
                builder.setItems(
                        args.getCharSequenceArray(getArgKey(0)),
                        findOnClickListener(id, parent, args, getArgKey(1)));
            }

            @Override
            Bundle getArgments(Object... args) {
                Bundle b = new Bundle();
                b.putCharSequenceArray(getArgKey(0), (CharSequence[]) args[0]);
                b.putBoolean(getArgKey(1), isFragment(args[1]));
                return b;
            }
        },
        ADAPTER {
            @Override
            void setValue(int id, Object parent, AlertDialog.Builder builder, Bundle args) {
                builder.setAdapter(
                        findListAdapter(id, parent, args, getArgKey(0)),
                        findOnClickListener(id, parent, args, getArgKey(1)));
            }

            @Override
            Bundle getArgments(Object... args) {
                Bundle b = new Bundle();
                b.putBoolean(getArgKey(0), isFragment(args[0]));
                b.putBoolean(getArgKey(1), isFragment(args[1]));
                return b;
            }
        },
        CURSOR {
            @Override
            void setValue(int id, Object parent, AlertDialog.Builder builder, Bundle args) {
                builder.setCursor(
                        findCursor(id, parent, args, getArgKey(0)),
                        findOnClickListener(id, parent, args, getArgKey(1)),
                        args.getString(getArgKey(2)));
            }

            @Override
            Bundle getArgments(Object... args) {
                Bundle b = new Bundle();
                b.putBoolean(getArgKey(0), isFragment(args[0]));
                b.putBoolean(getArgKey(1), isFragment(args[1]));
                b.putString(getArgKey(2), (String) args[2]);
                return b;
            }
        },
        MULTI_CHOICE_ITEMS {
            @Override
            void setValue(int id, Object parent, AlertDialog.Builder builder, Bundle args) {
                if (args.get(getArgKey(0)) instanceof CharSequence[]) {
                    builder.setMultiChoiceItems(
                            args.getCharSequenceArray(getArgKey(0)),
                            args.getBooleanArray(getArgKey(1)),
                            findOnMultiChoiceClickListener(id, parent, args, getArgKey(2)));
                }
                else if (args.get(getArgKey(0)) instanceof Boolean) {
                    builder.setMultiChoiceItems(
                            findCursor(id, parent, args, getArgKey(0)),
                            args.getString(getArgKey(1)),
                            args.getString(getArgKey(2)),
                            findOnMultiChoiceClickListener(id, parent, args, getArgKey(3)));
                }
            }

            @Override
            Bundle getArgments(Object... args) {
                Bundle b = new Bundle();
                if (args[0] instanceof CharSequence[]) {
                    b.putCharSequenceArray(getArgKey(0), (CharSequence[]) args[0]);
                    b.putBooleanArray(getArgKey(1), (boolean[]) args[1]);
                    b.putBoolean(getArgKey(2), isFragment(args[2]));
                }
                else if (args[0] instanceof Cursor) {
                    b.putBoolean(getArgKey(0), isFragment(args[0]));
                    b.putString(getArgKey(1), (String) args[1]);
                    b.putString(getArgKey(2), (String) args[2]);
                    b.putBoolean(getArgKey(3), isFragment(args[3]));
                }
                return b;
            }
        },
        SINGLE_CHOICE_ITEMS {
            @Override
            void setValue(int id, Object parent, AlertDialog.Builder builder, Bundle args) {
                if (args.get(getArgKey(0)) instanceof CharSequence[]) {
                    builder.setSingleChoiceItems(
                            args.getCharSequenceArray(getArgKey(0)),
                            args.getInt(getArgKey(1)),
                            findOnClickListener(id, parent, args, getArgKey(2)));
                }
                else if (args.get(getArgKey(2)) instanceof String) {
                    builder.setSingleChoiceItems(
                            findCursor(id, parent, args, getArgKey(0)),
                            args.getInt(getArgKey(1)),
                            args.getString(getArgKey(2)),
                            findOnClickListener(id, parent, args, getArgKey(2)));
                }
                else if (args.get(getArgKey(2)) instanceof Boolean) {
                    builder.setSingleChoiceItems(
                            findListAdapter(id, parent, args, getArgKey(0)),
                            args.getInt(getArgKey(1)),
                            findOnClickListener(id, parent, args, getArgKey(2)));
                }
            }

            @Override
            Bundle getArgments(Object... args) {
                Bundle b = new Bundle();
                if (args[0] instanceof CharSequence[]) {
                    b.putCharSequenceArray(getArgKey(0), (CharSequence[]) args[0]);
                    b.putInt(getArgKey(1), (Integer) args[1]);
                    b.putBoolean(getArgKey(2), isFragment(args[2]));
                }
                else if (args[0] instanceof Cursor) {
                    b.putBoolean(getArgKey(0), isFragment(args[0]));
                    b.putInt(getArgKey(1), (Integer) args[1]);
                    b.putString(getArgKey(2), (String) args[2]);
                    b.putBoolean(getArgKey(3), isFragment(args[3]));
                }
                else if (args[0] instanceof ListAdapter) {
                    b.putBoolean(getArgKey(0), isFragment(args[0]));
                    b.putInt(getArgKey(1), (Integer) args[1]);
                    b.putBoolean(getArgKey(2), isFragment(args[2]));
                }
                return b;
            }
        },
        ON_ITEM_SELECTED_LISTENER {
            @Override
            void setValue(int id, Object parent, AlertDialog.Builder builder, Bundle args) {
                builder.setOnItemSelectedListener(
                        findOnItemSelectedListener(id, parent, args, getArgKey(0)));
            }

            @Override
            Bundle getArgments(Object... args) {
                Bundle b = new Bundle();
                b.putBoolean(getArgKey(0), isFragment(args[0]));
                return b;
            }
        },
        VIEW {
            @Override
            void setValue(int id, Object parent, AlertDialog.Builder builder, Bundle args) {
                builder.setView(findView(id, parent, args, getArgKey(0)));
            }

            @Override
            Bundle getArgments(Object... args) {
                Bundle b = new Bundle();
                b.putBoolean(getArgKey(0), isFragment(args[0]));
                return b;
            }
        },
        INVERSE_BACKGROUND_FORCED {
            @Override
            void setValue(int id, Object parent, AlertDialog.Builder builder, Bundle args) {
                builder.setInverseBackgroundForced(args.getBoolean(getArgKey(0)));
            }

            @Override
            Bundle getArgments(Object... args) {
                Bundle b = new Bundle();
                b.putBoolean(getArgKey(0), (Boolean) args[0]);
                return b;
            }
        },
        ID {
            @Override
            void setValue(int id, Object parent, AlertDialog.Builder builder, Bundle args) {
            }

            @Override
            Bundle getArgments(Object... args) {
                Bundle b = new Bundle();
                b.putInt(getArgKey(0), (Integer) args[0]);
                return b;
            }
        };
        abstract void setValue(int id, Object parent, AlertDialog.Builder builder, Bundle args);

        Bundle getArgments(Object... args) {
            return null;
        }

        String getArgKey(int location) {
            return this.name() + "_ARG" + location;
        }

        DialogInterface.OnClickListener findOnClickListener(
                int id, Object parent, Bundle args, String key) {
            if (args.getBoolean(key)) {
                return new DialogFragmentEntity.OnClickListener(id, (OnClickListener) parent);
            } else {
                return null;
            }
        }

        DialogInterface.OnKeyListener findOnKeyListener(
                int id, Object parent, Bundle args, String key) {
            if (args.getBoolean(key)) {
                return new DialogFragmentEntity.OnKeyListener(id, (OnKeyListener) parent);
            } else {
                return null;
            }
        }

        DialogInterface.OnMultiChoiceClickListener findOnMultiChoiceClickListener(
                int id, Object parent, Bundle args, String key) {
            if (args.getBoolean(key)) {
                return new DialogFragmentEntity.OnMultiChoiceClickListener(
                        id, (OnMultiChoiceClickListener) parent);
            } else {
                return null;
            }
        }

        OnItemSelectedListener findOnItemSelectedListener(
                int id, Object parent, Bundle args, String key) {
            if (args.getBoolean(key)) {
                return (OnItemSelectedListener) parent;
            } else {
                return null;
            }
        }

        View findView(int id, Object parent, Bundle args, String key) {
            if (args.getBoolean(key)) {
                return ((ViewDelegate) parent).getView(id);
            } else {
                return null;
            }
        }

        Drawable findDrawable(int id, Object parent, Bundle args, String key) {
            if (args.getBoolean(key)) {
                return ((DrawableDelegate) parent).getDrawable(id);
            } else {
                return null;
            }
        }

        ListAdapter findListAdapter(int id, Object parent, Bundle args, String key) {
            if (args.getBoolean(key)) {
                return ((ListAdapterDelegate) parent).getAdapter(id);
            } else {
                return null;
            }
        }

        Cursor findCursor(int id, Object parent, Bundle args, String key) {
            if (args.getBoolean(key)) {
                return ((CursorDelegate) parent).getCursor(id);
            } else {
                return null;
            }
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        for (String key : getArguments().keySet()) {
            Bundle args = getArguments().getBundle(key);

            // setCancelable()はDialogFragmentに設定する
            if (Set.CANCELABLE.name().equals(key)) {
                Set.valueOf(key).setValue(getDialogId(), this, builder, args);
            } else {
                Set.valueOf(key).setValue(getDialogId(), getParent(), builder, args);
            }
        }
        return builder.create();
    }

    public void show(FragmentManager manager) {
        super.show(manager, TAG);
    }

    @Override
    public void dismiss() {
        super.onDismiss(getDialog());
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        if (getArguments().getBoolean(Set.ON_CANCEL_LISTENER.getArgKey(0))) {
            ((OnCancelListener) getParent()).onCancel(getDialogId(), dialog);
        }
    }

    Object getParent() {
        if (getTargetFragment() != null) {
            return getTargetFragment();
        } else {
            return getActivity();
        }
    }

    static boolean isFragment(Object object) {
        if (object instanceof FragmentActivity) {
            return true;
        }
        if (object instanceof Fragment) {
            return true;
        }
        if (object != null) {
            throw new IllegalArgumentException(
                    "Listener to implements FragmentActivity or Fragment.");
        }
        return false;
    }

    private int getDialogId() {
        Bundle args = getArguments().getBundle(Set.ID.name());
        return args != null ? args.getInt(Set.ID.getArgKey(0), -1) : -1;
    }

    public static class Builder {
        final Bundle B;
        final Context mContext;
        Fragment mTarget;

        /**
         * Constructor using a context for this builder and the
         * {@link AlertDialogFragment} it creates.
         */
        public Builder(Context context) {
            B = new Bundle();
            mContext = context;
        }

        /**
         * ID passed by the Listener and Delegate
         * 
         * @param id
         * @return
         */
        public Builder setId(int id) {
            B.putBundle(Set.ID.name(), Set.ID.getArgments(id));
            return this;
        }

        /**
         * @see AlertDialog.Builder#setTitle(int)
         */
        public Builder setTitle(int titleId) {
            return setTitle(mContext.getText(titleId));
        }

        /**
         * @see AlertDialog.Builder#setTitle(CharSequence)
         */
        public Builder setTitle(CharSequence title) {
            B.putBundle(Set.TITLE.name(), Set.TITLE.getArgments(title));
            return this;
        }

        /**
         * Listener to use {@link DialogFragmentInterface.ViewDelegate}
         * 
         * @see AlertDialog.Builder#setCustomTitle(View)
         */
        public Builder setCustomTitle(ViewDelegate customTitleView) {
            B.putBundle(Set.CUSTOM_TITLE.name(), Set.CUSTOM_TITLE.getArgments(customTitleView));
            return this;
        }

        /**
         * @see AlertDialog.Builder#setMessage(int)
         */
        public Builder setMessage(int messageId) {
            return setMessage(mContext.getText(messageId));
        }

        /**
         * @see AlertDialog.Builder#setMessage(CharSequence)
         */
        public Builder setMessage(CharSequence message) {
            B.putBundle(Set.MESSAGE.name(), Set.MESSAGE.getArgments(message));
            return this;
        }

        /**
         * @see AlertDialog.Builder#setIcon(int)
         */
        public Builder setIcon(int iconId) {
            B.putBundle(Set.ICON.name(), Set.ICON.getArgments(iconId));
            return this;
        }

        /**
         * Listener to use {@link DialogFragmentInterface.DrawableDelegate}
         * 
         * @see AlertDialog.Builder#setIcon(android.graphics.drawable.Drawable)
         */
        public Builder setIcon(DrawableDelegate icon) {
            B.putBundle(Set.ICON.name(), Set.ICON.getArgments(icon));
            return this;
        }

        /**
         * @see AlertDialog.Builder#setPositiveButton(CharSequence,
         *      OnClickListener)
         */
        public Builder setPositiveButton(int textId, final OnClickListener listener) {
            return setPositiveButton(mContext.getText(textId), listener);
        }

        /**
         * @see AlertDialog.Builder#setPositiveButton(CharSequence,
         *      OnClickListener)
         */
        public Builder setPositiveButton(CharSequence text, final OnClickListener listener) {
            B.putBundle(Set.POSITIVE_BUTTON.name(), Set.POSITIVE_BUTTON.getArgments(text, listener));
            return this;
        }

        /**
         * @see AlertDialog.Builder#setNegativeButton(int, OnClickListener)
         */
        public Builder setNegativeButton(int textId, final OnClickListener listener) {
            return setNegativeButton(mContext.getText(textId), listener);
        }

        /**
         * @see AlertDialog.Builder#setNegativeButton(CharSequence,
         *      OnClickListener)
         */
        public Builder setNegativeButton(CharSequence text, final OnClickListener listener) {
            B.putBundle(Set.NEGATIVE_BUTTON.name(), Set.NEGATIVE_BUTTON.getArgments(text, listener));
            return this;
        }

        /**
         * @see AlertDialog.Builder#setNeutralButton(int, OnClickListener)
         */
        public Builder setNeutralButton(int textId, final OnClickListener listener) {
            return setNeutralButton(mContext.getText(textId), listener);
        }

        /**
         * @see AlertDialog.Builder#setNeutralButton(CharSequence,
         *      OnClickListener)
         */
        public Builder setNeutralButton(CharSequence text, final OnClickListener listener) {
            B.putBundle(Set.NEUTRAL_BUTTON.name(), Set.NEUTRAL_BUTTON.getArgments(text, listener));
            return this;
        }

        /**
         * @see AlertDialog.Builder#setCancelable(boolean)
         */
        public Builder setCancelable(boolean cancelable) {
            B.putBundle(Set.CANCELABLE.name(), Set.CANCELABLE.getArgments(cancelable));
            return this;
        }

        /**
         * @see AlertDialog.Builder#setOnCancelListener(OnCancelListener)
         */
        public Builder setOnCancelListener(OnCancelListener onCancelListener) {
            B.putBundle(Set.ON_CANCEL_LISTENER.name(),
                    Set.ON_CANCEL_LISTENER.getArgments(onCancelListener));
            return this;
        }

        /**
         * @see AlertDialog.Builder#setOnKeyListener(OnKeyListener)
         */
        public Builder setOnKeyListener(OnKeyListener onKeyListener) {
            B.putBundle(Set.ON_KEY_LISTENER.name(), Set.ON_KEY_LISTENER.getArgments(onKeyListener));
            return this;
        }

        /**
         * @see AlertDialog.Builder#setItems(int, OnClickListener)
         */
        public Builder setItems(int itemsId, final OnClickListener listener) {
            return setItems(mContext.getResources().getTextArray(itemsId), listener);
        }

        /**
         * @see AlertDialog.Builder#setItems(CharSequence[], OnClickListener)
         */
        public Builder setItems(CharSequence[] items, final OnClickListener listener) {
            B.putBundle(Set.ITEMS.name(), Set.ITEMS.getArgments(items, listener));
            return this;
        }

        /**
         * Listener to use {@link DialogFragmentInterface.ListAdapterDelegate}
         * 
         * @see AlertDialog.Builder#setAdapter(android.widget.ListAdapter,
         *      android.content.DialogInterface.OnClickListener)
         */
        public Builder setAdapter(final ListAdapterDelegate adapter, final OnClickListener listener) {
            B.putBundle(Set.ADAPTER.name(), Set.ADAPTER.getArgments(adapter, listener));
            return this;
        }

        /**
         * Listener to use {@link DialogFragmentInterface.CursorDelegate}
         * 
         * @see AlertDialog.Builder#setCursor(android.database.Cursor,
         *      android.content.DialogInterface.OnClickListener, String)
         */
        public Builder setCursor(final CursorDelegate cursor, final OnClickListener listener,
                String labelColumn) {
            B.putBundle(Set.CURSOR.name(), Set.CURSOR.getArgments(cursor, listener, labelColumn));
            return this;
        }

        /**
         * @see AlertDialog.Builder#setMultiChoiceItems(int, boolean[],
         *      OnMultiChoiceClickListener)
         */
        public Builder setMultiChoiceItems(int itemsId, boolean[] checkedItems,
                final OnMultiChoiceClickListener listener) {
            return setMultiChoiceItems(mContext.getResources().getTextArray(itemsId),
                    checkedItems, listener);
        }

        /**
         * @see AlertDialog.Builder#setMultiChoiceItems(CharSequence[],
         *      boolean[], OnMultiChoiceClickListener)
         */
        public Builder setMultiChoiceItems(CharSequence[] items, boolean[] checkedItems,
                final OnMultiChoiceClickListener listener) {
            B.putBundle(Set.MULTI_CHOICE_ITEMS.name(),
                    Set.MULTI_CHOICE_ITEMS.getArgments(items, checkedItems, listener));
            return this;
        }

        /**
         * Listener to use {@link DialogFragmentInterface.CursorDelegate}
         * 
         * @see AlertDialog.Builder#setMultiChoiceItems(Cursor, String, String,
         *      android.content.DialogInterface.OnMultiChoiceClickListener)
         */
        public Builder setMultiChoiceItems(CursorDelegate cursor, String isCheckedColumn,
                String labelColumn, final OnMultiChoiceClickListener listener) {
            B.putBundle(Set.MULTI_CHOICE_ITEMS.name(), Set.MULTI_CHOICE_ITEMS.getArgments(
                    cursor, isCheckedColumn, labelColumn, listener));
            return this;
        }

        /**
         * @see AlertDialog.Builder#setSingleChoiceItems(int, int,
         *      OnClickListener)
         */
        public Builder setSingleChoiceItems(int itemsId, int checkedItem,
                final OnClickListener listener) {
            return setSingleChoiceItems(mContext.getResources().getTextArray(itemsId),
                    checkedItem, listener);
        }

        /**
         * Listener to use {@link DialogFragmentInterface.CursorDelegate}
         * 
         * @see AlertDialog.Builder#setSingleChoiceItems(Cursor, int, String,
         *      android.content.DialogInterface.OnClickListener)
         */
        public Builder setSingleChoiceItems(CursorDelegate cursor, int checkedItem,
                String labelColumn, final OnClickListener listener) {
            B.putBundle(Set.SINGLE_CHOICE_ITEMS.name(),
                    Set.SINGLE_CHOICE_ITEMS.getArgments(cursor, checkedItem, labelColumn, listener));
            return this;
        }

        /**
         * @see AlertDialog.Builder#setSingleChoiceItems(CharSequence[], int,
         *      OnClickListener)
         */
        public Builder setSingleChoiceItems(CharSequence[] items, int checkedItem,
                final OnClickListener listener) {
            B.putBundle(Set.SINGLE_CHOICE_ITEMS.name(),
                    Set.SINGLE_CHOICE_ITEMS.getArgments(items, checkedItem, listener));
            return this;
        }

        /**
         * Listener to use {@link DialogFragmentInterface.ListAdapterDelegate}
         * 
         * @see AlertDialog.Builder#setSingleChoiceItems(ListAdapter, int,
         *      android.content.DialogInterface.OnClickListener)
         */
        public Builder setSingleChoiceItems(ListAdapterDelegate adapter, int checkedItem,
                final OnClickListener listener) {
            B.putBundle(Set.SINGLE_CHOICE_ITEMS.name(),
                    Set.SINGLE_CHOICE_ITEMS.getArgments(adapter, checkedItem, listener));
            return this;
        }

        /**
         * @see AlertDialog.Builder#setOnItemSelectedListener(AdapterView.OnItemSelectedListener)
         */
        public Builder setOnItemSelectedListener(final AdapterView.OnItemSelectedListener listener) {
            B.putParcelable(Set.ON_ITEM_SELECTED_LISTENER.name(),
                    Set.ON_ITEM_SELECTED_LISTENER.getArgments(listener));
            return this;
        }

        /**
         * Listener to use {@link DialogFragmentInterface.ViewDelegate}
         * 
         * @see AlertDialog.Builder#setView(View)
         */
        public Builder setView(ViewDelegate view) {
            B.putBundle(Set.VIEW.name(), Set.VIEW.getArgments(view));
            return this;
        }

        /**
         * @see AlertDialog.Builder#setInverseBackgroundForced(boolean)
         */
        public Builder setInverseBackgroundForced(boolean useInverseBackground) {
            B.putBundle(Set.INVERSE_BACKGROUND_FORCED.name(),
                    Set.INVERSE_BACKGROUND_FORCED.getArgments(useInverseBackground));
            return this;
        }

        /**
         * @see AlertDialog.Builder#create()
         */
        public AlertDialogFragment create() {
            AlertDialogFragment adf = new AlertDialogFragment();
            adf.setArguments(B);
            adf.setTargetFragment(mTarget, 0);
            return adf;
        }

        /**
         * @see DialogFragment#setTargetFragment(Fragment, int)
         */
        public Builder setTargetFragment(Fragment fragment) {
            mTarget = fragment;
            return this;
        }

    }

}
