package WolfPack.OrderGeneratorService;

import java.io.IOException;
import okhttp3.*;


public class Registration{
    //registering to the clock service
    public static void postRegistration() throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
        .build();
      MediaType mediaType = MediaType.parse("application/json");
      RequestBody body = RequestBody.create(mediaType, "{ \"name\" : \"OrderGenerate\", \"uri\" : \"http://localhost:8083/generate\"}");
      Request request = new Request.Builder()
        .url("http://localhost:9000/registry")
        .method("POST", body)
        .addHeader("Content-Type", "application/json")
        .build();
      Response response = client.newCall(request).execute();
      System.out.println(response);
    }
};