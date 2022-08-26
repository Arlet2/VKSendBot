# VKSendBot
Бот предназначен для отправки сообщений от имени группы VK (получатели должны предварительно иметь диалог с этой группой).
Запуск производится через uber jar через консоль

Первый запуск

Необходимо создать файл auth.json рядом с jar файлом. JSON должен содержать ключ "token" (со значением токена от группы) и ключ "groupId" (со значением id группы).

Работа с программой

Для работы с программой можно воспользоваться интерактивным меню (команда help используется для отображения всех возможных команд). 
Программа работает только с *.order файлами!
Файл приказа состоит из сообщения и из отображаемых имён получателей (screen_name в VK API или же отображаемая ссылка в vk).

В начале указывается сообщением в виде: message:\n"Your text" (перенос строки после двоеточие обязательно! Количество переносов строки в кавычках неограничено).
Затем указываются имена получателей в виде: names:\n*имена получателей, разделенные переносом строки*. Важно! Имена разделяются только переносом строки!
Пример .order файла есть в папке orders

После исполнения приказа будет создан файл отчёта, где будут указаны ошибки при отправке, либо при конвертации имени в id. 
В случае невозможности создания файла весь отчёт будет написан в консоль.