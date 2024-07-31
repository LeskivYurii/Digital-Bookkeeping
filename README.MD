Problem:
A local library wants to switch to digital book control. You
need to implement a web application for them. Librarians
should be able to register readers, issue them books, and release books (after the reader returns
the book back to the library).

Functionality:
1) Pages for adding, editing, and deleting a person.
2) Pages for adding, editing, and deleting a book
3) Page with a list of all people (people are clickable - clicking takes you to the person's page).
4) Page with a list of all books (books are clickable - clicking takes you to the book's page).
5) A person's page, which shows the values of their fields and a list of the books they have taken. If the person
has not taken any books, the text "The person has not taken any books yet" should be displayed instead of the list.
6) A book page, which shows the values of the fields of this book and the name of the person who has taken this book.
If this book has not been taken by anyone, the text "This book is free" should be displayed.
7) On the book page, if the book is taken by a person, there should be a button "Release book" next to their name. 
This button is pressed by the librarian when the reader returns this book back to the library. After pressing this button,
the book becomes free again and disappears from the person's list of books.
8) On the book page, if the book is free, there should be a drop-down list (<select>)
with all the people and a button "Assign book". This button is pressed by the librarian
when the reader wants to take this book home. After pressing this button, the book
should start to belong to the selected person and should appear in his list of
books.
9) All fields are be validated - using @Valid and Spring Validator, if
required.