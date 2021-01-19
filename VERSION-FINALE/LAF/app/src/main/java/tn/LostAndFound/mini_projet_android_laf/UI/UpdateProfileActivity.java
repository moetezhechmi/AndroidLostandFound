package tn.LostAndFound.mini_projet_android_laf.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.itgate.uptofind.utils.TinyDB;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import tn.LostAndFound.mini_projet_android_laf.R;
import tn.LostAndFound.mini_projet_android_laf.Retrofit.RetrofitClient;
import tn.LostAndFound.mini_projet_android_laf.Retrofit.Service;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class UpdateProfileActivity extends AppCompatActivity implements View.OnClickListener  {
    private static int RESULT_LOAD_IMAGE = 1;
    //CompositeDisposable compositeDisposable= new CompositeDisposable();
    Uri picUri;

    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    private final static int ALL_PERMISSIONS_RESULT = 107;
    private final static int IMAGE_RESULT = 200;
    EditText txtEmail;
    EditText txtTel;
    EditText txtFirstName;
    EditText txtLastName;
    EditText txtOldPass;
    EditText txtNewPass;
    EditText txtConfirmePassword;
    ImageView imgUser;
    Button btnSaveUpdateProfile;
    tn.LostAndFound.mini_projet_android_laf.Retrofit.Service Service;
    String photo;
    String email;
    String idU;
    String phone;
    String firstName;
    String lastName;


    Bitmap mBitmap;

    public static String server="http://192.168.1.104:3002";



    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);


        SharedPreferences sharedPreferences = this.getSharedPreferences("myKey", MODE_PRIVATE);
        photo = new TinyDB(this).getString("photo");
        email =    new TinyDB(UpdateProfileActivity.this).getString("email");
        phone =    new TinyDB(UpdateProfileActivity.this).getString("phone");
        firstName =    new TinyDB(UpdateProfileActivity.this).getString("firstName");
        lastName =    new TinyDB(UpdateProfileActivity.this).getString("lastName");
        idU =    new TinyDB(UpdateProfileActivity.this).getString("_id");
        photo =    new TinyDB(UpdateProfileActivity.this).getString("photo");

        imgUser = findViewById(R.id.imgUser);

        txtEmail = findViewById(R.id.txtEmail);
        txtTel = findViewById(R.id.txtTel);
        txtFirstName = findViewById(R.id.txtFirstName);
        txtLastName = findViewById(R.id.txtLastName);
        txtOldPass = findViewById(R.id.txtOldPass);
        txtNewPass = findViewById(R.id.txtNewPass);
        txtConfirmePassword = findViewById(R.id.txtConfirmPass);
        btnSaveUpdateProfile= findViewById(R.id.btnSaveUpdateProfile);


        btnSaveUpdateProfile.setOnClickListener((View.OnClickListener) this);

        imgUser.setOnClickListener((View.OnClickListener) this);




//get le contenu de profil dans update profil
        String LastName=getIntent().getStringExtra("eLastName");
        String FirstName=getIntent().getStringExtra("eFirstName");
        String Email=getIntent().getStringExtra("eEmail");
        String Phone=getIntent().getStringExtra("ePhone");
        askPermissions();
        txtFirstName.setText(FirstName);
        txtLastName.setText(LastName);
        txtEmail.setText(Email);
        txtTel.setText(Phone);
        Picasso.get().load(RetrofitClient.server+photo).placeholder(R.drawable.profil)
                .error(R.drawable.profil)
                .into(imgUser);

        askPermissions();
        Retrofit retrofitClient = RetrofitClient.getInstance();
        Service = retrofitClient.create(Service.class);




    }

    private void askPermissions() {
        permissions.add(CAMERA);
        permissions.add(WRITE_EXTERNAL_STORAGE);
        permissions.add(READ_EXTERNAL_STORAGE);
        permissionsToRequest = findUnAskedPermissions(permissions);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }
    }


    public Intent getPickImageChooserIntent() {

        Uri outputFileUri = getCaptureImageOutputUri();

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = getPackageManager();

        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);

        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }
    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getExternalFilesDir("");
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profile.png"));
        }
        return outputFileUri;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

            ImageView imageView =findViewById(R.id.imgUser);

            if (requestCode == IMAGE_RESULT) {


                String filePath = getImageFilePath(data);
                if (filePath != null) {
                    mBitmap = BitmapFactory.decodeFile(filePath);
                    imageView.setImageBitmap(mBitmap);
                }
            }

        }

    }
    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (getApplication().checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }
    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }


    public String getImageFilePath(Intent data) {
        return getImageFromFilePath(data);
    }
    private String getImageFromFilePath(Intent data) {
        boolean isCamera = data == null || data.getData() == null;

        if (isCamera) return getCaptureImageOutputUri().getPath();
        else return getPathFromURI(data.getData());

    }
    private  String getPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable("pic_uri", picUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
   super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        picUri = savedInstanceState.getParcelable("pic_uri");
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (String perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                        }
                                    });
                            return;
                        }
                    }

                }

                break;
        }

    }
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void multipartImageUpload() {

            RequestBody idUser =
                    RequestBody.create(MediaType.parse("multipart/form-data"), idU);
            Log.e("asslema", idU);
            RequestBody tfirstName =
                    RequestBody.create(MediaType.parse("multipart/form-data"), txtFirstName.getText().toString());
            RequestBody tlastName =
                    RequestBody.create(MediaType.parse("multipart/form-data"), txtLastName.getText().toString());
            RequestBody temail =
                    RequestBody.create(MediaType.parse("multipart/form-data"), txtEmail.getText().toString());
            RequestBody tphone =
                    RequestBody.create(MediaType.parse("multipart/form-data"), txtTel.getText().toString());

            RequestBody oldpass =
                    RequestBody.create(MediaType.parse("multipart/form-data"), txtOldPass.getText().toString());

            RequestBody newpass =
                    RequestBody.create(MediaType.parse("multipart/form-data"), txtNewPass.getText().toString());
            System.out.println("done");


            Service.UpdateUser(idUser, tfirstName, tlastName, temail, tphone, oldpass,newpass)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String response) throws Exception {
                            System.out.println("5ach");
                            Toast.makeText(UpdateProfileActivity.this, "Votre modif a été effectuée", Toast.LENGTH_LONG).show();
                            JSONObject us = new JSONObject(response).getJSONObject("user");
                            System.out.println("a1 "+us);
                            String id= us.getString("_id");
                            String firstName =us.getString("firstName");
                            String email =us.getString("email");
                            String phone =us.getString("phone");
                            String photo =us.getString("photo");
                            String lastName =us.getString("lastName");
                            Log.d("khach",id);

                            new TinyDB(UpdateProfileActivity.this).putString("phone",phone);
                            new TinyDB(UpdateProfileActivity.this).putString("firstName",firstName);
                            new TinyDB(UpdateProfileActivity.this).putString("email", email);
                            new TinyDB(UpdateProfileActivity.this).putString("lastName", lastName);
                            new TinyDB(UpdateProfileActivity.this).putString("photo", photo);
                            new TinyDB(UpdateProfileActivity.this).putString("_id", id);

                        }
                    });

    }




    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgUser:
                startActivityForResult(getPickImageChooserIntent(), IMAGE_RESULT);
                break;

            case R.id.btnSaveUpdateProfile:
            //    if (txtNewPass.getText() == "") {
                    multipartImageUpload();
                    System.out.println("loaaaaad");
                    new TinyDB(this).putString("value","value");
                    Intent intent =new Intent(UpdateProfileActivity.this,HomeActivity.class);
                    startActivity(intent);



              //  }
              //  else {
                 /*   AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
                    dlgAlert.setIcon(R.drawable.error);

                    dlgAlert.setMessage("non identiques");
                    dlgAlert.setTitle("ATTENTION!!");
                    dlgAlert.setPositiveButton("OK", null);
                    dlgAlert.setCancelable(true);
                    dlgAlert.show();*/
                //}
                break;
        }
    }


}
