package com.example.abhinav.studentfirebase;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Context context = this;
    DatabaseReference databaseReference,dbuser,dbuserstudents;
    private TextView textViewName;
    private TextView textViewClass;
    private TextView textViewDivision;
    private TextView textViewHouse;
    private ImageButton imageButtonAttendance;
    private ImageButton imageButtonNotification;
    private ImageButton imageButtonFee;
    private ImageButton imageButtonService;
    private ImageButton imageButtonResult;
    private ImageButton imageButtonTask;
    private ImageView imageViewStudentPic;
    private ImageButton imageButtonRedDotAttendance;
    private ImageButton imageButtonRedDotFee;
    private ProgressDialog mProgress;
    FirebaseAuth.AuthStateListener mAuthlistner;
    ProgressDialog progressDialog;
    private FirebaseAuth auth;
    StorageReference mStorage;
    String name,clas,house,id,div,uid;
    Utility utility;
    Boolean processed=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mAuthlistner=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=firebaseAuth.getCurrentUser();
//                Log.d("ani", user.toString() + "user");
                if (user==null)
                {
                    Intent intent = new Intent(MenuActivity.this, AddStudentActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }

        };
        setContentView(R.layout.activity_menu);
        auth = FirebaseAuth.getInstance();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d("anu", "Token: " + token);

        utility=new Utility();
        id=utility.readFrompreferences(this,"id","");
        Log.d("ani",id+"id in menu");
        mProgress=new ProgressDialog(this);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("student");
        databaseReference.keepSynced(true);
        dbuser=FirebaseDatabase.getInstance().getReference().child("users");
        dbuser.keepSynced(true);

        dbuserstudents=FirebaseDatabase.getInstance().getReference().child("usersstudents");
        dbuserstudents.keepSynced(true);

        setupNavigationDrawer(toolbar);
        bindUIComponents();
        setAllTexts(id);
        setProfilePic();
        setOnClickListeners();
        checkuserexists();


   //     checkNextPayment();
      //  scheduleAlarm();
    }
    private void checkuserexists() {

        if (auth.getCurrentUser() != null) {
            dbuser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.hasChild(auth.getCurrentUser().getUid())) {
                        Intent intent = new Intent(MenuActivity.this,CreateAccountActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }


    void setProfilePic() {

        if(auth.getCurrentUser()!=null) {
            progressDialog.setMessage("Wait a sec..");
            progressDialog.show();
            dbuser.child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild("imageurl")) {
                        String url = dataSnapshot.child("imageurl").getValue().toString();
                        Glide.with(getApplicationContext()).load(url).asBitmap().centerCrop().into(new BitmapImageViewTarget(imageViewStudentPic) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                imageViewStudentPic.setImageDrawable(circularBitmapDrawable);
                                progressDialog.dismiss();

                            }
                        });
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    progressDialog.dismiss();

                }
            });
        }


//        //Picasso.with(context).load("http://i.imgur.com/DvpvklR.png").placeholder(R.drawable.spinning_circle).into(imageViewStudentPic);
//        String stringUri = localdb.getUri(student.getId());
//        if (!stringUri.equals("")) {
//            Uri uri = Uri.parse(stringUri);
//
//            //URI uri = new Uri();
//            Picasso.with(context).load(uri).networkPolicy(NetworkPolicy.OFFLINE)
//                    .transform(new CircleTransform()).into(imageViewStudentPic, new Callback() {
//                @Override
//                public void onSuccess() {
//                }
//
//                @Override
//                public void onError() {
//                    Picasso.with(context).load(R.drawable.male).transform(new CircleTransform()).into(imageViewStudentPic);
//                }
//            });
       // }
    }

    void setupNavigationDrawer(Toolbar toolbar) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        assert drawer != null;
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);
        android.view.Menu menu = navigationView.getMenu();

        final SubMenu subMenu = menu.addSubMenu("Student List");
        if(auth.getCurrentUser() != null) {
            processed=true;
            dbuserstudents.child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int count = (int) dataSnapshot.getChildrenCount();
                    Log.d("ani", count + "count");
                    if(processed){
                    for (final DataSnapshot child : dataSnapshot.getChildren()) {


                        Log.d("ani", count + child.getKey() + "count");
                        MenuItem menuItem = subMenu.add(R.id.nav_group_students, 0, 0, child.child("name").getValue().toString());
                        menuItem.setIcon(R.drawable.studentet);
                        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                Log.d("ani", child.getKey() + "key");
                                setAllTexts(child.getKey());
                                utility.saveTopreferences(MenuActivity.this, child.getKey());
//                    Student newStudent = dbHelper.getStudent(mystudent.getId());
//                    //MyUtils.logThis("selected student = " + newStudent.toString());
//                    localdb.setStudent(newStudent);
//                    Intent intent = getIntent();
//                    finish();
//                    startActivity(intent);

                                return false;
                            }
                        });
                    }
                        processed=false;
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

//        ArrayList<Student> listStudents = dbHelper.getAllStudents();
//        for (int i = 0; i < listStudents.size(); i++) {
//
//            final Student mystudent = listStudents.get(i);
//

//
//            menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//                @Override
//                public boolean onMenuItemClick(MenuItem item) {
//                    Student newStudent = dbHelper.getStudent(mystudent.getId());
//                    //MyUtils.logThis("selected student = " + newStudent.toString());
//                    localdb.setStudent(newStudent);
//                    Intent intent = getIntent();
//                    finish();
//                    startActivity(intent);
//                    return false;
//                }
//            });
            //MyUtils.logThis("MenuActivity - " + listStudents.get(i).toString());
        }//end for
        /*
        ArrayList<Student> listStudents = dbHelper.getAllStudents();
        for (int i = 0; i < listStudents.size(); i++) {

            tempStudent = listStudents.get(i);
            //MyUtils.logThis("temp student contact="+tempStudent.getContact());
            MenuItem menuItem = menu.add(R.id.nav_group_students, i, i, tempStudent.getName());
            menuItem.setIcon(R.drawable.studentet);
            menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    //item.getTitle();
                    Student newStudent = dbHelper.getStudent(tempStudent.getId());
                    localdb.setStudent(newStudent);
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                    return false;
                }
            });
        }
        */

        /*2
        ArrayList<Student> listStudents = dbHelper.getAllStudents();
        for (int i = 0; i < listStudents.size(); i++) {

            tempStudent = listStudents.get(i);
            //MyUtils.logThis("temp student contact="+tempStudent.getContact());
            int studentid = Integer.parseInt(student.getId());
            MenuItem menuItem = menu.add(R.id.nav_group_students, i, i, tempStudent.getName());
            menuItem.setIcon(R.drawable.studentet);
            menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    //item.getTitle();
                    //int studentid = item.getItemId();
                    //MyUtils.logThis("studentid = " + studentid);
                    MyUtils.logThis("tempStudent.getId() = " + tempStudent.getId());

                    //Student newStudent = dbHelper.getStudent(tempStudent.getId());
                    Student newStudent = dbHelper.getStudent(tempStudent.getId() + "");
                    localdb.setStudent(newStudent);
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                    return false;
                }
            });
        }*/



    private void bindUIComponents() {

        imageButtonAttendance = (ImageButton) findViewById(R.id.imageButtonAttendance);
        imageButtonNotification = (ImageButton) findViewById(R.id.imageButtonNotification);
        imageButtonFee = (ImageButton) findViewById(R.id.imageButtonFee);
        imageButtonService = (ImageButton) findViewById(R.id.imageButtonService);
        imageViewStudentPic = (ImageView) findViewById(R.id.imageViewStudentPic);
        imageButtonRedDotAttendance = (ImageButton) findViewById(R.id.imageButtonRedDotAttendance);
        imageButtonRedDotFee = (ImageButton) findViewById(R.id.imageButtonRedDotFee);

        imageButtonResult = (ImageButton) findViewById(R.id.imageButtonResult);
        imageButtonTask = (ImageButton) findViewById(R.id.imageButtonTask);
        progressDialog=new ProgressDialog(this);
        textViewName = (TextView) findViewById(R.id.textViewName);
        textViewClass = (TextView) findViewById(R.id.textViewClass);
        textViewDivision = (TextView) findViewById(R.id.textViewDivision);
        textViewHouse = (TextView) findViewById(R.id.textViewHouse);
        Firebase.setAndroidContext(this);
        mStorage= FirebaseStorage.getInstance().getReference();



    }

    private void setAllTexts(String id) {
        mProgress.setMessage("Processing");
        mProgress.show();
        if (auth.getCurrentUser() != null) {
            databaseReference.child(id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    name = dataSnapshot.child("name").getValue().toString();
                    clas = dataSnapshot.child("class").getValue().toString();
                    house = dataSnapshot.child("house").getValue().toString();
                    div = dataSnapshot.child("div").getValue().toString();

                    textViewName.setText(name);
                    textViewClass.setText(clas);
                    textViewDivision.setText(div);
                    textViewHouse.setText(house);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            mProgress.dismiss();

            Log.d("ani", clas + house + div + name + "details");

        }
    }


    private void setOnClickListeners() {
        imageButtonAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(context, AttendanceActivity.class);
//                startActivity(i);
            }
        });
        imageButtonNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Notification.class);
                startActivity(i);
            }
        });
        imageButtonFee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, FeeActivity.class);
                startActivity(i);
            }
        });
        imageButtonService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent i = new Intent(context, ServiceActivity.class);
                startActivity(i);
            }
        });
        imageButtonResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ResultActivity.class);
                startActivity(i);
            }
        });
        imageButtonTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, TaskActivity.class);
                startActivity(i);
            }
        });
        imageViewStudentPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, StudentInfoActivity.class);
                startActivity(i);
            }
        });

    }

    //------------------------      options     ------------------------------

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_menu, menu);
        return true;
    }
    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(mAuthlistner);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_help) {
//            Intent i = new Intent(context, HelpActivity.class);
//            startActivity(i);
            return true;
        } else if (id == R.id.action_change_pic) {
            pickFromGallery();
            return true;

//        } else if (id == R.id.action_logout) {
//            localdb.clear();
//            localdb.setInitialised(false);
//            dbHelper.clear();
          //  cancelAlarm();
//            Intent i = new Intent(context, AddStudentActivity.class);
//            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(i);
//            finish();
           // return true;
        }
        if(id==R.id.action_logout){
            auth.signOut();
        }
        if(id==R.id.location){
            Intent intent=new Intent(this,MapsActivity.class);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }

    //--------------------      end options     --------------------------------

    //--------------------      alarm     --------------------------------
//    public void scheduleAlarm() {
//        if (localdb.isAlarmSet())
//        {
//            return;
//        }
//        Intent intent = new Intent(this, AlarmReceiver.class);
//        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
//
//        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//
//        /* Set the alarm to start at 09:00 AM */
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.set(Calendar.HOUR_OF_DAY, 9);
//        calendar.set(Calendar.MINUTE, 00);
//        calendar.set(Calendar.AM_PM, Calendar.AM);
//
//        /* Repeating on every day interval */
//        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
//                1000 * 60 * 60 * 24, alarmIntent);
//
//        ComponentName receiver = new ComponentName(this, SampleBootReceiver.class);
//        PackageManager pm = getPackageManager();
//
//        pm.setComponentEnabledSetting(receiver,
//                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
//                PackageManager.DONT_KILL_APP);
//        localdb.setAlarm(true);
//        //Toast.makeText(this, "Alarm Scheduled", Toast.LENGTH_LONG).show();
//    }
//
//    public void cancelAlarm() {
//        Intent intent = new Intent(this, AlarmReceiver.class);
//        PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);
//        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        alarmManager.cancel(sender);
//    }
//    //--------------------      end alarm     --------------------------------
//
//    private void checkNextPayment() {
//
//        Fee nextFee = localdb.getNextPayment(student.getId());
//
//        if (nextFee == null) {
//            return;
//        }
//        String dueDate = nextFee.getDate();
//
//        GregorianCalendar inputDate = MyUtils.getGregorianCalendar(dueDate);
//        //  add 7 days to input date
//        //  7 days margin for next payment
//        assert inputDate != null;
//        inputDate.add(Calendar.DATE, 7);
//        GregorianCalendar todaysDate = new GregorianCalendar();
//
//        if (todaysDate.compareTo(inputDate) == -1) {
//            //imageButtonFee.setBackgroundTintList(new ColorStateList());
//            imageButtonRedDotFee.setVisibility(View.VISIBLE);
//            final Animation animation = new AlphaAnimation(1, 0);
//            animation.setDuration(800);
//            animation.setInterpolator(new LinearInterpolator());
//            animation.setRepeatCount(Animation.INFINITE);
//            animation.setRepeatMode(Animation.REVERSE);
//            imageButtonRedDotFee.startAnimation(animation);
//        }
//    }
//
//    /*-----------------------crop image------------------------------*/
//
    private static final int REQUEST_SELECT_PICTURE = 0x01;
//
    private void pickFromGallery() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_SELECT_PICTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_SELECT_PICTURE) {
                final Uri selectedUri = data.getData();
                if (selectedUri != null) {
                    startCropActivity(data.getData());
                } else {
                    Toast.makeText(context, "cannot_retrieve_selected_image", Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == UCrop.REQUEST_CROP) {
                handleCropResult(data);
            }
        }
        if (resultCode == UCrop.RESULT_ERROR) {
            handleCropError(data);
        }
    }

    private void startCropActivity(@NonNull Uri uri) {
        String destinationFileName = "MyImage.png";

        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), destinationFileName)));
        uCrop.withAspectRatio(1, 1);
        uCrop.withMaxResultSize(500, 500);
        uCrop = advancedConfig(uCrop);

        uCrop.start(MenuActivity.this);
    }

    Bitmap bitmap;

    private void handleCropResult(@NonNull Intent result) {
        progressDialog.setMessage("Changing...");
        progressDialog.show();
        final Uri resultUri = UCrop.getOutput(result);
        if (resultUri != null) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                bitmap = getCroppedBitmap(bitmap);

                Log.d("ani",auth.getCurrentUser().getUid()+auth.getCurrentUser().getEmail());

                StorageReference filestorage = mStorage.child("Profile Images").child(auth.getCurrentUser().getEmail()).child("Profilepic");
                filestorage.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                                             @Override
                                                                             public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                                 final Uri downloaduri = taskSnapshot.getDownloadUrl();
                                                                                 Log.d("ani", downloaduri + "dwnlduri");

                                                                                 dbuser.child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                     @Override
                                                                                     public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                         if (dataSnapshot.getValue() != null) {

                                                                                             dbuser.child(auth.getCurrentUser().getUid()).child("imageurl").setValue(downloaduri.toString());
                                                                                             imageViewStudentPic.setImageBitmap(bitmap);
                                                                                             progressDialog.dismiss();

                                                                                         }
                                                                                     }

                                                                                     @Override
                                                                                     public void onCancelled(DatabaseError databaseError) {
                                                                                         Log.d("ani", databaseError.getMessage());
                                                                                     }
                                                                                 });
                                                                             }
                                                                         });

               // localdb.setUri(resultUri.toString(), localdb.getStudent().getId());

//                Picasso.with(context).load(resultUri).networkPolicy(NetworkPolicy.OFFLINE).
//                        transform(new CircleTransform()).into(imageViewStudentPic, new Callback() {
//                    @Override
//                    public void onSuccess() {
//                        imageViewStudentPic.setImageBitmap(bitmap);
//                        //localdb.setUri(resultUri.toString(), student.getId());
//                    }
//
//                    @Override
//                    public void onError() {
//                        Picasso.with(context).load(R.drawable.male).transform(new CircleTransform()).into(imageViewStudentPic);
//                    }
//                });

                //Picasso.with(context).load(resultUri).placeholder(R.drawable.spinning_circle).into(imageViewStudentPic);

                //todo have a look
                //insert image into database
                //DatabaseHelper db = new DatabaseHelper(context);
                //db.insertImage(bitmap, new LocalDB(context).getStudent().getId().toString());

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(context, "cannot_retrieve_cropped_image", Toast.LENGTH_SHORT).show();
        }
    }

    private UCrop advancedConfig(@NonNull UCrop uCrop) {
        UCrop.Options options = new UCrop.Options();

        options.setCompressionFormat(Bitmap.CompressFormat.PNG);

        options.setCompressionQuality(90);


        //options.setMaxScaleMultiplier(5);
        options.setImageToCropBoundsAnimDuration(400);
        options.setDimmedLayerColor(Color.WHITE);
        options.setOvalDimmedLayer(true);
        //options.setShowCropFrame(false);
        //options.setCropGridStrokeWidth(20);
        //options.setCropGridColor(Color.GREEN);
        //options.setCropGridColumnCount(2);
        //options.setCropGridRowCount(1);


        // Color palette
        options.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        options.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        options.setActiveWidgetColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        options.setToolbarWidgetColor(ContextCompat.getColor(this, R.color.colorWhite));


        return uCrop.withOptions(options);
    }

    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    private void handleCropError(@NonNull Intent result) {
        final Throwable cropError = UCrop.getError(result);
        if (cropError != null) {
            MyUtils.logThis("MenuActivity - UcropError : " + cropError.getMessage());
            Toast.makeText(context, cropError.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "unexpected_error", Toast.LENGTH_SHORT).show();
        }
    }

    public Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    /*-----------------------end crop image------------------------------*/


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_query) {
//            Intent i = new Intent(context, QueryActivity.class);
//            startActivity(i);
        } else if (id == R.id.nav_add_student) {
            Intent i = new Intent(context, AddMoreStudentActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
