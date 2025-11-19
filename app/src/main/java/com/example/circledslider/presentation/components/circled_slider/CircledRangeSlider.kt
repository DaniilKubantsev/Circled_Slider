package com.example.circledslider.presentation.components.circled_slider

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.center
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toOffset
import com.example.circledslider.ui.theme.Parchment
import kotlin.math.max
import kotlin.math.min


@Composable
fun CircledRangeSlider(
    onStartValueChange: (Float) -> Unit,
    onFinishValueChange: (Float) -> Unit,
    enabled: Boolean = true,
    color: CircledSliderColors,
    thumbRadius: Float,
    activeTrackThickness: Float,
    inactiveTrackThickness: Float,
    paddingValues: PaddingValues,
    modifier: Modifier
) {
    val activeTrackColor: Color = Color.Red
    var size by remember { mutableStateOf(IntSize.Zero) }
    var radius by remember { mutableFloatStateOf(100f) }

    var startAngle by remember { mutableFloatStateOf(30f) }
    var finishAngle by remember { mutableFloatStateOf(120f) }
    
    val padding = PaddingValues((max(activeTrackThickness, inactiveTrackThickness)/4).dp)

    radius = min(size.height, size.width).toFloat()


    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .aspectRatio(1f)
            .rotate(-90f)
            .onGloballyPositioned {
                size = it.size
            }
            .then(modifier)
            .padding(paddingValues=padding)
    ) {
        InactiveTrack(
            color = color.inactiveTrackColor(),
            thickness = inactiveTrackThickness,
        )

        ActiveTrack(
            startAngle = startAngle,
            finishAngle = finishAngle,
            color = color.activeTrackColor(),
            thickness = activeTrackThickness,
        )



    }

}


@Composable
fun ActiveTrack(
    startAngle: Float,
    finishAngle: Float,
    color: Color,
    thickness: Float,
) {
    Canvas(
        modifier = Modifier
            .aspectRatio(1f)
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
            ),
            alpha = 0.7f
        )
    }
}


@Composable
fun InactiveTrack(
    color: Color,
    thickness: Float,
) {
    Canvas(
        modifier = Modifier
            .aspectRatio(1f)
    ) {
        drawCircle(
            color = color,
            radius = (this.size.minDimension) / 2,
            center = center,
            style = Stroke(width = thickness)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun CircledRangeSliderPreview(){

    val thumbRadius = 10f
    val activeTrackThickness = 100f
    val inactiveTrackThickness = 50f

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(color = Parchment)
    ) {
        CircledRangeSlider(
            onStartValueChange = {},
            onFinishValueChange = {},
            color = CircledRangeSliderDefaults.circledSliderColors(
                inactiveTrackColor = Color.Red,
                activeTrackColor = Color.Green
            ),
            paddingValues = PaddingValues(),
            thumbRadius = thumbRadius,
            activeTrackThickness = activeTrackThickness,
            inactiveTrackThickness = inactiveTrackThickness,
            modifier = Modifier
                //.background(color = Color.Blue)
                .fillMaxSize()
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


    @Composable
    fun circledSliderThickness(
        thumbRadius: Float,
        activeTrackThickness: Float,
        inactiveTrackThickness: Float,
        disabledThumbRadius: Float,
        disabledActiveTrackThickness: Float,
        disabledInactiveTrackThickness: Float,
    ): CircledSliderThickness = CircledSliderThickness(
        thumbRadius = thumbRadius,
        activeTrackThickness = activeTrackThickness,
        inactiveTrackThickness = inactiveTrackThickness,
        disabledThumbRadius = disabledThumbRadius,
        disabledActiveTrackThickness = disabledActiveTrackThickness,
        disabledInactiveTrackThickness = disabledInactiveTrackThickness,
    )
}


@Immutable
class CircledSliderThickness internal constructor(
    private val thumbRadius: Float,
    private val activeTrackThickness: Float,
    private val inactiveTrackThickness: Float,
    private val disabledThumbRadius: Float,
    private val disabledActiveTrackThickness: Float,
    private val disabledInactiveTrackThickness: Float,
) {
    @Composable
    fun thumbRadius(): Float = thumbRadius

    @Composable
    fun activeTrackThickness(): Float = activeTrackThickness

    @Composable
    fun inactiveTrackThickness(): Float = inactiveTrackThickness

    @Composable
    fun disabledThumbRadius(): Float = disabledThumbRadius

    @Composable
    fun disabledActiveTrackThickness(): Float = disabledActiveTrackThickness

    @Composable
    fun disabledInactiveTrackThickness(): Float = disabledInactiveTrackThickness
}


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
    fun thumbColor(): Color {
        return thumbColor
    }

    @Composable
    fun activeTrackColor(): Color {
        return activeTrackColor
    }

    @Composable
    fun inactiveTrackColor(): Color {
        return inactiveTrackColor
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