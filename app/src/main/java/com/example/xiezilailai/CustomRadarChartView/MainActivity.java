package com.example.xiezilailai.CustomRadarChartView;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener{

    private SeekBar[] bars=new SeekBar[8];

    private CustomRadarChartView view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        view=(CustomRadarChartView)findViewById(R.id.customPolygonsView);

        bars[1]=(SeekBar)findViewById(R.id.bar1);
        bars[2]=(SeekBar)findViewById(R.id.bar2);
        bars[3]=(SeekBar)findViewById(R.id.bar3);
        bars[4]=(SeekBar)findViewById(R.id.bar4);
        bars[5]=(SeekBar)findViewById(R.id.bar5);

        bars[1].setOnSeekBarChangeListener(this);
        bars[2].setOnSeekBarChangeListener(this);
        bars[3].setOnSeekBarChangeListener(this);
        bars[4].setOnSeekBarChangeListener(this);
        bars[5].setOnSeekBarChangeListener(this);

        bars[1].setTag(1);
        bars[2].setTag(2);
        bars[3].setTag(3);
        bars[4].setTag(4);
        bars[5].setTag(5);

        List<Integer>colors=new ArrayList<>();
        colors.add(getResources().getColor(R.color.green_5));
        colors.add(getResources().getColor(R.color.green_4));
        colors.add(getResources().getColor(R.color.green_3));
        colors.add(getResources().getColor(R.color.green_2));
        colors.add(getResources().getColor(R.color.green_1));

        view.setLayer(colors);

        bars[1].setProgress((int) (view.getLevel(0)*100));
        bars[2].setProgress((int) (view.getLevel(1)*100));
        bars[3].setProgress((int) (view.getLevel(2)*100));
        bars[4].setProgress((int) (view.getLevel(3)*100));
        bars[5].setProgress((int) (view.getLevel(4)*100));


    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

       view.setValue((int)seekBar.getTag()-1, (float) (seekBar.getProgress()/100.0));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
