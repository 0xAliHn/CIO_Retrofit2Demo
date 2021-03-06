package cio.android.retrofit2demo.books.book;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cio.android.retrofit2demo.R;
import cio.android.retrofit2demo.books.shared.BookContract;
import cio.android.retrofit2demo.books.updatebook.UpdateBookActivity;
import cio.android.retrofit2demo.di.Injector;
import cio.android.retrofit2demo.models.Book;
import timber.log.Timber;

public class BookActivity extends AppCompatActivity implements BookContract.View
{
    public static final String EXTRA_BOOK_ID = "EXTRA_BOOK_ID";

    @BindView( R.id.titleText )
    TextView titleText;

    @BindView( R.id.authorText )
    TextView authorText;

    @BindView( R.id.numPagesText )
    TextView numPagesText;

    @BindView( R.id.descriptionText )
    TextView descriptionText;

    private Book book;

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_book );

        ButterKnife.bind( this );

        BookPresenter bookPresenter = new BookPresenter( this, Injector.provideBookService() );
        bookPresenter.retrieveBook(getIntent().getLongExtra(EXTRA_BOOK_ID, 1));

        configureLayout();
    }

    @Override
    public void showBook (Book book)
    {
        this.book = book;

        titleText.setText( book.getTitle() );
        authorText.setText( book.getAuthor() );
        numPagesText.setText( String.format( Locale.getDefault(), "%d pages", book.getNumberOfPages() ) );
        descriptionText.setText( book.getDescription() );
    }

    @Override
    public void showErrorMessage ()
    {
        Toast.makeText( this, R.string.book_loading_unsuccessful, Toast.LENGTH_SHORT).show();
    }

    private void configureLayout ()
    {
        setSupportActionBar( (Toolbar) ButterKnife.findById( this, R.id.toolbar ) );
    }

    @OnClick( R.id.edit_book_fab)
    public void fabClicked()
    {
        Timber.d( "edit book with id: %d", this.book.getId() );

        Intent intent = new Intent( BookActivity.this, UpdateBookActivity.class );
        intent.putExtra( UpdateBookActivity.EXTRA_BOOK, this.book );
        startActivity(intent);

        finish();
    }

}
