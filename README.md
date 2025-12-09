# `CircledRangeSlider` for Jetpack Compose

<div align="center">
  <img src="https://github.com/user-attachments/assets/3b29ae3f-f008-47d2-abf6-af0b99690a35" width="200">
</div>

## Description
Composable with `RangedSlider` functionality but in cercled form. The size of the range is determined as the segment on the circle(called `inactiveTrack`), defined by two thumbs. The segment is called the `activeTrack`. Track thickness and turning radius are fully adjustable as showed below:


<div align="center">
  <img height="200" width=auto alt="image" src="https://github.com/user-attachments/assets/a92c9a6e-450c-4f6b-9d92-46a3ce0baad5" />
  <br>
  <p>thumbRadius = 70, activeTrackThickness = 120, inactiveTrackThickness = 100</p>
</div>




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
    modifier: Modifier
)

@Immutable
class CircledSliderColors internal constructor(
    private val thumbColor: Color,
    private val activeTrackColor: Color,
    private val inactiveTrackColor: Color,
    private val disabledThumbColor: Color,
    private val disabledActiveTrackColor: Color,
    private val disabledInactiveTrackColor: Color,
)
```
## Arguments
`startAngle` - defines the value and position of the start thumb in degrees;\
`finishAngle` - defines the value and position of the finish thumb in degrees;
`onStartAngleChange` - start value change callback;\
`onFinishAngleChange` - finish value change callback;\
`minDegree` - lower bound of available degree range;\
`maxDegree` - upper bound of available degree range;\
`minimalGap` - minimal distance between thumbs in degrees;\
`enabled` - enable/disable flag;\
`color` - CircledSliderColors object;\
`thumbRadius` - defines the thumb radius in px;\
`activeTrackThickness` - defines the thickness of active track in px\
`inactiveTrackThickness` - defines the thickness of inactive track in px\
`modifier` - Jetpack Compose modifier;
