package com.example.naiifipartner;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
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
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout pass_otp;
    private String verificationId;
    private FirebaseAuth mAuth;
    private EditText phoneNo ,otp;
    private TextView getotp , otpsent , resendOTP , countdowntimer , auto_capture , new_user;
    private boolean getotpclicked = false;
    private ProgressBar auto_capture_progress;
    private LinearLayout auto_capture_ll ;



    Dialog dialog;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        setContentView(R.layout.activity_login);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);


        initElements();
    }

    public void initElements(){

        mAuth = FirebaseAuth.getInstance();
        phoneNo = findViewById(R.id.txtPhone);
        otp = findViewById(R.id.txtotp);
        getotp = findViewById(R.id.get_otp);
        resendOTP = findViewById(R.id.resend_otp);
        otpsent = findViewById(R.id.otp_sent);
        countdowntimer = findViewById(R.id.countdown_timer);
        auto_capture=findViewById(R.id.auto_capture);
        new_user = findViewById(R.id.new_user);
        pass_otp=findViewById(R.id.Password_text_inputLayout);
        auto_capture_progress = findViewById(R.id.auto_capture_progress);
        auto_capture_ll  = findViewById(R.id.auto_capture_ll);




        new_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reg = new Intent(LoginActivity.this , RegisterActivity.class);
                startActivity(reg);
                finish();
            }
        });


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
                    new_user.setTextSize(15);
                    //getotp.setVisibility(View.VISIBLE);

                }
                if(editable.length()>10){
                    phoneNo.setError("Enter a valid number .");

                }

            }
        });



        otp.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                //auto_capture_progress.setVisibility(View.GONE);
                //auto_capture.setVisibility(View.GONE);
                auto_capture_ll.setVisibility(View.GONE);
                loginOnClick();


            }
        });

        getotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getotpOnclick();
                //auto_capture.setVisibility(View.VISIBLE);
                pass_otp.setVisibility(View.VISIBLE);
                getotp.setVisibility(View.GONE);
                auto_capture_progress.setVisibility(View.VISIBLE);

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



    public void loginOnClick(){

        String number = phoneNo.getText().toString().trim();
        String otp1 = otp.getText().toString().trim();

        if (number.length() == 10 && otp1.length() > 5){

            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
            dialog = new Dialog(LoginActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setContentView(R.layout.dialog_wait);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            verifyCode(otp1);


        }else {

            if (number.isEmpty() || number.length() < 10){

                phoneNo.setError("Valid number is required");
                phoneNo.requestFocus();

            }else if (otp1.isEmpty()){

                otp.setError("Valid OTP is required");
                otp.requestFocus();


            }


        }

    }

    private void verifyCode(String code){

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId,code);
        FirebaseAuth.getInstance().getFirebaseAuthSettings().setAppVerificationDisabledForTesting(true);
        signInWithCredential(credential);


    }

    private void signInWithCredential(PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){
                            dialog.dismiss();
                            user = FirebaseAuth.getInstance().getCurrentUser();
                            if (user !=null){

                                String uid = FirebaseAuth.getInstance().getUid();
                                DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference("PhoneNo").child(uid);

                                firebaseDatabase.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String value = snapshot.getValue(String.class);

                                        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Salons").child(value).child("info");

                                        db.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                String status = snapshot.getValue(String.class);

                                                if(status.equals("incomplete")){
                                                    Intent intent = new Intent(LoginActivity.this, InfoActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    startActivity(intent);

                                                    finish();

                                                }
                                                else if(status.equals("complete")) {
                                                    Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    startActivity(intent);

                                                    finish();
                                                }

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }else {

                                if (dialog != null){

                                    dialog.dismiss();
                                    Toast.makeText(LoginActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }
                });
    }

    public void getotpOnclick(){

        if (!getotpclicked){

            String num   = phoneNo.getText().toString().trim();

            if(num.length() != 10){

                phoneNo.setError("Valid number is required");
                phoneNo.requestFocus();

            }else {

                getotpclicked = true;
                String phoneNumber = "+91" + num;
                sendVerificationCode(phoneNumber);

                dialog = new Dialog(LoginActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setContentView(R.layout.dialog_wait);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();


            }

        }


    }

    private void sendVerificationCode(String phoneNumber) {

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth).setPhoneNumber(phoneNumber).setTimeout(60L, TimeUnit.SECONDS).setActivity(this).setCallbacks(mCallBack).build();

        PhoneAuthProvider.verifyPhoneNumber(options);


    }

    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {


        @Override
        public void onCodeSent(@NonNull String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {

            dialog.dismiss();

            auto_capture_ll.setVisibility(View.VISIBLE);

            super.onCodeSent(s, forceResendingToken);
            verificationId = s;



            countdowntimer.setVisibility(View.VISIBLE);

            new CountDownTimer(60000,1000){


                @Override
                public void onTick(long millisUntilFinished) {

                    countdowntimer.setText("" + millisUntilFinished/1000);

                }

                @Override
                public void onFinish() {
                    auto_capture_ll.setVisibility(View.GONE);
                    resendOTP.setVisibility(View.VISIBLE);
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

            Toast.makeText(LoginActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();

        }
    };

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
                if(c==0){
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
                    phoneNo.setError("Account does not exists , register first.");
                    phoneNo.requestFocus();
                    getotp.setVisibility(View.GONE);
                    new_user.setTextSize(18);
                }
                else if (c==1){
                    getotp.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }



}