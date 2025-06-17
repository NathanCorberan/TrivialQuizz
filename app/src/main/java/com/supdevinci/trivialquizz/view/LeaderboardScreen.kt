package com.supdevinci.trivialquizz.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import com.supdevinci.trivialquizz.viewmodel.LeaderboardViewModel
import com.supdevinci.trivialquizz.model.ScoreEntry
import com.supdevinci.trivialquizz.ui.theme.TrivialQuizzTheme
import com.supdevinci.trivialquizz.R
import androidx.compose.ui.text.style.TextOverflow

class LeaderboardActivity : ComponentActivity() {

    private val leaderboardViewModel: LeaderboardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TrivialQuizzTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    val scores by leaderboardViewModel.scores.collectAsState()
                    LeaderboardScreen(
                        scores = scores,
                        onClearScores = { leaderboardViewModel.clearScores() },
                        onBackClick = { finish() },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun LeaderboardScreen(
    scores: List<ScoreEntry>,
    onClearScores: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8F6FF))
    ) {
        // Header stylé
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(elevation = 6.dp, shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color(0xFF9146FF), Color(0xFFDF399E))
                    ),
                    shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                )
                .padding(bottom = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(Modifier.height(8.dp))
                Image(
                    painter = painterResource(id = R.drawable.ic_trophy),
                    contentDescription = "Trophy Icon",
                    modifier = Modifier
                        .size(44.dp)
                        .background(Color(0x33FFFFFF), shape = RoundedCornerShape(50))
                        .padding(6.dp)
                )
                Text(
                    "Leaderboard",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    "Top Players",
                    color = Color.White.copy(alpha = 0.8f),
                    style = MaterialTheme.typography.labelMedium
                )
                Spacer(Modifier.height(8.dp))
            }
        }

        // Actions
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = onBackClick,
                shape = RoundedCornerShape(14.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = "Back Icon",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text("Retour", style = MaterialTheme.typography.labelSmall)
            }
            TextButton(
                onClick = onClearScores,
                colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFD7263D)),
                shape = RoundedCornerShape(14.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_delete),
                    contentDescription = "Delete Icon",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text("Vider scores", fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.labelSmall)
            }
        }

        // Tableau
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .shadow(elevation = 3.dp, shape = RoundedCornerShape(10.dp)),
            shape = RoundedCornerShape(10.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .background(Color(0xFFEAE6FC))
                        .padding(vertical = 7.dp, horizontal = 2.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Text("#", fontWeight = FontWeight.Bold, color = Color(0xFF5B58B6), style = MaterialTheme.typography.labelSmall, modifier = Modifier.weight(0.6f))
                    Text("Joueur", fontWeight = FontWeight.Bold, color = Color(0xFF5B58B6), style = MaterialTheme.typography.labelSmall, modifier = Modifier.weight(1.5f))
                    Text("Catégorie", fontWeight = FontWeight.Bold, color = Color(0xFF5B58B6), style = MaterialTheme.typography.labelSmall, modifier = Modifier.weight(1.6f))
                    Text("Diffic.", fontWeight = FontWeight.Bold, color = Color(0xFF5B58B6), style = MaterialTheme.typography.labelSmall, modifier = Modifier.weight(1f))
                    Text("Score", fontWeight = FontWeight.Bold, color = Color(0xFF5B58B6), style = MaterialTheme.typography.labelSmall, modifier = Modifier.weight(0.9f))
                    Text("%", fontWeight = FontWeight.Bold, color = Color(0xFF5B58B6), style = MaterialTheme.typography.labelSmall, modifier = Modifier.weight(0.6f))
                    Text("Date", fontWeight = FontWeight.Bold, color = Color(0xFF5B58B6), style = MaterialTheme.typography.labelSmall, modifier = Modifier.weight(1.5f))
                }
                if (scores.isEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 15.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text("Aucun score enregistré", color = Color(0xFFB1B1B6), style = MaterialTheme.typography.labelSmall)
                    }
                } else {
                    scores.forEach { score ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(if (score.rank == 1) Color(0xFFFFFBEA) else Color.White)
                                .padding(vertical = 6.dp, horizontal = 2.dp)
                                .border(
                                    width = if (score.rank == 1) 1.dp else 0.dp,
                                    color = if (score.rank == 1) Color(0xFFEAA800) else Color.Transparent,
                                    shape = RoundedCornerShape(8.dp)
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "#${score.rank}",
                                fontWeight = FontWeight.Bold,
                                color = if (score.rank == 1) Color(0xFFEAA800) else Color(0xFF5B58B6),
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.weight(0.6f)
                            )
                            Text(
                                score.player,
                                fontWeight = FontWeight.Medium,
                                style = MaterialTheme.typography.labelSmall,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1.5f)
                            )
                            Text(
                                score.category,
                                color = Color(0xFF8C7BB3),
                                style = MaterialTheme.typography.labelSmall,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1.6f)
                            )
                            Text(
                                score.difficulty,
                                color = Color(0xFF8C7BB3),
                                style = MaterialTheme.typography.labelSmall,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                score.score,
                                fontWeight = FontWeight.Bold,
                                color = if (score.rank == 1) Color(0xFF3DD726) else Color(0xFF333333),
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.weight(0.9f)
                            )
                            Text(
                                "${score.percentage}%",
                                fontWeight = FontWeight.Bold,
                                color = if (score.percentage < 50) Color(0xFFD7263D) else Color(0xFF2196F3),
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.weight(0.6f)
                            )
                            Text(
                                score.date,
                                color = Color(0xFF9A9AB0),
                                style = MaterialTheme.typography.labelSmall,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1.5f)
                            )
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
    }
}