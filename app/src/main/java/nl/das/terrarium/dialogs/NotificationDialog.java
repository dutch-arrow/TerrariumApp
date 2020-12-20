package nl.das.terrarium.dialogs;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Looper;

import androidx.appcompat.app.AlertDialog;

public class NotificationDialog {

    private AlertDialog dialog;

    public NotificationDialog(Context context, String title, String message) {
        AlertDialog.Builder b = new AlertDialog.Builder(context);
        b.setMessage(message);
        b.setCancelable(false);
        b.setNeutralButton("Ok", (dialog, which) -> dialog.dismiss());
        b.setTitle(title);
        dialog = b.create();
    }

    public void show() {
        dialog.show();
    }
}
