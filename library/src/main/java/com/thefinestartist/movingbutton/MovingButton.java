package com.thefinestartist.movingbutton;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.nineoldandroids.view.ViewHelper;
import com.thefinestartist.movingbutton.enums.ButtonMovement;
import com.thefinestartist.movingbutton.enums.ButtonPosition;
import com.thefinestartist.movingbutton.enums.VibrationStrength;
import com.thefinestartist.movingbutton.utils.SoundUtil;
import com.thefinestartist.movingbutton.utils.VibrateUtil;

/**
 * Created by TheFinestArtist on 2/6/15.
 */
public class MovingButton extends Button {

    public interface OnPositionChangedListener {
        void onPositionChanged(int action, ButtonPosition position);
    }

    int movementVertical;
    int movementHorizontal;
    ButtonMovement buttonMovement;

    int offSetInner;
    int offSetOuter;

    VibrationStrength vibrationStrength;

    int eventVolume;

    OnPositionChangedListener onPositionChangedListener;

    public MovingButton(Context context) {
        this(context, null);
    }

    public MovingButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MovingButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.MovingButton, 0, 0);

        movementHorizontal = attr.getDimensionPixelSize(R.styleable.MovingButton_mb_movement_horizontal,
                getResources().getDimensionPixelSize(R.dimen.default_movement_horizontal));
        movementVertical = attr.getDimensionPixelSize(R.styleable.MovingButton_mb_movement_vertical,
                getResources().getDimensionPixelSize(R.dimen.default_movement_vertical));

        buttonMovement = movementHorizontal != 0 ? movementVertical != 0 ? ButtonMovement.ALL : ButtonMovement.LEFT_RIGHT
                : movementVertical != 0 ? ButtonMovement.TOP_BOTTOM : ButtonMovement.NONE;

        offSetInner = attr.getDimensionPixelSize(R.styleable.MovingButton_mb_offset_inner,
                getResources().getDimensionPixelSize(R.dimen.default_offset_inner));
        offSetOuter = attr.getDimensionPixelSize(R.styleable.MovingButton_mb_offset_outer,
                getResources().getDimensionPixelSize(R.dimen.default_offset_outer));

        vibrationStrength = VibrationStrength.values()[attr.getInt(R.styleable.MovingButton_mb_vibration_strength, 2)];
        eventVolume = attr.getInt(R.styleable.MovingButton_mb_event_volume, 50);

        attr.recycle();
    }

    /**
     * Touch Event
     */
    private boolean firstMoved;
    ButtonPosition currentPosition;

    private float halfWidth;
    private float halfHeight;

    private float centerX;
    private float centerY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                positionChanged(MotionEvent.ACTION_DOWN, ButtonPosition.STILL);
                soundAndVibrate();
                halfWidth = (float) this.getWidth() / 2.0f;
                halfHeight = (float) this.getHeight() / 2.0f;
                currentPosition = ButtonPosition.STILL;
                firstMoved = true;
                break;
            case MotionEvent.ACTION_MOVE:
                recalculateCenter();
                float diffX = event.getX() - centerX;
                float diffY = centerY - event.getY();

                switch (buttonMovement) {
                    case ALL:
                        double length = Math.sqrt(Math.pow(diffX, 2d) + Math.pow(diffY, 2d));
                        if (length > offSetOuter)
                            if (firstMoved) {
                                moveView(this, getPosition(diffX, diffY));
                                firstMoved = false;
                            } else
                                moveView(this, getDetailedPosition(diffX, diffY));
                        else if (length < offSetInner)
                            moveView(this, ButtonPosition.STILL);
                        break;
                    case TOP_BOTTOM:
                        if (diffY > offSetOuter)
                            moveView(this, ButtonPosition.UP);
                        else if (diffY < -offSetOuter)
                            moveView(this, ButtonPosition.DOWN);
                        else if (Math.abs(diffY) < offSetInner)
                            moveView(this, ButtonPosition.STILL);
                        break;
                    case LEFT_RIGHT:
                        if (diffX > offSetOuter)
                            moveView(this, ButtonPosition.RIGHT);
                        else if (diffX < -offSetOuter)
                            moveView(this, ButtonPosition.LEFT);
                        else if (Math.abs(diffX) < offSetInner)
                            moveView(this, ButtonPosition.STILL);
                        break;
                    case NONE:
                        break;
                }

                break;
            case MotionEvent.ACTION_UP:
                moveView(this, ButtonPosition.STILL);
                positionChanged(MotionEvent.ACTION_UP, ButtonPosition.STILL);
                break;
        }
        return super.onTouchEvent(event);
    }

    private void recalculateCenter() {
        switch (currentPosition) {
            case STILL:
                centerX = halfWidth;
                centerY = halfHeight;
                break;
            case RIGHT:
                centerX = halfWidth - movementHorizontal;
                centerY = halfHeight;
                break;
            case RIGHT_DOWN:
                centerX = halfWidth - movementHorizontal;
                centerY = halfHeight - movementVertical;
                break;
            case DOWN:
                centerX = halfWidth;
                centerY = halfHeight - movementVertical;
                break;
            case LEFT_DOWN:
                centerX = halfWidth + movementHorizontal;
                centerY = halfHeight - movementVertical;
                break;
            case LEFT:
                centerX = halfWidth + movementHorizontal;
                centerY = halfHeight;
                break;
            case LEFT_UP:
                centerX = halfWidth + movementHorizontal;
                centerY = halfHeight + movementVertical;
                break;
            case UP:
                centerX = halfWidth;
                centerY = halfHeight + movementVertical;
                break;
            case RIGHT_UP:
                centerX = halfWidth - movementHorizontal;
                centerY = halfHeight + movementVertical;
                break;
        }
    }

    private void moveView(View v, ButtonPosition position) {
        if (position == null || currentPosition == position)
            return;

        positionChanged(MotionEvent.ACTION_MOVE, position);
        soundAndVibrate();
        currentPosition = position;

        float goalViewX = 0, goalViewY = 0;
        switch (position) {
            case STILL:
                goalViewX = 0;
                goalViewY = 0;
                break;
            case LEFT:
                goalViewX = -movementHorizontal;
                goalViewY = 0;
                break;
            case RIGHT:
                goalViewX = movementHorizontal;
                goalViewY = 0;
                break;
            case UP:
                goalViewX = 0;
                goalViewY = -movementVertical;
                break;
            case DOWN:
                goalViewX = 0;
                goalViewY = movementVertical;
                break;
            case LEFT_UP:
                goalViewX = -movementHorizontal;
                goalViewY = -movementVertical;
                break;
            case LEFT_DOWN:
                goalViewX = -movementHorizontal;
                goalViewY = movementVertical;
                break;
            case RIGHT_UP:
                goalViewX = +movementHorizontal;
                goalViewY = -movementVertical;
                break;
            case RIGHT_DOWN:
                goalViewX = movementHorizontal;
                goalViewY = movementVertical;
                break;
        }

        ViewHelper.setTranslationX(v, goalViewX);
        ViewHelper.setTranslationY(v, goalViewY);
    }

    private double getAngle(double diffX, double diffY) {
        double length = Math.sqrt(Math.pow(diffX, 2d) + Math.pow(diffY, 2d));
        double angle = Math.asin(diffY / length);
        if (diffX >= 0d && diffY >= 0d)
            angle = angle + 0.0;
        else if (diffX < 0d && diffY >= 0d)
            angle = Math.PI - angle;
        else if (diffX < 0d && diffY < 0d)
            angle = -angle + Math.PI;
        else
            angle = angle + 2d * Math.PI;
        return angle * 180d / Math.PI;
    }

    private ButtonPosition getPosition(double diffX, double diffY) {
        return getPosition(getAngle(diffX, diffY));
    }

    private ButtonPosition getPosition(double angle) {
        if (67.5 > angle && angle >= 22.5)
            return ButtonPosition.RIGHT_UP;
        else if (22.5 > angle || angle >= 337.5)
            return ButtonPosition.RIGHT;
        else if (337.5 > angle && angle >= 292.5)
            return ButtonPosition.RIGHT_DOWN;
        else if (292.5 > angle && angle >= 247.5)
            return ButtonPosition.DOWN;
        else if (247.5 > angle && angle >= 202.5)
            return ButtonPosition.LEFT_DOWN;
        else if (202.5 > angle && angle >= 157.5)
            return ButtonPosition.LEFT;
        else if (157.5 > angle && angle >= 112.5)
            return ButtonPosition.LEFT_UP;
        else
            return ButtonPosition.UP;
    }

    private ButtonPosition getDetailedPosition(double diffX, double diffY) {
        return getDetailedPosition(getAngle(diffX, diffY));
    }

    private ButtonPosition getDetailedPosition(double angle) {
        if (11.25 > angle || angle >= 348.75)
            return ButtonPosition.RIGHT;
        else if (326.25 > angle && angle >= 303.75)
            return ButtonPosition.RIGHT_DOWN;
        else if (281.25 > angle && angle >= 258.75)
            return ButtonPosition.DOWN;
        else if (236.25 > angle && angle >= 213.75)
            return ButtonPosition.LEFT_DOWN;
        else if (191.25 > angle && angle >= 168.75)
            return ButtonPosition.LEFT;
        else if (146.25 > angle && angle >= 123.25)
            return ButtonPosition.LEFT_UP;
        else if (101.25 > angle && angle >= 78.75)
            return ButtonPosition.UP;
        else if (56.25 > angle && angle >= 33.75)
            return ButtonPosition.RIGHT_UP;
        else
            return null;
    }

    private void soundAndVibrate() {
        VibrateUtil.vibtate(getContext(), vibrationStrength);
        SoundUtil.playSound(getContext(), eventVolume);
    }

    private void positionChanged(int action, ButtonPosition position) {
        if (onPositionChangedListener != null)
            onPositionChangedListener.onPositionChanged(action, position);
    }

    /**
     * Getter & Setter
     */
    public int getMovementVertical() {
        return movementVertical;
    }

    public void setMovementVertical(int movementVertical) {
        this.movementVertical = movementVertical;
    }

    public int getMovementHorizontal() {
        return movementHorizontal;
    }

    public void setMovementHorizontal(int movementHorizontal) {
        this.movementHorizontal = movementHorizontal;
    }

    public ButtonMovement getButtonMovement() {
        return buttonMovement;
    }

    public void setButtonMovement(ButtonMovement buttonMovement) {
        this.buttonMovement = buttonMovement;
    }

    public int getOffSetInner() {
        return offSetInner;
    }

    public void setOffSetInner(int offSetInner) {
        this.offSetInner = offSetInner;
    }

    public int getOffSetOuter() {
        return offSetOuter;
    }

    public void setOffSetOuter(int offSetOuter) {
        this.offSetOuter = offSetOuter;
    }

    public VibrationStrength getVibrationStrength() {
        return vibrationStrength;
    }

    public void setVibrationStrength(VibrationStrength vibrationStrength) {
        this.vibrationStrength = vibrationStrength;
    }

    public int getEventVolume() {
        return eventVolume;
    }

    public void setEventVolume(int eventVolume) {
        this.eventVolume = eventVolume;
    }

    public ButtonPosition getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(ButtonPosition currentPosition) {
        this.currentPosition = currentPosition;
    }

    public OnPositionChangedListener getOnPositionChangedListener() {
        return onPositionChangedListener;
    }

    public void setOnPositionChangedListener(OnPositionChangedListener onPositionChangedListener) {
        this.onPositionChangedListener = onPositionChangedListener;
    }
}
