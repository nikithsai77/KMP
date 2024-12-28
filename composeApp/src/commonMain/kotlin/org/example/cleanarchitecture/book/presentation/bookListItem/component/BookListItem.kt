package org.example.cleanarchitecture.book.presentation.bookListItem.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cleanarchitecture.composeapp.generated.resources.Res
import cleanarchitecture.composeapp.generated.resources.compose_multiplatform
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import org.example.cleanarchitecture.book.domain.Book
import org.example.cleanarchitecture.core.LightBlue
import org.example.cleanarchitecture.core.SandYellow
import org.example.cleanarchitecture.core.presentation.PulseAnimation
import org.jetbrains.compose.resources.painterResource
import kotlin.math.round

@Composable
fun BookListItem(book: Book, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Surface(shape = RoundedCornerShape(32.dp), modifier = modifier.clickable(onClick = onClick), color = LightBlue.copy(alpha = 0.2f)) {
        Row(modifier = Modifier.padding(16.dp).fillMaxWidth().height(IntrinsicSize.Min), verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Box(modifier = Modifier.height(100.dp), contentAlignment = Alignment.Center) {
                var imageLoadResult by remember {
                    mutableStateOf<Result<Painter>?>(value = null)
                }
                val painter = rememberAsyncImagePainter(
                    model = book.imageUrl,
                    onSuccess = {
                        imageLoadResult = if (it.painter.intrinsicSize.width > 1 && it.painter.intrinsicSize.height > 1) Result.success(value = it.painter)
                        else Result.failure(exception = Exception("InValid"))
                    },
                    onError = {
                        it.result.throwable.printStackTrace()
                        imageLoadResult = Result.failure(exception = it.result.throwable)
                    }
                )

                val painterState by painter.state.collectAsStateWithLifecycle()
                val transition by animateFloatAsState(targetValue = if (painterState is AsyncImagePainter.State.Success) 1f else 0f, animationSpec = tween(durationMillis = 600))

                when(val result = imageLoadResult) {
                    null -> PulseAnimation(modifier = Modifier.size(60.dp))
                    else -> Image(painter = if (result.isSuccess) painter else painterResource(Res.drawable.compose_multiplatform), contentDescription = book.title, contentScale = if (result.isSuccess) ContentScale.Crop else ContentScale.Fit, modifier = Modifier.aspectRatio(ratio = 0.65f, matchHeightConstraintsFirst = true)
                        .graphicsLayer {
                            rotationX = (1f - transition) * 30f
                            val scale = 0.8f + (0.2f * transition)
                            scaleX = scale
                            scaleY = scale
                        })
                }
            }
            Column(modifier = Modifier.fillMaxHeight().weight(1f), verticalArrangement = Arrangement.Center) {
                Text(text = book.title, style = MaterialTheme.typography.titleMedium, maxLines = 2, overflow = TextOverflow.Ellipsis)

                book.authors.firstOrNull()?.let { authorName ->
                    Text(text = authorName, style = MaterialTheme.typography.bodyLarge, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }

                book.averageRating?.let { rating ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // 4.555
                        // round(4.555 * 10) = 46
                        // 46 / 10 = 4.6
                        Text(text = "${round(x = rating * 10) / 10}", style = MaterialTheme.typography.bodyMedium)
                        Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = SandYellow)
                    }
                }
            }

            Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, modifier = Modifier.size(36.dp))
        }
    }
}