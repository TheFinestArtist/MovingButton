package com.thefinestartist.movingbutton;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.Button;

import com.nineoldandroids.view.ViewHelper;
import com.thefinestartist.movingbutton.enums.ButtonPosition;
import com.thefinestartist.movingbutton.enums.MoveDirection;
import com.thefinestartist.movingbutton.utils.AudioUtil;
import com.thefinestartist.movingbutton.utils.VibrateUtil;

/**
 * Created by TheFinestArtist on 2/6/15.
 */
public class MovingButton extends Button {

    public interface OnPositionChangedListener {
        void onPositionChanged(int action, ButtonPosition position);
    }

    MoveDirection moveDirection;

    int movementLeft;
    int movementRight;
    int movementTop;
    int movementBottom;

    int offSetInner;
    int offSetOuter;

    int vibrationDuration;

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

        moveDirection = MoveDirection.values()[attr.getInt(R.styleable.MovingButton_mb_move_direction, 0)];

        movementLeft = attr.getDimensionPixelSize(R.styleable.MovingButton_mb_movement_left, 0);
        movementRight = attr.getDimensionPixelSize(R.styleable.MovingButton_mb_movement_right, 0);
        movementTop = attr.getDimensionPixelSize(R.styleable.MovingButton_mb_movement_top, 0);
        movementBottom = attr.getDimensionPixelSize(R.styleable.MovingButton_mb_movement_bottom, 0);

        int movement = attr.getDimensionPixelSize(R.styleable.MovingButton_mb_movement, 0);
        if (movement != 0)
            movementLeft = movementRight = movementTop = movementBottom = movement;

        offSetInner = attr.getDimensionPixelSize(R.styleable.MovingButton_mb_offset_inner,
                getResources().getDimensionPixelSize(R.dimen.default_offset_inner));
        offSetOuter = attr.getDimensionPixelSize(R.styleable.MovingButton_mb_offset_outer,
                getResources().getDimensionPixelSize(R.dimen.default_offset_outer));

        vibrationDuration = attr.getInt(R.styleable.MovingButton_mb_vibration_duration, 0);
        eventVolume = attr.getInt(R.styleable.MovingButton_mb_event_volume, 0);

        attr.recycle();
    }

    /**
     * Touch Event
     */
    ButtonPosition currentPosition = ButtonPosition.ORIGIN;

    private float halfWidth;
    private float halfHeight;

    private float centerX;
    private float centerY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                requestTouchEvent();
                positionChanged(MotionEvent.ACTION_DOWN, ButtonPosition.ORIGIN);
                soundAndVibrate();
                halfWidth = (float) this.getWidth() / 2.0f;
                halfHeight = (float) this.getHeight() / 2.0f;
                currentPosition = ButtonPosition.ORIGIN;
                break;
            case MotionEvent.ACTION_MOVE:
                recalculateCenter();
                float diffX = event.getX() - centerX;
                float diffY = centerY - event.getY();

                switch (moveDirection) {
                    case ALL: {
                        double length = Math.sqrt(Math.pow(diffX, 2d) + Math.pow(diffY, 2d));
                        if (length > offSetOuter)
                            if (currentPosition == ButtonPosition.ORIGIN)
                                moveView(this, getPositionForAll(diffX, diffY));
                            else
                                moveView(this, getDetailedPositionForAll(diffX, diffY));
                        else if (length < offSetInner)
                            moveView(this, ButtonPosition.ORIGIN);
                        break;
                    }
                    case HORIZONTAL_VERTICAL: {
                        double length = Math.sqrt(Math.pow(diffX, 2d) + Math.pow(diffY, 2d));
                        if (length > offSetOuter)
                            if (currentPosition == ButtonPosition.ORIGIN)
                                moveView(this, getPositionForHV(diffX, diffY));
                            else
                                moveView(this, getDetailedPositionForHV(diffX, diffY));
                        else if (length < offSetInner)
                            moveView(this, ButtonPosition.ORIGIN);
                        break;
                    }
                    case VERTICAL:
                        if (diffY > offSetOuter)
                            moveView(this, ButtonPosition.UP);
                        else if (diffY < -offSetOuter)
                            moveView(this, ButtonPosition.DOWN);
                        else if (Math.abs(diffY) < offSetInner)
                            moveView(this, ButtonPosition.ORIGIN);
                        break;
                    case HORIZONTAL:
                        if (diffX > offSetOuter)
                            moveView(this, ButtonPosition.RIGHT);
                        else if (diffX < -offSetOuter)
                            moveView(this, ButtonPosition.LEFT);
                        else if (Math.abs(diffX) < offSetInner)
                            moveView(this, ButtonPosition.ORIGIN);
                        break;
                    case STILL:
                        break;
                }

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                moveView(this, ButtonPosition.ORIGIN);
                positionChanged(MotionEvent.ACTION_UP, ButtonPosition.ORIGIN);
                break;
        }
        return super.onTouchEvent(event);
    }

    private void recalculateCenter() {
        switch (currentPosition) {
            case ORIGIN:
                centerX = halfWidth;
                centerY = halfHeight;
                break;
            case RIGHT:
                centerX = halfWidth - movementRight;
                centerY = halfHeight;
                break;
            case RIGHT_DOWN:
                centerX = halfWidth - movementRight;
                centerY = halfHeight - movementBottom;
                break;
            case DOWN:
                centerX = halfWidth;
                centerY = halfHeight - movementBottom;
                break;
            case LEFT_DOWN:
                centerX = halfWidth + movementLeft;
                centerY = halfHeight - movementBottom;
                break;
            case LEFT:
                centerX = halfWidth + movementLeft;
                centerY = halfHeight;
                break;
            case LEFT_UP:
                centerX = halfWidth + movementLeft;
                centerY = halfHeight + movementTop;
                break;
            case UP:
                centerX = halfWidth;
                centerY = halfHeight + movementTop;
                break;
            case RIGHT_UP:
                centerX = halfWidth - movementRight;
                centerY = halfHeight + movementTop;
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
            case ORIGIN:
                goalViewX = 0;
                goalViewY = 0;
                break;
            case LEFT:
                goalViewX = -movementLeft;
                goalViewY = 0;
                break;
            case RIGHT:
                goalViewX = movementRight;
                goalViewY = 0;
                break;
            case UP:
                goalViewX = 0;
                goalViewY = -movementTop;
                break;
            case DOWN:
                goalViewX = 0;
                goalViewY = movementBottom;
                break;
            case LEFT_UP:
                goalViewX = -movementLeft;
                goalViewY = -movementTop;
                break;
            case LEFT_DOWN:
                goalViewX = -movementLeft;
                goalViewY = movementBottom;
                break;
            case RIGHT_UP:
                goalViewX = +movementRight;
                goalViewY = -movementTop;
                break;
            case RIGHT_DOWN:
                goalViewX = movementRight;
                goalViewY = movementBottom;
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

    /**
     * ButtonMovement.ALL
     */
    private ButtonPosition getPositionForAll(double diffX, double diffY) {
        double angle = getAngle(diffX, diffY);

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

    private ButtonPosition getDetailedPositionForAll(double diffX, double diffY) {
        double angle = getAngle(diffX, diffY);

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

    /**
     * ButtonMovement.HORIZONTAL_VERTICAL
     */
    private ButtonPosition getPositionForHV(double diffX, double diffY) {
        double angle = getAngle(diffX, diffY);

        if (45 > angle || angle >= 315)
            return ButtonPosition.RIGHT;
        else if (315 > angle && angle >= 225)
            return ButtonPosition.DOWN;
        else if (225 > angle && angle >= 135)
            return ButtonPosition.LEFT;
        else
            return ButtonPosition.UP;
    }

    private ButtonPosition getDetailedPositionForHV(double diffX, double diffY) {
        double angle = getAngle(diffX, diffY);

        if (22.5 > angle || angle >= 342.5)
            return ButtonPosition.RIGHT;
        else if (292.5 > angle && angle >= 247.5)
            return ButtonPosition.DOWN;
        else if (202.5 > angle && angle >= 157.5)
            return ButtonPosition.LEFT;
        else if (112.5 > angle && angle >= 67.5)
            return ButtonPosition.UP;
        else
            return null;
    }

    private void soundAndVibrate() {
        VibrateUtil.vibtate(getContext(), vibrationDuration);
        AudioUtil.playKeyClickSound(getContext(), eventVolume);
    }

    private void positionChanged(int action, ButtonPosition position) {
        if (onPositionChangedListener != null)
            onPositionChangedListener.onPositionChanged(action, position);
    }

    private void requestTouchEvent() {
        ViewParent parent = getParent();
        if (parent != null)
            parent.requestDisallowInterceptTouchEvent(true);
    }


    // Getter and Setter
    public int getMovementLeft() {
        return movementLeft;
    }

    public void setMovementLeft(int movementLeft) {
        this.movementLeft = movementLeft;
    }

    public int getMovementRight() {
        return movementRight;
    }

    public void setMovementRight(int movementRight) {
        this.movementRight = movementRight;
    }

    public int getMovementTop() {
        return movementTop;
    }

    public void setMovementTop(int movementTop) {
        this.movementTop = movementTop;
    }

    public int getMovementBottom() {
        return movementBottom;
    }

    public void setMovementBottom(int movementBottom) {
        this.movementBottom = movementBottom;
    }

    public MoveDirection getMoveDirection() {
        return moveDirection;
    }

    public void setMoveDirection(MoveDirection moveDirection) {
        this.moveDirection = moveDirection;
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

    public int getVibrationDuration() {
        return vibrationDuration;
    }

    public void setVibrationDuration(int vibrationDuration) {
        this.vibrationDuration = vibrationDuration;
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

    public OnPositionChangedListener getOnPositionChangedListener() {
        return onPositionChangedListener;
    }

    public void setOnPositionChangedListener(OnPositionChangedListener onPositionChangedListener) {
        this.onPositionChangedListener = onPositionChangedListener;
    }
}
