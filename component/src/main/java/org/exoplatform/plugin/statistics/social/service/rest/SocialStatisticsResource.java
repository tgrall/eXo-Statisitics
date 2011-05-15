/*
 * Copyright (C) 2003-2011 eXo Platform SAS.
 * 
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 * 
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 *
 */
package org.exoplatform.plugin.statistics.social.service.rest;

import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.rest.resource.ResourceContainer;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.DELETE;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.plugin.statistics.social.service.SocialStatisticService;
import org.exoplatform.plugin.statistics.social.service.model.StatisticInterval;

@Path("social-statistics")
public class SocialStatisticsResource implements ResourceContainer {
    
    private static final Log log = ExoLogger.getLogger(SocialStatisticsResource.class);

    @GET
    @Path("/activities")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTotalStatistics()  {
        CacheControl cacheControl = new CacheControl();
        cacheControl.setMaxAge(10);
        MessageBean data = new MessageBean();
        List dataContent = new ArrayList();
        dataContent.add(this.getStatisticService().getTotalActivities());
        data.setData(dataContent);
        return Response.ok(data, MediaType.APPLICATION_JSON).cacheControl(cacheControl).build();

    }

    /*
    @GET
    @Path("/activities/weekly/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getWeeklyStatistics() {
    CacheControl cacheControl = new CacheControl();
    cacheControl.setNoCache(true);
    cacheControl.setNoStore(true);
    MessageBean data = new MessageBean();
    data.setData(this.getStatisticService().getWeeklyStatistics());
    return Response.ok(data, MediaType.APPLICATION_JSON).cacheControl(cacheControl).build();
    
    }
    
     * 
     */
    @GET
    @Path("/activities/weekly")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getWeeklyStatisticsByPage(
            @QueryParam("page") int page) {
        CacheControl cacheControl = new CacheControl();
        cacheControl.setNoCache(true);
        cacheControl.setNoStore(true);
        MessageBean data = new MessageBean();

        
        if (page == -1) {
            data.setData(this.getStatisticService().getWeeklyStatistics());
        } else {
            data.setData(this.getStatisticService().getStatistics(StatisticInterval.TYPE_WEEK, page, 5));
        }
        return Response.ok(data, MediaType.APPLICATION_JSON).cacheControl(cacheControl).build();

    }

    @GET
    @Path("/activities/monthly/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMonthlyStatistics() {
        CacheControl cacheControl = new CacheControl();
        cacheControl.setNoCache(true);
        cacheControl.setNoStore(true);
        MessageBean data = new MessageBean();
        data.setData(this.getStatisticService().getMonthlyStatistics());
        return Response.ok(data, MediaType.APPLICATION_JSON).cacheControl(cacheControl).build();

    }

    private Response deleteStatistics(int year, int idInYear, String type) {

        // issue with
        //   @DELETE
        //   @Path("/activities/{type}/{year}/{id}")

        CacheControl cacheControl = new CacheControl();
        cacheControl.setNoCache(true);
        cacheControl.setNoStore(true);


        MessageBean data = new MessageBean();
        int typeAsInt = 0;

        if (type.equalsIgnoreCase("daily")) {
            typeAsInt = StatisticInterval.TYPE_DAY;
        } else if (type.equalsIgnoreCase("weekly")) {
            typeAsInt = StatisticInterval.TYPE_WEEK;
        } else if (type.equalsIgnoreCase("monthly")) {
            typeAsInt = StatisticInterval.TYPE_MONTH;
        }

        data.setData(this.getStatisticService().deleteStatistic(typeAsInt, year, idInYear));

        return Response.ok(data, MediaType.APPLICATION_JSON).cacheControl(cacheControl).build();
    }

    @DELETE
    @Path("/activities/daily/{year}/{day}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteDayStatistics(
            @PathParam("year") int year,
            @PathParam("day") int day) {
        Response response = this.deleteStatistics(year, day, "daily");
        return response;
    }

    @DELETE
    @Path("/activities/monthly/{year}/{month}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteMonthStatistics(
            @PathParam("year") int year,
            @PathParam("month") int month) {
        Response response = this.deleteStatistics(year, month, "monthly");
        return response;
    }

    @POST
    @Path("/activities/monthly/{year}/{month}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response setMonthStatistics(
            @PathParam("year") int year,
            @PathParam("month") int month) {

        CacheControl cacheControl = new CacheControl();
        cacheControl.setNoCache(true);
        cacheControl.setNoStore(true);


        MessageBean data = new MessageBean();
        data.setData(this.getStatisticService().calculateMonthlyStatistics(year, month));

        return Response.ok(data, MediaType.APPLICATION_JSON).cacheControl(cacheControl).build();

    }

    @DELETE
    @Path("/activities/weekly/{year}/{week}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteWeekStatistics(
            @PathParam("year") int year,
            @PathParam("week") int week) {
        Response response = this.deleteStatistics(year, week, "weekly");
        return response;

    }

    @POST
    @Path("/activities/weekly/{year}/{week}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response setWeekStatistics(
            @PathParam("year") int year,
            @PathParam("week") int week) {
        CacheControl cacheControl = new CacheControl();
        cacheControl.setNoCache(true);
        cacheControl.setNoStore(true);
        MessageBean data = new MessageBean();
        data.setData(this.getStatisticService().calculateWeeklyStatistics(year, week));
        return Response.ok(data, MediaType.APPLICATION_JSON).cacheControl(cacheControl).build();
    }

    @POST
    @Path("/activities/daily/{year}/{day}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response setDayStatistics(
            @PathParam("year") int year,
            @PathParam("day") int day) {
        CacheControl cacheControl = new CacheControl();
        cacheControl.setNoCache(true);
        cacheControl.setNoStore(true);
        MessageBean data = new MessageBean();
        data.setData(this.getStatisticService().calculateDailyStatistics(year, day));
        return Response.ok(data, MediaType.APPLICATION_JSON).cacheControl(cacheControl).build();
    }

    @GET
    @Path("/activities/{type}/validate")
    @Produces(MediaType.APPLICATION_JSON)
    public Response validateStatistics(
            @QueryParam("nb") int numberOfLoop,
            @PathParam("type") String type) {
        CacheControl cacheControl = new CacheControl();
        cacheControl.setNoCache(true);
        cacheControl.setNoStore(true);
        List data = new ArrayList();
        if (type.equalsIgnoreCase("monthly")) {
            data = this.getStatisticService().validateMonthlyStatisticList(numberOfLoop);
        } else if (type.equalsIgnoreCase("weekly")) {
            data = this.getStatisticService().validateWeeklyStatisticList(numberOfLoop);
        }

        MessageBean result = new MessageBean();
        result.setData(data);
        return Response.ok(result, MediaType.APPLICATION_JSON).cacheControl(cacheControl).build();

    }

    @GET
    @Path("/activities/test-data")
    @Produces(MediaType.APPLICATION_JSON)
    public Response testData() {
        CacheControl cacheControl = new CacheControl();
        cacheControl.setNoCache(true);
        cacheControl.setNoStore(true);

        this.getStatisticService().updateActivitiesTestData();
        
        
        MessageBean result = new MessageBean();
        result.setData(null);
        return Response.ok(result, MediaType.APPLICATION_JSON).cacheControl(cacheControl).build();

    }

    private SocialStatisticService getStatisticService() {
        ExoContainer containerContext = ExoContainerContext.getCurrentContainer();
        return (SocialStatisticService) containerContext.getComponentInstanceOfType(SocialStatisticService.class);
    }
}
