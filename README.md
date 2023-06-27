# Yandex ToDo Application
### Made by [@ricardomilozio](https://t.me/ricardomilozio)

## Project *killer-features*
- Swiping both left & right
    - Swiping **left** to delete. Item will be removed from Recycler View. Trash icon will be drawn at the red background while swiping.
    - Swiping **right** to check. Item **isDone** param will be inverted & item will be recreated by the RecyclerView. Check box icon will be drawn at the green background while swiping.
- Drag & drop items behaviour in RecyclerView. User can change tasks order.
- Both **Day** and **Night** Themes supported
- **Responsive UI**
    - CalendarView is shown depending on the status of the deadline Switch. Scaling of other UI components is done automatically.
    - Text becomes strikethrough when the task is completed and vice versa
    - Fragment navigation implemented

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


Что спёр (позаимствовал) из репозитория: экстеншены для вьюх, утилитные методы для преобразования даты в корректный формат, организация неймспейсов.
Надеюсь, что это не особо критично...
