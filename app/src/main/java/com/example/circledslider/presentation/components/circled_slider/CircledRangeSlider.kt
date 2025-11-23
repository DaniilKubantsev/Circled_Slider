package com.example.circledslider.presentation.components.circled_slider

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.center
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toOffset
import com.example.circledslider.ui.theme.DarkOliveGreen
import com.example.circledslider.ui.theme.Olivine
import com.example.circledslider.ui.theme.Parchment
import com.example.circledslider.ui.theme.PrimaryColor
import com.example.circledslider.ui.theme.SecondaryColor
import com.example.circledslider.ui.theme.SurfaceColor
import com.example.circledslider.ui.theme.Umber
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin


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
) {
    val density = LocalDensity.current

    // Component diameter in px
    var diameter by remember { mutableFloatStateOf(0f) }

    var startValue by remember { mutableFloatStateOf(startAngle) }
    var finishValue by remember { mutableFloatStateOf(finishAngle) }

    var isHeightBigger by remember { mutableStateOf(false) }

    val thumbSize = with(density) {(2 * thumbRadius).toDp()}
    
    var thumbColor by remember { mutableStateOf(Color.Transparent) }
    var activeTrackColor by remember { mutableStateOf(Color.Transparent) }
    var inactiveTrackColor by remember { mutableStateOf(Color.Transparent) }

    thumbColor = color.thumbColor(enabled)
    activeTrackColor = color.activeTrackColor(enabled)
    inactiveTrackColor = color.inactiveTrackColor(enabled)

    val mx = with(density) { (max(activeTrackThickness, inactiveTrackThickness)/2).toDp() }
    val padding = PaddingValues(mx)


    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier

            .rotate(-90f)
            .onGloballyPositioned {
                isHeightBigger = it.size.width > it.size.height
            }
            .then(modifier)
            .aspectRatio(1f)
            .padding(paddingValues = padding)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
        ) {
            diameter = this.size.minDimension
        }

        InactiveTrack(
            color = inactiveTrackColor,
            thickness = inactiveTrackThickness,
            isHeightBigger = isHeightBigger
        )

        ActiveTrack(
            startAngle = startValue,
            finishAngle = finishValue,
            color = activeTrackColor,
            thickness = activeTrackThickness,
            isHeightBigger = isHeightBigger
        )


        // START THUMB
        Canvas(
            modifier = Modifier
                .size(thumbSize)
                .offset {
                    IntOffset(
                        (diameter / 2 * cos(Math.toRadians(startValue.toDouble()))).toInt(),
                        (diameter / 2 * sin(Math.toRadians(startValue.toDouble()))).toInt()
                    )
                }
                .pointerInput(enabled) {
                    detectDragGestures(
                        onDrag = { _, dragAmount ->
                            
                            var currentPosition = getCurrentPosition(startValue, diameter / 2)
                            currentPosition += dragAmount
                            val newAngle = calculateNewAngle(currentPosition)
                            Log.w("START_THUMB__ON_DRUG", "cond: ${newAngle in minDegree..(finishValue - minimalGap)} | newAngle: ${newAngle}")
                            if (enabled && newAngle in minDegree..(finishValue - minimalGap)) {
                                startValue = newAngle
                            }

                            onStartAngleChange(startValue)
                        }
                    )
                }

        ) {
            drawCircle(
                color = thumbColor,
                radius = thumbRadius,
                center = center,
            )
        }


        // FINISH THUMB
        Canvas(
            modifier = Modifier
                .size(thumbSize)
                .offset {
                    IntOffset(
                        (diameter / 2 * cos(Math.toRadians(finishValue.toDouble()))).toInt(),
                        (diameter / 2 * sin(Math.toRadians(finishValue.toDouble()))).toInt()
                    )
                }
                .pointerInput(enabled) {
                    detectDragGestures(
                        onDrag = { _, dragAmount ->

                            var currentPosition = getCurrentPosition(finishValue, diameter / 2)
                            currentPosition += dragAmount
                            val newAngle = calculateNewAngle(currentPosition)

                            if (enabled && newAngle in (startValue + minimalGap)..maxDegree) {
                                finishValue = newAngle
                            }

                            onFinishAngleChange(finishValue)
                        }
                    )
                }

        ) {
            drawCircle(
                color = thumbColor,
                radius = thumbRadius,
                center = center,
            )
        }


    }

}


private fun getCurrentPosition(
    angle: Float,
    radius: Float
): Offset {
    return Offset(
        x = radius * cos(Math.toRadians(angle.toDouble())).toFloat(),
        y = radius * sin(Math.toRadians(angle.toDouble())).toFloat()
    )
}

private fun calculateNewAngle(
    currentPosition: Offset,
    center: Offset = Offset.Zero,
): Float {
    val (dx, dy) = currentPosition - center
    val radians = atan2(dy, dx).toDouble()
    var degrees = Math.toDegrees(radians).toFloat()
    if (degrees < 0) degrees += 360f
    return degrees
}


@Preview(showBackground = true)
@Composable
fun CircledRangeSliderPreview(){

    val thumbRadius = 50f
    val activeTrackThickness = 130f
    val inactiveTrackThickness = 100f

    var start by remember { mutableFloatStateOf(30f) }
    var finish by remember { mutableFloatStateOf(120f) }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(color = SurfaceColor)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
        ){
            Text("${start}")
            Text("${finish}")
        }

        CircledRangeSlider(
            startAngle = start,
            finishAngle = finish,
            onStartAngleChange = { start = it },
            onFinishAngleChange = { finish = it },
            color = CircledRangeSliderDefaults.circledSliderColors(
                inactiveTrackColor = SecondaryColor,
                activeTrackColor = PrimaryColor,
                thumbColor = SurfaceColor
            ),
//            enabled = false,
            paddingValues = PaddingValues(),
            thumbRadius = thumbRadius,
            activeTrackThickness = activeTrackThickness,
            inactiveTrackThickness = inactiveTrackThickness,
            modifier = Modifier

        )
    }
}



@Composable
fun ActiveTrack(
    startAngle: Float,
    finishAngle: Float,
    color: Color,
    thickness: Float,
    isHeightBigger: Boolean,
) {
    Canvas(
        modifier = Modifier
            .aspectRatio(1f, isHeightBigger)
            //.fillMaxSize()
    ) {
        drawArc(
            color = color,
            startAngle = startAngle,
            useCenter = false,
            sweepAngle = finishAngle - startAngle,
            size = Size(
                width = this.size.minDimension ,
                height = this.size.minDimension
            ),
            style = Stroke(
                width = thickness,
                cap = StrokeCap.Round
            )
        )
    }
}


@Composable
fun InactiveTrack(
    color: Color,
    thickness: Float,
    isHeightBigger: Boolean,
) {
    Canvas(
        modifier = Modifier
            .aspectRatio(1f, isHeightBigger)

    ) {
        drawCircle(
            color = color,
            radius = (this.size.minDimension) / 2,
            center = center,
            style = Stroke(width = thickness)
        )
    }
}





object CircledRangeSliderDefaults {
    @Composable
    fun circledSliderColors(
        thumbColor: Color = Color.White,
        activeTrackColor: Color = Color.DarkGray,
        inactiveTrackColor: Color = Color.LightGray,
        disabledThumbColor: Color = thumbColor.copy(alpha = 0.5f),
        disabledActiveTrackColor: Color = activeTrackColor.copy(alpha = 0.5f),
        disabledInactiveTrackColor: Color = inactiveTrackColor.copy(alpha = 0.5f),
    ) : CircledSliderColors = CircledSliderColors(
        thumbColor = thumbColor,
        activeTrackColor = activeTrackColor,
        inactiveTrackColor = inactiveTrackColor,
        disabledThumbColor = disabledThumbColor,
        disabledActiveTrackColor = disabledActiveTrackColor,
        disabledInactiveTrackColor = disabledInactiveTrackColor,
    )


//    @Composable
//    fun circledSliderThickness(
//        thumbRadius: Float,
//        activeTrackThickness: Float,
//        inactiveTrackThickness: Float,
//        disabledThumbRadius: Float,
//        disabledActiveTrackThickness: Float,
//        disabledInactiveTrackThickness: Float,
//    ): CircledSliderThickness = CircledSliderThickness(
//        thumbRadius = thumbRadius,
//        activeTrackThickness = activeTrackThickness,
//        inactiveTrackThickness = inactiveTrackThickness,
//        disabledThumbRadius = disabledThumbRadius,
//        disabledActiveTrackThickness = disabledActiveTrackThickness,
//        disabledInactiveTrackThickness = disabledInactiveTrackThickness,
//    )
}


//@Immutable
//class CircledSliderThickness internal constructor(
//    private val thumbRadius: Float,
//    private val activeTrackThickness: Float,
//    private val inactiveTrackThickness: Float,
//    private val disabledThumbRadius: Float,
//    private val disabledActiveTrackThickness: Float,
//    private val disabledInactiveTrackThickness: Float,
//) {
//    @Composable
//    fun thumbRadius(): Float = thumbRadius
//
//    @Composable
//    fun activeTrackThickness(): Float = activeTrackThickness
//
//    @Composable
//    fun inactiveTrackThickness(): Float = inactiveTrackThickness
//
//    @Composable
//    fun disabledThumbRadius(): Float = disabledThumbRadius
//
//    @Composable
//    fun disabledActiveTrackThickness(): Float = disabledActiveTrackThickness
//
//    @Composable
//    fun disabledInactiveTrackThickness(): Float = disabledInactiveTrackThickness
//}


@Immutable
class CircledSliderColors internal constructor(
    private val thumbColor: Color,
    private val activeTrackColor: Color,
    private val inactiveTrackColor: Color,
    private val disabledThumbColor: Color,
    private val disabledActiveTrackColor: Color,
    private val disabledInactiveTrackColor: Color,
) {
    @Composable
    fun thumbColor(enabled: Boolean): Color {
        return if (enabled) thumbColor else disabledThumbColor
    }

    @Composable
    fun activeTrackColor(enabled: Boolean): Color {
        return if (enabled) activeTrackColor else inactiveTrackColor
    }

    @Composable
    fun inactiveTrackColor(enabled: Boolean): Color {
        return if (enabled) inactiveTrackColor else disabledInactiveTrackColor
    }

    @Composable
    fun disabledInactiveTrackColor(): Color {
        return disabledInactiveTrackColor
    }

    @Composable
    fun disabledThumbColor(): Color {
        return disabledThumbColor
    }

    @Composable
    fun disabledActiveTrackColor(): Color {
        return disabledActiveTrackColor
    }
}