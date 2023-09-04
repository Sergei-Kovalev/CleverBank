# CheckRunner

Этот проект создан как тестовое задание для Backend development course от компании Clevertec :clap:
---
___Содержание:___    
* Используемый стек.
* Реализованный функционал.
* Инструкция по запуску.
* CRUD операции с примерами входных и выходных данных.
___     
    
# Используемый стек и библиотеки.
1. Java 17
2. Gradle 8.2
3. PostgreSQL 15
4. TomCat 10.1.13
5. JDBC
6. Lombock
7. Servlets
8. Snakeyaml 2.1
9. pdfbox 3.0.0
10. IntelliJ IDEA Community Edition
11. Postman
___

# Реализованный функционал
1. Операции снятия и пополнения счёта.
2. Операции перевода средств другому клиенту Clever-Bank и
   клиенту другого банка.    
3. Операции по начислению процентов по счёту в последний день года в размере, который берется из .yml файла. Проверка необходимости начисления происходит раз в 30 секунд.
4. Конфигурационные данные хранятся в файле - properties.yml.
5. После каждой операции формируется чек в формате .txt
    * чеки хранятся в файле check в корне проекта
6. JavaDoc'и хранятся в папке javaDoc. Стартовая страница: index.html.
7. Реализован функционал получения выписки со всеми транзакциями за период. Возможные периоды: месяц, год, весь период. 
    * выписка формируется в корень проекта в формате .txt
8. Реализованы CRUD операции для всех сущностей (кроме сущности Check, которая существует только для получения последовательного номера нового чека).
9. Реализован функционал получения выписки приходно-расходных операций по счёту.
    * вызов с помощью Servlet
    * формат файла .pdf
    * В связи с использованием IntelliJ IDEA Community Edition место сохранения изменено. Адрес места формирования: директорияTomcat/bin/statement-money
10. Реализовано сквозное логирование методов основного сервиса Controller.class. Логи сохраняются в папку logs проекта.
___

# Инструкция по запуску

1. Создать таблицы в базе данных PostgreSQL с помощью скрипта, который расположен в папке resources проекта (/src/main/recources). Выполнение скрипта также наполнит таблицы базовыми данными.
2. Для работы консольного приложения достаточно запустить главный класс проекта - CleverBank.class
3. При написании проекта использовалась IntelliJ IDEA Community Edition, поэтому для работы Servlet необходимо:
* в Gradle собрать War файл.. 
    * находится по пути build/libs/ 
    * название CleverBank.war
* скопировать его и поместить в сервер TomCat
    * в папку "папка сервера"\apache-tomcat-someversion\webapps
* запустить сервер Tomcat.
* теперь можно обращаться к базе данных через браузер.

___

# CRUD операции с примерами входных и выходных данных.
Сущности для которых реализованы CRUD операции:
1. Account. - счета
2. Bank. - банки
3. Currency. - валюты
4. Transaction. - транзакции
5. TransactionType. - типы транзакций (пополнение, перевод и т.п.)
6. User. - клиенты
7. MoneyStatement* данный сервелет реализует только получение приходно-расходных операций по счёту.

### примеры CRUD для Account:
запрос Get / Read:
http://localhost:8080/CleverBank/account?id=1
* где id - id запрашиваемого счёта
* ответ:
~~~
Account(id=1, name=AS12 ASDG 1200 2132 ASDA 353A 2132, openingDate=1970-01-01T00:00, balance=10000.0, user=User(id=1, name=Сотников Кирилл Артёмович, login=kirill, password=password), bank=Bank(id=1, name=Clever-Bank), currency=Currency(id=1, name=BYN))
~~~
запрос Post / Create:
http://localhost:8080/CleverBank/account?name=AS12 4306 1200 2132 DGSS 56SD 9999&balance=550.23&user=1&bank=2&currency=1
* где name - имя создаваемого счёта
* balance - первоначальный баланс счёта
* user - id пользователя открывшего счёт
* bank - id банка в котором открывается счёт
* currency - id валюты операций по счёту
* ответ:
~~~
Account(id=43, name=AS12 4306 1200 2132 DGSS 56SD 9999, openingDate=2023-09-04T00:46:15.347257800, balance=550.23, user=User(id=1, name=Сотников Кирилл Артёмович, login=kirill, password=password), bank=Bank(id=2, name=Alfa Bank), currency=Currency(id=1, name=BYN))
~~~
запрос Put / Update:
http://localhost:8080/CleverBank/account?balance=1000&id=43
* где balance - сумма для записи в баланс счёта
* id - id счёта
* ответ:
~~~
Account(id=43, name=AS12 4306 1200 2132 DGSS 56SD 9999, openingDate=2023-09-04T00:46:15.347258, balance=1000.0, user=User(id=1, name=Сотников Кирилл Артёмович, login=kirill, password=password), bank=Bank(id=2, name=Alfa Bank), currency=Currency(id=1, name=BYN))
~~~
запрос Delete:
http://localhost:8080/CleverBank/account?balance=1000&id=43
* где id - id удаляемого счёта
* ответ:
~~~
Клиент с id 43 удален.
~~~
* либо 
~~~
Клиента с таким id нет в базе данных
~~~
### примеры CRUD для Bank:
запрос Get / Read:
http://localhost:8080/CleverBank/bank?id=1
* где id - id банка
* ответ:
~~~
Bank(id=1, name=Clever-Bank)
~~~
запрос Post / Create:
http://localhost:8080/CleverBank/bank?name=BelSwissBank
* где name - имя нового банка.
* ответ:
~~~
Bank(id=7, name=BelSwissBank)
~~~
запрос Put / Update:
http://localhost:8080/CleverBank/bank?name=MyOwn&id=7
* где id - id изменяемого банка
* name - новое имя банка
* ответ:
~~~
Bank(id=7, name=MyOwn)
~~~
запрос Delete:
http://localhost:8080/CleverBank/bank?id=7
* где id - id удаляемого банка
* ответ:
~~~
Банк с id 7 удален.
~~~
либо
~~~
Банка с таким id нет в базе данных
~~~
### примеры CRUD для Currency:
запрос Get / Read:
http://localhost:8080/CleverBank/currency?id=1
* где id - id валюты
* ответ:
~~~
Currency(id=1, name=BYN)
~~~
запрос Post / Create:
http://localhost:8080/CleverBank/currency?name=SWW
* где name - имя новой валюты
* ответ:
~~~
Currency(id=7, name=SWW)
~~~
запрос Put / Update:
http://localhost:8080/CleverBank/currency?name=TUN&id=7
* где name - новое имя валюты
* id - id изменяемой валюты
* ответ:
~~~
Currency(id=7, name=TUN)
~~~
запрос Delete:
http://localhost:8080/CleverBank/currency?id=7
* где id - id валюты для удаления
* ответ:
~~~
Наименование валюты с id 7 удалено.
~~~
либо
~~~
Наименования валюты с таким id нет в базе данных
~~~
### примеры CRUD для Transaction:
запрос Get / Read:
http://localhost:8080/CleverBank/transaction?id=1
* где id - id транзакции
* ответ:
~~~
Transaction(id=1, date=2000-01-01T11:00, type=TransactionType(id=3, name=Пополнение счета), accountSender=Account(id=0, name=null, openingDate=null, balance=0.0, user=null, bank=null, currency=null), accountRecipient=Account(id=1, name=AS12 ASDG 1200 2132 ASDA 353A 2132, openingDate=1970-01-01T00:00, balance=10000.0, user=User(id=1, name=Сотников Кирилл Артёмович, login=kirill, password=password), bank=Bank(id=1, name=Clever-Bank), currency=Currency(id=1, name=BYN)), amount=5000.0)
~~~
запрос Post / Create:
http://localhost:8080/CleverBank/transaction?type=2&sender=10&recipient=1&amount=500
* где type - id типа транзакции
* sender - id счёта отправителя
* recipient - id счёта получателя
* amount - сумма платежа
* ответ:
~~~
Transaction(id=13, date=2023-09-04T01:18:17.102374100, type=TransactionType(id=2, name=Снятие средств), accountSender=Account(id=10, name=AS12 4306 1200 2132 DGSS 56SD 3233, openingDate=2007-04-15T00:00, balance=1200.0, user=User(id=10, name=Акрень Клим Дмитриевич, login=klim, password=klim), bank=Bank(id=1, name=Clever-Bank), currency=Currency(id=1, name=BYN)), accountRecipient=Account(id=1, name=AS12 ASDG 1200 2132 ASDA 353A 2132, openingDate=1970-01-01T00:00, balance=10000.0, user=User(id=1, name=Сотников Кирилл Артёмович, login=kirill, password=password), bank=Bank(id=1, name=Clever-Bank), currency=Currency(id=1, name=BYN)), amount=500.0)
~~~
запрос Put / Update:
http://localhost:8080/CleverBank/transaction?id=1&amount=5000
* где id - id транзакции
* amount - сумма на которую "перепишется" размер транзакции
* ответ:
~~~
Transaction(id=1, date=2000-01-01T11:00, type=TransactionType(id=3, name=Пополнение счета), accountSender=Account(id=0, name=null, openingDate=null, balance=0.0, user=null, bank=null, currency=null), accountRecipient=Account(id=1, name=AS12 ASDG 1200 2132 ASDA 353A 2132, openingDate=1970-01-01T00:00, balance=10000.0, user=User(id=1, name=Сотников Кирилл Артёмович, login=kirill, password=password), bank=Bank(id=1, name=Clever-Bank), currency=Currency(id=1, name=BYN)), amount=5000.0)
~~~
запрос Delete:
http://localhost:8080/CleverBank/transaction?id=13
* где id - id транзакции
* ответ:
~~~
Транзакция с id 13 удалена.
~~~
либо
~~~
Транзакций с таким id нет в базе данных
~~~
### примеры CRUD для TransactionType:
запрос Get / Read:
http://localhost:8080/CleverBank/transactionType?id=1
* где id - id типа транзакций
* ответ:
~~~
TransactionType(id=1, name=Перевод)
~~~
запрос Post / Create:
http://localhost:8080/CleverBank/transactionType?name=Деноминация
* где name - имя нового типа транзакций.
* ответ:
~~~
TransactionType(id=7, name=Деноминация)
~~~
запрос Put / Update:
http://localhost:8080/CleverBank/transactionType?id=7&name=Капитализация
* где id - id типа транзакции для изменения
* name - новое имя типа транзакции
* ответ:
~~~
TransactionType(id=7, name=Капитализация)
~~~
запрос Delete:
http://localhost:8080/CleverBank/transactionType?id=7
* где id - id типа транзакции для удаления
* ответ:
~~~
Вид транзакций с id 7 удален.
~~~
либо
~~~
Вида транзакций с таким id нет в базе данных
~~~
### примеры CRUD для User:
запрос Create / Read:
http://localhost:8080/CleverBank/user?id=1
* где id - id клиента
* ответ:
~~~
User(id=1, name=Сотников Кирилл Артёмович, login=kirill, password=password)
~~~
запрос Post / Create:
http://localhost:8080/CleverBank/user?name=Вася&login=vasia&password=1234
* где name - имя нового клиента
* login - логин клиента
* password - пароль клиента
* ответ:
~~~
User(id=36, name=Вася, login=vasia, password=1234)
~~~
запрос Put / Update:
http://localhost:8080/CleverBank/user?name=ДИМОООН&login=dima&password=321&id=36
* где name - новое имя клиента
* login - новый логин клиента
* password - новый пароль клиента
* id - id клиента
* ответ:
~~~
User(id=36, name=ДИМОООН, login=dima, password=321)
~~~
запрос Delete:
http://localhost:8080/CleverBank/user?id=36
* где id - id клиента
* ответ:
~~~
Клиент с id 36 удален.
~~~
либо
~~~
Клиента с таким id нет в базе данных
~~~

### Запрос для получения money-statement:
метод: GET
* пример запроса:
  http://localhost:8080/CleverBank/moneyStatement?accountName=AS12 ASDG 1200 2132 ASDA 353A 2132&fromDate=1970-01-01&toDate=2023-09-05
* где accountName - имя(номер) счёта / формат XXXX XXXX XXXX XXXX XXXX XXXX XXXX
* fromDate - дата начала операций по счёту / формат 0000-00-00 год-месяц цифрами-день
* toDate - дата окончания операций по счёту
* ответ
~~~
Ваш отчёт готов проверьте папку: apache-tomcat-10.1.13\bin\statement-money
~~~

PS: понимаю что логин/пароль пользователя лучше передавать на фронт в зашифрованном виде. К сожалению не остаётся времени разобраться как сделать это без Spring'а.

###### CПАСИБО ЗА ВНИМАНИЕ К МОЕМУ ПРОЕКТУ!!!
