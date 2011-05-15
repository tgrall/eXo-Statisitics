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
package org.exoplatform.plugin.statistics.social.service.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import junit.framework.TestCase;

/**
 *
 * @author tgrall
 */
public class StatisticIntervalTest extends TestCase {

    private Date dateToTest = null;

    public StatisticIntervalTest(String testName) {
        super(testName);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2011);
        cal.set(Calendar.MONTH, 2);
        cal.set(Calendar.DAY_OF_MONTH, 18);
        cal.set(Calendar.HOUR, 4);
        cal.set(Calendar.MINUTE, 3);
        cal.set(Calendar.SECOND, 45);
        cal.set(Calendar.MILLISECOND, 23);

        dateToTest = cal.getTime();

        int value = cal.get(Calendar.WEEK_OF_YEAR);

    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetStartDateForMonthByID() {
        int year = 2011;
        int month = 2;
        StatisticInterval instance = new StatisticInterval(year, month, StatisticInterval.TYPE_MONTH);

        Calendar cal = Calendar.getInstance();

        Long longTimeStart = new Long("1298934000000");
        cal.setTimeInMillis(longTimeStart);
        Date expResultStart = cal.getTime();
        Date resultStart = instance.getStartDate();
        assertEquals(resultStart, expResultStart);

        Long longTimeEnd = new Long("1301608799999");
        cal.setTimeInMillis(longTimeEnd);
        Date expResultEnd = cal.getTime();
        Date resultEnd = instance.getEndDate();
        assertEquals(resultEnd, expResultEnd);


    }

    public void testGetStartDateForWeekByID() {
        int year = 2011;
        int week = 12;
        StatisticInterval instance = new StatisticInterval(year, week, StatisticInterval.TYPE_WEEK);
        Calendar cal = Calendar.getInstance();
        Long longTimeStart = new Long("1299970800000");
        cal.setTimeInMillis(longTimeStart);
        Date expResultStart = cal.getTime();
        Date resultStart = instance.getStartDate();
        assertEquals(resultStart, expResultStart);

        Long longTimeEnd = new Long("1300575599999");
        cal.setTimeInMillis(longTimeEnd);
        Date expResultEnd = cal.getTime();
        Date resultEnd = instance.getEndDate();
        assertEquals(resultEnd, expResultEnd);
    }

    public void testGetStartDateForWeekByDate() {
        Calendar cal = Calendar.getInstance();

        // create the interval from this date
        StatisticInterval instance = new StatisticInterval(dateToTest, StatisticInterval.TYPE_WEEK);

        // the startdate for this week is: Sun May 15 00:00:00 CEST 2011
        // so let's initialize this date as expected result

        Long longTimeStart = new Long("1299970800000");
        cal.setTimeInMillis(longTimeStart);
        Date expResultStart = cal.getTime();
        Date resultStart = instance.getStartDate();
        assertEquals(resultStart, expResultStart);

        Long longTimeEnd = new Long("1300575599999");
        cal.setTimeInMillis(longTimeEnd);
        Date expResultEnd = cal.getTime();
        Date resultEnd = instance.getEndDate();
        assertEquals(resultEnd, expResultEnd);


    }

    public void testIntervalDatesForMonthByDate() {
        Calendar cal = Calendar.getInstance();
        StatisticInterval instance = new StatisticInterval(dateToTest, StatisticInterval.TYPE_MONTH);
        Long longTimeStart = new Long("1298934000000");
        cal.setTimeInMillis(longTimeStart);
        Date expResultStart = cal.getTime();
        Date resultStart = instance.getStartDate();
        assertEquals(resultStart, expResultStart);

        Long longTimeEnd = new Long("1301608799999");
        cal.setTimeInMillis(longTimeEnd);
        Date expResultEnd = cal.getTime();
        Date resultEnd = instance.getEndDate();
        assertEquals(resultEnd, expResultEnd);
    }

    public void testIntervalAsString() {
        StatisticInterval instance = new StatisticInterval(2008, 1, StatisticInterval.TYPE_MONTH);
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
        String formattedStartDate = formatter.format(instance.getStartDate());
        String formattedEndDate = formatter.format(instance.getEndDate());
        String expStartDate = "02-01-2008";
        String expEndDate = "02-29-2008";
        assertEquals(formattedStartDate, expStartDate);
        assertEquals(formattedEndDate, expEndDate);


    }
}
