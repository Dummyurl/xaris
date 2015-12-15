package snapchattapp.texnlog.com.snapchatapp.UploadImg;



import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import snapchattapp.texnlog.com.snapchatapp.R;

/**
 * Created by Charis on 11/12/2015.
 */


public class Upload extends AppCompatActivity implements View.OnClickListener {

    public static final String UPLOAD_URL = "http://projectdb.esy.es/upload.php";
    public static final String UPLOAD_KEY = "image";
    public static final String TAG = "MY MESSAGE";


    private int RESULT_LOAD_IMAGE = 1;
    private File mediaStorageDir,mediaFile;
    private Button buttonChoose;
    private Button buttonUpload;
    private Button buttonView;


    private ImageView imageView;

    private Bitmap bitmap;

    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        Intent intent=getIntent();
        String image=intent.getStringExtra("image");
//        buttonChoose = (Button) findViewById(R.id.buttonChoose);

        buttonUpload = (Button) findViewById(R.id.buttonUpload);
        buttonView   = (Button) findViewById(R.id.buttonView);

        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageURI(Uri.parse(image));
        imageView.setRotation(90);
        imageView.setAdjustViewBounds(true);
//        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);
        buttonView.setOnClickListener(this);
        mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "MyCameraApp");
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "Custom_"+ ".jpg");
        bitmap=BitmapFactory.decodeFile(mediaFile.getPath());
    }



    private void ViewPage(){

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse("http://projectdb.esy.es/PhUpload.php"));
        startActivity(intent);
    }


    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void uploadImage(){
        class UploadImage extends AsyncTask<Bitmap,Void,String>{

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Upload.this, "Uploading Image", "Please wait...",true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bitmap = params[0];
                String uploadImage = getStringImage(bitmap);
                Log.d(TAG,uploadImage);
                HashMap<String,String> data = new HashMap<>();
                data.put(UPLOAD_KEY, uploadImage);

                String result = rh.sendPostRequest(UPLOAD_URL,data);

                return result;
            }
        }

        UploadImage ui = new UploadImage();

        ui.execute(bitmap);
    }

    @Override
    public void onClick(View v) {

        if(v == buttonUpload){
            uploadImage();
        }
        if(v == buttonView){
            ViewPage();
        }
    }
}


