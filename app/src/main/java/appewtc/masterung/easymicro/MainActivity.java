package appewtc.masterung.easymicro;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    //Explicit การประกาศตัวแปร
    private Button signInButton, singUpButton;
    private EditText userEditText, passwordEditText;
    private String userString, passwordString;
    private MyConstante myConstante;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myConstante = new MyConstante();

        //Bind Widget
        signInButton = (Button) findViewById(R.id.button);
        singUpButton = (Button) findViewById(R.id.button2);
        userEditText = (EditText) findViewById(R.id.editText4);
        passwordEditText = (EditText) findViewById(R.id.editText5);


        //SignUp Controller
        singUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SingUpActivity.class));
            }
        });


        //SignIn Controller
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Get Value From Edit Text
                userString = userEditText.getText().toString().trim();
                passwordString = passwordEditText.getText().toString().trim();

                //Check Space
                if (userString.equals("") || passwordString.equals("")) {
                    //Have Space
                    MyAlert myAlert = new MyAlert(MainActivity.this,
                            R.drawable.kon48,
                            getResources().getString(R.string.title_haveSpace),
                            getResources().getString(R.string.message_haveSpace));
                    myAlert.myDialog();
                } else {

                    //No Space
                    GetUser getUser = new GetUser(MainActivity.this);
                    getUser.execute(myConstante.getUrlJSoN());

                }


            }   // onClick
        });


    }   // Main Method

    private class GetUser extends AsyncTask<String, Void, String> {

        //Explicit
        private Context context;
        private String[] nameStrings, imageStrings;
        private String truePasswordString;
        private boolean aBoolean = true;

        public GetUser(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... strings) {

            try {

                OkHttpClient okHttpClient = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                Request request = builder.url(strings[0]).build();
                Response response = okHttpClient.newCall(request).execute();
                return response.body().string();

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }   // doIn

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d("6novV3", "JSON ==> " + s);

            try {

                JSONArray jsonArray = new JSONArray(s);

                nameStrings = new String[jsonArray.length()];
                imageStrings = new String[jsonArray.length()];

                for (int i=0;i<jsonArray.length();i++) {

                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    nameStrings[i] = jsonObject.getString("Name");
                    imageStrings[i] = jsonObject.getString("Image");

                    //CheckLog
                    Log.d("6novV4", "Name(" + i + ") ==> " + nameStrings[i]);

                    if (userString.equals(jsonObject.getString("User"))) {

                        aBoolean = false;
                        truePasswordString = jsonObject.getString("Password");

                    }

                }   // for

                if (aBoolean) {
                    MyAlert myAlert = new MyAlert(context,
                            R.drawable.bird48,
                            "User False",
                            "No " + userString + " in my Database");
                    myAlert.myDialog();
                } else if (passwordString.equals(truePasswordString)) {
                    Toast.makeText(context, "Welcome", Toast.LENGTH_SHORT).show();

                } else {
                    MyAlert myAlert = new MyAlert(context,
                            R.drawable.bird48,
                            "Passowrd False",
                            "Please Try Again Password False");
                    myAlert.myDialog();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }


        }   // onPost

    }   // Class


}   // Main Class นี่คื่อ คลาสหลัก นะจรัา
