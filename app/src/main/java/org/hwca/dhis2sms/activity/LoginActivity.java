package org.hwca.dhis2sms.activity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.hwca.dhis2sms.R;
import org.hwca.dhis2sms.entity.Settings;
import org.hwca.dhis2sms.entity.User;
import org.hwca.dhis2sms.utils.AeSimpleSHA1;
import org.hwca.dhis2sms.utils.URLConstants;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import cz.msebera.android.httpclient.Header;
import es.dmoral.toasty.Toasty;
import io.realm.Realm;

public class LoginActivity extends AppCompatActivity {
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.realm = Realm.getDefaultInstance();

        callMainActivity();
    }

    private void callMainActivity() {
        User user = realm.where(User.class).findFirst();
        if (user != null) {
            final Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
            mainIntent.putExtra("username", user.getUsername());
            mainIntent.putExtra("password", user.getPassword());
            mainIntent.putExtra("loginResponse", user.getLoginResponse());

            startActivity(mainIntent);
            finish();
        }
    }

    public void performLogin(View view) {
        final EditText baseUrl = findViewById(R.id.baseUrl);
        final EditText usernameField = findViewById(R.id.username);
        final EditText passwordField = findViewById(R.id.password);

        final String username = usernameField.getText().toString();
        final String password = passwordField.getText().toString();
        final String url = baseUrl.getText().toString();

        if (username.length() == 0 || password.length() == 0 || url.length() == 0)
            Toasty.warning(getApplicationContext(), "All of fields are required please!", Toast.LENGTH_SHORT, false).show();
         else {
            final Settings setting = realm.where(Settings.class).findFirst();

            if (setting == null)
                proceedLoginFromInternet(url, username, password);
             else {
                try {
                    proceedLoginFromLocalDatabase(username, password);
                } catch (UnsupportedEncodingException e) {
                    Toasty.info(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT, false).show();
                } catch (NoSuchAlgorithmException e) {
                    Toasty.info(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT, false).show();
                }
            }

        }
    }

    private void proceedLoginFromLocalDatabase(
            final String username,
            final String password)
            throws UnsupportedEncodingException, NoSuchAlgorithmException {
       final User user = realm.where(User.class).equalTo("username", username).equalTo("password", AeSimpleSHA1.SHA1(password)).findFirst();

        if (user == null)
            Toasty.error(getApplicationContext(), "Wrong credentials, try again please ", Toast.LENGTH_SHORT, false).show();
         else
            openMainActivityAfterSuccessLogin(user.getLoginResponse(), username, password);
    }

    private void proceedLoginFromInternet(final String baseUrl, final String username, final String password) {
        final AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", "Basic ".concat(Base64.encodeToString(username.concat(":").concat(password).getBytes(), Base64.DEFAULT)));
        client.get(baseUrl.concat(URLConstants.LOGIN_URL), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                final User currentUser = realm.where(User.class).equalTo("username", username).equalTo("password", password).findFirst();

                if (currentUser == null) {
                    final String email = "";

                    realm.beginTransaction();
                    User user = realm.where(User.class).equalTo("id", "user").findFirst();
                    if (user == null)
                        user = realm.createObject(User.class, "user");
                    user.setEmail(email);
                    user.setUsername(username);
                    user.setPassword(password);
                    user.setLoginResponse(response);
                    realm.commitTransaction();
                }

                final Settings settings = realm.where(Settings.class).findFirst();
                if (settings == null) {
                    realm.beginTransaction();
                    Settings setting = realm.createObject(Settings.class, "setting");
                    setting.setBaseUrl(baseUrl);
                    setting.setLastConnection(new Date());
                    realm.commitTransaction();
                }
                openMainActivityAfterSuccessLogin(response, username, password);
            }

            @Override
            public void onFailure(final int statusCode, final Header[] headers,
                                  final byte[] errorResponse, final Throwable e) {
                Toasty.error(getApplicationContext(), "Incorrect username or password ", Toast.LENGTH_SHORT, false).show();
            }
        });
    }

    private void openMainActivityAfterSuccessLogin(final byte[] response, final String username, final String password) {
        final Intent intent = new Intent(getBaseContext(), MainActivity.class);

        intent.putExtra("username", username);
        intent.putExtra("password", password);
        intent.putExtra("loginResponse", response);

        finish();
        startActivity(intent);
    }
}
