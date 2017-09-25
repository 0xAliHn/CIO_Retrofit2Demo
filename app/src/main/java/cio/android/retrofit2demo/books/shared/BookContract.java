package cio.android.retrofit2demo.books.shared;

import cio.android.retrofit2demo.models.Book;

/**
 * The contract between the view and presenter
 */
public interface BookContract
{
    interface View
    {
        void showErrorMessage ();

        void showBook (Book book);
    }

}
