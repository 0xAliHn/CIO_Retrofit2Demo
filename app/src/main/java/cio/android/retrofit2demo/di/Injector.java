package cio.android.retrofit2demo.di;

import android.support.v4.BuildConfig;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import cio.android.retrofit2demo.AdeptAndroid;
import cio.android.retrofit2demo.Constants;
import cio.android.retrofit2demo.data.BookService;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

import static okhttp3.logging.HttpLoggingInterceptor.Level.HEADERS;
import static okhttp3.logging.HttpLoggingInterceptor.Level.NONE;


public class Injector
{
    private static final String CACHE_CONTROL = "Cache-Control";
    private static final String VERSION = "version";
    private static final String USER_AGENT = "User-Agent";
    private static final String ADEPT_ANDROID_APP = "Adept-Android-App";

    private static Retrofit provideRetrofit (String baseUrl)
    {
        return new Retrofit.Builder()
                .baseUrl( baseUrl )
                .client( provideOkHttpClient() )
                .addConverterFactory( GsonConverterFactory.create() )
                .build();
    }

    private static OkHttpClient provideOkHttpClient ()
    {
        return new OkHttpClient.Builder()
                .addInterceptor( provideUrlAndHeaderInterceptor() )
                .addInterceptor( provideHttpLoggingInterceptor() )
                .addInterceptor( provideOfflineCacheInterceptor() )
                .addNetworkInterceptor( provideCacheInterceptor() )
                .cache( provideCache() )
                .build();
    }

    private static Cache provideCache ()
    {
        Cache cache = null;
        try
        {
            cache = new Cache( new File( AdeptAndroid.getInstance().getCacheDir(), "http-cache" ),
                               10 * 1024 * 1024 ); // 10 MB
        }
        catch (Exception e)
        {
            Timber.e( e, "Could not create Cache!" );
        }
        return cache;
    }

    private static HttpLoggingInterceptor provideHttpLoggingInterceptor ()
    {
        HttpLoggingInterceptor httpLoggingInterceptor =
                new HttpLoggingInterceptor( new HttpLoggingInterceptor.Logger()
                {
                    @Override
                    public void log (String message)
                    {
                        Timber.d( message );
                    }
                } );
        httpLoggingInterceptor.setLevel( BuildConfig.DEBUG ? HEADERS : NONE );
        return httpLoggingInterceptor;
    }

    private static Interceptor provideCacheInterceptor ()
    {
        return new Interceptor()
        {
            @Override
            public Response intercept (Chain chain) throws IOException
            {
                Response response = chain.proceed( chain.request() );

                // re-write response header to force use of cache
                CacheControl cacheControl = new CacheControl.Builder()
                        .maxAge( 2, TimeUnit.MINUTES )
                        .build();

                return response.newBuilder()
                        .header( CACHE_CONTROL, cacheControl.toString() )
                        .build();
            }
        };
    }

    public static Interceptor provideOfflineCacheInterceptor ()
    {
        return new Interceptor()
        {
            @Override
            public Response intercept (Chain chain) throws IOException
            {
                Request request = chain.request();

                if ( !AdeptAndroid.hasNetwork() )
                {
                    CacheControl cacheControl = new CacheControl.Builder()
                            .maxStale( 7, TimeUnit.DAYS )
                            .build();

                    request = request.newBuilder()
                            .cacheControl( cacheControl )
                            .build();
                }

                return chain.proceed( request );
            }
        };
    }

    private static Interceptor provideUrlAndHeaderInterceptor ()
    {
        return new Interceptor()
        {
            @Override
            public Response intercept (Chain chain) throws IOException
            {
                Request request = chain.request();
                HttpUrl url = request.url()
                                     .newBuilder()
                                     .addQueryParameter( VERSION, BuildConfig.VERSION_NAME )
                                     .build();

                Request.Builder builder = request.newBuilder().url( url );
                builder.addHeader( USER_AGENT, ADEPT_ANDROID_APP );

                return chain.proceed( builder.build() );
            }
        };
    }

    public static BookService provideBookService ()
    {
        return provideRetrofit( Constants.BASE_URL ).create( BookService.class );
    }

}
