package com.joaoandrade.passwordarchive.Network;

import com.joaoandrade.passwordarchive.Network.Response.EmpresaResponse;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SiteService {
    @GET("companies/suggest")
    Call<List<EmpresaResponse>> obterLogo(@Query("query") String nomeEmpresa);
}
