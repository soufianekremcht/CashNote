package com.soufianekre.cashnotes.ui.google_sign_in;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.soufianekre.cashnotes.R;
import com.soufianekre.cashnotes.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.Binds;

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

    private GoogleSignInClient mGoogleSignInClient;

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
        if (getSupportActionBar()!= null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        userProfileSignOut.setOnClickListener(v -> {
            signOut();
        });

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



    }

    private void signOut() {

        new MaterialDialog.Builder(this)
                .title("Confirm signing out :")
                .content("Your data will not be deleted after signing out. you can recover your data with the same account.")
                .positiveText(R.string.confirm)
                .onPositive((dialog, which) -> {
                    mGoogleSignInClient.signOut()
                            .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    // ...
                                    showMessage("You sign out successfuly");
                                    onBackPressed();
                                }
                            });
                })
                .negativeText(R.string.cancel)
                .onNegative((dialog, which) -> dialog.dismiss())
                .show();

    }

    private void revokeAccess() {
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener(this, task -> onBackPressed());
    }
}
