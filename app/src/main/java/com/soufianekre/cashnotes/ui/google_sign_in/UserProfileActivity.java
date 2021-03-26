package com.soufianekre.cashnotes.ui.google_sign_in;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.soufianekre.cashnotes.R;
import com.soufianekre.cashnotes.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

@SuppressLint("NonConstantResourceId")
public class UserProfileActivity extends BaseActivity {


    @BindView(R.id.user_profile_toolbar)
    Toolbar userProfileToolbar;
    @BindView(R.id.user_profile_img)
    ImageView userProfile;
    @BindView(R.id.user_profile_name)
    TextView userProfileName;
    @BindView(R.id.user_profile_email)
    TextView userProfileEmail;
    @BindView(R.id.user_profile_sign_out)
    Button userProfileSignOut;

    String personName;
    String personGivenName;
    String personFamilyName;
    String personEmail;
    String personId;
    Uri personPhoto;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        setUnBinder(ButterKnife.bind(this));

        setSupportActionBar(userProfileToolbar);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();

            userProfileName.setText(personName);
            userProfileEmail.setText(personEmail);
            Glide.with(this)
                    .asBitmap()
                    .load(personPhoto)
                    .into(userProfile);
        }

        userProfileSignOut.setOnClickListener(v -> {
            signOut();
        });


    }

    private void signOut() {
        new MaterialDialog.Builder(this)
                .title("Confirm signing out :")
                .content("Your data will not be deleted after signing out. you can recover your data with the same account.")
                .positiveText(R.string.confirm)
                .onPositive((dialog, which) -> {
                    AuthUI.getInstance()
                            .signOut(this)
                            .addOnCompleteListener(task -> {
                                showMessage("You sign out successfully");
                                finish();
                            });
                })
                .negativeText(R.string.cancel)
                .onNegative((dialog, which) -> dialog.dismiss())
                .show();

    }


}
