package com.atd.xiwei;

import android.app.*;
import android.os.*;
import android.support.v7.app.AppCompatActivity;
import com.atd.xiwei.CircleView;
import android.widget.*;
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
		rect = (CircleView)findViewById(R.id.rect1);
		/*if ( circle1 == null ||circle2==null/*rect==null)
		{
			Toast.makeText(this,"null ",Toast.LENGTH_LONG).show();
		}*/
		/*if(getResources().getDrawable(R.drawable.photo_one) != null)
		{
			Toast.makeText(this,"not null ",Toast.LENGTH_LONG).show();
		}else
		{
			Toast.makeText(this,"null ",Toast.LENGTH_LONG).show();
		}*/
		circle1.setImageDrawble(getResources().getDrawable(R.drawable.photo_one));
        circle2.setImageDrawble(getResources().getDrawable(R.drawable.photo_two));
		rect.setImageDrawble(getResources().getDrawable(R.drawable.photo_three));
    }
}
