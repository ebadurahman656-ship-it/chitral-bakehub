package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Bakery
import com.example.model.BakeryProduct
import com.example.model.CartItem
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BakeryDetailBottomSheet(
    bakery: Bakery,
    products: List<BakeryProduct>,
    cartItems: List<CartItem>,
    onAddToCart: (BakeryProduct) -> Unit,
    onIncrement: (BakeryProduct) -> Unit,
    onDecrement: (BakeryProduct) -> Unit,
    onDismiss: () -> Unit
) {
    val bakeryProducts = products.filter { it.bakeryId == bakery.id }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = PureWhite,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 36.dp)
                .testTag("bakery_detail_sheet")
        ) {
            // Header Image Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(SoftBakeryBeige)
            ) {
                Image(
                    painter = painterResource(id = bakery.imageRes),
                    contentDescription = bakery.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .background(ChocolateBrown.copy(alpha = 0.6f), shape = RoundedCornerShape(20.dp))
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = PureWhite)
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    OvenStatusBadge(status = bakery.ovenStatus)
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                // Name & Rating
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = bakery.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = ChocolateBrown
                    )

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = SoftBakeryBeige
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = GoldenHoney, modifier = Modifier.size(16.dp))
                            Text(
                                text = "${bakery.rating} (${bakery.reviewCount})",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = ChocolateBrown
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Location / Address
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = BakeryOrange, modifier = Modifier.size(16.dp))
                    Text(
                        text = bakery.address,
                        style = MaterialTheme.typography.bodySmall,
                        color = RoastedCoffee,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Detailed Description
                Text(
                    text = bakery.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = DeepBrownText,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Meta row
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = WarmCream,
                    border = androidx.compose.foundation.BorderStroke(1.dp, WarmBeigeBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("PREPARATION", style = MaterialTheme.typography.labelSmall, color = MutedBrownText)
                            Text("${bakery.prepTimeMin}-${bakery.prepTimeMax} mins", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = ChocolateBrown)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("DELIVERY FEE", style = MaterialTheme.typography.labelSmall, color = MutedBrownText)
                            Text("Rs. ${bakery.deliveryFeePkr}", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = BakeryOrange)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("MINIMUM ORDER", style = MaterialTheme.typography.labelSmall, color = MutedBrownText)
                            Text("Rs. ${bakery.minOrderPkr}", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = ChocolateBrown)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Products available from this bakery
                Text(
                    text = "Fresh Bakes from ${bakery.name} (${bakeryProducts.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = ChocolateBrown
                )

                Spacer(modifier = Modifier.height(10.dp))

                bakeryProducts.forEach { prod ->
                    val inCart = cartItems.find { it.product.id == prod.id }?.quantity ?: 0
                    BakeryProductCard(
                        product = prod,
                        quantityInCart = inCart,
                        onAddToCart = { onAddToCart(prod) },
                        onIncrement = { onIncrement(prod) },
                        onDecrement = { onDecrement(prod) },
                        modifier = Modifier.padding(vertical = 6.dp)
                    )
                }

                if (bakeryProducts.isEmpty()) {
                    Text(
                        text = "New batches are currently being prepared in the stone oven.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MutedBrownText,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                }
            }
        }
    }
}
