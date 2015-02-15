package com.thefinestartist.movingbutton.sample;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.crashlytics.android.Crashlytics;
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
    @InjectView(R.id.movement_left_bt)
    View movementLeftBt;
    @InjectView(R.id.movement_left_tv)
    TextView movementLeftTv;
    @InjectView(R.id.movement_right_bt)
    View movementRightBt;
    @InjectView(R.id.movement_right_tv)
    TextView movementRightTv;
    @InjectView(R.id.movement_top_bt)
    View movementTopBt;
    @InjectView(R.id.movement_top_tv)
    TextView movementTopTv;
    @InjectView(R.id.movement_bottom_bt)
    View movementBottomBt;
    @InjectView(R.id.movement_bottom_tv)
    TextView movementBottomTv;
    @InjectView(R.id.movement_inner_offset_bt)
    View innerOffsetBt;
    @InjectView(R.id.movement_inner_offset_tv)
    TextView innerOffsetTv;
    @InjectView(R.id.movement_outer_offset_bt)
    View outerOffsetBt;
    @InjectView(R.id.movement_outer_offset_tv)
    TextView outerOffsetTv;
    @InjectView(R.id.volume_bt)
    View volumeBt;
    @InjectView(R.id.volume_tv)
    TextView volumeTv;
    @InjectView(R.id.vibration_strength_bt)
    View vibrationStrengthBt;
    @InjectView(R.id.vibration_strength_tv)
    TextView vibrationStrengthTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Crashlytics.start(this);
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

        movementLeftBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(MainActivity.this)
                        .title(getString(R.string.movement_left))
                        .items(getDPLists())
                        .itemsCallbackSingleChoice(dpFromPx(movingButton.getMovementLeft()) + 10, new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog materialDialog, View view, int which, CharSequence text) {
                                movingButton.setMovementLeft(pxFromDp(which - 10));
                                movementLeftTv.setText("" + (which - 10) + "dp");
                            }
                        })
                        .positiveText(getString(R.string.choose))
                        .show();
            }
        });

        movementRightBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(MainActivity.this)
                        .title(getString(R.string.movement_right))
                        .items(getDPLists())
                        .itemsCallbackSingleChoice(dpFromPx(movingButton.getMovementRight()) + 10, new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog materialDialog, View view, int which, CharSequence text) {
                                movingButton.setMovementRight(pxFromDp(which - 10));
                                movementRightTv.setText("" + (which - 10) + "dp");
                            }
                        })
                        .positiveText(getString(R.string.choose))
                        .show();
            }
        });

        movementTopBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(MainActivity.this)
                        .title(getString(R.string.movement_top))
                        .items(getDPLists())
                        .itemsCallbackSingleChoice(dpFromPx(movingButton.getMovementTop()) + 10, new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog materialDialog, View view, int which, CharSequence text) {
                                movingButton.setMovementTop(pxFromDp(which - 10));
                                movementTopTv.setText("" + (which - 10) + "dp");
                            }
                        })
                        .positiveText(getString(R.string.choose))
                        .show();
            }
        });

        movementBottomBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(MainActivity.this)
                        .title(getString(R.string.movement_bottom))
                        .items(getDPLists())
                        .itemsCallbackSingleChoice(dpFromPx(movingButton.getMovementBottom()) + 10, new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog materialDialog, View view, int which, CharSequence text) {
                                movingButton.setMovementBottom(pxFromDp(which - 10));
                                movementBottomTv.setText("" + (which - 10) + "dp");
                            }
                        })
                        .positiveText(getString(R.string.choose))
                        .show();
            }
        });

        innerOffsetBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(MainActivity.this)
                        .title(getString(R.string.inner_offset))
                        .items(getDPLists2())
                        .itemsCallbackSingleChoice(dpFromPx(movingButton.getOffSetInner()), new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog materialDialog, View view, int which, CharSequence text) {
                                movingButton.setOffSetInner(pxFromDp(which));
                                innerOffsetTv.setText("" + which + "dp");
                            }
                        })
                        .positiveText(getString(R.string.choose))
                        .show();
            }
        });

        outerOffsetBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(MainActivity.this)
                        .title(getString(R.string.outer_offset))
                        .items(getDPLists2())
                        .itemsCallbackSingleChoice(dpFromPx(movingButton.getOffSetOuter()), new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog materialDialog, View view, int which, CharSequence text) {
                                movingButton.setOffSetOuter(pxFromDp(which));
                                outerOffsetTv.setText("" + which + "dp");
                            }
                        })
                        .positiveText(getString(R.string.choose))
                        .show();
            }
        });

        volumeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(MainActivity.this)
                        .title(getString(R.string.volume))
                        .items(getVolumeLists())
                        .itemsCallbackSingleChoice(movingButton.getEventVolume(), new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog materialDialog, View view, int which, CharSequence text) {
                                movingButton.setEventVolume(which);
                                volumeTv.setText("" + which);
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

    private String[] getMoveDirectionNames() {
        MoveDirection[] states = MoveDirection.values();
        String[] names = new String[states.length];
        for (int i = 0; i < states.length; i++)
            names[i] = states[i].name();
        return names;
    }

    private String[] getVibrationStrengthNames() {
        VibrationStrength[] states = VibrationStrength.values();
        String[] names = new String[states.length];
        for (int i = 0; i < states.length; i++)
            names[i] = states[i].name();
        return names;
    }

    private String[] getDPLists() {
        String[] names = new String[41];
        for (int i = 0; i < names.length; i++)
            names[i] = "" + (i - 10) + "dp";
        return names;
    }

    private String[] getDPLists2() {
        String[] names = new String[51];
        for (int i = 0; i < names.length; i++)
            names[i] = "" + i + "dp";
        return names;
    }

    private String[] getVolumeLists() {
        String[] names = new String[101];
        for (int i = 0; i < names.length; i++)
            names[i] = "" + i;
        return names;
    }

    private int dpFromPx(int px) {
        return (int) (px / getResources().getDisplayMetrics().density);
    }

    private int pxFromDp(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }
}
