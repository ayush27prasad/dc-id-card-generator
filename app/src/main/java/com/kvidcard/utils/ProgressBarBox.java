package com.kvidcard.utils;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class ProgressBarBox {
    private static AlertDialog progressDialog;
    public static void showProgressDialog(Context context, String title) {
        // Create a ProgressBar programmatically
        ProgressBar progressBar = new ProgressBar(context);
        progressBar.setIndeterminate(true);

        // Optionally, style your ProgressBar
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
                );
        progressBar.setLayoutParams(layoutParams);

        // Use MaterialAlertDialogBuilder to create the dialog
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
        builder.setTitle(title); // Optionally set a title
        builder.setView(progressBar); // Set the ProgressBar as the dialog's view
        builder.setCancelable(false); // Optional: make the dialog non-cancelable

        progressDialog = builder.create();
        progressDialog.show();
    }

     public static void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
