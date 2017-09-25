package cio.android.retrofit2demo.books.addbook;

import java.io.IOException;

import cio.android.retrofit2demo.books.shared.BookContract;
import cio.android.retrofit2demo.data.BookService;
import cio.android.retrofit2demo.models.Book;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class AddBookPresenter
{
    private final BookContract.View bookView;
    private final BookService service;

    public AddBookPresenter (BookContract.View bookView, BookService service)
    {
        this.bookView = bookView;
        this.service = service;
    }

    public void saveBook (String title, String author, String numPages, String description)
    {
        Book book = new Book( title, author, Integer.parseInt( numPages ), description );

        service.saveBook( book ).enqueue( new Callback<Book>()
        {
            @Override
            public void onResponse (Call<Book> call, Response<Book> response)
            {
                if ( response.isSuccessful() )
                {
                    bookView.showBook( response.body() );
                    Timber.i( "Book data was successfully saved to the API." );
                }
                else
                {
                    try
                    {
                        Timber.i( "The response failed: %s", response.errorBody().string() );
                        bookView.showErrorMessage();
                    }
                    catch ( IOException ignored )
                    {
                        // no op
                    }
                }
            }

            @Override
            public void onFailure (Call<Book> call, Throwable t)
            {
                bookView.showErrorMessage();
                Timber.e( t, "Unable to save the book data to the API." );
            }
        } );
    }
}