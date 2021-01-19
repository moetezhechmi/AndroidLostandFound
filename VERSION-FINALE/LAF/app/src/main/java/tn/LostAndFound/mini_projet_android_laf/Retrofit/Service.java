package tn.LostAndFound.mini_projet_android_laf.Retrofit;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import tn.LostAndFound.mini_projet_android_laf.models.AddChatResponse;
import tn.LostAndFound.mini_projet_android_laf.models.AddCommentResponse;
import tn.LostAndFound.mini_projet_android_laf.models.GetCommentsResponse;
import tn.LostAndFound.mini_projet_android_laf.models.GetMesPubResponse;
import tn.LostAndFound.mini_projet_android_laf.models.GetPubResponse;

public interface Service {

    @POST("users/signIn")
    @FormUrlEncoded
    Observable<String> loginUser(@Field("email")String email,
                                 @Field("password")String password
    );

    @POST("users/signUp")
    @Multipart
    Observable<String> RegisterUser(@Part("firstName") RequestBody firstName,
                                    @Part("lastName") RequestBody lastName,
                                    @Part("email") RequestBody email,
                                    @Part("password") RequestBody password,
                                    @Part("phone")RequestBody phone,
                                    @Part MultipartBody.Part image, @Part("upload") RequestBody name

                                    );
    @POST("publications/AddPublication")
    @Multipart
    Observable<String> AjoutPub(@Part("title") RequestBody title,
                                @Part("textPub") RequestBody textPub,
                                @Part("ownerId") RequestBody ownerId,
                                @Part("rue") RequestBody rue,
                                @Part("ville") RequestBody ville,
                                @Part("gouvernaurat") RequestBody gouvernaurat,
                                @Part("latitude") RequestBody latitude,
                                @Part("longitude") RequestBody longitude,
                                @Part("nom")RequestBody nom,
                                @Part MultipartBody.Part image, @Part("file") RequestBody name
    );
    @Multipart
    @POST("/upload")
    Call<ResponseBody> postImage(@Part MultipartBody.Part image, @Part("upload") RequestBody name);


    @POST("users/updateUser")
    @Multipart
    Observable<String> UpdateUser(@Part("userId") RequestBody userId,
                                  @Part("firstName") RequestBody firstName,
                                  @Part("lastName") RequestBody lastName,
                                  @Part("email") RequestBody email,
                                  @Part("phone")RequestBody phone,
                                  @Part("oldPassword")RequestBody oldPassword,
                                  @Part("newPassword")RequestBody newPassword

                                  //  @Part RequestBody image, @Part("upload") MultipartBody.Part name


    );

    @GET("publications/getAllPub")
    Observable<GetPubResponse> getPub();


    @POST("publications/addLikeToPublication")
    @FormUrlEncoded
    Observable<String> AddLikeToPub(@Field("publicationId") String publicationId,
                                    @Field("userId") String userId
    );

    @POST("publications/dislikePublication")
    @FormUrlEncoded
    Observable<String> dislikeLikeToPub(@Field("publicationId") String publicationId,
                                    @Field("userId") String userId
    );



    @POST("publications/getListLikesByPublicati")
    @FormUrlEncoded
    Observable<String> getListLikesByPublication(@Field("publicationId") String publicationId
    );



    @POST("publications/addComment")
    @Multipart
    Observable<String> addComment(@Part("publicationId") RequestBody publicationId,
                                  @Part("userId") RequestBody userId,
                                  @Part("textPub") RequestBody textPub
    );

    @POST("publications/deletePublication")
    @FormUrlEncoded
    Observable<String> deletePublication(@Field("publicationId") String publicationId

    );
    @POST("publications/addComment")
    @FormUrlEncoded
    Observable<AddCommentResponse> addComment(@Field("publicationId") String publicationId,
                                              @Field("userId") String userId,
                                              @Field("textPub") String textPub
    );
    @POST("publications/getCommentsByPublication")
    @FormUrlEncoded
    Observable<GetCommentsResponse> getCommentsByPublication(@Field("publicationId") String publicationId

    );

    @POST("publications/deleteComment")
    @FormUrlEncoded
    Observable<String> deleteComment(@Field("publicationId") String publicationId,
                                     @Field("commentId") String commentId


    );

    @POST("publications/getPubByUser")
    @FormUrlEncoded
    Observable<GetMesPubResponse> getAllPublicationsByOwner(@Field("ownerId") String ownerId

    );

    @POST("chats/addRoom")
    @FormUrlEncoded
    Observable<AddChatResponse> addRoom(@Field("user1") String user1,
                                        @Field("user2") String user2,
                                        @Field("idRoom") String idRoom
    );





}
