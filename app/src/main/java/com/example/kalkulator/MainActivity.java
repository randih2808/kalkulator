package com.example.kalkulator;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;


public class MainActivity extends AppCompatActivity {
    // ID dari semua tombol numerik
    private int[] numericButtons = {R.id.btnZero, R.id.btnOne, R.id.btnTwo, R.id.btnThree, R.id.btnFour, R.id.btnFive, R.id.btnSix, R.id.btnSeven, R.id.btnEight, R.id.btnNine};
    // ID semua tombol operator
    private int[] operatorButtons = {R.id.btnAdd, R.id.btnSubtract, R.id.btnMultiply, R.id.btnDivide};
    // Menampilkan output
    private TextView txtScreen;
    // Nyatakan apakah tombol yang terakhir ditekan adalah angka atau bukan
    private boolean lastNumeric;
    // Menyatakan bahwa keadaan saat ini dalam kesalahan atau tidak
    private boolean stateError;
    // Jika benar, jangan izinkan untuk menambahkan DOT lain
    private boolean lastDot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // TextView
        this.txtScreen = (TextView) findViewById(R.id.txtScreen);
        // sett OnClickListener ke tombol numerik
        setNumericOnClickListener();
        // Mengatur OnClickListener ke tombol operator, tombol sama dengan dan tombol titik desimal
        setOperatorOnClickListener();
    }

    /**
     * sett OnClickListener ke tombol numerik.
     */
    private void setNumericOnClickListener() {
        // Membuat OnClickListener
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cukup tambahkan/atur teks tombol yang diklik
                Button button = (Button) v;
                if (stateError) {
                    // Jika eror, tampilkan pesan eror
                    txtScreen.setText(button.getText());
                    stateError = false;
                } else {
                    // Jika tidak, sudah ada ekspresi yang valid jadi tambahkan
                    txtScreen.append(button.getText());
                }
                lastNumeric = true;
            }
        };
        for (int id : numericButtons) {
            findViewById(id).setOnClickListener(listener);
        }
    }

    /**
     * Mengatur OnClickListener ke tombol operator, tombol sama dengan dan tombol titik desimal.
     */
    private void setOperatorOnClickListener() {
        // Membuan OnClick
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Jika status saat ini Error, jangan tambahkan operator
                // Jika input terakhir hanya angka, tambahkan operator
                if (lastNumeric && !stateError) {
                    Button button = (Button) v;
                    txtScreen.append(button.getText());
                    lastNumeric = false;
                    lastDot = false;
                }
            }
        };
        for (int id : operatorButtons) {
            findViewById(id).setOnClickListener(listener);
        }
        // Titik desimal '.'
        findViewById(R.id.btnDot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastNumeric && !stateError && !lastDot) {
                    txtScreen.append(".");
                    lastNumeric = false;
                    lastDot = true;
                }
            }
        });
        // Tombol hapus 'C'
        findViewById(R.id.btnClear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtScreen.setText("");  // Clear the screen
                // Reset all the states and flags
                lastNumeric = false;
                stateError = false;
                lastDot = false;
            }
        });
        // Tombol sama dengan '='
        findViewById(R.id.btnEqual).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEqual();
            }
        });
    }

    /**
     * Logika untuk menghitung solusi.
     */
    private void onEqual() {
        // Jika eror,
        // jika input angka, solusi ditemukan
        if (lastNumeric && !stateError) {
            // Read the expression
            String txt = txtScreen.getText().toString();
            // Create an Expression (A class from exp4j library)
            Expression expression = new ExpressionBuilder(txt).build();
            try {
                // Calculate the result and display
                double result = expression.evaluate();
                txtScreen.setText(String.valueOf(result));
                lastDot = true; // Result contains a dot
            } catch (ArithmeticException ex) {
                // Display an error message
                txtScreen.setText("Error");
                stateError = true;
                lastNumeric = false;
            }
        }
    }
}