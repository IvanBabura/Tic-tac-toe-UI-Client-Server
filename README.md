# Tic-tac-toe-UI-Client-Server

**Language**: 
[English](#English),
[Русский](#Русский)
____
# English

## 1 Short description
Tic-tac-toe. Client-server architecture using sockets and GUI.
This is not the final version, but already working.

## 2 Project Features
Multi-threaded client-server architecture. The client and server run separately from each other, i.e. through different JARs. Exchange of messages on a socket.
All the logic of the game on the server. The client only has the logic of its GUI and connection to the server.
Client GUI is working, but you can send coordinates through a text field too (a rudiment from the original version of the project).
The ability to support several games at the same time on the server.
The size of each field of the game can be selected at the start of the game itself from 3x3 to 10x10.
The server log is written to the corresponding file.
 

## 3 Known bugs and problems:
- When you click "stop server", the server stream and connection are stopped (as it should be), but the streams of running games are not.
- After the end of the game, the client cannot reconnect, because the connection thread is interrupted, the corresponding flag is left and an IllegalThreadStateException is thrown. When you restart the application, everything is ok including reconnection.
- When disconnecting a player who has not yet started the game, he is not removed from the field. «Dead fields» appear. There is no check for the readiness of both players to play (this can get rid of dead fields).

## 4 Future tasks:
- [ ] Add sending messages by pressing Enter.
- [ ] Add "X" and "0" icons to buttons instead of text.
- [ ] Continue to transfer the project to a more abstract level (for example, to make a less rigid relations between the GUI and the client logic, to stick interfaces).
- [ ] There are no tests. They are needed by the logic of the game and the verification of connections.
- [ ] Rewrite server logging. Make it more detailed.
- [ ] In the client, the program logic flow is closely related to the GUI. This does not affect this application in any way, but not very well.
- [ ] Rewrite the way of exchanging messages, because now it is a crutch: first, the "trigger" is sent, then the "action". It is possible to make one single class, for example, "Messages" and treat it as a message class. This will reduce network load and simplify your code.
- [ ] The side field with the client's log can be converted into a chat. So far, this is just communication with the server and a log of the client's actions.
- [ ] Implement a DBMS for storing fields and players.

## 5 Optional
This is just fun for me and training in refactoring. I'm not claiming anything serious. It was once a ~~crutch~~ term paper at the university. She was terrible ~~(yes, even worse than now)~~. I decided to refactor the project, complete the GUI, fix bugs.
It is clear that it is easier to rewrite this from scratch, but I will try to eliminate the bugs as much as possible without rewriting the code, but only making edits.

____
# Русский

## 1 Краткое описание
Крестики-нолики. Клиент-серверная архитектура с использованием сокетов и GUI.
Это не конечная версия, но уже рабочая.

## 2 Особенности проекта
Многопоточная клиент-серверная архитектура. Клиент и сервер запускаются отдельно друг от друга, т.е. через разные JAR-ники. Обмен сообщений по сокету. 
Вся логика игры на сервере. У клиента только логика его GUI и связь с сервером.
GUI рабочее, но можно отправлять координаты через текстовое поле (рудимент от первоначальной версии проекта).
Возможность поддерживать одновременно несколько игр на сервере.
Размер каждого поля игры можно выбрать при старте самой игры от 3х3 до 10х10.
Лог сервера записывается в соответствующий файл.
 

## 3 Известные баги и проблемы:
- При нажатии «остановить сервер» поток сервера и соединение останавливаются (как и должно быть), а вот потоки запущенных игр – нет.
- После завершения игры клиенту не удаётся переподключиться, тк поток соединения прерывается, оставляется соответствующий флаг и вылетает IllegalThreadStateException. При перезапуске приложения всё ок.
- При отключении игрока, который ещё не начал игру, он не удаляется из поля. Появляются «мёртвые поля». Нет проверки на готовность обоих игроков к игре (этим можно избавляться от мёртвых полей).

## 4 Что можно доработать 
- [ ] Добавить отправку сообщений по кнопке Enter.
- [ ] Добавить на кнопки иконки "X" и "0" вместо текста.
- [ ] Продолжить переводить проект на более абстрактный уровень (например, сделать менее жёсткую связь между GUI и логикой клиента, налепить интерфейсов).
- [ ] Отсутствуют тесты. Они нужны логике игры и проверки подключений.
- [ ] Переписать логирование сервера. Сделать более подробным.
- [ ] В клиенте поток логики программы тесно связан с GUI. На данное приложение это никак не влияет, но не очень хорошо.
- [ ] Переписать способ обмена сообщений, тк сейчас оно костыльное: сначала отправляется "триггер", потом "действие". Можно сделать один единый класс, например, «Messages» и обрабатывать как класс сообщения. Это уменьшить нагрузку на сеть и упростит код.
- [ ] Боковое поле с логом у клиента можно переделать под чат. Пока что это просто общение с сервером и лог действий клиента.
- [ ] Внедрить СУБД для хранения полей и игроков.

## 5 Дополнительно
Это просто развлечение для меня и тренировка в рефакторинге. Ни на что серьёзное я не претендую. Когда-то это была ~~костыльная~~ курсовая работа. Она была ужасна ~~(да, даже хуже, чем сейчас)~~. Я решил зарефакторить проект, доделать GUI, поустранять баги.
Понятное дело, что такое проще с нуля переписать, но я попытаться максимально устранить баги, не переписывая код, а лишь внося правки.
