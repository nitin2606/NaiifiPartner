package com.example.naiifipartner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class RegisterActivity extends AppCompatActivity {




    private TextInputLayout pass_otp;
    private String verificationId;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private EditText phoneNo ,otp , emailId,name;
    private  TextView getotp , otpsent , resendOTP , countdowntimer , auto_capture , already_user ;
    private boolean getotpclicked = false;
    Dialog dialog;
    Dialog dialog1 ;
    FirebaseUser user;
    private DatabaseReference mRootRef;
    private DatabaseReference mPhoneRef;
    private LinearLayout auto_capture_ll;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        setContentView(R.layout.activity_register);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.custom_status));

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setNavigationBarColor(getResources().getColor(R.color.white));
        initElements();




    }

    public void initElements(){

        mAuth = FirebaseAuth.getInstance();

        name = findViewById(R.id.txtName);
        emailId = findViewById(R.id.txtEmail);
        phoneNo = findViewById(R.id.txtPhone);
        otp = findViewById(R.id.txtotp);
        getotp = findViewById(R.id.get_otp);
        resendOTP = findViewById(R.id.resend_otp);
        otpsent = findViewById(R.id.otp_sent);
        countdowntimer = findViewById(R.id.countdown_timer);
        auto_capture = findViewById(R.id.auto_capture);
        already_user = findViewById(R.id.already_user);
        pass_otp=findViewById(R.id.Password_text_inputLayout);

        auto_capture_ll  =findViewById(R.id.auto_capture_ll);


        phoneNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length()==10){
                    readPhone(editable.toString().trim());
                }
                if(editable.length()<10){
                    already_user.setTextSize(15);
                    //getotp.setVisibility(View.VISIBLE);

                }
                if(editable.length()>10){
                    phoneNo.setError("Enter a valid number .");
                }

            }
        });


        already_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i  = new Intent(RegisterActivity.this , LoginActivity.class);
                startActivity(i);
                finish();
            }
        });


        otp.addTextChangedListener(new TextWatcher() {

            final Handler handler = new Handler(Looper.getMainLooper());
            Runnable workRunnable;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                handler.removeCallbacks(workRunnable);
                workRunnable = () -> doSmth(editable.toString());
                handler.postDelayed(workRunnable, 1500 /*delay*/);

            }
        });


        getotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getotpOnclick();
               // auto_capture.setVisibility(View.VISIBLE);
                pass_otp.setVisibility(View.VISIBLE);
                getotp.setVisibility(View.GONE);

            }
        });

        resendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getotp.setVisibility(View.GONE);
                getotpOnclick();
                resendOTP.setVisibility(View.GONE);

            }
        });


    }

    private final void doSmth(String str) {
        //auto_capture.setVisibility(View.GONE);
        auto_capture_ll.setVisibility(View.GONE);
        loginOnClick();
    }

    public void loginOnClick(){

        String number = phoneNo.getText().toString().trim();
        String otp1 = otp.getText().toString().trim();
        String email = emailId.getText().toString().trim();
        String salonName = name.getText().toString().trim();

        if (number.length() == 10 && otp1.length() > 4){

            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
            dialog1 = new Dialog(RegisterActivity.this);
            dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog1.setContentView(R.layout.dialog_wait);
            dialog1.setCanceledOnTouchOutside(false);
            dialog1.show();
            verifyCode(otp1);


        }else {

            if (number.isEmpty() || number.length() < 10 || email.isEmpty() || salonName.isEmpty() ){

                name.setError("Name required");
                emailId.setError("Valid Email required");
                emailId.requestFocus();
                phoneNo.setError("Valid number is required");
                phoneNo.requestFocus();

            }else if (otp1.isEmpty() || otp1 .length() < 5){

                otp.setError("Valid OTP is required");
                otp.requestFocus();


            }


        }

    }

    private void verifyCode(String code){

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId,code);
        FirebaseAuth.getInstance().getFirebaseAuthSettings().setAppVerificationDisabledForTesting(false);
        signInWithCredential(credential);


    }

    private void signInWithCredential(PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){
                            user = FirebaseAuth.getInstance().getCurrentUser();

                            if (user !=null){
                                uploadData(name.getText().toString().trim(),phoneNo.getText().toString().trim() ,emailId.getText().toString().trim());

                            }else {

                                if (dialog != null){

                                    dialog1.dismiss();
                                    Toast.makeText(RegisterActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }
                });
    }

    public void getotpOnclick(){

        if (!getotpclicked){

            String num = phoneNo.getText().toString().trim();

            if(num.length() != 10){

                phoneNo.setError("Valid number is required");
                phoneNo.requestFocus();

            }else {

                getotpclicked = true;
                String phoneNumber = "+91" + num;
                sendVerificationCode(phoneNumber);
                getotp.setTextColor(Color.parseColor("#C0BEBE"));
                dialog = new Dialog(RegisterActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_wait);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();


            }

        }


    }

    private void sendVerificationCode(String phoneNumber) {

        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth).setPhoneNumber(phoneNumber).setTimeout(60L, TimeUnit.SECONDS).setActivity(this).setCallbacks(mCallBack)
                        .build();

        PhoneAuthProvider.verifyPhoneNumber(options);


    }

    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {


        @Override
        public void onCodeSent(@NonNull String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {

            dialog.dismiss();
            //otpsent.setText("OTP has been sent yo your mobile number");
            //otpsent.setVisibility(View.VISIBLE);
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;

            countdowntimer.setVisibility(View.VISIBLE);
            //auto_capture.setVisibility(View.VISIBLE);
            auto_capture_ll.setVisibility(View.VISIBLE);

            new CountDownTimer(60000,1000){


                @Override
                public void onTick(long millisUntilFinished) {

                    countdowntimer.setText("" + millisUntilFinished/1000);

                }

                @Override
                public void onFinish() {
                    //auto_capture.setVisibility(View.GONE);
                    auto_capture_ll.setVisibility(View.VISIBLE);
                    resendOTP.setVisibility(View.VISIBLE);
                    otpsent.setVisibility(View.GONE);
                    countdowntimer.setVisibility(View.INVISIBLE);

                }
            }.start();
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            String code = phoneAuthCredential.getSmsCode();
            if (code != null){

                otp.setText(code);
                verifyCode(code);

            }

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

            getotpclicked = false;
            //getotp.setTextColor(Color.parseColor("00000FF"));
            Toast.makeText(RegisterActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();

        }
    };

    private  void uploadData(String salonName, String phoneNo , String emailId){
        mRootRef = FirebaseDatabase.getInstance().getReference("Salons");

        String firebaseId = mAuth.getCurrentUser().getUid();
        String salonId = getAlphaNumericString(phoneNo);
        HashMap<String,Object> map=new HashMap<>();
        map.put("name",salonName);
        map.put("email",emailId);
        map.put("phoneNo",phoneNo);
        map.put("firebaseId",firebaseId);
        map.put("salonId",salonId);
        map.put("vstatus","unverified");
        map.put("info","incomplete");


        mRootRef.child(phoneNo).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                writePhone(phoneNo);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, "Some error occurred !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onBackPressed(){

    }

    private String getAlphaNumericString(String n) {


        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String number = n;
        Random random = new Random();

        StringBuilder num = new StringBuilder(4);
        StringBuilder str = new StringBuilder(4);

        for (int i = 0; i<5; i++) {

            num.append(number.charAt(random.nextInt(9)));
        }
        for(int j=0 ; j<5 ; j++){
            str.append(AlphaNumericString.charAt(random.nextInt(25)));
        }

        return num.toString()+ str;

    }

    private void  readPhone(String phone){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Salons");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int c = 0;
                for(DataSnapshot d : snapshot.getChildren()){
                     if(d.getKey().equals(phone)){
                         c=c+1;
                     }
                }
                if(c==1){
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
                    phoneNo.setError("Account already exists,login instead .");
                    phoneNo.requestFocus();
                    getotp.setVisibility(View.GONE);
                    already_user.setTextSize(18);
                }
                else if (c==0){
                    getotp.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void writePhone(String phone){
        mPhoneRef = FirebaseDatabase.getInstance().getReference("PhoneNo");
        String firebaseId = mAuth.getCurrentUser().getUid();
        HashMap<String ,Object> map = new HashMap<>();
        map.put(firebaseId, phone);

        mPhoneRef.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {



                phoneNo.setText("");
                dialog1.dismiss();
                Toast.makeText(RegisterActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(RegisterActivity.this,InfoActivity.class);
                startActivity(i);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, "Some error occurred !", Toast.LENGTH_SHORT).show();
            }
        });
    }

}