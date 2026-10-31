package dev.lchang.appuesan.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import dev.lchang.appuesan.data.model.CountryModel
import dev.lchang.appuesan.presentation.favorites.FavoritesViewModel


val mockCountries = listOf(
    CountryModel("Perú", 50,"https://flagcdn.com/w320/pe.png"),
    CountryModel("Brasil", 11,"https://flagcdn.com/w320/br.png"),
    CountryModel("España", 1,"https://flagcdn.com/w320/es.png"),
    CountryModel("Portugal", 7,"https://flagcdn.com/w320/pt.png"),
    CountryModel("Argentina", 4,"https://flagcdn.com/w320/ar.png"),
    CountryModel("Japón", 30,"https://flagcdn.com/w320/jp.png"),
    CountryModel("Francia", 5,"https://flagcdn.com/w320/fr.png"),
)

@Composable
fun HomeScreen(viewModel: FavoritesViewModel = viewModel()){
    val favorites by viewModel.favoriteCountries.collectAsState()
    val favoriteNames = favorites.map { it.name }.toSet()

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .statusBarsPadding(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Ranking Fifa 2026", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn{
            items(mockCountries){ country ->
                val isFavorite = favoriteNames.contains(country.name)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Image(
                                contentDescription = country.name,
                                modifier = Modifier.size(64.dp),
                                contentScale = ContentScale.Crop,
                                painter = rememberAsyncImagePainter(country.imageUrl)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(country.name, style = MaterialTheme.typography.titleMedium)
                                Text("Ranking Fifa: " + country.ranking)
                            }
                        }
                        IconButton(onClick = { viewModel.toggleFavorite(country) }) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = if (isFavorite) "Eliminar de favoritos" else "Agregar a favoritos",
                                tint = if (isFavorite) Color.Red else Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}
