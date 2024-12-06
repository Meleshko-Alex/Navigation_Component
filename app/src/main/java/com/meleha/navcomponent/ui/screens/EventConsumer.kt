package com.meleha.navcomponent.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.withContext

@Composable
fun <T> EventConsumer(channel: ReceiveChannel<T>, block: (T) -> Unit) {
    val blockState by rememberUpdatedState(block)
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(Unit) {
        withContext(Dispatchers.Main.immediate) {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                for (event in channel) blockState(event)
            }
        }
    }
}