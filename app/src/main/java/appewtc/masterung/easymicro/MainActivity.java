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

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

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

        private Context context;

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


        }   // onPost

    }   // Class


}   // Main Class นี่คื่อ คลาสหลัก นะจรัา
