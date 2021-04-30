package net.vyl.misredessociales;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "FacebookApp";
    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private EditText txt_contenido;
    private ShareLinkContent content;
    private ShareButton shareButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Registrar una devolución de llamada
        callbackManager = CallbackManager.Factory.create();

        txt_contenido = findViewById(R.id.txt_contenido);
        txt_contenido.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                content = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse(String.valueOf(txt_contenido.getText())))
                        .build();
                shareButton.setShareContent(content);
                shareButton.setVisibility(View.VISIBLE);
            }
        });
        ShareDialog shareDialog = new ShareDialog(this);
        // this part is optional
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                Toast.makeText(MainActivity.this, "¡Éxito en la publicación!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, "Se canceló correctamente.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(MainActivity.this, "Ocurrió un error D:", Toast.LENGTH_SHORT).show();
            }});
        shareButton = (ShareButton)findViewById(R.id.fb_share_button);
        shareButton.setVisibility(View.GONE);
        txt_contenido.setVisibility(View.GONE);

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
        // If using in a fragment
        //loginButton.setFragment(this);

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(MainActivity.this, "¡Éxito en la autenticación!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onSuccess: ¡Éxito en la autenticación!");
                txt_contenido.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, "Se canceló correctamente.", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onCancel: Se canceló correctamente.");
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(MainActivity.this, "Ocurrió un error D:", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onError: Ocurrió un error D: " + exception.getMessage());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}