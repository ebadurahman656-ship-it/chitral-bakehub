package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BakeryCategory
import com.example.ui.components.BakeryProductCard
import com.example.ui.theme.*
import com.example.viewmodel.BakeHubUiState
import com.example.viewmodel.BakeHubViewModel

@Composable
fun CustomerSearchScreen(
    uiState: BakeHubUiState,
    viewModel: BakeHubViewModel,
    modifier: Modifier = Modifier
) {
    var searchInput by remember { mutableStateOf(uiState.searchQuery) }

    val matchedProducts = remember(searchInput, uiState.selectedCategory, uiState.products) {
        uiState.products.filter { prod ->
            val matchesCategory = (uiState.selectedCategory == BakeryCategory.ALL) || (prod.category == uiState.selectedCategory)
            val matchesText = searchInput.isBlank() ||
                    prod.name.contains(searchInput, ignoreCase = true) ||
                    prod.description.contains(searchInput, ignoreCase = true)
            matchesCategory && matchesText
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WarmCream)
    ) {
        // Search Header
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = PureWhite,
            shadowElevation = 2.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Search Chitral BakeHub",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = ChocolateBrown
                )
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = searchInput,
                    onValueChange = {
                        searchInput = it
                        viewModel.setSearchQuery(it)
                    },
                    placeholder = { Text("Search walnut bread, almond cake, biscuits...", fontSize = 13.sp) },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = BakeryOrange)
                    },
                    trailingIcon = {
                        if (searchInput.isNotEmpty()) {
                            IconButton(onClick = {
                                searchInput = ""
                                viewModel.setSearchQuery("")
                            }) {
                                Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear", tint = MutedBrownText)
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BakeryOrange,
                        unfocusedBorderColor = WarmBeigeBorder,
                        focusedContainerColor = PureWhite,
                        unfocusedContainerColor = PureWhite
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("search_input_field")
                )
            }
        }

        // Horizontal Category Filter Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            BakeryCategory.values().forEach { cat ->
                val isSelected = cat == uiState.selectedCategory
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = if (isSelected) BakeryOrange else PureWhite,
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) BakeryOrange else WarmBeigeBorder),
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .clickable { viewModel.setCategory(cat) }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(text = cat.iconEmoji, fontSize = 12.sp)
                        Text(
                            text = cat.title,
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isSelected) PureWhite else DeepBrownText,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }
        }

        // Results
        if (matchedProducts.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "🥖", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No baked creations match your search",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = ChocolateBrown
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Try searching for 'Walnut', 'Almond', 'Cake', or 'Naan'.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MutedBrownText
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 80.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Text(
                        text = "${matchedProducts.size} bakery item${if (matchedProducts.size > 1) "s" else ""} found",
                        style = MaterialTheme.typography.bodySmall,
                        color = MutedBrownText,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }

                val pairs = matchedProducts.chunked(2)
                items(pairs) { pair ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        pair.forEach { prod ->
                            val inCart = uiState.cartItems.find { it.product.id == prod.id }?.quantity ?: 0
                            Box(modifier = Modifier.weight(1f)) {
                                BakeryProductCard(
                                    product = prod,
                                    quantityInCart = inCart,
                                    onAddToCart = { viewModel.addToCart(prod) },
                                    onIncrement = { viewModel.incrementCartItem(prod) },
                                    onDecrement = { viewModel.decrementCartItem(prod) }
                                )
                            }
                        }
                        if (pair.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}
