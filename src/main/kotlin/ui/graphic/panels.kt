package ui.graphic

import Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.google.gson.JsonIOException
import com.google.gson.JsonSyntaxException
import order.Order
import androidx.compose.runtime.Composable
import order.OrderParser
import services.SendService
import java.io.FileNotFoundException
import java.io.IOException
import java.lang.Exception

private val defaultVerticalPadding = 60.dp

fun SendService.getGroupId() = groupActor.groupId ?: -1
fun SendService.getToken() = groupActor.accessToken ?: ""

@Composable
fun panel(
    verticalPadding: Dp = 0.dp,
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Center,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier.fillMaxHeight().padding(vertical = verticalPadding).then(modifier),
        verticalArrangement = verticalArrangement,
        content = content
    )
}

@Composable
fun leftPanel(
    modifier: Modifier = Modifier,
    groupId: MutableState<Int>,
    token: MutableState<String>,
    sendService: SendService,
    errorDialogMessage: MutableState<String>,
    infoDialogMessage: MutableState<String>
) {
    panel(
        verticalPadding = defaultVerticalPadding,
        modifier = Modifier.then(modifier),
        verticalArrangement = Arrangement.Top
    ) {
        val openChangeGroup = remember { mutableStateOf(false) }

        Button(
            onClick = {
                try {
                    Application.readAuthJson()
                    groupId.value = sendService.getGroupId()
                    token.value = sendService.getToken()
                } catch (e: FileNotFoundException) {
                    errorDialogMessage.value = "Файл auth.json не найден"
                    return@Button
                } catch (e: Throwable) {
                    when (e) {
                        is JsonSyntaxException, is JsonIOException -> {
                            errorDialogMessage.value = "JSON некорректно собран. " +
                                    "Проверьте наличие полей token, groupId. " +
                                    "Также они должны находиться в корневом объекте."
                        }

                        else -> {
                            errorDialogMessage.value = "Неизвестная ошибка JSON: ${e.message}"
                        }
                    }
                    return@Button
                }

                infoDialogMessage.value = "Успех"
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = "Считать файл auth.json"
            )
        }

        Button(
            onClick = {
                openChangeGroup.value = true
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = "Сменить group id и токен"
            )
        }

        if (openChangeGroup.value) {
            GroupDialog(
                onDismissRequest = {
                    openChangeGroup.value = false
                },
                token = token,
                groupId = groupId,
                sendService = sendService
            )
        }
    }
}

@Composable
fun centerPanel(
    modifier: Modifier,
    order: MutableState<Order>,
    groupId: MutableState<Int>,
    token: MutableState<String>,
    sendService: SendService,
    infoDialogMessage: MutableState<String>,
    errorDialogMessage: MutableState<String>
) {

    val showSubmitDialog = remember { mutableStateOf(false) }
    val errorMessage = remember { mutableStateOf("") }

    panel(
        verticalPadding = 0.dp,
        modifier = Modifier.then(modifier),
        verticalArrangement = Arrangement.Top
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "SendBot v. " + this.javaClass.getPackage().implementationVersion + " (by Arlet)",
                style = TextStyle(Color.Black),
                fontSize = 1.em,
                textAlign = TextAlign.Center
            )

            SelectionContainer {
                Text(
                    text = "https://github.com/Arlet2/VKSendBot",
                    style = TextStyle(Color.Gray),
                    fontSize = 1.em,
                    textAlign = TextAlign.Center
                )
            }

            SelectionContainer {
                Text(
                    text = "Group id: ${if (groupId.value == -1) "неизвестно" else groupId.value}",
                    textAlign = TextAlign.Center
                )
            }

            Text(
                text = "Токен: ${
                    if (token.value == "") "неизвестно" else
                        "*****" + token.value.takeLast(5)
                }",
                textAlign = TextAlign.Center
            )

            if (errorMessage.value != "") {
                SelectionContainer {
                    Text(errorMessage.value, color = Color.Red)
                }
            }

            val scrollableDataHeight = 200.dp

            Text(
                text = "Текст приказа (прокручивается):",
                textAlign = TextAlign.Center,
            )

            Box(
                modifier = Modifier.fillMaxWidth().heightIn(max = scrollableDataHeight),
                contentAlignment = Alignment.Center
            ) {
                SelectionContainer {
                    Text(
                        text = if (order.value.msg == "") "<Приказ отсутствует>" else order.value.msg,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.verticalScroll(state = rememberScrollState())
                    )
                }
            }

            Text(
                text = "Список (прокручивается):",
                textAlign = TextAlign.Center
            )

            Box(
                modifier = Modifier.fillMaxWidth().heightIn(max = scrollableDataHeight),
                contentAlignment = Alignment.Center
            ) {
                SelectionContainer {
                    Text(
                        text =
                        if (order.value.names.isEmpty()) "<Список пуст>"
                        else {
                            var resultString = ""

                            for (name in order.value.names) {
                                resultString += name + "\n"
                            }

                            resultString
                        },
                        textAlign = TextAlign.Center,
                        modifier = Modifier.verticalScroll(rememberScrollState())
                    )
                }
            }

            Button(
                onClick = {
                    showSubmitDialog.value = true
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "Отправить приказ",
                )
            }

            if (showSubmitDialog.value) {
                SubmitDialog(
                    onDismissRequest = { showSubmitDialog.value = false },
                    actionOnSubmit = {
                        if (order.value.msg == "") {
                            errorDialogMessage.value = "Сообщение не может быть пустым"
                            return@SubmitDialog
                        }

                        if (order.value.names.isEmpty()) {
                            errorDialogMessage.value = "Должен быть хотя бы один отправитель"
                            return@SubmitDialog
                        }

                        val startTime = System.currentTimeMillis()
                        try {
                            sendService.executeSendOrder(order.value)
                        } catch (e: Exception) {
                            errorDialogMessage.value = "Ошибка: ${e.message}"
                            return@SubmitDialog
                        }

                        infoDialogMessage.value = "Приказ исполнен за ${System.currentTimeMillis() - startTime} мс\n" +
                                "Был создан отчёт об отправке."
                    }
                )
            }
        }
    }
}

@Composable
fun rightPanel(
    modifier: Modifier,
    order: MutableState<Order>,
    errorDialogMessage: MutableState<String>,
    infoDialogMessage: MutableState<String>
) {
    val showOrderCreatingDialog = remember { mutableStateOf(false) }
    val showFileDialog = remember { mutableStateOf(false) }

    val orderParser = OrderParser()

    Box(
        modifier = Modifier.then(modifier)
    ) {
        Box(
            contentAlignment = Alignment.TopEnd,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 35.dp)
        ) {
            Button(
                onClick = {
                    infoDialogMessage.value =
                        """Это приложение предназначено для рассылки информации от имени группы VK.
                            |Можно отправлять сообщения только тем получателям, у которых есть диалог с этой группой.
                            |Приказом называется текст для отправки и список получателей этого текста.
                            |Приказ можно задать как с помощью кнопок, так и считать из файлов.
                            |Файл приказа задается специальным форматом, с которым можно ознакомиться на гите
                            |Также есть возможность сменить сообщество (либо только токен): с помощью файла или кнопок.
                            |Если менять с помощью файла, то достаточно просто создать файл auth.json рядом с jar-файлом
                            |Этот файл должен содержать всего два поля: token и groupId
                        """.trimMargin()
                }
            ) {
                Text("Help")
            }
        }

        panel(
            verticalPadding = defaultVerticalPadding,
            modifier = Modifier.then(modifier),
            verticalArrangement = Arrangement.Top,
        ) {


            Button(
                onClick = {
                    showFileDialog.value = true

                },
                modifier = Modifier.align(Alignment.CenterHorizontally),
                enabled = !showFileDialog.value // защита от псевдо двойного нажатия
            ) {
                Text(
                    text = "Считать приказ из файла"
                )
            }

            Button(
                onClick = {
                    showOrderCreatingDialog.value = true
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "Ввести приказ"
                )
            }

            if (showOrderCreatingDialog.value) {
                OrderDialog(
                    onDismissRequest = { showOrderCreatingDialog.value = false },
                    order = order
                )
            }

            if (showFileDialog.value) {
                ComposeFileDialog(
                    onCloseRequest = { fileName ->
                        showFileDialog.value = false
                        if (fileName == "")
                            return@ComposeFileDialog
                        try {
                            order.value = orderParser.parseOrder(fileName)
                        } catch (e: IOException) {
                            errorDialogMessage.value = "Не удалось открыть файл"
                            return@ComposeFileDialog
                        }
                        infoDialogMessage.value = "Успешно."
                    }
                )
            }

        }
    }
}
