package com.atd.xiwei;

import android.app.*;
import android.os.*;
import android.support.v7.app.AppCompatActivity;
import com.atd.xiwei.CircleView;
public class MainActivity extends AppCompatActivity 
{
	private CircleView circle1;
	private CircleView circle2;
	private CircleView rect;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		circle1 = (CircleView)findViewById(R.id.cir1);
		circle2 = (CircleView)findViewById(R.id.cir2);
		rect = (CircleView)findViewById(R.id.rect);
		
        
    }
}
