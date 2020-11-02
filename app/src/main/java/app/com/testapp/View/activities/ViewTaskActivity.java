package app.com.testapp.View.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.net.Uri;
import android.os.Bundle;

import app.com.testapp.Model.utils.Constants;
import app.com.testapp.R;
import app.com.testapp.View.fragments.ImageFragment;
import app.com.testapp.View.fragments.JsonDataFragment;
import app.com.testapp.View.fragments.NotificationFragment;

public class ViewTaskActivity extends AppCompatActivity implements NotificationFragment.OnNotificationFragmentInteractionListener,
        ImageFragment.OnImageFragmentInteractionListener,
        JsonDataFragment.OnJsonFragmentInteractionListener {

    private int fragment_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);

        fragment_code = getIntent().getIntExtra(Constants.FRAGMENT_CODE, 0);
        if (fragment_code == 0001) {
            startNewFragment(NotificationFragment.newInstance(), "NotificationFragment", false);
        } else if (fragment_code == 0002) {
            startNewFragment(ImageFragment.newInstance(), "ImageFragment", false);
        } else {
            startNewFragment(JsonDataFragment.newInstance(), "JsonDataFragment", false);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    void startNewFragment(final Fragment frag, final String tag, boolean backstack) {
        final FragmentTransaction fragmentTransaction = this.getSupportFragmentManager()
                .beginTransaction();
        if (this.getSupportFragmentManager().findFragmentById(R.id.container) != null) {
            if (backstack) {
                fragmentTransaction.replace(R.id.container, frag, tag);
                fragmentTransaction.addToBackStack(null);
            } else {
                fragmentTransaction.replace(R.id.container, frag, tag);
                fragmentTransaction.addToBackStack(tag);
            }
        } else {
            fragmentTransaction.add(R.id.container, frag, tag);
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

    /**
     * Clear pop up back stack.
     */
    void clearPopUpBackStack() {
        if (getSupportFragmentManager() != null) {
            for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); ++i) {
                getSupportFragmentManager().popBackStack();
            }
        }
    }

    @Override
    public void onImageFragmentInteraction(Uri uri) {

    }

    @Override
    public void onNotificationFragmentInteraction(Uri uri) {

    }

    @Override
    public void onJsonFragmentInteraction(Uri uri) {

    }
}
