# Yandex ToDo Application
### Made by [@ricardomilozio](https://t.me/ricardomilozio)

## Project *killer-features*
- Swiping both left & right
    - Swiping **left** to delete. Item will be removed from Recycler View. Trash icon will be drawn at the red background while swiping.
    - Swiping **right** to check. Item **isDone** param will be inverted & item will be recreated by the RecyclerView. Check box icon will be drawn at the green background while swiping.
- Drag & drop items behaviour in RecyclerView. User can change tasks order.
- Both **Day** and **Night** Themes supported
- **Responsive UI**
    - Text becomes strikethrough when the task is completed and vice versa
    - Fragment navigation implemented
- Dagger DI
- Room Database

## Homework [1] ✅
В моей реализации есть пара особенностей.
1. Дизайн не как на Figma. Но это нормально, Figma нужна была только для вдохновения)
2. Service package сделана по факту для мока данных. Пока LiveData лежит в VM. Но я это исправлю как подключу ROOM.
3. Навигация переживает поворот экрана, но данные во фрагменты нет. Вернее переживают, но не все. Тоже пофикшу при подключении ROOM.
4. TodoItemsRepository сделан для мока данных! Реализован как singleton. Но пока только мокает данные! Данные добавляются, recycler view перерисовывает, все классно, но LiveData живет в ViewModel. Просто поэкспериментировал с observable list в LiveData с помощью extensions. Поэтому и не вызывается метод в ItemsTodoRepository :)

## Homework [1] - refactoring
В соответствии с советами ревьювера (прекрасными советами, между прочим) сделал:
1. Переход на Flow
2. Баги в UI - теперь DatePickerDialog, а не вылезающий календарь
3. Переход в Clean Architecture
4. Дизайн по Figma
5. Collapsing Toolbar (с багом открытия после navigate Up - но пофикшу, обещаю...)
6. Diff Util для Recycler View

## Homework [2] ✅
Вышло очень косячно имхо. Все сделано, но есть баги в UI.
1. Таска при свайпе может уехать навечно за границы экрана -> причина: рассинхрон с сервером
2. Таски при изменении состояния (тот же свайп) начинают хаотично переставляться -> причина: попытка сортировки по дате изменения/создания от поздней к ранней. Но сервер возвращает в хаотичном порядке, из-за момента, когда показываются старые данные и еще не прогрузились новые, ресайклер начинает переставлять данные по алгоритму DiffUtil.
3. Колапсинг тулбар иногда не колапсинг...


Ретрофит я прикручивал в последний день (ночью), поэтому вся эта часть выглядит так, будто ее писал психопат, сорри...
Но! Есть и хорошие новости! Сделан Dagger DI (сидел разбирался после лекции). Реализована чистая архитектура (может, в коде она немного грязная...). Обработка данных вся на Kotlin Flow (раньше делал только на LiveData и State из Jetpack). Буду рад любым советам!


Если есть возможность, сделайте акцент, пожалуйста, на TaskRepository.kt
Я не совсем понимаю каким образом нам нужно синхронизировать задачи одновременно с бэкенда и с нашей БД. Сейчас сделана полумера - все сваливается в один Flow, считаются последние ревизии, у кого больше - тот победил.