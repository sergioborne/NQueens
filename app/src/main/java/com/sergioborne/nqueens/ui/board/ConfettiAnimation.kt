package com.sergioborne.nqueens.ui.board

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay
import kotlin.random.Random

private data class Particle(
    val color: Color,
    val startX: Float,
    val startY: Float,
    val endX: Float,
    val endY: Float,
    val startTime: Long
)

@Composable
fun ConfettiAnimation(
    modifier: Modifier = Modifier,
    onAnimationFinished: () -> Unit
) {
    val particles = remember { mutableListOf<Particle>() }
    val animationTime = remember { Animatable(0f) }
    val colors = listOf(Color.Red, Color.Blue, Color.Green, Color.Yellow, Color.Magenta, Color.Cyan)

    LaunchedEffect(Unit) {
        val totalDuration = 5000L
        val explosionInterval = 500L
        var elapsed = 0L

        while (elapsed < totalDuration) {
            val startX = Random.nextFloat()
            val startY = Random.nextFloat()
            for (i in 0..100) {
                particles.add(
                    Particle(
                        color = colors.random(),
                        startX = startX,
                        startY = startY,
                        endX = Random.nextFloat() * 2 - 1,
                        endY = Random.nextFloat() * 2 - 1,
                        startTime = elapsed
                    )
                )
            }
            delay(explosionInterval)
            elapsed += explosionInterval
        }

        onAnimationFinished()
    }

    LaunchedEffect(Unit) {
        animationTime.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 5000, easing = LinearEasing)
        )
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        val currentTime = animationTime.value * 5000
        particles.forEach { particle ->
            val particleProgress = ((currentTime - particle.startTime) / 1500f).coerceIn(0f, 1f)
            if (particleProgress > 0 && particleProgress < 1) {
                val currentX = particle.startX * width + particle.endX * width / 2 * particleProgress
                val currentY = particle.startY * height + particle.endY * height / 2 * particleProgress
                drawCircle(
                    color = particle.color,
                    center = Offset(currentX, currentY),
                    radius = 10f,
                    alpha = (1f - particleProgress)
                )
            }
        }
    }
}

@Preview
@Composable
private fun FireworksAnimationPreview() {
    ConfettiAnimation {}
}