package com.digitalmoney.home.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.digitalmoney.home.R;
import com.digitalmoney.home.fragments.AdminFragment;
import com.digitalmoney.home.fragments.NotificationFragment;
import com.digitalmoney.home.fragments.OfferFragment;
import com.digitalmoney.home.fragments.PlanFragment;
import com.digitalmoney.home.fragments.ReferralFragment;
import com.digitalmoney.home.fragments.TaskFragment;
import com.digitalmoney.home.fragments.WalletFragment;
import com.digitalmoney.home.models.User;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import static com.digitalmoney.home.Utility.Utils.TYPEFACE_PATH_BOLD;
import static com.digitalmoney.home.Utility.Utils.TYPEFACE_PATH_REGULAR;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Typeface          typeface;
    private TextView          tvUserReferralCode;
    private TextView          tvUserName;
    private DrawerLayout      drawer;
    private Toolbar           toolbar;
    private ImageView         btnEditProfile;
    private NavigationView    navigationView;
    private FirebaseAuth      mAuth;
    private DatabaseReference mDatabase;
    private String            user_id;
    private int               REQUEST_INVITE = 101;
    private String            referralCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();

        displaySelectedScreen(R.id.nav_task);
    }


    private void initUI() {

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        user_id = mAuth.getCurrentUser().getUid();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle); toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        typeface = Typeface.createFromAsset(getAssets(), TYPEFACE_PATH_REGULAR);
        View header = navigationView.getHeaderView(0);
        tvUserName = (TextView) header.findViewById(R.id.tvUserName);
        btnEditProfile = (ImageView) header.findViewById(R.id.btnEditProfile);

        tvUserReferralCode = (TextView) header.findViewById(R.id.tvUserReferralCode);
        tvUserName.setTypeface(typeface);
        Typeface typefaceBold = Typeface.createFromAsset(getAssets(), TYPEFACE_PATH_BOLD);
        tvUserReferralCode.setTypeface(typefaceBold);

        tvUserName.setText("####");
        tvUserReferralCode.setText("####");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userName = user.getDisplayName();
            tvUserName.setText(userName);
            settingProfile();
        }

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User profile = dataSnapshot.child("users").child(user.getUid()).child("profile").getValue(User.class);

                if (profile!=null){
                    referralCode = profile.getReferral_code();
                    tvUserReferralCode.setText(referralCode);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Error:-->", "loadPost:onCancelled", databaseError.toException());
                Toast.makeText(getApplicationContext(),
                        databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }




    private void settingProfile(){

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                startActivity(new Intent(getApplicationContext(), SettingActivity.class));
            }
        });
    }




/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/


   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/


    private void displaySelectedScreen(int itemId) {

        Fragment fragment = null;

        switch (itemId) {

            case R.id.nav_task:
                fragment = new TaskFragment();
                break;
            case R.id.nav_notification:
                fragment = new NotificationFragment();
                break;
            case R.id.nav_wallet:
                fragment = new WalletFragment();
                break;
            case R.id.nav_referral:
                fragment = new ReferralFragment();
                break;
            case R.id.nav_offer:
                fragment = new OfferFragment();
                break;
            case R.id.nav_plan:
                fragment = new PlanFragment();
                break;
            case R.id.nav_invite_earn:
                shareApplication();
                break;
            case R.id.nav_support:
                openMail();
                break;
            case R.id.nav_logout:
                logoutApp();
                break;
            case R.id.nav_admin:
                fragment = new AdminFragment();
                break;

        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        drawer.closeDrawer(GravityCompat.START);
    }




    private void logoutApp() {
        mAuth.signOut();
        Intent intent = new Intent(MainActivity.this, LoginWithEmail.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }




    private void openMail() {

        final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        final PackageManager pm = getPackageManager();
        final List<ResolveInfo> matches = pm.queryIntentActivities(intent, 0);
        ResolveInfo best = null;
        for (final ResolveInfo info : matches)
            if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail")) best = info;
        if (best != null)
            intent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
        startActivity(intent);
    }


    private void shareApplication() {

        Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.app_name))
                .setMessage(getResources().getString(R.string.invite_title_message)+"-"+referralCode)
                .setDeepLink(Uri.parse(getString(R.string.invitation_deep_link)))
                .setCustomImage(Uri.parse(getString(R.string.invitation_custom_image)))
                //.setCallToActionText(getString(R.string.invitation_cta))
                .build();
        startActivityForResult(intent, REQUEST_INVITE);
    }



    private void referWithFriend(){

        String referralMessageText = "Digital Application\n" +
                "\n" +
                "I am earning Paytm cash using Digital Money app.\n" +
                "You can earn money for refer your friend and complete simple task\n" +
                "\n" +
                "Use My Referral code 60106 on signup\n" +
                "\n" +
                "Link: "+Uri.parse(getString(R.string.invitation_deep_link));


        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/html");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml(referralMessageText));
        startActivity(Intent.createChooser(sharingIntent,getResources().getString(R.string.app_name)));

    }



    @SuppressWarnings("StatementWithEmptyBody")

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        displaySelectedScreen(item.getItemId());
        return true;
    }

}

