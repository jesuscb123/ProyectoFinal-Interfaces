package dam2.jetpack.proyectofinal.core.components.navigation

// En un nuevo fichero, por ejemplo, EventStatsChart.kt


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun EventStatsChart(completedCount: Int, acceptedCount: Int) {
    val total = (completedCount + acceptedCount).coerceAtLeast(1) // Evitar división por cero
    val completedPercentage = completedCount.toFloat() / total
    val acceptedPercentage = acceptedCount.toFloat() / total

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        Bar(
            value = completedCount,
            percentage = completedPercentage,
            label = "Completados",
            color = MaterialTheme.colorScheme.primary
        )
        Bar(
            value = acceptedCount,
            percentage = acceptedPercentage,
            label = "Aceptados",
            color = MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
private fun RowScope.Bar(
    value: Int,
    percentage: Float,
    label: String,
    color: androidx.compose.ui.graphics.Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.weight(1f)
    ) {
        Text(
            text = value.toString(),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .fillMaxHeight(percentage)
                .clip(MaterialTheme.shapes.medium)
                .background(color)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
    }
}
