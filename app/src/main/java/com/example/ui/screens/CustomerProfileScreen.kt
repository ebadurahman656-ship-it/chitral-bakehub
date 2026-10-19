package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppRole
import com.example.ui.theme.*
import com.example.viewmodel.BakeHubUiState
import com.example.viewmodel.BakeHubViewModel

@Composable
fun CustomerProfileScreen(
    uiState: BakeHubUiState,
    viewModel: BakeHubViewModel,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(WarmCream),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // User Profile Card
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = PureWhite),
                border = androidx.compose.foundation.BorderStroke(1.dp, WarmBeigeBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(ChocolateBrown),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "AR", color = PureWhite, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    }

                    Column {
                        Text(
                            text = "Ali Rehman",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = ChocolateBrown
                        )
                        Text(
                            text = "+92 345 8812490 • Chitral Town",
                            style = MaterialTheme.typography.bodySmall,
                            color = MutedBrownText
                        )
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = SoftBakeryBeige,
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            Text(
                                text = "BakeHub Loyal Patron (Tier 1)",
                                style = MaterialTheme.typography.labelSmall,
                                color = RoastedCoffee,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }

        // Marketplace Ecosystem Role Switcher
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = PureWhite),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, BakeryOrange)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(text = "🔄", fontSize = 16.sp)
                        Text(
                            text = "Switch Platform View / Persona",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = ChocolateBrown
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Experience all 4 integrated systems of Chitral BakeHub:",
                        style = MaterialTheme.typography.bodySmall,
                        color = MutedBrownText
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    AppRole.values().forEach { role ->
                        val isSelected = role == uiState.currentRole
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) SoftBakeryBeige else WarmCream,
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) BakeryOrange else WarmBeigeBorder),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { viewModel.setRole(role) }
                                .testTag("select_role_${role.name}")
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(text = role.iconEmoji, fontSize = 22.sp)
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = role.title,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) BakeryOrange else DeepBrownText
                                    )
                                    Text(
                                        text = role.subtitle,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MutedBrownText,
                                        fontSize = 11.sp
                                    )
                                }
                                if (isSelected) {
                                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = "Active", tint = BakeryOrange, modifier = Modifier.size(20.dp))
                                }
                            }
                        }
                    }
                }
            }
        }

        // Demo Notice Banner
        item {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = WarningBg,
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkAmberWarning.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(imageVector = Icons.Default.Info, contentDescription = null, tint = DarkAmberWarning, modifier = Modifier.size(20.dp))
                    Column {
                        Text(
                            text = "Chitral Valley Pilot Demo Notice",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = DarkAmberWarning
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "This prototype demonstrates the complete multi-bakery marketplace experience with simulated local data across Shahi Bazar, Chew Bazar, Denin, and Drosh. Live payment gateways and GPS telemetry can be connected to real cloud backends.",
                            style = MaterialTheme.typography.bodySmall,
                            color = DeepBrownText,
                            fontSize = 11.sp,
                            lineHeight = 15.sp
                        )
                    }
                }
            }
        }

        // About Chitral BakeHub
        item {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = PureWhite),
                border = androidx.compose.foundation.BorderStroke(1.dp, WarmBeigeBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "About Chitral BakeHub",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = ChocolateBrown
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Brand Tagline: Freshly prepared. Locally delivered.\nInspired by traditional stone-oven bakers of the Hindukush mountains.",
                        style = MaterialTheme.typography.bodySmall,
                        color = RoastedCoffee,
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}
