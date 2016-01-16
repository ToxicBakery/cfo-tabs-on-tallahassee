package com.ToxicBakery.app.tabsontallahassee.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.ToxicBakery.app.tabsontallahassee.R;

public class DialogDownloadFragment extends DialogFragment implements DialogInterface.OnClickListener {

    public static final String TAG = "DialogDownloadFragment";

    private static final String EXTRA_TITLE = "EXTRA_TITLE";
    private static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    private static final String EXTRA_URL = "EXTRA_URL";

    private String title;
    private String message;
    private String url;

    public static DialogDownloadFragment createInstance(@NonNull String title,
                                                @NonNull String message,
                                                @NonNull String url) {

        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_TITLE, title);
        bundle.putString(EXTRA_MESSAGE, message);
        bundle.putString(EXTRA_URL, url);

        DialogDownloadFragment dialogDownload = new DialogDownloadFragment();
        dialogDownload.setArguments(bundle);
        return dialogDownload;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        title = bundle.getString(EXTRA_TITLE);
        message = bundle.getString(EXTRA_MESSAGE);
        url = bundle.getString(EXTRA_URL);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.download, this)
                .setNegativeButton(android.R.string.cancel, null)
                .create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                getContext().startActivity(intent);
                break;
        }
    }

}
