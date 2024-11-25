package com.example.surovets_kotlin_lv1_2

data class Original(
    val height: String,
    val width: String,
    val size: String,
    val url: String,
    val mp4_size: String,
    val mp4: String,
    val webp_size: String,
    val webp: String,
    val frames: String,
    val hash: String
)

data class Images(
    val original: Original
)


data class Gif(
    val type: String,
    val id: String,
    val url: String,
    val slug: String,
    val bitly_gif_url: String,
    val bitly_url: String,
    val embed_url: String,
    val username: String,
    val source: String,
    val title: String,
    val rating: String,
    val content_url: String,
    val source_tld: String,
    val source_post_url: String,
    val is_stecker: String,
    val import_datetime: String,
    val trending_datetime: String,
    val images: Images
)

data class Gifs(
    val data: List<Gif>
)
