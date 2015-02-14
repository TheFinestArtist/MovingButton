package com.thefinestartist.movingbutton.sample;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.thefinestartist.movingbutton.MovingButton;
import com.thefinestartist.movingbutton.enums.ButtonPosition;
import com.thefinestartist.movingbutton.enums.MoveDirection;
import com.thefinestartist.movingbutton.enums.VibrationStrength;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends ActionBarActivity {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.moving_button)
    MovingButton movingButton;
    @InjectView(R.id.current_position_info)
    TextView currentPositionInfo;
    @InjectView(R.id.move_direction_bt)
    View moveDirectionBt;
    @InjectView(R.id.move_direction_tv)
    TextView moveDirectionTv;
    @InjectView(R.id.vibration_strength_bt)
    View vibrationStrengthBt;
    @InjectView(R.id.vibration_strength_tv)
    TextView vibrationStrengthTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);

        movingButton.setOnPositionChangedListener(new MovingButton.OnPositionChangedListener() {
            @Override
            public void onPositionChanged(int action, ButtonPosition position) {
                currentPositionInfo.setText(position.name());
            }
        });

        moveDirectionBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(MainActivity.this)
                        .title(getString(R.string.move_direction))
                        .items(getMoveDirectionNames())
                        .itemsCallbackSingleChoice(movingButton.getMoveDirection().ordinal(), new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                MoveDirection moveDirection = MoveDirection.values()[which];
                                movingButton.setMoveDirection(moveDirection);
                                moveDirectionTv.setText(moveDirection.name());
                            }
                        })
                        .positiveText(getString(R.string.choose))
                        .show();

            }
        });

        vibrationStrengthBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(MainActivity.this)
                        .title(getString(R.string.vibration_strength))
                        .items(getVibrationStrengthNames())
                        .itemsCallbackSingleChoice(movingButton.getVibrationStrength().ordinal(), new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog materialDialog, View view, int which, CharSequence text) {
                                VibrationStrength vibrationStrength = VibrationStrength.values()[which];
                                movingButton.setVibrationStrength(vibrationStrength);
                                vibrationStrengthTv.setText(vibrationStrength.name());
                            }
                        })
                        .positiveText(getString(R.string.choose))
                        .show();
            }
        });
    }

    public static String[] getMoveDirectionNames() {
        MoveDirection[] states = MoveDirection.values();
        String[] names = new String[states.length];
        for (int i = 0; i < states.length; i++)
            names[i] = states[i].name();
        return names;
    }

    public static String[] getVibrationStrengthNames() {
        VibrationStrength[] states = VibrationStrength.values();
        String[] names = new String[states.length];
        for (int i = 0; i < states.length; i++)
            names[i] = states[i].name();
        return names;
    }
}
