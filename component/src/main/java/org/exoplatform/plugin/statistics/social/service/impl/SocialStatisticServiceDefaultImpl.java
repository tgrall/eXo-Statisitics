/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.exoplatform.plugin.statistics.social.service.impl;

import java.util.ArrayList;
import javax.jcr.RepositoryException;
import org.exoplatform.container.xml.InitParams;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;

import org.chromattic.api.ChromatticSession;
import org.exoplatform.commons.chromattic.ChromatticLifeCycle;
import org.exoplatform.commons.chromattic.ChromatticManager;


import org.exoplatform.plugin.statistics.social.service.SocialStatisticService;
import org.exoplatform.plugin.statistics.social.service.dao.StatisticItemDAO;
import org.exoplatform.plugin.statistics.social.service.model.ActivityStatsWeekly;
import org.exoplatform.plugin.statistics.social.service.model.SocialStatistic;
import org.exoplatform.plugin.statistics.social.service.model.StatisticInterval;
import org.exoplatform.services.jcr.impl.core.query.QueryImpl;

import org.picocontainer.Startable;

/**
 *
 * @author tgrall
 */
public class SocialStatisticServiceDefaultImpl implements SocialStatisticService, Startable {

    //TODO : add management capabilities: JMX about execution time, reset & so on
    // these should also be exposed as REST
    //TODO : review logging and support debug/info to trace data
    private static final Log log = ExoLogger.getLogger(SocialStatisticServiceDefaultImpl.class);
    private final ChromatticLifeCycle lifeCycle;

    public SocialStatisticServiceDefaultImpl(InitParams params, ChromatticManager manager) throws Exception {
        ChromatticLifeCycle lifeCycle = manager.getLifeCycle("social-statistics");
        this.lifeCycle = lifeCycle;

    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
    }

    /**
     * This total number of stats is based on the aggregation of weekly stats.
     * For performance reason I want to avoid querying the full exo:activities object.
     * 
     * This means that all the weeks must be calculated to allow correct numbers
     * 
     * @return the total number of activities based on the weekly stats. 
     */
    @Override
    public StatisticItemDAO getTotalActivities() {
        StatisticItemDAO statisticItemDAO = new StatisticItemDAO();

        long numberOfItems = 0;
        String parentFolder = PARENT_FOLDER_WEEKLY_STATS;
        String sumString = null;

        long startTime = System.currentTimeMillis();
        List<StatisticItemDAO> results = new ArrayList();
        log.info("==== Start Total Activities Stats  ");
        try {
            ChromatticSession session = lifeCycle.getChromattic().openSession();

            // do not know how to control the order by with the
            // Chromattic query builder so I use the standard QueryManager
            String countActivitiesQuery = "select * from exo:statisticItem where "
                    + "jcr:path like '/exo:applications/Social_Statistics/" + parentFolder + "/%' "
                    + "order by startDate asc ";
            QueryManager qm = session.getJCRSession().getWorkspace().getQueryManager();
            Query queryJCR = qm.createQuery(countActivitiesQuery, Query.SQL);
            QueryResult result = queryJCR.execute();
            NodeIterator nodeIterator = result.getNodes();
            while (nodeIterator.hasNext()) {
                Node statItem = nodeIterator.nextNode();

                // store the first date as the start of the stat
                // so only the first one
                if (statisticItemDAO.getStartDate() == null) {
                    statisticItemDAO.setStartDate(statItem.getProperty(SocialStatistic.FIELD_START_DATE).getDate().getTime());
                }

                statisticItemDAO.setEndDate(statItem.getProperty(SocialStatistic.FIELD_END_DATE).getDate().getTime());
                numberOfItems = numberOfItems + statItem.getProperty(SocialStatistic.FIELD_VALUE_COUNT).getLong();
                statisticItemDAO.setValue(numberOfItems);
                statisticItemDAO.setElapsedTime(System.currentTimeMillis() - startTime);
                statisticItemDAO.setExecutionDate(new Date());

                sumString = sumString + " + " + statItem.getProperty(SocialStatistic.FIELD_VALUE_COUNT).getLong();

            }


        } catch (Exception e) {
            log.error(e);
        } finally {
        }
        log.info("==== End Total Activities Stats (" + (System.currentTimeMillis() - startTime) + "ms)");

        return statisticItemDAO;

    }

    public List getStatistics(int type, int page, int pageSize) {
        String parentFolder = "";
        String typeLabel = "";
        if (type == StatisticInterval.TYPE_MONTH) {
            parentFolder = PARENT_FOLDER_MONTHLY_STATS;
            typeLabel = StatisticInterval.TYPE_MONTH_TEXT;
        } else if (type == StatisticInterval.TYPE_WEEK) {
            parentFolder = PARENT_FOLDER_WEEKLY_STATS;
            typeLabel = StatisticInterval.TYPE_WEEK_TEXT;
        } else if (type == StatisticInterval.TYPE_DAY) {
            parentFolder = PARENT_FOLDER_DAILY_STATS;
            typeLabel = StatisticInterval.TYPE_DAY_TEXT;
        }


        long startTime = System.currentTimeMillis();
        List<StatisticItemDAO> results = new ArrayList();
        log.info("==== Start Return Stats  ");
        try {
            ChromatticSession session = lifeCycle.getChromattic().openSession();

            // do not know how to control the order by with the
            // Chromattic query builder so I use the standard QueryManager
            String countActivitiesQuery = "select * from exo:statisticItem where "
                    + "jcr:path like '/exo:applications/Social_Statistics/" + parentFolder + "/%' "
                    + "order by startDate desc ";
            QueryManager qm = session.getJCRSession().getWorkspace().getQueryManager();


            QueryImpl queryJCR = (QueryImpl) qm.createQuery(countActivitiesQuery, Query.SQL);
            // the first query page is 0, so we should remove 1
            int offSet = (page - 1) * pageSize;
            queryJCR.setOffset(offSet);
            queryJCR.setLimit(pageSize);

            QueryResult result = queryJCR.execute();
            NodeIterator nodeIterator = result.getNodes();
            while (nodeIterator.hasNext()) {
                Node statItem = nodeIterator.nextNode();
                StatisticItemDAO itemDAO = new StatisticItemDAO();
                itemDAO.setType(typeLabel);
                itemDAO.setId(statItem.getProperty(SocialStatistic.FIELD_ID_IN_YEAR).getLong());
                itemDAO.setYear(statItem.getProperty(SocialStatistic.FIELD_YEAR).getLong());
                itemDAO.setStartDate(statItem.getProperty(SocialStatistic.FIELD_START_DATE).getDate().getTime());
                itemDAO.setEndDate(statItem.getProperty(SocialStatistic.FIELD_END_DATE).getDate().getTime());
                itemDAO.setValue(statItem.getProperty(SocialStatistic.FIELD_VALUE_COUNT).getLong());

                results.add(itemDAO);
            }



        } catch (Exception e) {
            log.error(e);
        } finally {
        }
        log.info("==== End Start Return Stats (" + (System.currentTimeMillis() - startTime) + "ms)");

        return results;

    }

    public List getStatistics(int type) {

        String parentFolder = "";
        String typeLabel = "";
        if (type == StatisticInterval.TYPE_MONTH) {
            parentFolder = PARENT_FOLDER_MONTHLY_STATS;
            typeLabel = StatisticInterval.TYPE_MONTH_TEXT;
        } else if (type == StatisticInterval.TYPE_WEEK) {
            parentFolder = PARENT_FOLDER_WEEKLY_STATS;
            typeLabel = StatisticInterval.TYPE_WEEK_TEXT;
        } else if (type == StatisticInterval.TYPE_DAY) {
            parentFolder = PARENT_FOLDER_DAILY_STATS;
            typeLabel = StatisticInterval.TYPE_DAY_TEXT;
        }


        long startTime = System.currentTimeMillis();
        List<StatisticItemDAO> results = new ArrayList();
        log.info("==== Start Return Stats  ");
        try {
            ChromatticSession session = lifeCycle.getChromattic().openSession();

            // do not know how to control the order by with the
            // Chromattic query builder so I use the standard QueryManager
            String countActivitiesQuery = "select * from exo:statisticItem where "
                    + "jcr:path like '/exo:applications/Social_Statistics/" + parentFolder + "/%' "
                    + "order by startDate desc ";
            QueryManager qm = session.getJCRSession().getWorkspace().getQueryManager();
            Query queryJCR = qm.createQuery(countActivitiesQuery, Query.SQL);
            QueryResult result = queryJCR.execute();
            NodeIterator nodeIterator = result.getNodes();
            while (nodeIterator.hasNext()) {
                Node statItem = nodeIterator.nextNode();
                StatisticItemDAO itemDAO = new StatisticItemDAO();
                itemDAO.setType(typeLabel);
                itemDAO.setId(statItem.getProperty(SocialStatistic.FIELD_ID_IN_YEAR).getLong());
                itemDAO.setYear(statItem.getProperty(SocialStatistic.FIELD_YEAR).getLong());
                itemDAO.setStartDate(statItem.getProperty(SocialStatistic.FIELD_START_DATE).getDate().getTime());
                itemDAO.setEndDate(statItem.getProperty(SocialStatistic.FIELD_END_DATE).getDate().getTime());
                itemDAO.setValue(statItem.getProperty(SocialStatistic.FIELD_VALUE_COUNT).getLong());
                results.add(itemDAO);
            }


        } catch (Exception e) {
            log.error(e);
        } finally {
        }
        log.info("==== End Start Return Stats (" + (System.currentTimeMillis() - startTime) + "ms)");

        return results;

    }

    public List getDailyStatistics() {
        return getStatistics(StatisticInterval.TYPE_DAY);
    }

    public List getWeeklyStatistics() {
        return getStatistics(StatisticInterval.TYPE_WEEK);
    }

    public List getMonthlyStatistics() {
        return getStatistics(StatisticInterval.TYPE_MONTH);
    }

    // TODO : check multi threading
    public long calculateDailyStatistics() {
        long numberOfItems = -1;
        long startTime = System.currentTimeMillis();
        log.info("==== Start of the Daily stats calculation ");
        try {
            ChromatticSession session = lifeCycle.getChromattic().openSession();
            // Aggregate last week data
            Calendar cal = Calendar.getInstance();
            SocialStatistic statItem = this.setDayStat(session, cal.get(Calendar.YEAR), cal.get(Calendar.DAY_OF_YEAR), false);
            session.save();

        } catch (Exception e) {
            log.error(e);
        } finally {
        }
        log.info("==== End of the Daily stats calculation (" + (System.currentTimeMillis() - startTime) + "ms)");
        return numberOfItems;
    }

    public List calculateDailyStatistics(int year, int day) {
        List<StatisticItemDAO> results = new ArrayList();
        long numberOfItems = -1;
        long startTime = System.currentTimeMillis();
        log.info("==== Start of the Daily stats calculation (Manual Call : " + year + "-" + day + ")");
        try {
            ChromatticSession session = lifeCycle.getChromattic().openSession();
            // Aggregate last week data
            SocialStatistic statItem = this.setDayStat(session, year, day, true);
            results.add(this.getStatisticDAO(statItem, StatisticInterval.TYPE_DAY));
            session.save();


        } catch (Exception e) {
            log.error(e);
        } finally {
        }
        log.info("==== Start of the Daily stats calculation (Manual Call : " + year + "-" + day + ")(" + (System.currentTimeMillis() - startTime) + "ms)");

        return results;
    }

    public List calculateWeeklyStatistics(int year, int week) {
        List<StatisticItemDAO> results = new ArrayList();
        long numberOfItems = -1;
        long startTime = System.currentTimeMillis();
        log.info("==== Start of the Weekly stats calculation (Manual Call : " + year + "-" + week + ")");
        try {
            ChromatticSession session = lifeCycle.getChromattic().openSession();
            // Aggregate last week data
            SocialStatistic statItem = this.setWeekStat(session, year, week, true);
            results.add(this.getStatisticDAO(statItem, StatisticInterval.TYPE_WEEK));
            session.save();


        } catch (Exception e) {
            log.error(e);
        } finally {
        }
        log.info("==== Start of the Weekly stats calculation (Manual Call : " + year + "-" + week + ")(" + (System.currentTimeMillis() - startTime) + "ms)");

        return results;
    }

    public List calculateMonthlyStatistics(int year, int month) {
        List<StatisticItemDAO> results = new ArrayList();
        long numberOfItems = -1;

        int monthNumberInJava = month - 1;

        long startTime = System.currentTimeMillis();
        log.info("==== Start of the Montly stats calculation (Manual Call : " + year + "-" + monthNumberInJava + ")");
        try {
            ChromatticSession session = lifeCycle.getChromattic().openSession();
            // Aggregate last week data
            SocialStatistic statItem = this.setMonthStat(session, year, monthNumberInJava, true);
            StatisticItemDAO itemDAO = this.getStatisticDAO(statItem, StatisticInterval.TYPE_MONTH);
            results.add(itemDAO);
            session.save();

        } catch (Exception e) {
            log.error(e);
        } finally {
        }
        log.info("==== Start of the Monthy stats calculation (Manual Call : " + year + "-" + (month - 1) + ")(" + (System.currentTimeMillis() - startTime) + "ms)");
        return results;
    }

    // TODO : check multi threading
    public long calculateWeeklyStatistics() {
        long numberOfItems = -1;
        long startTime = System.currentTimeMillis();
        log.info("==== Start of the Weekly stats calculation ");

        try {
            ChromatticSession session = lifeCycle.getChromattic().openSession();
            // Aggregate last week data
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.WEEK_OF_YEAR, -1);
            SocialStatistic statItem = this.setWeekStat(session, cal.get(Calendar.YEAR), cal.get(Calendar.WEEK_OF_YEAR), false);
            session.save();
        } catch (Exception e) {
            log.error(e);
        } finally {
            //lifeCycle.closeContext(context, true);
            //manager.getSynchronization().setSaveOnClose(true);
            //RequestLifeCycle.end();
        }

        log.info("==== End of the Weekly stats calculation (" + (System.currentTimeMillis() - startTime) + "ms)");
        return numberOfItems;
    }

    // TODO : check multi threading
    public long calculateMonthlyStatistics() {
        long numberOfItems = -1;
        long startTime = System.currentTimeMillis();
        log.info("==== Start of the Monthly stats calculation ");
        try {
            ChromatticSession session = lifeCycle.getChromattic().openSession();
            // Aggregate last week data
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -1);
            SocialStatistic statItem = this.setMonthStat(session, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), false);
            session.save();

        } catch (Exception e) {
            log.error(e);
        } finally {
            //lifeCycle.closeContext(context, true);
            //manager.getSynchronization().setSaveOnClose(true);
            //RequestLifeCycle.end();
        }
        log.info("==== End of the Monthly stats calculation (" + (System.currentTimeMillis() - startTime) + "ms)");
        return numberOfItems;
    }

    private SocialStatistic setDayStat(ChromatticSession session, int year, int day, boolean allowUpdate) throws RepositoryException {
        StatisticInterval interval = new StatisticInterval(year, day, StatisticInterval.TYPE_DAY);
        return this.saveStats(session, interval, StatisticInterval.TYPE_DAY, allowUpdate);
    }

    private SocialStatistic setWeekStat(ChromatticSession session, int year, int week, boolean allowUpdate) throws RepositoryException {
        StatisticInterval interval = new StatisticInterval(year, week, StatisticInterval.TYPE_WEEK);
        return this.saveStats(session, interval, StatisticInterval.TYPE_WEEK, allowUpdate);
    }

    private SocialStatistic setMonthStat(ChromatticSession session, int year, int month, boolean allowUpdate) throws RepositoryException {
        StatisticInterval interval = new StatisticInterval(year, month, StatisticInterval.TYPE_MONTH);
        return this.saveStats(session, interval, StatisticInterval.TYPE_MONTH, allowUpdate);
    }

    public List deleteStatistic(int type, int year, int idInYear) {

        String parentFolder = "";
        String typeLabel = "";
        if (type == StatisticInterval.TYPE_MONTH) {
            parentFolder = PARENT_FOLDER_MONTHLY_STATS;
            typeLabel = StatisticInterval.TYPE_MONTH_TEXT;
        } else if (type == StatisticInterval.TYPE_WEEK) {
            parentFolder = PARENT_FOLDER_WEEKLY_STATS;
            typeLabel = StatisticInterval.TYPE_WEEK_TEXT;
        } else if (type == StatisticInterval.TYPE_DAY) {
            parentFolder = PARENT_FOLDER_DAILY_STATS;
            typeLabel = StatisticInterval.TYPE_DAY_TEXT;
        }

        List result = new ArrayList();
        long startTime = System.currentTimeMillis();
        log.info("==== Start of the validateStatisticList calculation ");

        try {
            ChromatticSession session = lifeCycle.getChromattic().openSession();
            StatisticItemDAO itemDAO = new StatisticItemDAO();
            String name = year + "-" + idInYear;
            String pathToSearch = "./" + parentFolder + "/" + name;
            // check if the stat file exists
            SocialStatistic statItem = session.findByPath(SocialStatistic.class, pathToSearch);
            if (statItem == null) {
                itemDAO.setYear(year);
                itemDAO.setId(idInYear);
                itemDAO.setType(typeLabel);
                itemDAO.setValue(-1);
                itemDAO.setElapsedTime(System.currentTimeMillis() - startTime);
                itemDAO.setExecutionDate(new Date());
            } else {
                itemDAO.setId(statItem.getIdInYear());
                itemDAO.setYear(statItem.getYear());
                itemDAO.setStartDate(statItem.getStartDate());
                itemDAO.setEndDate(statItem.getEndDate());
                itemDAO.setValue(-1);
                itemDAO.setType(typeLabel);
                itemDAO.setElapsedTime(System.currentTimeMillis() - startTime);
                itemDAO.setExecutionDate(new Date());
                session.remove(statItem);
                session.save();
            }

            if (itemDAO != null) {
            }

            result.add(itemDAO);


        } catch (Exception e) {
            log.error(e);
        } finally {
        }

        return result;
    }

    private SocialStatistic saveStats(ChromatticSession session, StatisticInterval interval, int type, boolean allowUpdate) throws RepositoryException {
        ActivityStatsWeekly parent = getStatisticParent(session, type);
        long numberOfItems = -1;
        long startTime = System.currentTimeMillis();

        //interval.
        boolean callSave = false;

        // if month we should store the "human readable value" so month+1 
        // (since in programming we start at 0)            
        long idInYear = interval.getId();
        if (StatisticInterval.TYPE_MONTH == type) {
            idInYear = idInYear + 1;
        }

        String statisticEntry = interval.getYear() + "-" + idInYear;

        SocialStatistic statItem = session.findByPath(SocialStatistic.class, "./" + parent.getName() + "/" + statisticEntry);
        if (statItem == null) {
            statItem = session.create(SocialStatistic.class, statisticEntry);
            parent.getChildren().add(statItem);
            callSave = true;
        }

        if (callSave || allowUpdate) {
            numberOfItems = this.getNumberOfActivities(interval, session);
            statItem.setValue(numberOfItems);

            statItem.setIdInYear((int) idInYear);
            statItem.setYear(interval.getYear());
            statItem.setStartDate(interval.getStartDate());
            statItem.setEndDate(interval.getEndDate());
            statItem.setExecutionDate(new Date());
            statItem.setElapsedTime(System.currentTimeMillis() - startTime);
            callSave = true;
        }

        if (callSave) {
            session.save();
        }

        return statItem;
    }

    /**
     * return JCR Node that contains the weekly stats. If the node is not present, this method creates it.
     * If the node
     * @param session
     * @return JCR Node that contains the weekly stats
     */
    private ActivityStatsWeekly getStatisticParent(ChromatticSession session, int type) {

        String parentFolder = "";
        if (type == StatisticInterval.TYPE_MONTH) {
            parentFolder = PARENT_FOLDER_MONTHLY_STATS;
        } else if (type == StatisticInterval.TYPE_WEEK) {
            parentFolder = PARENT_FOLDER_WEEKLY_STATS;
        } else if (type == StatisticInterval.TYPE_DAY) {
            parentFolder = PARENT_FOLDER_DAILY_STATS;
        }


        //TODO: remove string: replace by configuration
        ActivityStatsWeekly weeklyStat = session.findByPath(ActivityStatsWeekly.class, "./" + parentFolder);
        if (weeklyStat == null) {
            // create a new item
            weeklyStat = session.insert(ActivityStatsWeekly.class, parentFolder);
            session.save();
        }

        return weeklyStat;
    }

    /**
     * This method do a Query on the social workspace to could the number of activities in an interval
     * @param interval
     * @param session
     * @return the number of activities, in the whole workspace, of the interval
     * @throws RepositoryException 
     */
    private long getNumberOfActivities(StatisticInterval interval, ChromatticSession session) throws RepositoryException {
        long queryStartTime = System.currentTimeMillis();
        String countActivitiesQuery = "select uuid from exo:activity where exo:postedTime >= "
                + interval.getStartDate().getTime()
                + " and exo:postedTime <= " + interval.getEndDate().getTime();
        QueryManager qm = session.getJCRSession().getWorkspace().getQueryManager();
        Query queryJCR = qm.createQuery(countActivitiesQuery, Query.SQL);
        QueryResult result = queryJCR.execute();

        NodeIterator nodeIterator = result.getNodes();
        long numberOfActivities = nodeIterator.getSize();
        nodeIterator = null;

        log.info("Result " + numberOfActivities + " - Query : " + countActivitiesQuery + " - execution time : " + (System.currentTimeMillis() - queryStartTime) + "ms");



        return numberOfActivities;
    }

    public List validateMonthlyStatisticList(int numberOfMonth) {
        return validateStatisticList(numberOfMonth, StatisticInterval.TYPE_MONTH);
    }

    public List validateWeeklyStatisticList(int numberOfWeek) {
        return validateStatisticList(numberOfWeek, StatisticInterval.TYPE_WEEK);
    }

    public List validateDailyStatisticList(int numberOfDay) {
        return validateStatisticList(numberOfDay, StatisticInterval.TYPE_DAY);
    }

    /**
     * this method check each month and weeks, and return the status of the stats.
     * @param numberOfMonths
     * @return List of week and month with their status
     */
    public List validateStatisticList(int numberOfPeriodToCheck, int type) {

        String parentFolder = "";
        String typeLabel = "";
        if (type == StatisticInterval.TYPE_MONTH) {
            parentFolder = PARENT_FOLDER_MONTHLY_STATS;
            typeLabel = StatisticInterval.TYPE_MONTH_TEXT;
        } else if (type == StatisticInterval.TYPE_WEEK) {
            parentFolder = PARENT_FOLDER_WEEKLY_STATS;
            typeLabel = StatisticInterval.TYPE_WEEK_TEXT;
        } else if (type == StatisticInterval.TYPE_DAY) {
            parentFolder = PARENT_FOLDER_DAILY_STATS;
            typeLabel = StatisticInterval.TYPE_DAY_TEXT;
        }

        List result = new ArrayList();
        long startTime = System.currentTimeMillis();
        log.info("==== Start of the validateStatisticList calculation ");

        try {
            ChromatticSession session = lifeCycle.getChromattic().openSession();

            // check each month from now to the number of months (parameter)



            for (int i = 0; i < numberOfPeriodToCheck; i++) {

                Calendar cal = Calendar.getInstance();

                StatisticItemDAO itemDAO = new StatisticItemDAO();
                String name = null; //cal.get(Calendar.YEAR) + "-";
                int idToSave = -1;

                if (StatisticInterval.TYPE_MONTH == type) {
                    cal.add(Calendar.MONTH, -i);
                    name = cal.get(Calendar.YEAR) + "-";
                    name = name + (cal.get(Calendar.MONTH) + 1); // get human readable month
                    idToSave = cal.get(Calendar.MONTH) + 1;
                } else if (StatisticInterval.TYPE_WEEK == type) {
                    cal.add(Calendar.WEEK_OF_YEAR, -i);
                    name = cal.get(Calendar.YEAR) + "-";
                    name = name + cal.get(Calendar.WEEK_OF_YEAR);
                    idToSave = cal.get(Calendar.WEEK_OF_YEAR);
                } else if (StatisticInterval.TYPE_DAY == type) {
                    cal.add(Calendar.DAY_OF_YEAR, -i);
                    name = cal.get(Calendar.YEAR) + "-";
                    name = name + cal.get(Calendar.DAY_OF_YEAR);
                    idToSave = cal.get(Calendar.DAY_OF_YEAR);
                }


                String pathToSearch = "./" + parentFolder + "/" + name;
                // check if the stat file exists
                SocialStatistic statItem = session.findByPath(SocialStatistic.class, pathToSearch);
                if (statItem == null) {
                    StatisticInterval interval = new StatisticInterval(cal.getTime(), type);
                    itemDAO.setYear(cal.get(Calendar.YEAR));
                    itemDAO.setId(idToSave);
                    itemDAO.setType(typeLabel);
                    itemDAO.setValue(-1);
                    itemDAO.setElapsedTime(System.currentTimeMillis() - startTime);
                    itemDAO.setExecutionDate(new Date());
                    itemDAO.setStartDate(interval.getStartDate());
                    itemDAO.setEndDate(interval.getEndDate());

                } else {
                    itemDAO.setId(statItem.getIdInYear());
                    itemDAO.setYear(statItem.getYear());
                    itemDAO.setStartDate(statItem.getStartDate());
                    itemDAO.setEndDate(statItem.getEndDate());
                    itemDAO.setValue(statItem.getValue());
                    itemDAO.setType(typeLabel);
                    itemDAO.setElapsedTime(statItem.getElapsedTime());
                    itemDAO.setExecutionDate(statItem.getExecutionDate());
                }

                result.add(itemDAO);

            }


        } catch (Exception e) {
            log.error(e);
        } finally {
        }

        log.info("==== End of the validateStatisticList calculation (" + (System.currentTimeMillis() - startTime) + "ms)");


        return result;
    }

    public StatisticItemDAO getStatisticDAO(SocialStatistic statItem, int type) {
        String typeLabel = "";
        if (type == StatisticInterval.TYPE_MONTH) {
            typeLabel = StatisticInterval.TYPE_MONTH_TEXT;
        } else if (type == StatisticInterval.TYPE_WEEK) {
            typeLabel = StatisticInterval.TYPE_WEEK_TEXT;
        } else if (type == StatisticInterval.TYPE_DAY) {
            typeLabel = StatisticInterval.TYPE_DAY_TEXT;
        }

        StatisticItemDAO itemDAO = new StatisticItemDAO();
        itemDAO.setType(typeLabel);
        itemDAO.setId(statItem.getIdInYear());
        itemDAO.setYear(statItem.getYear());
        itemDAO.setStartDate(statItem.getStartDate());
        itemDAO.setEndDate(statItem.getEndDate());
        itemDAO.setValue(statItem.getValue());
        return itemDAO;
    }

    public void updateActivitiesTestData() {
        throw new IllegalStateException("Method not implemented");


//        try {
//            ChromatticSession session = lifeCycle.getChromattic().openSession();
//            
//            String totalAct = "select * from exo:activity";
//            //"select uuid from exo:activity where exo:postedTime < 1293836400000 order by exo:postedTime asc";
//            // "select * from exo:activity";
//
//            QueryManager qm = session.getJCRSession().getWorkspace().getQueryManager();
//            Query queryJCR = qm.createQuery(totalAct, Query.SQL);
//            QueryResult result = queryJCR.execute();
//            NodeIterator nodeIterator = result.getNodes();
//            Calendar cal = Calendar.getInstance();
//
//            System.out.println("NUMBER OF ACTIVITIES : " + nodeIterator.getSize());
//
//
//            while (nodeIterator.hasNext()) {
//                Node activity = (Node) nodeIterator.next();
//                java.util.Random r = new java.util.Random();
//                int daysOp = (int) r.nextInt(400);
//                cal.add(Calendar.DAY_OF_YEAR, (daysOp * -1));
//
//                activity.setProperty("exo:postedTime", cal.getTimeInMillis());
//                activity.setProperty("exo:updatedTimestamp", cal.getTimeInMillis());
//
//                activity.save();
//                cal.setTime(new Date());
//            }
//
//
//        } catch (Exception e) {
//            log.error(e);
//        } finally {
//        }
    }
}
