package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Order
import com.example.model.OrderStatus
import com.example.model.OrderType
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderTrackingSheet(
    order: Order,
    onDismiss: () -> Unit
) {
    val steps = listOf(
        Pair(OrderStatus.PLACED, "Order Placed"),
        Pair(OrderStatus.ACCEPTED, "Bakery Confirmed"),
        Pair(OrderStatus.IN_OVEN, "Baking in Oven"),
        Pair(OrderStatus.READY_FOR_PICKUP, "Ready at Counter"),
        Pair(OrderStatus.OUT_FOR_DELIVERY, "Out for Delivery"),
        Pair(OrderStatus.DELIVERED, "Delivered")
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = PureWhite,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 36.dp)
                .testTag("order_tracking_modal")
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "Order ${order.orderNumber}",
                            style = MaterialTheme.typography.titleLarge,
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
                        text = "${order.bakeryName} • Chitral Valley",
                        style = MaterialTheme.typography.bodySmall,
                        color = MutedBrownText
                    )
                }

                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = ChocolateBrown)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Current Status Highlight Box
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = SoftBakeryBeige,
                border = androidx.compose.foundation.BorderStroke(1.dp, WarmBeigeBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(if (order.status == OrderStatus.DELIVERED) DarkGreenSuccess else BakeryOrange),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = when (order.status) {
                                OrderStatus.PLACED -> Icons.Default.Receipt
                                OrderStatus.ACCEPTED -> Icons.Default.ThumbUp
                                OrderStatus.IN_OVEN -> Icons.Default.Whatshot
                                OrderStatus.READY_FOR_PICKUP -> Icons.Default.Storefront
                                OrderStatus.OUT_FOR_DELIVERY -> Icons.Default.TwoWheeler
                                OrderStatus.DELIVERED -> Icons.Default.Check
                                OrderStatus.REJECTED -> Icons.Default.Error
                            },
                            contentDescription = null,
                            tint = PureWhite,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Column {
                        Text(
                            text = order.status.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = ChocolateBrown
                        )
                        Text(
                            text = order.estimatedDeliveryTime,
                            style = MaterialTheme.typography.bodySmall,
                            color = DeepBrownText
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Visual Timeline Stepper
            Text(
                text = "Live Preparation & Dispatch Steps",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = DeepBrownText
            )

            Spacer(modifier = Modifier.height(12.dp))

            steps.forEachIndexed { index, (statusStep, label) ->
                val isCompleted = order.status.stepIndex >= statusStep.stepIndex
                val isCurrent = order.status == statusStep

                val circleBg by animateColorAsState(
                    targetValue = when {
                        isCurrent -> BakeryOrange
                        isCompleted -> DarkGreenSuccess
                        else -> WarmBeigeBorder
                    },
                    label = "timeline_circle"
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(32.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .clip(CircleShape)
                                .background(circleBg),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isCompleted && !isCurrent) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = PureWhite,
                                    modifier = Modifier.size(12.dp)
                                )
                            } else if (isCurrent) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(PureWhite)
                                )
                            }
                        }

                        if (index < steps.size - 1) {
                            Box(
                                modifier = Modifier
                                    .width(2.dp)
                                    .height(26.dp)
                                    .background(if (order.status.stepIndex > statusStep.stepIndex) DarkGreenSuccess else WarmBeigeBorder)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
                            color = if (isCurrent) BakeryOrange else if (isCompleted) DeepBrownText else MutedBrownText
                        )
                        if (isCurrent) {
                            Text(
                                text = "Active Stage • Verified by Bakery System",
                                style = MaterialTheme.typography.bodySmall,
                                fontSize = 11.sp,
                                color = RoastedCoffee
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Safety Handover PIN & Rider Info
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = WarmCream),
                border = androidx.compose.foundation.BorderStroke(1.dp, WarmBeigeBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Handover Verification PIN",
                                style = MaterialTheme.typography.labelSmall,
                                color = MutedBrownText
                            )
                            Text(
                                text = order.handoverPin,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = ChocolateBrown,
                                letterSpacing = 4.sp
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = GoldenHoney
                        ) {
                            Text(
                                text = "SHOW TO RIDER",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = ChocolateBrown,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Share this 4-digit code only when your warm bakery box arrives at your doorstep in ${order.deliveryAddress}.",
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 11.sp,
                        color = RoastedCoffee
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Order Items & Total
            Text(
                text = "Items Ordered (${order.items.size})",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = DeepBrownText
            )
            Spacer(modifier = Modifier.height(6.dp))
            order.items.forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "${item.quantity}x ${item.product.name}", style = MaterialTheme.typography.bodyMedium, color = DeepBrownText)
                    Text(text = "Rs. ${item.product.pricePkr * item.quantity}", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = ChocolateBrown)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = WarmBeigeBorder)
            Spacer(modifier = Modifier.height(6.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Total Paid via ${order.paymentMethod}", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = DeepBrownText)
                Text(text = "Rs. ${order.totalPkr}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = BakeryOrange)
            }
        }
    }
}
