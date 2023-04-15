package ui.graphic

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
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

        val errorDialogMessage = remember { mutableStateOf("") }
        val infoDialogMessage = remember { mutableStateOf("") }

        Row(modifier = Modifier.fillMaxSize()) {
            leftPanel(
                modifier = Modifier.weight(1f),
                token = token,
                groupId = groupId,
                sendService = sendService,
                errorDialogMessage = errorDialogMessage,
                infoDialogMessage = infoDialogMessage
            )
            centerPanel(
                modifier = Modifier.weight(1f),
                order = order,
                groupId = groupId,
                token = token,
                sendService = sendService,
                errorDialogMessage = errorDialogMessage,
                infoDialogMessage = infoDialogMessage
            )
            rightPanel(
                modifier = Modifier.weight(1f),
                order = order,
                errorDialogMessage = errorDialogMessage,
                infoDialogMessage = infoDialogMessage
            )
        }

        if (errorDialogMessage.value != "")
            ErrorDialog({ errorDialogMessage.value = "" }, message = errorDialogMessage.value)

        if (infoDialogMessage.value != "")
            InfoDialog({ infoDialogMessage.value = "" }, message = infoDialogMessage.value)
    }


    override fun startInteraction() {
        application {
            Window(
                onCloseRequest = ::exitApplication,
                title = "VKSendBot",
                icon = BitmapPainter(useResource("icon.png", ::loadImageBitmap))
            ) {
                MaterialTheme {
                    application()
                }
            }
        }
    }
}