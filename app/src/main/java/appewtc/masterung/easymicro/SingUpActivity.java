package appewtc.masterung.easymicro;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.jibble.simpleftp.SimpleFTP;

import java.io.File;

public class SingUpActivity extends AppCompatActivity {

    //Explicit
    private EditText nameEditText, userEditText, passwordEditText;
    private ImageView imageView, takePhotoImageView;
    private Button button;
    private String nameString, userString, passwordString, imageString,
            imagePathString, imageNameString;
    private Uri uri;
    private boolean aBoolean = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        //Bind Widget
        nameEditText = (EditText) findViewById(R.id.editText);
        userEditText = (EditText) findViewById(R.id.editText2);
        passwordEditText = (EditText) findViewById(R.id.editText3);
        imageView = (ImageView) findViewById(R.id.imageView);
        button = (Button) findViewById(R.id.button3);
        takePhotoImageView = (ImageView) findViewById(R.id.imageView4);

        //Take Photo
        takePhotoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 1);

            }   // onClick
        });

        //SignUp Controller
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Get Value From Edit text
                nameString = nameEditText.getText().toString().trim();
                userString = userEditText.getText().toString().trim();
                passwordString = passwordEditText.getText().toString().trim();

                //Check Space
                if (nameString.equals("") ||
                        userString.equals("") ||
                        passwordString.equals("")) {

                    //Have Space
                    Log.d("5novV1", "Have Space");
                    MyAlert myAlert = new MyAlert(SingUpActivity.this,
                            R.drawable.doremon48,
                            getResources().getString(R.string.title_haveSpace),
                            getResources().getString(R.string.message_haveSpace));
                    myAlert.myDialog();

                } else if (aBoolean) {
                    // Non Choose Image
                    MyAlert myAlert = new MyAlert(SingUpActivity.this,
                            R.drawable.nobita48,
                            getResources().getString(R.string.title_ImageChoose),
                            getResources().getString(R.string.massage_ImageChoose));
                    myAlert.myDialog();

                } else {

                    //Upload To Server
                    upLoadImage();
                    upLoadSting();

                }

            }   // onClick
        });


        //Image Controller
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Intent to Other App
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "โปรเลือกแอฟ เลือกรูป"), 0);

            }   // onClick
        });


    }   // Main Method

    private class AddNewUser extends AsyncTask<String, Void, String> {

        //Explicit
        private Context context;

        public AddNewUser(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... strings) {

            try {

                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody requestBody = new FormEncodingBuilder()
                        .add("isAdd", "true")
                        .add("Name", nameString)
                        .add("User", userString)
                        .add("Password", passwordString)
                        .add("Image", imageString)
                        .build();
                Request.Builder builder = new Request.Builder();
                Request request = builder.url(strings[0]).post(requestBody).build();
                Response response = okHttpClient.newCall(request).execute();
                return response.body().string();

            } catch (Exception e) {
                Log.d("6novV2", "e doInBack ==> " + e.toString());
                return null;
            }


        }   // doInBack

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d("6novV2", "Result ==> " + s);
            if (Boolean.parseBoolean(s)) {
                Toast.makeText(SingUpActivity.this, "Thank You Save OK",
                        Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(SingUpActivity.this, "Error cannon Save User",
                        Toast.LENGTH_SHORT).show();
            }

        }   // onPost

    }   // AddNewUser Class

    private void upLoadSting() {

        imageString = "http://swiftcodingthai.com/mic/Images" + imageNameString;

        MyConstante myConstante = new MyConstante();
        AddNewUser addNewUser = new AddNewUser(SingUpActivity.this);
        addNewUser.execute(myConstante.getUrlAddUser());

    }

    private void upLoadImage() {

        //Change Policy
        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy
                .Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);

        try {

            MyConstante myConstante = new MyConstante();
            SimpleFTP simpleFTP = new SimpleFTP();
            simpleFTP.connect(myConstante.getHostString(),
                    myConstante.getPortAnInt(),
                    myConstante.getUserString(),
                    myConstante.getPasswordString());
            simpleFTP.bin();
            simpleFTP.cwd("Images");
            simpleFTP.stor(new File(imagePathString));
            simpleFTP.disconnect();

            Toast.makeText(SingUpActivity.this, "Upload Image Finish",
                    Toast.LENGTH_SHORT).show();


        } catch (Exception e) {
            e.printStackTrace();
        }



    }   // upLoadImage

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == 0) && (resultCode == RESULT_OK)) {
            //Result Success
            Log.d("5novV1", "Result OK");

            //SetUp Choose Image to ImageView
            uri = data.getData();
            try {

                Bitmap bitmap = BitmapFactory
                        .decodeStream(getContentResolver()
                                .openInputStream(uri));
                imageView.setImageBitmap(bitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }

            //Check Choosed
            aBoolean = false;

            //Find Path of Image Choose
            imagePathString = myFindPath(uri);
            Log.d("6novV1", "Path ==> " + imagePathString);

            //Find Name of Image Choose
            imageNameString = imagePathString.substring(imagePathString.lastIndexOf("/"));
            Log.d("6novV1", "Name ==> " + imageNameString);

        } else if ((requestCode == 1)&&(resultCode == RESULT_OK)) {
            //Take Photo OK

            uri = data.getData();

            try {

                Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                imageView.setImageBitmap(bitmap);


            } catch (Exception e) {
                Log.d("19novV1", "e ==> " + e.toString());
            }

            //Check Choosed
            aBoolean = false;

            //Find Path of Image Choose
            imagePathString = myFindPath(uri);
            Log.d("19novV1", "Path ==> " + imagePathString);

            //Find Name of Image Choose
            imageNameString = imagePathString.substring(imagePathString.lastIndexOf("/"));
            Log.d("19novV1", "Name ==> " + imageNameString);

        }   // if

    }   // onActivityResult

    private String myFindPath(Uri uri) {

        String result = null;
        String[] strings = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, strings,
                null , null, null);

        if (cursor != null) {

            cursor.moveToFirst();
            int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            result = cursor.getString(index);

        } else {
            result = uri.getPath();
        }   // if


        return result;
    }

}   // Main Class