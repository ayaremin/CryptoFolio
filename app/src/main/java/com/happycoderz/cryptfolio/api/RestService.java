package com.happycoderz.cryptfolio.api;

import com.happycoderz.cryptfolio.models.Coin;
import com.happycoderz.cryptfolio.models.ICO;
import com.happycoderz.cryptfolio.models.IcoResult;
import io.reactivex.Observable;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RestService {

  @GET("v1") Observable<IcoResult> getICOs();

  @GET("ticker/?limit=0") Observable<List<Coin>> getCoins(
      @Query("convert") String currency
  );

  @GET("?time=3d&resolution=1h") Observable<ArrayList<ArrayList<Object>>> getGraphForCurrency(
      @Query("pair") String symbol, @Query("market") String market
  );
}

