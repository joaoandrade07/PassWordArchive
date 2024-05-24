package com.joaoandrade.passwordarchive.Network.Response;

import com.squareup.moshi.Json;

public class EmpresaResponse {

    @Json(name = "logo")
    private final String logo;

    @Json(name = "name")
    private final String name;

    public EmpresaResponse(String logo, String name) {
        this.logo = logo;
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public String getName() {
        return name;
    }
}
