package snapchattapp.texnlog.com.snapchatapp.Friends_Users.AsyncTask;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import snapchattapp.texnlog.com.snapchatapp.Friends_Users.Users;
import snapchattapp.texnlog.com.snapchatapp.UserConnection.UserLocalStore;

/**
 * Created by SoRa1 on 10/12/2015.
 */
public class LoadProfileImageASYNC extends AsyncTask {
    private ImageView imageVIEW;
    private Context conTEXT;
    private Users user;
    private Bitmap bitmap;

    public LoadProfileImageASYNC(Context context,ImageView imageView)
    {
        imageVIEW=imageView;
        conTEXT=context;
        UserLocalStore localStore=new UserLocalStore(context);
        user=localStore.getLoggedInUser();



    }

    @Override
    protected Object doInBackground(Object[] objects)
    {
        String serviceURL=user.getC_photoPath();

        try
        {
            HttpURLConnection connection= (HttpURLConnection) new URL(serviceURL).openConnection();
            bitmap= BitmapFactory.decodeStream(connection.getInputStream());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        imageVIEW.setImageBitmap(bitmap);
    }
}
