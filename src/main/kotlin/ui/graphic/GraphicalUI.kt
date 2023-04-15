package ui.graphic

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import order.Order
import services.SendService
import ui.UI

class GraphicalUI(private val sendService: SendService) : UI(sendService) {
    @Composable
    fun application() {
        val order = remember { mutableStateOf(Order("", arrayOf())) }
        val groupId = remember {
            mutableStateOf(
                if (sendService.groupActor != null) sendService.groupActor.groupId ?: -1 else -1
            )
        }
        val token = remember {
            mutableStateOf(
                if (sendService.groupActor != null) sendService.groupActor.accessToken ?: "" else ""
            )
        }
        Row(modifier = Modifier.fillMaxSize()) {
            leftPanel(
                modifier = Modifier.weight(1f),
                token = token,
                groupId = groupId,
                sendService = sendService
            )
            centerPanel(
                modifier = Modifier.weight(1f),
                order = order,
                groupId = groupId,
                token = token
            )
            rightPanel(
                modifier = Modifier.weight(1f),
                order
            )
        }
    }


    override fun startInteraction() {
        application {
            Window(onCloseRequest = ::exitApplication, title = "VKSendBot") {
                MaterialTheme {
                    application()
                }
            }
        }
    }
}