package com.video.corpus.controllers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;

import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.video.corpus.Interface.Response_string;
import com.video.corpus.R;
import com.video.corpus.global.commonclass;
import com.video.corpus.network.NetworkRequestHeaders;
import com.video.corpus.network.NetworkRequestPost;
import org.json.JSONObject;
import java.util.Arrays;
import java.util.HashMap;


public class LoginActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener{
    private CallbackManager callbackManager;
    private Response_string<String> loginresponse;
    private TextInputEditText edtEmail, edtPwd;
    private Context context;
    private commonclass cc;
    private  AlertDialog alertDialog;
    private GoogleApiClient mGoogleApiClient;
    private com.facebook.login.widget.LoginButton facebookLoginButton;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context=LoginActivity.this;
        cc=new commonclass(context);
        edtEmail =findViewById(R.id.Edt_email);
        edtPwd =findViewById(R.id.Edt_pwd);


        readresponse();
        facebooklogin();

        ImageView signInButton = findViewById(R.id.gplus);
     //   signInButton.setSize(SignInButton.SIZE_STANDARD);
        ImageView fbicon = findViewById(R.id.fblogin);
        facebookLoginButton=findViewById(R.id.login_button);

       /*Google sigin init*/
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder (GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes (new Scope(Scopes.PLUS_LOGIN))
                .requestEmail ()
                .build ();

        mGoogleApiClient = new GoogleApiClient.Builder (this)
                .enableAutoManage (LoginActivity.this,LoginActivity.this)
                .addApi (Auth.GOOGLE_SIGN_IN_API, gso)
                .addApi (Plus.API)
                .build ();

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent googleIntent = new Intent (Auth.GoogleSignInApi.getSignInIntent (mGoogleApiClient));
                startActivityForResult (googleIntent, RC_SIGN_IN);
            }
        });

        facebookLoginButton.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                facebooklogin();
            }
        });

        fbicon.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
               facebookLoginButton.performClick();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.

      //  GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

    }

    //login validations
    public void btnlognclick(View view) {

        if(edtEmail.getText().toString().trim().length()>0 && edtPwd.getText().toString().trim().length()>0)
        {
            if(!edtEmail.getText().toString().contains(" ") && !edtPwd.getText().toString().contains(" "))
            {
                showalert();
                setparams();
            }
            else
            {
                showtaost(context,getResources().getString(R.string.emptyspace));
            }
        }
        else
        {
            showtaost(context,getResources().getString(R.string.emptyfields));
        }
    }

    private void setparams()
    {
        try {
            HashMap<String,String> hashMap=new HashMap<>();
            hashMap.put("uname", edtEmail.getText().toString());
            hashMap.put("password", edtPwd.getText().toString());
            hashMap.put("subdeviceidentifier", getMACAddress());

            new NetworkRequestHeaders(login_url, hashMap, loginresponse).execute();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    //read response
    private void readresponse() {
        loginresponse = new Response_string<String>() {
            @Override
            public void readResponse(String res) {
                showlogs("login_response", res);

                if (!TextUtils.isEmpty(res)) {
                    if(readstatuscode(res))
                    {
                        success();
                    }
                    else
                    {
                        Error(res);
                    }
                }
                else
                {
                   Error(res);
                }
            }
        };
    }

    public void success()
    {
        showtaost(context,getResources().getString(R.string.success));
        Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void Error(String msg)
    {
        alertdismiss();
        showtaost(context,msg);
    }

    //read status code
    public boolean readstatuscode(String res)
    {
        boolean result=false;
        try{
            JSONObject jsonObjectMain=new JSONObject(res);
            JSONObject jsonObject_status=jsonObjectMain.optJSONObject("responseStatus");
            if(jsonObject_status.optString("statusCode","").length()>0
                    && jsonObject_status.optString("statusCode","").equals("200"))
            {
               result=true;
               cc.resetsharedpref();
              cc.setSessionId(jsonObjectMain.optString("sessionId",""));
              cc.setcustomerName(jsonObjectMain.optString("customerName",""));
              cc.setisLoggedIn(true);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
       return result;
    }



    public void showalert()
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        LayoutInflater layoutInflater=(LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        assert layoutInflater != null;
        View view=layoutInflater.inflate(R.layout.showprogress,nullParent);
        builder.setView(view);
        builder.setCancelable(false);
         alertDialog=builder.create();
         assert  alertDialog.getWindow()!=null;
         alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();
    }

    public void alertdismiss()
    {
        if(alertDialog!=null && alertDialog.isShowing())
        {
            alertDialog.dismiss();
        }
    }

    @Override
    public void onStop()
    {
        super.onStop();
        alertdismiss();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent (data);
            handleSignInResult (result);
        }
        callbackManager.onActivityResult (requestCode, resultCode, data);
    }


    private void handleSignInResult(GoogleSignInResult completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getSignInAccount();
            // Getting FB User Data
            if (account != null && isnotempty(account.toString())) {
                showalert();
                fetchsociallogindata_google(account);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //FB
    private void facebooklogin()
    {
        //Facebook sdk initialization before setContentView

//initializing the CallbackManager object
        callbackManager = CallbackManager.Factory.create();
//obtaining reference for the LoginButton widget in the layout xml
        facebookLoginButton = findViewById(R.id.login_button);
/*setting read permissions for the LoginButton. public_profile will give access to the user’s public information and “email” will give access to his/her registered email id*/
        facebookLoginButton.setReadPermissions(Arrays.asList("public_profile", "email"));
//registering the CallbackManager with the LoginButton class
        facebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                setFacebookData(loginResult);
            }

            @Override
            public void onCancel() {
                showlogs("fb_cancel","fb_cancel");
            }

            @Override
            public void onError(FacebookException exception) {
                showlogs("fb_Error","fb_Error");
            }

        });
    }



    private void setFacebookData(final LoginResult loginResult)
    {
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject jsonObject,
                                            GraphResponse response) {
                        // Getting FB User Data
                        if(isnotempty(jsonObject.toString()))
                        {
                            showalert();
                            fetchsociallogindata(jsonObject);
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,first_name,last_name,email,gender");
        request.setParameters(parameters);
        request.executeAsync();
    }


    //fetch social login data
    private  void  fetchsociallogindata(JSONObject object)
    {

        try {
            JSONObject openAuthObject = new JSONObject ();
            JSONObject subscriberObject = new JSONObject ();
            subscriberObject.put ("title", (object.optString ("gender","").equals ("male") ? "Mr." : "Mrs."));
            subscriberObject.put ("firstname", object.optString ("first_name",""));
            subscriberObject.put ("lastname", object.optString ("last_name",""));
            subscriberObject.put ("emailid", object.optString ("email",""));
            subscriberObject.put ("userName", object.optString ("id",""));
            subscriberObject.put ("password", reg_auth);
            subscriberObject.put ("countryId", COUNTRY_ID);
            subscriberObject.put ("cityId", CITY_ID);
            subscriberObject.put ("streamProfile", reg_global);
            subscriberObject.put ("openAuthType", reg_fb);
            subscriberObject.put ("remarks", "");
            subscriberObject.put ("tokenId", object.optString ("id",""));
            openAuthObject.put ("devicecategory", reg_open_auth);
            openAuthObject.put ("subscriber", subscriberObject);
            new NetworkRequestPost(login_url_social,openAuthObject,getMACAddress(),loginresponse).execute();
        } catch (Exception e) {
            e.printStackTrace();

        }

    }


    //fetch social login data
    private  void  fetchsociallogindata_google(GoogleSignInAccount client)
    {
        try {


            String gender = "Mr.";
            try {
                Person person = Plus.PeopleApi.getCurrentPerson (mGoogleApiClient);
                gender = person.getGender () == 1 ? "Mrs." : "Mr.";
            } catch (Exception e) {
                Log.e ("Google error", e.getMessage ());
            }

            JSONObject openAuthObject = new JSONObject ();
            JSONObject subscriberObject = new JSONObject ();
            subscriberObject.put ("title", gender);
            subscriberObject.put ("firstname", client.getDisplayName ());
            subscriberObject.put ("lastname", "");
            subscriberObject.put ("emailid", client.getEmail ());
            subscriberObject.put ("userName", client.getId());
            subscriberObject.put ("password", reg_auth);
            subscriberObject.put ("countryId", COUNTRY_ID);
            subscriberObject.put ("cityId", CITY_ID);
            subscriberObject.put ("streamProfile", reg_global);
            subscriberObject.put ("openAuthType", reg_gplus);
            subscriberObject.put ("remarks", "");
            subscriberObject.put ("tokenId", client.getId ());
            openAuthObject.put ("devicecategory", reg_open_auth);
            openAuthObject.put ("subscriber", subscriberObject);
            new NetworkRequestPost(login_url_social,openAuthObject,getMACAddress(),loginresponse).execute();
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }
}
