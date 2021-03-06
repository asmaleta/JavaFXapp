# Task
* Заменить консольный клиент на клиент с графическим интерфейсом пользователя(GUI). 
В функционал клиента должно входить:

1. Окно с авторизацией/регистрацией.
2. Отображение текущего пользователя.
3. Таблица, отображающая все объекты из коллекции
а. Каждое поле объекта - отдельная колонка таблицы.
б. Строки таблицы можно фильтровать/сортировать по значениям любой из колонок. Сортировку и фильтрацию значений столбцов реализовать с помощью Streams API.
с. Поддержка всех команд из предыдущих лабораторных работ.
4. Область, визуализирующую объекты коллекции
а. Объекты должны быть нарисованы с помощью графических примитивов с использованием Graphics, Canvas или аналогичных средств графической библиотеки.
5. При визуализации использовать данные о координатах и размерах объекта.
6. Объекты от разных пользователей должны быть нарисованы разными цветами.
7. При нажатии на объект должна выводиться информация об этом объекте.
8. При добавлении/удалении/изменении объекта, он должен автоматически появиться/исчезнуть/измениться  на области как владельца, так и всех других клиентов. 
9. При отрисовке объекта должна воспроизводиться согласованная с преподавателем анимация.
10. Возможность редактирования отдельных полей любого из объектов (принадлежащего пользователю). Переход к редактированию объекта возможен из таблицы с общим списком объектов и из области с визуализацией объекта.
11.Возможность удаления выбранного объекта (даже если команды remove ранее не было).
Перед непосредственной разработкой приложения необходимо согласовать прототип интерфейса с преподавателем. Прототип интерфейса должен быть создан с помощью средства для построения прототипов интерфейсов(mockplus, draw.io, etc.)
