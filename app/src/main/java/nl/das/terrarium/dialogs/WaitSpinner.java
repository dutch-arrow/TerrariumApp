package nl.das.terrarium.dialogs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AlertDialog;

import nl.das.terrarium.R;

public class WaitSpinner {
    private Context context;
    private AlertDialog waitSpinner;

    public WaitSpinner(Context context) {
        this.context = context;
    }

    public void start() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        builder.setView(inflater.inflate(R.layout.wait_dialog, null));
        builder.setCancelable(true);
        waitSpinner = builder.create();
        waitSpinner.show();
        resizeDialog();
    }

    public void dismiss() {
        waitSpinner.dismiss();
    }
    /**
     * To resize the size of this dialog
     */
    private void resizeDialog() {
        try {
            Window window = waitSpinner.getWindow();
            if (context == null || window == null) return;
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
            // Adjust width and height
//            int height = displayMetrics.heightPixels;
//            int width = displayMetrics.widthPixels;
//            window.setLayout((int) (width ), (int) (height));
            // Change background
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));// make tranparent around the popup
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }
    }
}
