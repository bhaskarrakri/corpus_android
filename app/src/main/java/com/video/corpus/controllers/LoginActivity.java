package com.video.corpus.controllers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import com.video.corpus.Interface.Response_string;
import com.video.corpus.R;
import com.video.corpus.global.commonclass;
import com.video.corpus.network.NetworkRequestHeaders;
import org.json.JSONObject;
import java.util.HashMap;


public class LoginActivity extends BaseActivity {
    private Response_string<String> loginresponse;
    private TextInputEditText edtEmail, edtPwd;
    private Context context;
    private commonclass cc;
    private  AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context=LoginActivity.this;
        cc=new commonclass(context);
        edtEmail =findViewById(R.id.Edt_email);
        edtPwd =findViewById(R.id.Edt_pwd);

    }

    //login validations
    public void btnlognclick(View view) {

        if(edtEmail.getText().toString().trim().length()>0 && edtPwd.getText().toString().trim().length()>0)
        {
            if(!edtEmail.getText().toString().contains(" ") && !edtPwd.getText().toString().contains(" "))
            {
                showalert();
                readresponse();
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
            hashMap.put("subdeviceidentifier", "abcdef");

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
        cc.setusername(edtEmail.getText().toString());
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

}
