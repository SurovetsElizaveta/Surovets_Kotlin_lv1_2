package com.example.surovets_kotlin_lv1_2

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.request.ImageRequest
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun GifsScreen(gifsRetrofit: GifsRetrofit) {
    var gifsList by rememberSaveable { mutableStateOf<List<Gif>?>(null) }
    var isLoading by rememberSaveable { mutableStateOf(false) }
    var isFailed by rememberSaveable { mutableStateOf(false) }
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
    val coroutineScope = rememberCoroutineScope()
    val handler = CoroutineExceptionHandler { _, exception ->
        run {
            isLoading = false
            isFailed = true
        }
    }
    val BASE_URL =
        "https://api.giphy.com/"

    Column(modifier = Modifier.fillMaxSize()) {
        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }

            isFailed -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column() {
                        Box(modifier = Modifier.clickable(onClick = {
                            ShowGifs(BASE_URL,
                                coroutineScope,
                                gifsRetrofit,
                                handler,
                                { gifsList = it },
                                { isLoading = it },
                                { isFailed = it })
                        })) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = null,
                                modifier = Modifier.align(Alignment.Center),
                            )
                        }
                    }
                }
            }

            else -> {
                if (isLandscape) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(10.dp)
                    )
                    {
                        items(gifsList ?: emptyList()) { gif ->
                            val imageRequest = ImageRequest.Builder(LocalContext.current)
                                .data(gif.images.original.url)
                                .decoderFactory(GifDecoder.Factory())
                                .build()


                            AsyncImage(
                                model = imageRequest,
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(5.dp)
                                    .clip(RoundedCornerShape(20.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(gifsList ?: emptyList()) { gif ->
                            val imageRequest = ImageRequest.Builder(LocalContext.current)
                                .data(gif.images.original.url)
                                .decoderFactory(GifDecoder.Factory())
                                .build()

                            Box( modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center) {
                                AsyncImage(
                                    model = imageRequest,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .padding(5.dp)
                                        .clip(RoundedCornerShape(20.dp)),
                                    alignment = Alignment.Center,
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                }
            }
        }
    }
    Row(modifier = Modifier.fillMaxWidth()) {
        Button(onClick = {
            coroutineScope.launch(handler) {}
            ShowGifs(
                BASE_URL,
                coroutineScope,
                gifsRetrofit,
                handler,
                { gifsList = it },
                { isLoading = it },
                { isFailed = it }
            )
        }, modifier = Modifier.align(Alignment.Bottom)) {
            Text(text = R.string.load_button.toString())
        }
    }
}


fun ShowGifs(
    url: String,
    coroutineScope: CoroutineScope,
    gifsRetrofit: GifsRetrofit,
    handler: CoroutineExceptionHandler,
    setGifsList: (List<Gif>?) -> Unit,
    setLoading: (Boolean) -> Unit,
    setFailed: (Boolean) -> Unit
) {
    coroutineScope.launch(handler) {
        setLoading(true)
        setFailed(false)
        try {
            val response = gifsRetrofit.requestGifs(url)
            setGifsList(response?.data)
        } catch (e: Exception) {
            setFailed(true)
        } finally {
            setLoading(false)
        }
    }
}
