package cio.android.retrofit2demo.books.addbook;

import android.os.Bundle;

import butterknife.OnClick;
import cio.android.retrofit2demo.R;
import cio.android.retrofit2demo.books.shared.BaseBookActivity;
import cio.android.retrofit2demo.di.Injector;


public class AddBookActivity extends BaseBookActivity
{
    private AddBookPresenter presenter;

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate( savedInstanceState );

        presenter = new AddBookPresenter( this, Injector.provideBookService() );
    }

    @OnClick( R.id.save_book_fab)
    public void fabClicked()
    {
        presenter.saveBook( titleText.getText().toString(),
                            authorText.getText().toString(),
                            numPagesText.getText().toString(),
                            descriptionText.getText().toString() );
    }

}
