package algonquin.cst2335.finalprojectassignment;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    private static Retrofit retrofit = null;

    static Retrofit getClient() {

        retrofit = new Retrofit.Builder()
                .baseUrl("https://owlbot.info/api/v4/dictionary/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }

}
