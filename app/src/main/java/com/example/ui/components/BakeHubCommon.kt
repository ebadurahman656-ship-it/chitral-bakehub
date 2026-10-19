package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.SampleData
import com.example.model.AppRole
import com.example.model.BakeryCategory
import com.example.model.OvenStatus
import com.example.ui.theme.*

@Composable
fun BakeHubHeader(
    currentLocation: String,
    currentRole: AppRole,
    onLocationClick: () -> Unit,
    onRoleClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = PureWhite,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Brand Visual + Title
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.clickable { onRoleClick() }
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(ChocolateBrown),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_bakehub_icon),
                            contentDescription = "Chitral BakeHub Logo",
                            modifier = Modifier.size(28.dp),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Column {
                        Text(
                            text = "Chitral BakeHub",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = ChocolateBrown,
                            letterSpacing = (-0.3).sp
                        )
                        Text(
                            text = "Freshly prepared. Locally delivered.",
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 10.sp,
                            color = MutedBrownText,
                            maxLines = 1
                        )
                    }
                }

                // Role Switcher Button & Location
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Location Pill
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = SoftBakeryBeige,
                        border = androidx.compose.foundation.BorderStroke(1.dp, WarmBeigeBorder),
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .clickable(onClick = onLocationClick)
                            .testTag("location_selector_button")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "Delivery Location",
                                tint = BakeryOrange,
                                modifier = Modifier.size(15.dp)
                            )
                            Text(
                                text = currentLocation.split(",").firstOrNull() ?: currentLocation,
                                style = MaterialTheme.typography.labelMedium,
                                fontSize = 11.sp,
                                color = ChocolateBrown,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Select Location",
                                tint = ChocolateBrown,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    // Role Chip (Tap to switch into Bakery KDS, Rider, or Admin mode)
                    Surface(
                        shape = CircleShape,
                        color = ChocolateBrown,
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .clickable(onClick = onRoleClick)
                            .testTag("role_switcher_button")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = currentRole.iconEmoji,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HeroPromotionalBanner(
    onOrderNowClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .testTag("hero_promotional_banner"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = ChocolateBrown),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(148.dp)
        ) {
            // Background warm bakery hero image
            Image(
                painter = painterResource(id = R.drawable.img_hero_banner),
                contentDescription = "Chitral mountain bakery oven display",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Dark gradient overlay for WCAG AAA contrast
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                ChocolateBrown.copy(alpha = 0.92f),
                                ChocolateBrown.copy(alpha = 0.75f),
                                Color.Transparent
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(16.dp)
                    .widthIn(max = 240.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = GoldenHoney,
                    modifier = Modifier.padding(bottom = 6.dp)
                ) {
                    Text(
                        text = "SHAHI BAZAR FRESH",
                        style = MaterialTheme.typography.labelSmall,
                        color = ChocolateBrown,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }

                Text(
                    text = "Artisanal Walnut Breads & Honey Tarts",
                    style = MaterialTheme.typography.titleMedium,
                    color = PureWhite,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 20.sp,
                    maxLines = 2
                )

                Text(
                    text = "Stone-oven baked upon order confirmation.",
                    style = MaterialTheme.typography.bodySmall,
                    color = SoftBakeryBeige,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(top = 2.dp, bottom = 8.dp)
                )

                Button(
                    onClick = onOrderNowClick,
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BakeryOrange),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 4.dp),
                    modifier = Modifier
                        .height(34.dp)
                        .testTag("hero_order_button")
                ) {
                    Text(
                        text = "Order Fresh Bake",
                        style = MaterialTheme.typography.labelMedium,
                        color = PureWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
fun CategorySelectorRail(
    selectedCategory: BakeryCategory,
    onCategorySelected: (BakeryCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Categories",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = ChocolateBrown,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            BakeryCategory.values().forEach { category ->
                val isSelected = category == selectedCategory
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onCategorySelected(category) }
                        .padding(vertical = 4.dp)
                        .testTag("category_chip_${category.name}")
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isSelected) BakeryOrange else PureWhite)
                            .border(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = if (isSelected) BakeryOrange else WarmBeigeBorder,
                                shape = RoundedCornerShape(16.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = category.iconEmoji,
                            fontSize = 24.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = category.title,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) BakeryOrange else DeepBrownText,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}

@Composable
fun OvenStatusBadge(status: OvenStatus, modifier: Modifier = Modifier) {
    val (bgColor, textColor, icon) = when (status) {
        OvenStatus.ACTIVE -> Triple(SuccessBg, DarkGreenSuccess, "●")
        OvenStatus.BUSY -> Triple(WarningBg, DarkAmberWarning, "⏳")
        OvenStatus.PAUSED -> Triple(ErrorBg, DarkRedError, "⏸")
    }

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = bgColor,
        border = androidx.compose.foundation.BorderStroke(1.dp, textColor.copy(alpha = 0.5f)),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
            Text(text = icon, fontSize = 10.sp, color = textColor)
            Text(
                text = status.label,
                style = MaterialTheme.typography.labelSmall,
                color = textColor,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp
            )
        }
    }
}

@Composable
fun LocationSelectionDialog(
    currentLocation: String,
    onSelectLocation: (String) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = PureWhite,
        title = {
            Text(
                text = "Choose Delivery Location",
                style = MaterialTheme.typography.titleMedium,
                color = ChocolateBrown,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Select your neighborhood in Chitral for accurate oven prep & delivery estimates:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MutedBrownText
                )

                SampleData.deliveryLocations.forEach { loc ->
                    val isSelected = loc == currentLocation
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSelected) SoftBakeryBeige else WarmCream,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) BakeryOrange else WarmBeigeBorder
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { onSelectLocation(loc) }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = if (isSelected) Icons.Default.CheckCircle else Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = if (isSelected) BakeryOrange else RoastedCoffee,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = loc,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = DeepBrownText
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close", color = BakeryOrange, fontWeight = FontWeight.Bold)
            }
        }
    )
}
