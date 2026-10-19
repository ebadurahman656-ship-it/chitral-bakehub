package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BakeryCategory
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.viewmodel.BakeHubUiState
import com.example.viewmodel.BakeHubViewModel

@Composable
fun CustomerHomeScreen(
    uiState: BakeHubUiState,
    viewModel: BakeHubViewModel,
    onRoleClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val totalCartCount = uiState.cartItems.sumOf { it.quantity }
    val totalCartPrice = uiState.cartItems.sumOf { it.product.pricePkr * it.quantity }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(WarmCream)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = if (totalCartCount > 0) 100.dp else 24.dp)
        ) {
            // Top App Bar / Header
            item {
                BakeHubHeader(
                    currentLocation = uiState.selectedLocation,
                    currentRole = uiState.currentRole,
                    onLocationClick = { viewModel.toggleLocationPicker(true) },
                    onRoleClick = onRoleClick
                )
            }

            // Search Bar Input
            item {
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = PureWhite,
                        border = androidx.compose.foundation.BorderStroke(1.dp, WarmBeigeBorder),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { viewModel.setCustomerTab(com.example.model.CustomerNavTab.SEARCH) }
                            .testTag("home_search_bar")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 14.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = MutedBrownText
                            )
                            Text(
                                text = "Search bakeries, walnut bread, cakes, pastries...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MutedBrownText,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }

            // Promotional Hero Banner
            item {
                HeroPromotionalBanner(
                    onOrderNowClick = {
                        val walnutLoaf = uiState.products.find { it.id == "prod_1" }
                        if (walnutLoaf != null) {
                            viewModel.addToCart(walnutLoaf)
                            viewModel.setCheckoutOpen(true)
                        }
                    }
                )
            }

            // Categories Rail
            item {
                CategorySelectorRail(
                    selectedCategory = uiState.selectedCategory,
                    onCategorySelected = { cat ->
                        viewModel.setCategory(cat)
                    }
                )
            }

            // Local Chitral Bakeries Section Header
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Local Chitral Bakeries",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = ChocolateBrown
                        )
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = SoftBakeryBeige,
                            border = androidx.compose.foundation.BorderStroke(1.dp, WarmBeigeBorder)
                        ) {
                            Text(
                                text = "${uiState.bakeries.size} Artisans Active",
                                style = MaterialTheme.typography.labelSmall,
                                color = BakeryOrange,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Authentic wood-fired stone hearth bakehouses across Chitral Valley",
                        style = MaterialTheme.typography.bodySmall,
                        color = MutedBrownText
                    )
                }
            }

            // Bakery Cards
            items(uiState.bakeries) { bakery ->
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                    BakeryCard(
                        bakery = bakery,
                        onClick = { viewModel.setSelectedBakery(bakery) }
                    )
                }
            }

            // Freshly Baked Products Section
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (uiState.selectedCategory == BakeryCategory.ALL) "Freshly Baked Today" else "${uiState.selectedCategory.title} from Valley Ovens",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = ChocolateBrown
                    )
                    Text(
                        text = "Real-time Stock",
                        style = MaterialTheme.typography.labelSmall,
                        color = RoastedCoffee
                    )
                }
            }

            // Product Cards in a 2-Column Grid Simulation
            val filteredProducts = if (uiState.selectedCategory == BakeryCategory.ALL) {
                uiState.products
            } else {
                uiState.products.filter { it.category == uiState.selectedCategory }
            }

            val productPairs = filteredProducts.chunked(2)
            items(productPairs) { pair ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
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

        // Floating Sticky Cart Bar
        AnimatedVisibility(
            visible = totalCartCount > 0,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = ChocolateBrown,
                shadowElevation = 8.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .clickable { viewModel.setCheckoutOpen(true) }
                    .testTag("floating_cart_bar")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "$totalCartCount item${if (totalCartCount > 1) "s" else ""} in Basket",
                            style = MaterialTheme.typography.labelMedium,
                            color = GoldenHoney,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Rs. $totalCartPrice",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = PureWhite
                        )
                    }

                    Button(
                        onClick = { viewModel.setCheckoutOpen(true) },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BakeryOrange),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        modifier = Modifier.height(38.dp)
                    ) {
                        Text(
                            text = "View Basket →",
                            style = MaterialTheme.typography.labelMedium,
                            color = PureWhite,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
