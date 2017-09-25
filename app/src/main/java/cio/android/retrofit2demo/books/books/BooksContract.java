package cio.android.retrofit2demo.books.books;

import java.util.List;

import cio.android.retrofit2demo.models.Book;

/**
 * The contract between the view and presenter
 */
public interface BooksContract
{

    interface View
    {

        void showBooks (List<Book> books);

        void showErrorMessage ();

        void refresh ();
    }

}
