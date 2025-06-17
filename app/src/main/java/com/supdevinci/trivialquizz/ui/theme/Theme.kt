package com.supdevinci.trivialquizz.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Light theme color scheme
private val LightColorScheme = lightColorScheme(
    primary = Primary,
    primaryContainer = PrimaryVariant,
    secondary = Secondary,
    secondaryContainer = SecondaryVariant,
    background = Background,
    surface = Surface,
    error = Error,
    onPrimary = OnPrimary,
    onSecondary = OnSecondary,
    onBackground = OnBackground,
    onSurface = OnSurface,
    onError = OnError
)

// Dark theme color scheme
private val DarkColorScheme = darkColorScheme(
    primary = PrimaryDark,
    primaryContainer = PrimaryVariantDark,
    secondary = SecondaryDark,
    secondaryContainer = SecondaryVariantDark,
    background = BackgroundDark,
    surface = SurfaceDark,
    error = ErrorDark,
    onPrimary = OnPrimaryDark,
    onSecondary = OnSecondaryDark,
    onBackground = OnBackgroundDark,
    onSurface = OnSurfaceDark,
    onError = OnErrorDark
)

// Custom quiz colors for use in the app
data class QuizColors(
    val gradientStart: Color,
    val gradientEnd: Color,
    val cardBackground: Color,
    val progressTrack: Color,
    val progressIndicator: Color,
    val easyColor: Color,
    val mediumColor: Color,
    val hardColor: Color,
    val optionDefault: Color,
    val optionSelected: Color,
    val optionCorrect: Color,
    val optionIncorrect: Color,
    val optionBorderDefault: Color,
    val optionBorderSelected: Color,
    val optionBorderCorrect: Color,
    val optionBorderIncorrect: Color
)

// Light theme quiz colors
private val LightQuizColors = QuizColors(
    gradientStart = QuizGradientStart,
    gradientEnd = QuizGradientEnd,
    cardBackground = QuizCardBackground,
    progressTrack = QuizProgressTrack,
    progressIndicator = QuizProgressIndicator,
    easyColor = EasyColor,
    mediumColor = MediumColor,
    hardColor = HardColor,
    optionDefault = OptionDefault,
    optionSelected = OptionSelected,
    optionCorrect = OptionCorrect,
    optionIncorrect = OptionIncorrect,
    optionBorderDefault = OptionBorderDefault,
    optionBorderSelected = OptionBorderSelected,
    optionBorderCorrect = OptionBorderCorrect,
    optionBorderIncorrect = OptionBorderIncorrect
)

// Dark theme quiz colors (could be customized further)
private val DarkQuizColors = QuizColors(
    gradientStart = QuizGradientStart,
    gradientEnd = QuizGradientEnd,
    cardBackground = SurfaceDark,
    progressTrack = Color.DarkGray,
    progressIndicator = PrimaryDark,
    easyColor = EasyColor,
    mediumColor = MediumColor,
    hardColor = HardColor,
    optionDefault = Color.DarkGray,
    optionSelected = Color(0xFF3F2C60),
    optionCorrect = Color(0xFF1B3D1C),
    optionIncorrect = Color(0xFF3F1D1D),
    optionBorderDefault = Color.Gray,
    optionBorderSelected = PrimaryDark,
    optionBorderCorrect = EasyColor,
    optionBorderIncorrect = HardColor
)

// Local composition for quiz colors
val LocalQuizColors = staticCompositionLocalOf { LightQuizColors }

@Composable
fun TrivialQuizzTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Set to false by default to use our custom colors
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val quizColors = if (darkTheme) DarkQuizColors else LightQuizColors

    // Update status bar color
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    CompositionLocalProvider(LocalQuizColors provides quizColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}

// Extension function to easily access quiz colors
object QuizTheme {
    val colors: QuizColors
        @Composable
        get() = LocalQuizColors.current
}
