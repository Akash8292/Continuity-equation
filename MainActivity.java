package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    String[] dom =  {"Electromagnetism","Fluid Dynamics","Computer Vision","Energy and Heat"};
    AutoCompleteTextView autoCompleteTxt;
    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn=findViewById(R.id.button);
        TextView tv=findViewById(R.id.tv);
        AutoCompleteTextView x=findViewById(R.id.x);
        AutoCompleteTextView y=findViewById(R.id.y);
        AutoCompleteTextView z=findViewById(R.id.z);
        AutoCompleteTextView t=findViewById(R.id.t);

        if(!Python.isStarted()){
            Python.start(new AndroidPlatform(this));
        }

        Python py=Python.getInstance();
        final PyObject pyobj1=py.getModule("divergence");
        final PyObject pyobj2=py.getModule("derivative");
        final PyObject pyobj3=py.getModule("subs");

        autoCompleteTxt=findViewById(R.id.act);
        ArrayAdapter<String> adapterItems= new ArrayAdapter<>(this, R.layout.dropitem, dom);
        autoCompleteTxt.setAdapter(adapterItems);

        autoCompleteTxt.setOnItemClickListener((parent, view, position, id) -> {
            String domain_nam = parent.getItemAtPosition(position).toString();
            Toast.makeText(getApplicationContext(),"Domain: "+domain_nam,Toast.LENGTH_SHORT).show();
            pos=position;

            TextInputEditText tiet1=findViewById(R.id.tiet1);
            TextInputEditText tiet2=findViewById(R.id.tiet2);
            clear(tiet1,tiet2,x,y,z,t);
            switch (position){
                case 0:
                    tiet1.setHint("Current Density Vector Ex(x^2  y^2  z^1)");
                    tiet2.setHint("Charge Density");
                    break;
                case 1:
                    tiet1.setHint("Density of Fluid");
                    tiet2.setHint("Velocity Vector of Fluid Ex(x^2  y^2  z^1)");
                    break;
                case 2:
                    tiet1.setHint("Image Intensity");
                    tiet2.setHint("Optical Velocity Vector Ex(x^2 y^2 z^1)");
                    break;
                case 3:
                    tiet1.setHint("Energy Flux Vector Ex(x^2  y^2  z^1)");
                    tiet2.setHint("Energy Density");
                    break;
            }
        });


        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                try {
                    TextInputEditText tiet1 = findViewById(R.id.tiet1);
                    TextInputEditText tiet2 = findViewById(R.id.tiet2);

                    PyObject obj1 = null;
                    PyObject obj2 = null;
                    PyObject obj3 = null;

                    int valx = Integer.parseInt(x.getText().toString());
                    int valy = Integer.parseInt(y.getText().toString());
                    int valz = Integer.parseInt(z.getText().toString());
                    int valt = Integer.parseInt(t.getText().toString());
                    switch (pos) {
                        case 0:
                        case 3:
                            String etiet1 = tiet1.getText().toString();
                            String etiet2 = tiet2.getText().toString();

                            obj1 = pyobj1.callAttr("divergence", etiet1, valx, valy, valz);
                            obj2 = pyobj2.callAttr("derivative", etiet2, valt);

                            if (obj1.toInt() + obj2.toInt() == 0) {
                                tv.setText("It is Continuous");
                            } else {
                                tv.setText("It is not Continuous");
                            }
                            break;
                        case 1:
                        case 2:
                            String ftiet1 = tiet1.getText().toString();
                            String ftiet2 = tiet2.getText().toString();

                            obj1 = pyobj1.callAttr("divergence", ftiet2, valx, valy, valz);
                            obj2 = pyobj2.callAttr("derivative", ftiet1, valt);
                            obj3 = pyobj3.callAttr("sub", ftiet1, valt);

                            if ((((obj3.toInt()) * (obj1.toInt())) + obj2.toInt()) == 0) {
                                tv.setText("It is Continuous");
                            } else {
                                tv.setText("It is not Continuous");
                            }
                            break;
                    }
                }
                catch (Exception e){
                    tv.setText("Enter a Valid Input!!");
                }
            }
        });
    }
    public void clear(TextInputEditText v1,TextInputEditText v2,AutoCompleteTextView a1,AutoCompleteTextView a2,AutoCompleteTextView a3,AutoCompleteTextView a4){
        v1.setText("");
        v2.setText("");
        a1.setText("");
        a2.setText("");
        a3.setText("");
        a4.setText("");
    }
}




