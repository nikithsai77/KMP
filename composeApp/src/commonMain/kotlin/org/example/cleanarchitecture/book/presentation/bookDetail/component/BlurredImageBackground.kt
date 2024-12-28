package org.example.cleanarchitecture.book.presentation.bookDetail.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cleanarchitecture.composeapp.generated.resources.Res
import cleanarchitecture.composeapp.generated.resources.compose_multiplatform
import coil3.compose.rememberAsyncImagePainter
import org.example.cleanarchitecture.core.DarkBlue
import org.example.cleanarchitecture.core.DesertWhite
import org.example.cleanarchitecture.core.SandYellow
import org.example.cleanarchitecture.core.presentation.PulseAnimation
import org.jetbrains.compose.resources.painterResource

@Composable
fun BlurredImageBackground(imageUrl: String?, isFavorite: Boolean, onFavoriteClick: () -> Unit, onBackClick: () -> Unit, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    var imageLoadResult by remember {
        mutableStateOf<Result<Painter>?>(value = null)
    }
    val painter = rememberAsyncImagePainter(
        model = imageUrl,
        onSuccess = {
            imageLoadResult = if(it.painter.intrinsicSize.width > 1 && it.painter.intrinsicSize.height > 1) Result.success(value = it.painter)
            else Result.failure(Throwable("InValid"))
        },
        onError = {
            it.result.throwable.printStackTrace()
            imageLoadResult =  Result.failure(Throwable(it.result.throwable))
        }
    )
    Box(modifier) {
        Column(modifier = Modifier.fillMaxSize()) {
             //Blur
             Image(painter = painter, contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxWidth().fillMaxHeight(fraction = 0.3f).blur(20.dp).background(DarkBlue))
             //Non Blur
             Box(modifier = Modifier.weight(0.7f).fillMaxWidth().background(DesertWhite))
        }

        IconButton(onClick = onBackClick, modifier = Modifier.align(Alignment.TopStart).padding(top = 16.dp, start = 16.dp).statusBarsPadding()) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color.Black)
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.fillMaxHeight(fraction = 0.15f))
            ElevatedCard(modifier = Modifier.height(230.dp).aspectRatio(ratio = 2/3f), shape = RoundedCornerShape(8.dp), elevation = CardDefaults.elevatedCardElevation(defaultElevation = 15.dp)) {
                  AnimatedContent(targetState = imageLoadResult) {result ->
                    when(result) {
                        null -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            PulseAnimation(modifier = Modifier.size(60.dp))
                        }
                        else -> {
                            Box {
                                Image(painter = if(result.isSuccess) painter else painterResource(Res.drawable.compose_multiplatform),
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize().background(Color.Transparent),
                                    contentScale = if (result.isSuccess) ContentScale.Crop else ContentScale.Fit)

                                IconButton(
                                    onClick = onFavoriteClick,
                                    modifier = Modifier.align(Alignment.BottomEnd)
                                        .background(
                                            brush = Brush.radialGradient(
                                                colors = listOf(SandYellow, Color.Transparent)
                                            )
                                        )
                                ) {
                                    Icon(imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder, contentDescription = null, tint = Color.Red)
                                }
                            }
                        }
                    }
                }
            }
            content()
        }
    }
}