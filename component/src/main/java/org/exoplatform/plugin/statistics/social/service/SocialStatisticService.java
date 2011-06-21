/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.exoplatform.plugin.statistics.social.service;

import java.util.List;
import org.exoplatform.plugin.statistics.social.service.dao.StatisticItemDAO;

/**
 * The Social Statistic Service is executed by a cron job, or a manual call and it is responsible of 
 * calculating the various statistics regarding eXo Social Usage.
 * 
 * This service is here to avoid complex live queries. So it stores the data as node in the JCR, we have multiple
 * types of Statistics:
 *  - Monthly
 *  - Weekly
 *  - Daily
 * Each on has it own folder.
 *
 * 
 * 
 * 
 *
 * @author tgrall
 */
public interface SocialStatisticService {

    public final static String STATISTIC_ITEM_NODE_TYPE = "exo:statisticItem";
    public final static String STATISTIC_WEEKLY_STATS_NODE_TYPE = "exo:activityStatsWeekly";
    
    
    public final static String STATISTIC_ITEM_TYPE_WEEK = "WEEK";
    public final static String STATISTIC_ITEM_TYPE_MONTH = "MONTH";
    public final static String STATISTIC_ITEM_TYPE_DAY = "DAY";
    
    
    public final String PARENT_FOLDER_WEEKLY_STATS = "WeeklyStats";
    public final String PARENT_FOLDER_MONTHLY_STATS = "MonthlyStats";
    public final String PARENT_FOLDER_DAILY_STATS = "DailyStats";
    
    
    // method used by the automatic jobs
    public long calculateDailyStatistics();    
    public long calculateWeeklyStatistics();
    public long calculateMonthlyStatistics();
        
    // manual methods
    public List calculateDailyStatistics(int year, int day);
    public List calculateWeeklyStatistics(int year, int week);
    public List calculateMonthlyStatistics(int year, int month);

    public List getDailyStatistics();
    public List getWeeklyStatistics();
    public List getMonthlyStatistics();
    
    public StatisticItemDAO getTotalActivities();
    public List getStatistics(int type, int page, int pageSize);
    

    // utility method
    public List validateMonthlyStatisticList(int numberOfMonth);    
    public List validateWeeklyStatisticList(int numberOfWeek);
    public List validateDailyStatisticList(int numberOfDay);
    
    public List deleteStatistic(int type, int year, int idInYear);
    
    
    // internal method, should be pushed in tests
    public void updateActivitiesTestData();    
    
}
