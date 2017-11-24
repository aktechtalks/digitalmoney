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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.digitalmoney.home.R;
import com.digitalmoney.home.fragments.AdminFragment;
import com.digitalmoney.home.fragments.EarnFragment;
import com.digitalmoney.home.fragments.NotificationFragment;
import com.digitalmoney.home.fragments.OfferFragment;
import com.digitalmoney.home.fragments.PlanFragment;
import com.digitalmoney.home.fragments.ShareFragment;
import com.digitalmoney.home.fragments.TaskFragment;
import com.digitalmoney.home.fragments.WalletFragment;
import com.digitalmoney.home.models.User;
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

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Typeface typeface;
    private TextView tvUserReferralCode;
    private TextView tvUserName;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        user_id = mAuth.getCurrentUser().getUid();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle); toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initUI();

        displaySelectedScreen(R.id.nav_task);
    }


    private void initUI() {

        typeface = Typeface.createFromAsset(getAssets(), TYPEFACE_PATH_REGULAR);
        View header = navigationView.getHeaderView(0);
        tvUserName = (TextView) header.findViewById(R.id.tvUserName);
        tvUserReferralCode = (TextView) header.findViewById(R.id.tvUserReferralCode);
        tvUserName.setTypeface(typeface);
        Typeface typefaceBold = Typeface.createFromAsset(getAssets(), TYPEFACE_PATH_BOLD);
        tvUserReferralCode.setTypeface(typefaceBold);

        tvUserName.setText("#####");
        tvUserReferralCode.setText("#####");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            Uri photoUrl = user.getPhotoUrl();
            boolean emailVerified = user.isEmailVerified();
            String uid = user.getUid();
        }

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User profile = dataSnapshot.child("users").child(user.getUid()).child("profile").getValue(User.class);

                if (profile!=null){
                    String mobile_number = profile.getUserMobileno();
                    String user_name = profile.getUserName();
                    tvUserName.setText(user_name);
                    tvUserReferralCode.setText(mobile_number);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Error:-->", "loadPost:onCancelled", databaseError.toException());
                Toast.makeText(getApplicationContext(), databaseError.getMessage(),Toast.LENGTH_LONG).show();

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




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


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
                fragment = new ShareFragment();
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

        Toast.makeText(this, "Share with friends", Toast.LENGTH_SHORT).show();
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/html");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml("<p> Your digital money has referal code.</p>"));
        startActivity(Intent.createChooser(sharingIntent,"Share using"));
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        displaySelectedScreen(item.getItemId());
        return true;
    }

    public void setActionBarTitle(String title){
        toolbar.setTitle(title);
    }

}

