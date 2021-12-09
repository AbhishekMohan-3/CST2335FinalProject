package algonquin.cst2335.finalprojectassignment;

import retrofit2.Call;
import retrofit2.http.GET;

import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIInterface {
    @GET("{name}")
    Call<APIResponse> getResponse(@Header("Authorization") String token , @Path("name") String query);
}

