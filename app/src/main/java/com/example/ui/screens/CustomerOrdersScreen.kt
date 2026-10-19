package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Order
import com.example.model.OrderStatus
import com.example.model.OrderType
import com.example.ui.theme.*
import com.example.viewmodel.BakeHubUiState
import com.example.viewmodel.BakeHubViewModel

@Composable
fun CustomerOrdersScreen(
    uiState: BakeHubUiState,
    viewModel: BakeHubViewModel,
    modifier: Modifier = Modifier
) {
    val activeOrders = uiState.orders.filter { it.status != OrderStatus.DELIVERED && it.status != OrderStatus.REJECTED }
    val pastOrders = uiState.orders.filter { it.status == OrderStatus.DELIVERED || it.status == OrderStatus.REJECTED }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WarmCream)
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = PureWhite,
            shadowElevation = 2.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "My Bakery Orders",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = ChocolateBrown
                )
                Text(
                    text = "Real-time kitchen tracking & scheduled deliveries",
                    style = MaterialTheme.typography.bodySmall,
                    color = MutedBrownText
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            if (activeOrders.isNotEmpty()) {
                item {
                    Text(
                        text = "ACTIVE PREPARATION & DISPATCH (${activeOrders.size})",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = BakeryOrange,
                        letterSpacing = 0.5.sp
                    )
                }

                items(activeOrders) { order ->
                    ActiveOrderCard(
                        order = order,
                        onTrackClick = { viewModel.setTrackedOrder(order) }
                    )
                }
            }

            if (pastOrders.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "PAST DELIVERIES",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = RoastedCoffee,
                        letterSpacing = 0.5.sp
                    )
                }

                items(pastOrders) { order ->
                    PastOrderCard(
                        order = order,
                        onTrackClick = { viewModel.setTrackedOrder(order) }
                    )
                }
            }

            if (uiState.orders.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "🧺", fontSize = 48.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "No orders yet",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = ChocolateBrown
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Order fresh walnut loaves or sweet halwa pastries from local Chitral ovens.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MutedBrownText,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ActiveOrderCard(
    order: Order,
    onTrackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.5.dp, BakeryOrange, RoundedCornerShape(14.dp))
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onTrackClick)
            .testTag("active_order_${order.orderNumber}"),
        colors = CardDefaults.cardColors(containerColor = PureWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = order.orderNumber,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = ChocolateBrown
                    )
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = if (order.orderType == OrderType.PREPARE_NOW) BakeryOrange else GoldenHoney
                    ) {
                        Text(
                            text = order.orderType.displayName,
                            style = MaterialTheme.typography.labelSmall,
                            color = if (order.orderType == OrderType.PREPARE_NOW) PureWhite else ChocolateBrown,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Text(
                    text = "PIN: ${order.handoverPin}",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = BakeryOrange
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = order.bakeryName,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = DeepBrownText
            )

            Text(
                text = order.items.joinToString(", ") { "${it.quantity}x ${it.product.name}" },
                style = MaterialTheme.typography.bodySmall,
                color = MutedBrownText,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Current status bar
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = SoftBakeryBeige,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = if (order.orderType == OrderType.PREPARE_NOW) Icons.Default.Whatshot else Icons.Default.Schedule,
                        contentDescription = null,
                        tint = BakeryOrange,
                        modifier = Modifier.size(18.dp)
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = order.status.title,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = ChocolateBrown
                        )
                        Text(
                            text = order.estimatedDeliveryTime,
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 11.sp,
                            color = DeepBrownText
                        )
                    }

                    Button(
                        onClick = onTrackClick,
                        colors = ButtonDefaults.buttonColors(containerColor = BakeryOrange),
                        shape = RoundedCornerShape(6.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.height(30.dp)
                    ) {
                        Text("Track", fontSize = 11.sp, color = PureWhite, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun PastOrderCard(
    order: Order,
    onTrackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, WarmBeigeBorder, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onTrackClick),
        colors = CardDefaults.cardColors(containerColor = PureWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = order.orderNumber,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = ChocolateBrown
                    )
                    Text(
                        text = "• ${order.placedAtTime}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MutedBrownText
                    )
                }
                Text(
                    text = order.bakeryName,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = DeepBrownText
                )
                Text(
                    text = "Rs. ${order.totalPkr} • ${order.status.title}",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (order.status == OrderStatus.DELIVERED) DarkGreenSuccess else DarkRedError,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "View Details",
                tint = MutedBrownText,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
