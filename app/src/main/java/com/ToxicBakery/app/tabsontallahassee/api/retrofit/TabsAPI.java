package com.ToxicBakery.app.tabsontallahassee.api.retrofit;

import com.ToxicBakery.app.tabsontallahassee.models.BillDataWrapper;
import com.ToxicBakery.app.tabsontallahassee.models.BillPagedList;
import com.ToxicBakery.app.tabsontallahassee.models.MembershipDataWrapper;
import com.ToxicBakery.app.tabsontallahassee.models.OrganizationDetailPagedList;
import com.ToxicBakery.app.tabsontallahassee.models.PersonDataWrapper;
import com.ToxicBakery.app.tabsontallahassee.models.PersonPagedList;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface TabsAPI {

    @GET("bills/?format=vnd.api%2Bjson")
    Observable<BillPagedList> getBillsList(@Query("page") int page);

    @GET("people/?format=vnd.api%2Bjson")
    Observable<PersonPagedList> getPersonList(@Query("page") int page);

    @GET("{uuid}/?format=vnd.api%2Bjson")
    Observable<PersonDataWrapper> getPersonDetailed(@Path("uuid") String uuid);

    @GET("organizations/?format=vnd.api%2Bjson")
    Observable<OrganizationDetailPagedList> getOrganizationDetailList(@Query("page") int page);

    @GET("{uuid}/?format=vnd.api%2Bjson")
    Observable<MembershipDataWrapper> getMembership(@Path("uuid") String uuid);

    @GET("{uuid}/?format=vnd.api%2Bjson")
    Observable<BillDataWrapper> getBill(@Path("uuid") String uuid);

}
