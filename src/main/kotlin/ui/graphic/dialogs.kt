package ui.graphic

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.AwtWindow
import order.Order
import services.SendService
import java.awt.FileDialog
import java.awt.Frame
import java.util.ArrayList

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ErrorDialog(onDismissRequest: () -> Unit, message: String = "") {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = "Ошибка") },
        text = { Text(text = message) },
        buttons = {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = onDismissRequest
                ) {
                    Text(text = "Ок")
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun InfoDialog(onDismissRequest: () -> Unit, message: String = "") {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = "Информация") },
        text = {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = message, textAlign = TextAlign.Center)
            }
        },
        buttons = {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = onDismissRequest
                ) {
                    Text(text = "Ок")
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun GroupDialog(
    onDismissRequest: () -> Unit,
    token: MutableState<String>,
    groupId: MutableState<Int>,
    sendService: SendService
) {
    val newGroupId = remember { mutableStateOf("") }
    val newToken = remember { mutableStateOf("") }

    val errorMessage = remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = "Смена groupId") },
        text = {
            Column(
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        verticalArrangement = Arrangement.Top
                    ) {
                        Text(text = "Group id")

                        TextField(
                            value = newGroupId.value,
                            onValueChange = { newValue -> newGroupId.value = newValue },
                            placeholder = { Text(text = "Введите новый groupId") }
                        )
                    }
                }

                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        verticalArrangement = Arrangement.Top
                    ) {
                        Text(text = "Token")

                        TextField(
                            value = newToken.value,
                            onValueChange = { newValue -> newToken.value = newValue },
                            placeholder = { Text(text = "Введите новый токен") }
                        )
                    }
                }

                Text(
                    text = errorMessage.value,
                    color = Color.Red
                )
            }
        },
        buttons = {
            Row(
                modifier = Modifier.padding(all = 8.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        errorMessage.value = ""

                        try {
                            groupId.value = newGroupId.value.toInt()
                        } catch (e: NumberFormatException) {
                            errorMessage.value = "Group id должно быть целым числом"
                            groupId.value = -1
                            return@Button
                        }

                        token.value = newToken.value

                        if (token.value == "") {
                            errorMessage.value = "Токен не может быть пустым"
                            return@Button
                        }

                        sendService.changeGroupActor(groupId.value, token.value)

                        onDismissRequest()
                    }
                ) {
                    Text("Принять")
                }

                Button(
                    onClick = onDismissRequest
                ) {
                    Text("Отмена")
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun OrderDialog(
    onDismissRequest: () -> Unit,
    order: MutableState<Order>
) {

    val orderText = remember { mutableStateOf("") }
    val orderReceivers = remember { mutableStateOf("") }

    val errorMessage = remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = "Ввод приказа") },
        text = {
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Введите текст для отправки",
                            textAlign = TextAlign.Center
                        )

                        TextField(
                            value = orderText.value,
                            onValueChange = { newValue -> orderText.value = newValue },
                            placeholder = { Text(text = "Текст...") }
                        )
                    }
                }

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Введите получателей через пробел, перенос строки",
                            textAlign = TextAlign.Center
                        )

                        TextField(
                            value = orderReceivers.value,
                            onValueChange = { newValue -> orderReceivers.value = newValue },
                            placeholder = { Text(text = "Ссылки vk.com/*** или просто имена пользователей") }
                        )
                    }
                }

                Text(
                    text = errorMessage.value,
                    color = Color.Red
                )
            }
        },
        buttons = {
            Row(
                modifier = Modifier.padding(all = 8.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        errorMessage.value = ""

                        val receivers: ArrayList<String> =
                            ArrayList<String>(orderReceivers.value.split(Regex("[\n\\s]")))

                        if (orderText.value == "") {
                            errorMessage.value = "Текст приказа не может быть пустым"
                            return@Button
                        }

                        if (receivers.isEmpty() || receivers.size == 1 && receivers[0] == "") {
                            errorMessage.value = "Должен быть хотя бы один получатель"
                            return@Button
                        }

                        order.value = Order(orderText.value, receivers.toTypedArray())

                        onDismissRequest()
                    }
                ) {
                    Text("Ввести")
                }

                Button(
                    onClick = onDismissRequest
                ) {
                    Text("Отмена")
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SubmitDialog(
    onDismissRequest: () -> Unit,
    actionOnSubmit: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = "Подтверждение") },
        text = {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Вы уверены?", textAlign = TextAlign.Center)
            }
        },
        buttons = {
            Row(
                modifier = Modifier.padding(all = 8.dp).widthIn(min = 300.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        actionOnSubmit()
                        onDismissRequest()
                    }
                ) {
                    Text("Да")
                }

                Button(
                    onClick = {
                        onDismissRequest()
                    }
                ) {
                    Text("Отмена")
                }
            }
        }
    )
}

@Composable
fun ComposeFileDialog(
    parent: Frame? = null,
    onCloseRequest: (path: String?) -> Unit
) = AwtWindow(
    create = {
        object : FileDialog(parent, "Выберите файл", LOAD) {
            override fun setVisible(b: Boolean) {
                super.setVisible(b)
                if (b) {
                    onCloseRequest("${directory ?: ""}${file ?: ""}")
                }
            }
        }
    },
    dispose = FileDialog::dispose
)