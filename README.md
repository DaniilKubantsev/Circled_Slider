# `CircledRangeSlider` for Jetpack Compose

## Description
Composable with `RangedSlider` functionality but in cercled form. The size of the range is determined as the segment on the circle, defined by two thumbs.

![Screen_recording_20251209_182701 (online-video-cutter com)](https://github.com/user-attachments/assets/3b29ae3f-f008-47d2-abf6-af0b99690a35)


## Signature
```kotlin
@Composable
fun CircledRangeSlider(
    startAngle: Float,
    finishAngle: Float,
    onStartAngleChange: (Float) -> Unit,
    onFinishAngleChange: (Float) -> Unit,
    minDegree: Float = 0f,
    maxDegree: Float = 360f,
    minimalGap: Float = 10f,
    enabled: Boolean = true,
    color: CircledSliderColors,
    thumbRadius: Float,
    activeTrackThickness: Float,
    inactiveTrackThickness: Float,
    paddingValues: PaddingValues,
    modifier: Modifier
)
```
## Arguments
`startAngle`: Float
