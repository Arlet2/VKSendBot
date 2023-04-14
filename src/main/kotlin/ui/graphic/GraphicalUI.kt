package ui.graphic

import androidx.compose.material.MaterialTheme
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import services.SendService
import ui.UI

class GraphicalUI(sendService: SendService?) : UI(sendService) {

    @Composable
    @Preview
    fun lol() {
        var text by remember { mutableStateOf("Hello, Worldd!") }

        MaterialTheme {
            Button(onClick = {
                text = "Hello, Desktop!"
            }) {
                Text(text)
            }
        }
    }

    override fun startInteraction() {
        application {
            Window(onCloseRequest = ::exitApplication) {
                lol()
            }
        }
    }
}