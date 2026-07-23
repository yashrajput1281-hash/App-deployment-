package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val CscCyan = Color(0xFF00A8FF)
val CscRedSwoosh = Color(0xFFFF233B)
val CscDarkBg = Color(0xFF0D111A)

@Composable
fun CscLogo3D(
    modifier: Modifier = Modifier,
    size: Dp = 84.dp
) {
    val infiniteTransition = rememberInfiniteTransition(label = "SwooshGlow")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.75f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alphaPulse"
    )

    Box(
        modifier = modifier
            .size(size)
            .shadow(14.dp, CircleShape, spotColor = CscCyan, ambientColor = CscRedSwoosh)
            .clip(CircleShape)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1E293B),
                        Color(0xFF0F172A),
                        CscDarkBg
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Drawing 3D Red Orbit Swoosh Ring and Cyan Accent with specular gradient paths
        Canvas(modifier = Modifier.size(size)) {
            val w = this.size.width
            val h = this.size.height

            // Outer 3D Red Swoosh Glow Shadow
            val swooshPath = Path().apply {
                moveTo(w * 0.85f, h * 0.30f)
                cubicTo(w * 0.52f, h * 0.16f, w * 0.15f, h * 0.28f, w * 0.15f, h * 0.52f)
                cubicTo(w * 0.15f, h * 0.74f, w * 0.52f, h * 0.88f, w * 0.86f, h * 0.74f)
            }

            drawPath(
                path = swooshPath,
                brush = Brush.horizontalGradient(
                    colors = listOf(CscRedSwoosh.copy(alpha = pulseAlpha), Color(0xFFFF5252), CscRedSwoosh)
                ),
                style = Stroke(width = w * 0.085f)
            )

            // Inner Cyan Metallic Orbital Arc
            val cyanArc = Path().apply {
                moveTo(w * 0.20f, h * 0.42f)
                cubicTo(w * 0.35f, h * 0.22f, w * 0.78f, h * 0.22f, w * 0.90f, h * 0.36f)
            }

            drawPath(
                path = cyanArc,
                brush = Brush.linearGradient(
                    colors = listOf(CscCyan, Color(0xFF80D8FF), CscCyan),
                    start = Offset(0f, 0f),
                    end = Offset(w, h)
                ),
                style = Stroke(width = w * 0.045f)
            )
        }

        // 3D Embossed Center CSC Letters
        Box(contentAlignment = Alignment.Center) {
            // Dark 3D Depth Shadow offset text
            Text(
                text = "CSC",
                fontSize = (size.value * 0.33f).sp,
                fontWeight = FontWeight.Black,
                color = Color.Black.copy(alpha = 0.8f),
                letterSpacing = 1.2.sp,
                modifier = Modifier.align(Alignment.Center)
            )
            // Primary Cyan 3D Text
            Text(
                text = "CSC",
                fontSize = (size.value * 0.33f).sp,
                fontWeight = FontWeight.Black,
                color = CscCyan,
                letterSpacing = 1.2.sp
            )
        }
    }
}
