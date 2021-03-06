package com.gjk;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.gjk.helper.GeneralHelper;

public class LoginDialog extends DialogFragment {

    private static final String LOGTAG = "LoginDialog";

    private EditText mEmailLogin;
    private EditText mPasswordLogin;
    private LoginButton mFacebookLoginButton;
    private UiLifecycleHelper mUiHelper;
    private GraphUser mFacebookUser;

    /*
     * The activity that creates an instance of this dialog fragment must implement this interface in order to receive
     * event callbacks. Each method passes the DialogFragment in case the host needs to query it.
     */
    public interface NoticeDialogListener {
        public void onLoginDialogPositiveClick(LoginDialog dialog);

        public void onLoginDialogFacebookClick(LoginDialog dialog);

        public void onLoginDialogNegativeClick(LoginDialog dialog);
    }

    // Use this instance of the interface to deliver action events
    NoticeDialogListener mListener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString() + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.login_dialog, null);

        mEmailLogin = (EditText) view.findViewById(R.id.emailLogin);
        mEmailLogin.requestFocus();
        mEmailLogin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams
                            .SOFT_INPUT_STATE_VISIBLE);
                }
            }
        });
        mPasswordLogin = (EditText) view.findViewById(R.id.passwordLogin);
        mFacebookLoginButton = (LoginButton) view.findViewById(R.id.facebookLoginButton);
        mFacebookLoginButton.setReadPermissions("email", "public_profile");
        mFacebookLoginButton.setApplicationId(getActivity().getResources().getString(R.string.app_id));
        mFacebookLoginButton.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
            @Override
            public void onUserInfoFetched(GraphUser user) {
                if (user != null) {
                    mFacebookUser = user;
                    mListener.onLoginDialogFacebookClick(LoginDialog.this);
                }
            }
        });
        mUiHelper = new UiLifecycleHelper(getActivity(), null);
        mUiHelper.onCreate(savedInstanceState);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.login, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                }).setNegativeButton(R.string.go_to_register, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mListener.onLoginDialogNegativeClick(LoginDialog.this);
            }
        }).setCancelable(false);
        Dialog d = builder.create();
        d.setCancelable(false);
        d.setCanceledOnTouchOutside(false);
        setCancelable(false);
        return d;
    }

    @Override
    public void onStart() {
        super.onStart(); // super.onStart() is where dialog.show() is actually called on the underlying dialog, so we
        // have to do it after this point
        AlertDialog d = (AlertDialog) getDialog();
        if (d != null) {
            Button positiveButton = d.getButton(Dialog.BUTTON_POSITIVE);
            if (positiveButton != null) {
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isLoginReady()) {
                            mListener.onLoginDialogPositiveClick(LoginDialog.this);
                            mPasswordLogin.setText("");
                        }
                    }
                });
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mUiHelper.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mUiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUiHelper.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mUiHelper.onSaveInstanceState(outState);
    }

    public String getEmail() {
        return mEmailLogin.getText().toString();
    }

    public String getPassword() {
        return mPasswordLogin.getText().toString();
    }

    public GraphUser getFacebookUser() {
        return mFacebookUser;
    }

    public void handleOnActivityRequest(int requestCode, int resultCode, Intent data) {
        mUiHelper.onActivityResult(requestCode, resultCode, data);
    }

    private boolean isLoginReady() {

        boolean ready = true;
        if (getEmail().isEmpty()) {
            GeneralHelper.showLongToast(getActivity(), "Email was left empty!");
            ready = false;
        }
        if (getPassword().isEmpty()) {
            GeneralHelper.showLongToast(getActivity(), "Password was left empty!");
            ready = false;
        }
        return ready;
    }

}