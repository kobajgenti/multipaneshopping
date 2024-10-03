package com.example.multipaneshopping

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

data class Product(val name: String, val price: String, val description: String)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ShoppingApp()
                }
            }
        }
    }
}

@Composable
fun ShoppingApp() {
    val products = listOf(
        Product("Product A", "$100", "This is a great product A."),
        Product("Product B", "$150", "This is product B with more features."),
        Product("Product C", "$200", "Premium product C.")
    )
    var selectedProduct by remember { mutableStateOf<Product?>(null) }
    val navController = rememberNavController()
    val isLandscape = LocalConfiguration.current.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Shopping List",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(16.dp)
        )
        if (isLandscape) {
            Row(modifier = Modifier.weight(1f)) {
                ProductList(
                    products = products,
                    selectedProduct = selectedProduct,
                    onProductClick = { selectedProduct = it },
                    modifier = Modifier.weight(1f)
                )
                ProductDetails(
                    product = selectedProduct,
                    modifier = Modifier.weight(1f)
                )
            }
        } else {
            NavHost(navController = navController, startDestination = "productList", modifier = Modifier.weight(1f)) {
                composable("productList") {
                    ProductList(
                        products = products,
                        selectedProduct = selectedProduct,
                        onProductClick = {
                            selectedProduct = it
                            navController.navigate("productDetails")
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
                composable("productDetails") {
                    ProductDetailsWithBackButton(
                        product = selectedProduct,
                        onBackClick = {
                            navController.popBackStack()
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
fun ProductList(
    products: List<Product>,
    selectedProduct: Product?,
    onProductClick: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(products) { product ->
            Text(
                text = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onProductClick(product) }
                    .background(if (product == selectedProduct) Color.LightGray else Color.Transparent)
                    .padding(16.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsWithBackButton(
    product: Product?,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        TopAppBar(
            title = { Text("Product Details") },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        )
        ProductDetails(product = product, modifier = Modifier.weight(1f))
    }
}

@Composable
fun ProductDetails(
    product: Product?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        if (product != null) {
            Column {
                Text(text = product.name, style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = product.price, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = product.description, style = MaterialTheme.typography.bodyMedium)
            }
        } else {
            Text(
                text = "Select a product to view details.",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}