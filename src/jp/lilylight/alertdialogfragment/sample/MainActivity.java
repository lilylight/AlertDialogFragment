
package jp.lilylight.alertdialogfragment.sample;

import jp.lilylight.alertdialogfragment.AlertDialogFragment;
import jp.lilylight.alertdialogfragment.DialogFragmentInterface.OnClickListener;
import jp.lilylight.alertdialogfragment.DialogFragmentInterface.ListAdapterDelegate;
import jp.lilylight.alertdialogfragment.R;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements OnClickListener, ListAdapterDelegate {

    private static final int ID_DIALOG_ALERT = 1;
    private static final String[] ITEMS = new String[] {
            "アイテム１",
            "アイテム２",
            "アイテム３",
            "アイテム４",
            "アイテム５",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(android.R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });
    }

    @Override
    public void onClick(int id, DialogInterface dialog, int which) {
        switch (id) {
            case ID_DIALOG_ALERT:
                Toast.makeText(this, ITEMS[which], Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
    }

    @Override
    public ListAdapter getAdapter(int id) {
        return new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ITEMS);
    }

    private void showAlertDialog() {
        new AlertDialogFragment.Builder(this)
//              .setTargetFragment(this) Fragmentの場合
                .setId(ID_DIALOG_ALERT)
                .setIcon(R.drawable.ic_launcher)
                .setTitle("サンプル")
                .setAdapter(this, this)
                .setNeutralButton(android.R.string.cancel, null)
                .create()
                .show(getSupportFragmentManager());
    }
}
