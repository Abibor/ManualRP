package com.example.manualrp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    //Объект для аутентификации
    private FirebaseAuth mAuth;
    //Находим элементы для layout
    private EditText edLogin, edPassword;
    private Button bStart, bSignUp, bSignIn, bSignOut;
    private TextView tvUserName;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activtity);
        Log.d("myLogs", "--Class Login start--");

        setTitle("Авторизация");
        tvUserName = findViewById(R.id.tvUserEmail);
        edLogin = findViewById(R.id.edLogin);
        edPassword = findViewById(R.id.edPassword);
        tvUserName = findViewById(R.id.tvUserEmail);
        bStart = findViewById(R.id.bStart);
        bSignIn = findViewById(R.id.bSignIn);
        bSignUp = findViewById(R.id.bSignUp);
        bSignOut = findViewById(R.id.bSignOut);


        // Получаем инстанцию переменной аутентификации
        mAuth = FirebaseAuth.getInstance();
    }

    protected void onStart() {
        super.onStart();
        Log.d("myLogs", "--Class Login onStart--");

        // Код проверки вхада
        FirebaseUser cUser = mAuth.getCurrentUser();
        // если пользователь вошел в аккаунт, то будет отображаться экран с кнопкой и именем полльзователя
        if(cUser != null)
        {
            // настраиваем видимость кнопки и текста Visible
            showSigned();

            // получаем в строку имя пользователя
            String userName = "Вы вошли как: " + cUser.getEmail();
            tvUserName.setText(userName);

        }
        // в противном случае если пользователь не вошел в аккаунт, то будет отображаться экран регистрации
        else {
            // настраиваем невидимость кнопки и текста Gone
            notSigned();

        }
        Toast.makeText(this,"User not null", Toast.LENGTH_SHORT).show();
    }

    public void onClickSignUp(View view)
    {
        if(!TextUtils.isEmpty(edLogin.getText().toString()) && !TextUtils.isEmpty(edPassword.getText().toString()))
        {
            mAuth.createUserWithEmailAndPassword(edLogin.getText().toString(), edPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                // Слушатель регистрации пользователя onComplete
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        showSigned();
                        sendEmailVer();

                        Toast.makeText(getApplicationContext(),"User SighUp Successful", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        notSigned();
                        Toast.makeText(getApplicationContext(),"User SignUp Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Please entere email and password", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickSignIn(View view)
    {
        if(!TextUtils.isEmpty(edLogin.getText().toString()) && !TextUtils.isEmpty(edPassword.getText().toString()))
        {
            mAuth.signInWithEmailAndPassword(edLogin.getText().toString(), edPassword.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        showSigned();
                        Toast.makeText(getApplicationContext(),"User SighIn Successful", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        notSigned();
                        Toast.makeText(getApplicationContext(),"User SignIn Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void onClickSignOut(View view)
    {
        // выход из аккаунта
        FirebaseAuth.getInstance().signOut();
        notSigned();
    }

    private void showSigned()
    {
        FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        if(user.isEmailVerified())
        {
            String userName = "Вы вошли как: " + user.getEmail();
            String userGet = user.getEmail();
            Log.d("myLogs", "userGet - " + userGet);

            tvUserName.setText(userName);

            bStart.setVisibility(View.VISIBLE);
            tvUserName.setVisibility(View.VISIBLE);
            bSignOut.setVisibility(View.VISIBLE);
            edLogin.setVisibility(View.GONE);
            edPassword.setVisibility(View.GONE);
            bSignIn.setVisibility(View.GONE);
            bSignUp.setVisibility(View.GONE);
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Check your Email for verification user account", Toast.LENGTH_SHORT).show();
        }
    }

    private void notSigned()
    {
        bStart.setVisibility(View.GONE);
        tvUserName.setVisibility(View.GONE);
        bSignOut.setVisibility(View.GONE);
        edLogin.setVisibility(View.VISIBLE);
        edPassword.setVisibility(View.VISIBLE);
        bSignIn.setVisibility(View.VISIBLE);
        bSignUp.setVisibility(View.VISIBLE);

    }

    public void onClickStart(View view)
    {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    private void sendEmailVer()
    {
        FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    //Toast.makeText(getApplicationContext(),"Check your Email for verification user account", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(),"Для входа перейдите по ссылке в письме в указанной почте", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Send email filed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
