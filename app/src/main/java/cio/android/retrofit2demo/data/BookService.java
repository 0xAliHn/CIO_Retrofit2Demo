package cio.android.retrofit2demo.data;

import java.util.List;
import java.util.Map;

import cio.android.retrofit2demo.models.Book;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BookService
{
    @GET( "books" )
    Call<List<Book>> getBooks();

    @GET( "books" )
    Call<List<Book>> search (@Query( "q" ) String query);

    @GET( "books/{id}" )
    Call<Book> getBook (@Path( "id" ) Long id);

    @POST( "books" )
    Call<Book> saveBook (@Body Book book);

    @PUT( "books/{id}" )
    Call<Book> updateBook (@Path( "id" ) Long id, @Body Book book);

    @DELETE( "books/{id}")
    Call<Void> deleteBook (@Path( "id" ) Long id);

    @PATCH( "books/{id}" )
    Call<Book> updateBookValue (@Path( "id" ) Long id, @Body Map<String, String> bookValue);

}
