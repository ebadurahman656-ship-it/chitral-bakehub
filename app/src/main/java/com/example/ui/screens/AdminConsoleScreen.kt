package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.OrderStatus
import com.example.ui.theme.*
import com.example.viewmodel.BakeHubUiState
import com.example.viewmodel.BakeHubViewModel

@Composable
fun AdminConsoleScreen(
    uiState: BakeHubUiState,
    viewModel: BakeHubViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WarmCream)
    ) {
        // Admin Top Bar
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = ChocolateBrown,
            shadowElevation = 3.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "CHITRAL BAKEHUB • OPERATIONS & GOVERNANCE",
                    style = MaterialTheme.typography.labelSmall,
                    color = GoldenHoney,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Valley Marketplace Admin Console",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = PureWhite
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Stats row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(shape = RoundedCornerShape(8.dp), color = RoastedCoffee, modifier = Modifier.weight(1f)) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text("ACTIVE BAKERIES", style = MaterialTheme.typography.labelSmall, color = SoftBakeryBeige, fontSize = 9.sp)
                            Text("${uiState.bakeries.size}", style = MaterialTheme.typography.titleSmall, color = PureWhite, fontWeight = FontWeight.Bold)
                        }
                    }
                    Surface(shape = RoundedCornerShape(8.dp), color = RoastedCoffee, modifier = Modifier.weight(1f)) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text("FLEET RIDERS", style = MaterialTheme.typography.labelSmall, color = SoftBakeryBeige, fontSize = 9.sp)
                            Text("11 Active", style = MaterialTheme.typography.titleSmall, color = PureWhite, fontWeight = FontWeight.Bold)
                        }
                    }
                    Surface(shape = RoundedCornerShape(8.dp), color = RoastedCoffee, modifier = Modifier.weight(1f)) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text("MARKET COMMISSION", style = MaterialTheme.typography.labelSmall, color = SoftBakeryBeige, fontSize = 9.sp)
                            Text("${uiState.commissionPercent}%", style = MaterialTheme.typography.titleSmall, color = GoldenHoney, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Section 1: Bakery Approval Queue
            item {
                Text(
                    text = "1. BAKERY MERCHANT ONBOARDING APPROVALS",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = ChocolateBrown
                )
            }

            if (uiState.pendingBakeryApprovals.isEmpty()) {
                item {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = PureWhite,
                        border = androidx.compose.foundation.BorderStroke(1.dp, WarmBeigeBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "All artisan bakery applications processed and approved.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MutedBrownText,
                            modifier = Modifier.padding(14.dp)
                        )
                    }
                }
            } else {
                items(uiState.pendingBakeryApprovals) { applicant ->
                    Card(
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = PureWhite),
                        border = androidx.compose.foundation.BorderStroke(1.dp, WarmBeigeBorder)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = applicant, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = DeepBrownText)
                                Text(text = "Kitchen & Food Safety Inspection Verified", style = MaterialTheme.typography.bodySmall, color = DarkGreenSuccess)
                            }
                            Button(
                                onClick = { viewModel.approveBakery(applicant) },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = BakeryOrange),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = PureWhite, modifier = Modifier.size(14.dp))
                                    Text("Approve", color = PureWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            // Section 2: Commission Settings
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = PureWhite),
                    border = androidx.compose.foundation.BorderStroke(1.dp, WarmBeigeBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "2. Marketplace Commission Tier",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = ChocolateBrown
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Current marketplace take rate on completed oven orders:",
                            style = MaterialTheme.typography.bodySmall,
                            color = MutedBrownText
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${uiState.commissionPercent}% (Standard Marketplace)",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = BakeryOrange
                            )

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Button(
                                    onClick = { viewModel.setCommission((uiState.commissionPercent - 1).coerceAtLeast(5)) },
                                    shape = RoundedCornerShape(6.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = SoftBakeryBeige),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                    modifier = Modifier.height(34.dp)
                                ) {
                                    Text("-1%", color = ChocolateBrown, fontWeight = FontWeight.Bold)
                                }
                                Button(
                                    onClick = { viewModel.setCommission((uiState.commissionPercent + 1).coerceAtMost(25)) },
                                    shape = RoundedCornerShape(6.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = SoftBakeryBeige),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                    modifier = Modifier.height(34.dp)
                                ) {
                                    Text("+1%", color = ChocolateBrown, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            // Section 3: Delivery Zones & Winter Elevation Surcharges
            item {
                Text(
                    text = "3. VALLEY DELIVERY ZONES & WINTER SURCHARGE",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = ChocolateBrown
                )
            }

            items(uiState.deliveryZones) { zone ->
                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = PureWhite),
                    border = androidx.compose.foundation.BorderStroke(1.dp, WarmBeigeBorder)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = zone.name, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = DeepBrownText)
                                Text(text = "Base Fee: Rs. ${zone.baseFeePkr} • ~${zone.avgDeliveryMins} mins", style = MaterialTheme.typography.bodySmall, color = MutedBrownText)
                            }
                            Switch(
                                checked = zone.isEnabled,
                                onCheckedChange = { viewModel.toggleZone(zone.id) },
                                colors = SwitchDefaults.colors(checkedTrackColor = DarkGreenSuccess)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Winter/Snow Surcharge: Rs. ${zone.winterSurchargePkr}",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = RoastedCoffee
                            )

                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                IconButton(
                                    onClick = { viewModel.updateWinterSurcharge(zone.id, -10) },
                                    modifier = Modifier.size(30.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.Remove, contentDescription = "Decrease surcharge", tint = ChocolateBrown)
                                }
                                IconButton(
                                    onClick = { viewModel.updateWinterSurcharge(zone.id, 10) },
                                    modifier = Modifier.size(30.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.Add, contentDescription = "Increase surcharge", tint = ChocolateBrown)
                                }
                            }
                        }
                    }
                }
            }

            // Section 4: All-Orders Live Overview
            item {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "4. ALL-ORDERS VALLEY OVERVIEW (${uiState.orders.size})",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = ChocolateBrown
                )
            }

            items(uiState.orders) { order ->
                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = PureWhite),
                    border = androidx.compose.foundation.BorderStroke(1.dp, WarmBeigeBorder)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = "${order.orderNumber} • ${order.bakeryName}", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = DeepBrownText)
                            Text(text = "Customer: ${order.customerName} • Rs. ${order.totalPkr}", style = MaterialTheme.typography.bodySmall, color = MutedBrownText)
                        }
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = when (order.status) {
                                OrderStatus.DELIVERED -> SuccessBg
                                OrderStatus.REJECTED -> ErrorBg
                                else -> WarningBg
                            }
                        ) {
                            Text(
                                text = order.status.title,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = when (order.status) {
                                    OrderStatus.DELIVERED -> DarkGreenSuccess
                                    OrderStatus.REJECTED -> DarkRedError
                                    else -> DarkAmberWarning
                                },
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
