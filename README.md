# Moving Button

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-MovingButton-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/1537)
[![Platform](https://img.shields.io/badge/platform-android-green.svg)](http://developer.android.com/index.html)
[![API](https://img.shields.io/badge/API-3%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=3)
[![License](https://img.shields.io/badge/License-MIT-blue.svg?style=flat)](http://opensource.org/licenses/MIT)
[![Join the chat at https://gitter.im/TheFinestArtist/MovingButton](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/TheFinestArtist/MovingButton?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

Android button which moves in eight direction.

## Preview

![Preview](https://github.com/thefinestartist/movingbutton/blob/master/art/preview.gif)

## Sample Demo

You can download demo movie file here : [demo.mov](https://github.com/thefinestartist/movingbutton/raw/master/art/demo.mov)

It's also on Youtube:

<a href="http://www.youtube.com/watch?v=myheMkavjzk">
  <img alt="Youtube"
       src="https://github.com/thefinestartist/movingbutton/blob/master/art/youtube.png" />
</a>

## Sample Project

You can download the latest sample APK from this repo here: [sample-release.apk](https://github.com/thefinestartist/movingbutton/raw/master/sample/sample-release.apk)

It's also on Google Play:

<a href="https://play.google.com/store/apps/details?id=com.thefinestartist.movingbutton.sample">
  <img alt="Get it on Google Play"
       src="https://developer.android.com/images/brand/en_generic_rgb_wo_60.png" />
</a>

Having the sample project installed is a good way to be notified of new releases.

## Gradle Dependency (jcenter)

Easily reference the library in your Android projects using this dependency in your module's `build.gradle` file:

```Gradle
dependencies {
    compile 'com.thefinestartist:movingbutton:1.0.0'
}
```

## Requirements

It supports Android API 3+.

It uses [nineoldandroid](http://nineoldandroids.com/) for view animation.

## Attrubutes

```xml
<!--Button Move Direction (Default : all)-->
<attr name="mb_move_direction">
    <enum name="all" value="0" />
    <enum name="horizontal_vertical" value="1" />
    <enum name="horizontal" value="2" />
    <enum name="vertical" value="3" />
    <enum name="still" value="4" />
</attr>

<!--Button Movement (Default : 0dp)-->
<!--Since DEFAULT value is ZERO, set up your button movement larger than zero.-->
<!--You can set up the movement as minus value-->
<attr name="mb_movement" format="dimension" />
<attr name="mb_movement_left" format="dimension" />
<attr name="mb_movement_right" format="dimension" />
<attr name="mb_movement_top" format="dimension" />
<attr name="mb_movement_bottom" format="dimension" />

<!--Button Event Offset (Default : 16dp for inner & 23dp for outer)-->
<!--Helps to calculate touch event from button-->
<attr name="mb_offset_inner" format="dimension" />
<attr name="mb_offset_outer" format="dimension" />

<!--Vibrate on button movement (Default : 0)-->
<!--android.permission.VIBRATE Permission required-->
<!--if you set the duration as 0, no Permission required!-->
<attr name="mb_event_volume" format="integer" />

<!--Play sound on button movement (Default : 0)-->
<attr name="mb_event_volume" format="integer" />
```

## Layout Examples

```xml
<com.thefinestartist.movingbutton.MovingButton
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/moving_button"
    android:layout_width="100dp"
    android:layout_height="40dp"
    app:mb_move_direction="vertical"
    app:mb_event_volume="0"
    app:mb_vibration_duration="20"
    app:mb_movement="10dp" />

<com.thefinestartist.movingbutton.MovingButton
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/moving_button"
    android:layout_width="100dp"
    android:layout_height="40dp"
    app:mb_move_direction="all"
    app:mb_event_volume="50"
    app:mb_vibration_duration="20"
    app:mb_movementLeft="10dp"
    app:mb_movementRight="15dp"
    app:mb_movementTop="5dp"
    app:mb_movementBottom="20dp"
    app:mb_offset_inner="16dp"
    app:mb_offset_outer="23dp" />
```

## Setter & Getter

```java
// Move Direction
movingButton.getMoveDirection();
movingButton.setMoveDirection(MoveDirection.ALL);

// Movement (in Pixel dimension)
movingButton.getMovement();
movingButton.setMovement(10);
movingButton.getMovementLeft();
movingButton.setMovementLeft(10);
movingButton.getMovementRight();
movingButton.setMovementRight(10);
movingButton.getMovementTop();
movingButton.setMovementTop(10);
movingButton.getMovementBottom();
movingButton.setMovementBottom(10);

// Offset (in Pixel dimension)
movingButton.getOffSetInner();
movingButton.setOffSetInner(10);
movingButton.getOffSetOuter();
movingButton.setOffSetOuter(10);

// Vibration
movingButton.getVibrationDuration();
movingButton.setVibrationDuration(20);

// Volume
movingButton.getEventVolume();
movingButton.setEventVolume(50);

// Current Position
movingButton.getCurrentPosition();
```

## Listener

```java
public interface OnPositionChangedListener {
    // returns MotionEvent action and changed button position
    void onPositionChanged(int action, ButtonPosition position);
}

movingButton.setOnPositionChangedListener(new MovingButton.OnPositionChangedListener() {
    @Override
    public void onPositionChanged(int action, ButtonPosition position) {
        //your code here
    }
});
```

## License

```
The MIT License (MIT)

Copyright (c) 2015 TheFinestArtist

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
```
