package com.supdevinci.trivialquizz.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.supdevinci.trivialquizz.model.Question
import com.supdevinci.trivialquizz.model.UserAnswer
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.ui.text.style.TextOverflow
import androidx.core.text.HtmlCompat
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import com.supdevinci.trivialquizz.R

@Composable
fun QuizScreen(
    questions: List<Question>,
    playerName: String,
    categoryName: String,
    difficultyDisplay: String,
    onQuizFinished: (Int, Int, List<UserAnswer>) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var currentQuestionIndex by remember { mutableStateOf(0) }
    var selectedAnswer by remember { mutableStateOf<String?>(null) }
    var userAnswers by remember { mutableStateOf<List<UserAnswer>>(emptyList()) }
    var score by remember { mutableStateOf(0) }
    var timeLeft by remember { mutableStateOf(30) }
    var isAnswerSubmitted by remember { mutableStateOf(false) }

    val currentQuestion = questions.getOrNull(currentQuestionIndex)
    val progress = animateFloatAsState(
        targetValue = (currentQuestionIndex + 1).toFloat() / questions.size.toFloat(),
        label = "Progress Animation"
    )

    // Fonction pour décoder le HTML
    fun decodeHtml(html: String): String {
        return HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
    }

    // Fonction pour obtenir la couleur selon la difficulté
    @Composable
    fun getDifficultyColor(difficulty: String): Color {
        return when (difficulty.lowercase()) {
            "easy" -> Color(0xFF81C784)
            "medium" -> Color(0xFFFFB300)
            "hard" -> Color(0xFFE57373)
            else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        }
    }

    // Fonction pour gérer la sélection de réponse
    val handleAnswer = fun(answer: String?) {
        if (isAnswerSubmitted) return
        isAnswerSubmitted = true
        selectedAnswer = answer

        val isCorrect = answer == currentQuestion?.correctAnswer
        if (isCorrect) score++

        val userAnswer = UserAnswer(
            question = currentQuestion?.question ?: "",
            userAnswer = answer ?: "No answer",
            correctAnswer = currentQuestion?.correctAnswer ?: "",
            isCorrect = isCorrect
        )
        userAnswers = userAnswers + userAnswer

        coroutineScope.launch {
            delay(1500)
            if (currentQuestionIndex < questions.size - 1) {
                currentQuestionIndex++
                selectedAnswer = null
                isAnswerSubmitted = false
            } else {
                onQuizFinished(score, questions.size, userAnswers)
            }
        }
    }

    // Effet du timer
    LaunchedEffect(currentQuestionIndex, isAnswerSubmitted) {
        timeLeft = 30
        if (!isAnswerSubmitted) {
            while (timeLeft > 0) {
                delay(1000)
                timeLeft--
            }
            if (timeLeft <= 0 && !isAnswerSubmitted) {
                handleAnswer(null)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                        MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)
                    )
                )
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header avec progression et timer
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Question ${currentQuestionIndex + 1}/${questions.size}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold
                )

                Box(
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f),
                            RoundedCornerShape(16.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_timer),
                            contentDescription = "Timer",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${timeLeft}s",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Barre de progression
            LinearProgressIndicator(
                progress = { progress.value },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = Color(0xFFDF399E),
                trackColor = Color.White.copy(alpha = 0.3f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Carte question
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // En-tête question
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = currentQuestion?.category ?: "",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )

                        val difficulty = currentQuestion?.difficulty ?: "medium"
                        Text(
                            text = difficulty.replaceFirstChar { it.uppercase() },
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = getDifficultyColor(difficulty)
                        )
                    }

                    // Texte de la question
                    Text(
                        text = decodeHtml(currentQuestion?.question ?: ""),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Options de réponse
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        val allAnswers = remember(currentQuestion) {
                            val answers = mutableListOf<String>()
                            currentQuestion?.let {
                                answers.addAll(it.incorrectAnswers)
                                answers.add(it.correctAnswer)
                                answers.shuffle()
                            }
                            answers
                        }

                        allAnswers.forEachIndexed { index, answer ->
                            val isSelected = selectedAnswer == answer
                            val isCorrectAnswer = currentQuestion?.correctAnswer == answer
                            val showCorrectness = isAnswerSubmitted

                            val backgroundColor = when {
                                showCorrectness && isCorrectAnswer -> Color(0xFFE7F6E7)
                                showCorrectness && isSelected && !isCorrectAnswer -> Color(0xFFFEEAEA)
                                isSelected -> Color(0xFFF0E6FF)
                                else -> Color.White
                            }

                            val borderColor = when {
                                showCorrectness && isCorrectAnswer -> Color(0xFF4CAF50)
                                showCorrectness && isSelected && !isCorrectAnswer -> Color(0xFFF44336)
                                isSelected -> Color(0xFF9146FF)
                                else -> Color(0xFFE0E0E0)
                            }

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp)
                                    .border(
                                        width = 2.dp,
                                        color = borderColor,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .background(
                                        color = backgroundColor,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .clickable(enabled = !isAnswerSubmitted) {
                                        handleAnswer(answer)
                                    }
                                    .padding(16.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Lettre de l'option (A, B, C, D)
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .background(
                                                color = if (isSelected) Color(0xFF9146FF) else Color(0xFFF0F0F0),
                                                shape = CircleShape
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = ('A' + index).toString(),
                                            color = if (isSelected) Color.White else Color.Black,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Text(
                                        text = decodeHtml(answer),
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Bouton suivant
                    Button(
                        onClick = {
                            if (isAnswerSubmitted) {
                                if (currentQuestionIndex < questions.size - 1) {
                                    currentQuestionIndex++
                                    selectedAnswer = null
                                    isAnswerSubmitted = false
                                } else {
                                    onQuizFinished(score, questions.size, userAnswers)
                                }
                            } else {
                                handleAnswer(selectedAnswer)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF9146FF)
                        )
                    ) {
                        Text(
                            text = if (isAnswerSubmitted) {
                                if (currentQuestionIndex < questions.size - 1) "Question suivante" else "Terminer le quiz"
                            } else "Valider la réponse",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}