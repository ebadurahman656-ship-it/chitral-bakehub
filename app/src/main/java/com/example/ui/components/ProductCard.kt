package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Bakery
import com.example.model.BakeryProduct
import com.example.ui.theme.*

@Composable
fun BakeryCard(
    bakery: Bakery,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .border(1.dp, WarmBeigeBorder, RoundedCornerShape(16.dp))
            .testTag("bakery_card_${bakery.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = PureWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Bakery Image with graceful Placeholder fallback
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(148.dp)
                    .background(SoftBakeryBeige)
            ) {
                // Image layer
                Image(
                    painter = painterResource(id = bakery.imageRes),
                    contentDescription = "Photo of ${bakery.name}",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Dark gradient overlay on bottom for text readability
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .align(Alignment.BottomCenter)
                        .background(
                            androidx.compose.ui.graphics.Brush.verticalGradient(
                                colors = listOf(Color.Transparent, ChocolateBrown.copy(alpha = 0.7f))
                            )
                        )
                )

                // Oven status pill top right
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                ) {
                    OvenStatusBadge(status = bakery.ovenStatus)
                }

                // Rating pill bottom start
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = ChocolateBrown.copy(alpha = 0.9f),
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = GoldenHoney,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "${bakery.rating} (${bakery.reviewCount} reviews)",
                            style = MaterialTheme.typography.labelSmall,
                            color = PureWhite,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Location badge bottom end
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = SoftBakeryBeige.copy(alpha = 0.95f),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(10.dp)
                ) {
                    Text(
                        text = "📍 ${bakery.location}",
                        style = MaterialTheme.typography.labelSmall,
                        color = ChocolateBrown,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            // Bakery Details Content
            Column(modifier = Modifier.padding(16.dp)) {
                // Name
                Text(
                    text = bakery.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = ChocolateBrown,
                    fontSize = 18.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Description (Prominent)
                Text(
                    text = if (bakery.description.isNotBlank()) bakery.description else "Traditional artisanal bakery serving fresh bakes in ${bakery.location}.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = DeepBrownText,
                    lineHeight = 20.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Operational metadata
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(text = "⏱️", fontSize = 12.sp)
                        Text(
                            text = "${bakery.prepTimeMin}-${bakery.prepTimeMax} mins",
                            style = MaterialTheme.typography.bodySmall,
                            color = RoastedCoffee,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Text(text = "•", color = WarmBeigeBorder)

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(text = "🛵", fontSize = 12.sp)
                        Text(
                            text = "Rs. ${bakery.deliveryFeePkr} Delivery",
                            style = MaterialTheme.typography.bodySmall,
                            color = DeepBrownText,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Text(text = "•", color = WarmBeigeBorder)

                    Text(
                        text = "Min Rs. ${bakery.minOrderPkr}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MutedBrownText
                    )
                }

                // Specialties Tags & Action Button
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.weight(1f, fill = false)
                    ) {
                        bakery.specialties.take(2).forEach { spec ->
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = SoftBakeryBeige
                            ) {
                                Text(
                                    text = spec,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = RoastedCoffee,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }
                    }

                    Button(
                        onClick = onClick,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BakeryOrange),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                        modifier = Modifier
                            .height(38.dp)
                            .testTag("explore_bakery_${bakery.id}")
                    ) {
                        Text(
                            text = "View Bakes →",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = PureWhite,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BakeryProductCard(
    product: BakeryProduct,
    quantityInCart: Int,
    onAddToCart: () -> Unit,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, WarmBeigeBorder, RoundedCornerShape(12.dp))
            .testTag("product_card_${product.id}"),
        colors = CardDefaults.cardColors(containerColor = PureWhite),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Product photo
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(115.dp)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                Image(
                    painter = painterResource(id = product.imageRes),
                    contentDescription = product.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Rapid Oven or Custom Badge
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (product.isRapidOven) BakeryOrange else ChocolateBrown,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(6.dp)
                ) {
                    Text(
                        text = if (product.isRapidOven) "⚡ Prepare Now" else "🗓️ Schedule Only",
                        style = MaterialTheme.typography.labelSmall,
                        color = PureWhite,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                if (!product.isLiveInStock) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(ChocolateBrown.copy(alpha = 0.75f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "SOLD OUT TODAY",
                            color = PureWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Column {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = DeepBrownText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = product.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MutedBrownText,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 11.sp,
                    lineHeight = 15.sp,
                    modifier = Modifier.height(30.dp)
                )

                Text(
                    text = product.weightOrPortion,
                    style = MaterialTheme.typography.labelSmall,
                    color = RoastedCoffee,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Rs. ${product.pricePkr}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = ChocolateBrown
                )

                if (!product.isLiveInStock) {
                    Text(
                        text = "Out of Stock",
                        style = MaterialTheme.typography.labelSmall,
                        color = DarkRedError,
                        fontWeight = FontWeight.Bold
                    )
                } else if (quantityInCart == 0) {
                    Button(
                        onClick = onAddToCart,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BakeryOrange),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 4.dp),
                        modifier = Modifier
                            .height(34.dp)
                            .testTag("add_product_${product.id}")
                    ) {
                        Text(
                            text = "ADD",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = PureWhite,
                            fontSize = 12.sp
                        )
                    }
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(SoftBakeryBeige, RoundedCornerShape(8.dp))
                            .border(1.dp, WarmBeigeBorder, RoundedCornerShape(8.dp))
                    ) {
                        IconButton(
                            onClick = onDecrement,
                            modifier = Modifier
                                .size(32.dp)
                                .testTag("decrement_${product.id}")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Remove,
                                contentDescription = "Decrease count",
                                tint = ChocolateBrown,
                                modifier = Modifier.size(14.dp)
                            )
                        }

                        Text(
                            text = "$quantityInCart",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = ChocolateBrown,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )

                        IconButton(
                            onClick = onIncrement,
                            modifier = Modifier
                                .size(32.dp)
                                .testTag("increment_${product.id}")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Increase count",
                                tint = ChocolateBrown,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
