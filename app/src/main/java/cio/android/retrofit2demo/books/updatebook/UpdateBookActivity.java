package cio.android.retrofit2demo.books.updatebook;

import android.os.Bundle;

import butterknife.OnClick;
import cio.android.retrofit2demo.R;
import cio.android.retrofit2demo.books.shared.BaseBookActivity;
import cio.android.retrofit2demo.di.Injector;
import cio.android.retrofit2demo.models.Book;

public class UpdateBookActivity extends BaseBookActivity
{
    public static final String EXTRA_BOOK = "EXTRA_BOOK";

    private UpdateBookPresenter presenter;

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate( savedInstanceState );

        presenter = new UpdateBookPresenter( this, Injector.provideBookService() );
        presenter.setBook( (Book) getIntent().getParcelableExtra( EXTRA_BOOK ) );

        configureLayout( presenter.getBook() );
    }

    private void configureLayout (Book book)
    {
        titleText.setText( book.getTitle() );
        authorText.setText( book.getAuthor() );
        numPagesText.setText( String.valueOf( book.getNumberOfPages() ) );
        descriptionText.setText( book.getDescription() );
    }

    @OnClick( R.id.save_book_fab)
    public void fabClicked()
    {
        presenter.updateBook( titleText.getText().toString());
    }

}
