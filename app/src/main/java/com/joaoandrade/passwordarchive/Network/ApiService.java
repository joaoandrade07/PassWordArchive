package com.joaoandrade.passwordarchive.Network;

import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class ApiService {

    private static SiteService INSTACE;

    public static SiteService getInstace(){
        if(INSTACE==null){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://autocomplete.clearbit.com/v1/")
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build();

            INSTACE = retrofit.create(SiteService.class);
        }

        return INSTACE;
    }
}
